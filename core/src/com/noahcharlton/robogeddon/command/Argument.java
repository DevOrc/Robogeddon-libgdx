package com.noahcharlton.robogeddon.command;

public class Argument {

    private final String text;

    public Argument(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Arg(" + text + ")";
    }
}
