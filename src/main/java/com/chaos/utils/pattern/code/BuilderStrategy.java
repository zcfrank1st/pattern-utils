package com.chaos.utils.pattern.code;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.List;

public class BuilderStrategy implements Strategy {
    @Override
    public JavaFile makeFile(String packagePath, String className, List<Field> fields) {

        TypeSpec builder = TypeSpec.classBuilder("Builder")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .build();

        TypeSpec clazz = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addType(builder)
                .build();


        return JavaFile.builder(packagePath, clazz)
                .build();
    }
}
