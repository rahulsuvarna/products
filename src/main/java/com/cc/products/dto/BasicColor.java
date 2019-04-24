package com.cc.products.dto;

import java.util.EnumSet;

public enum BasicColor {

    BLUE("0000FF"),
    RED("FF0000"),
    PINK("FFC0CB"),
    BLACK("000000"),
    GREEN("008000"),
    GREY("808080"),
    ORANGE("FFA500"),
    PURPLE("800080"),
    WHITE("FFFFFF"),
    YELLOW("FFFF00")
    ;

    private static final BasicColor[] copyOfValues = values();
    private String hexCode;

    BasicColor(String hexCode) {
        this.hexCode = hexCode;
    }

    public static BasicColor forName(String color) {
        for (BasicColor value : copyOfValues) {
            if (value.name().equals(color)) {
                return value;
            }
        }
        return null;
    }

    public String getHexCode() {
        return hexCode;
    }

}
