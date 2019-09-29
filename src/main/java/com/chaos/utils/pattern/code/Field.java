package com.chaos.utils.pattern.code;

import javax.lang.model.type.TypeMirror;

public class Field {
    private TypeMirror type;
    private String name;

    public TypeMirror getType() {
        return type;
    }

    public void setType(TypeMirror type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
