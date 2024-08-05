package com.ari.auto.code.generator.enums;

public enum DataType {
    STRING("String"),
    INTEGER("Integer"),
    LONG("Long"),
    DOUBLE("Double"),
    CHARACTER("Character"),
    BOOLEAN("Boolean"),
    LIST_OF_STRING("List<String>"),
    LIST_OF_INTEGER("List<Integer>"),
    LIST_OF_LONG("List<Long>"),
    LIST_OF_DOUBLE("List<Double>"),
    LIST_OF_CHARACTER("List<Character>"),
    LIST_OF_BOOLEAN("List<Boolean>");

    final String type;

    DataType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

/**
 * Math: Logarithm, complex number, quadratic equation and binomial theorem, vector algebra
 * Physics: Unit and measurement, Force and Motion,
 * Chemistry: Atomic structure, chemical bonding and solution
 */