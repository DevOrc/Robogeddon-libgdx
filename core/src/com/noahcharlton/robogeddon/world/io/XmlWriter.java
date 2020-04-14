package com.noahcharlton.robogeddon.world.io;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class XmlWriter {
	private final Writer writer;
	private final Array<String> stack = new Array();
	private String currentElement;
	private boolean indentNextClose;

	public int indent;

	public XmlWriter (FileHandle handle) {
		this.writer = handle.writer(false, String.valueOf(StandardCharsets.UTF_8));
	}

	private void indent () {
		int count = indent;
		if (currentElement != null) count++;
		for (int i = 0; i < count; i++)
			write('\t');
	}

	public XmlWriter element (String name) {
		if (startElementContent()) write('\n');
		indent();
		write('<');
		write(name);
		currentElement = name;
		return this;
	}

	public XmlWriter element (String name, Object text) {
		return element(name).text(text).pop();
	}

	private boolean startElementContent () {
		if (currentElement == null) return false;
		indent++;
		stack.add(currentElement);
		currentElement = null;
		write(">");
		return true;
	}

	public XmlWriter attribute (String name, Object value) {
		if (currentElement == null) throw new IllegalStateException();
		write(' ');
		write(name);
		write("=\"");
		write(value == null ? "null" : value.toString());
		write('"');
		return this;
	}

	public XmlWriter text (Object text) {
		startElementContent();
		String string = text == null ? "null" : text.toString();
		indentNextClose = string.length() > 64;
		if (indentNextClose) {
			write('\n');
			indent();
		}
		write(string);
		if (indentNextClose) write('\n');
		return this;
	}

	public XmlWriter pop () {
		if (currentElement != null) {
			write("/>\n");
			currentElement = null;
		} else {
			indent = Math.max(indent - 1, 0);
			if (indentNextClose) indent();
			write("</");
			write(stack.pop());
			write(">\n");
		}
		indentNextClose = true;
		return this;
	}

	private void write(char c){
		try {
			writer.write(c);
		} catch(IOException e) {
			throw new WorldIOException(e);
		}
	}

	private void write(String s){
		try {
			writer.write(s);
		} catch(IOException e) {
			throw new WorldIOException(e);
		}
	}

	public void close () {
		try {
			writer.close();
		} catch(IOException e) {
			throw new WorldIOException(e);
		}
	}

	public void flush () {
		try {
			writer.flush();
		} catch(IOException e) {
			throw new WorldIOException(e);
		}
	}
}