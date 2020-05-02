package com.noahcharlton.robogeddon.client.watchdog;

import com.noahcharlton.robogeddon.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorDialog extends JFrame implements WindowListener {

    private static final int MAX_STACKTRACE_LENGTH = 10000;
    private static final Color background = new Color(100, 100, 100);

    ErrorDialog(Throwable error) {
        super(getThrowableMessage(error));
        var pane = new JPanel();
        pane.setLayout(new BorderLayout());
        pane.setBackground(background);

        pane.add(createTitleComponent(error), BorderLayout.NORTH);
        pane.add(createLogComponent(error), BorderLayout.CENTER);
        pane.add(createButtonBar(error), BorderLayout.SOUTH);


        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.add(pane);
        this.setMinimumSize(new Dimension(400, 300));
        this.addWindowListener(this);
        this.setResizable(true);
    }

    private static String getThrowableMessage(Throwable error) {
        return "Game Crash: " + (error.getMessage() == null ? error.getClass().getSimpleName() : error.getMessage());
    }

    private Component createButtonBar(Throwable error) {
        var buttons = new JPanel(new FlowLayout());
        var window = this;

        buttons.setBackground(background);
        buttons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        buttons.add(createButton("Copy Stacktrace", ()-> {
            var clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(getDetails(error)), null);
        }));
        buttons.add(createButton("Close",
                () -> window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING))));

        return buttons;
    }

    private JButton createButton(String text, Runnable runnable) {
        var button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.DARK_GRAY);
        button.setFocusPainted(false);
        button.addActionListener(e -> runnable.run());
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                button.setForeground(Color.BLACK);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setForeground(Color.WHITE);
            }
        });

        return button;
    }

    private Label createTitleComponent(Throwable error) {
        var title = new Label(getThrowableMessage(error));

        title.setFont(new Font(null, Font.PLAIN, 18));
        title.setBackground(Color.DARK_GRAY);
        title.setAlignment(Label.CENTER);
        title.setForeground(Color.WHITE);
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        return title;
    }

    private Component createLogComponent(Throwable error) {
        var text = new JTextArea(getDetails(error));
        var scrollPane = new JScrollPane(text);
        text.setBackground(background);
        text.setForeground(Color.WHITE);
        text.setEditable(false);
        text.setTabSize(4);
        text.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        return scrollPane;
    }

    private String getDetails(Throwable error) {
        StringWriter writer = new StringWriter();
        error.printStackTrace(new PrintWriter(writer));

        String output = writer.toString();

        if(output.length() > MAX_STACKTRACE_LENGTH){
            return output.substring(0, MAX_STACKTRACE_LENGTH).concat(" ...");
        }

        return output;
    }

    @Override
    public void windowClosed(WindowEvent e) {
        Log.debug("Error dialog closed! Shutting down JVM");
        System.exit(-1);
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
