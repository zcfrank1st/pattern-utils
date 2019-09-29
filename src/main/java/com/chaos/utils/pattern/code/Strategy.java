package com.chaos.utils.pattern.code;

import com.squareup.javapoet.JavaFile;

import java.util.List;

public interface Strategy {
    JavaFile makeFile(String packagePath, String className, List<Field> fields);
}
