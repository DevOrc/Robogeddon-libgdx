package com.noahcharlton.robogeddon.command;

public class Argument {

    private final String text;

    public Argument(String text) {
        this.text = text;
    }

    public <E extends Enum<E>> E asEnum(Class<E> clazz){
        return Enum.valueOf(clazz, text);
    }

    public int asInt(){
        return Integer.parseInt(text);
    }

    public String asString(){
        return text;
    }

    @Override
    public String toString() {
        return "Arg(" + text + ")";
    }
}
