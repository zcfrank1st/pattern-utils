package com.chaos.utils.pattern.code;

import com.squareup.javapoet.JavaFile;

import javax.annotation.processing.Filer;
import java.io.IOException;
import java.util.List;

public class CodeGenerator {
    private Strategy strategy;
    private String packagePath;
    private String className;
    private List<Field> fields;
    private Filer filer;

    public CodeGenerator(Strategy strategy, String packagePath, String className, List<Field> fields, Filer filer) {
        this.strategy = strategy;
        this.packagePath = packagePath;
        this.className = className;
        this.filer = filer;
        this.fields = fields;
    }

    public void generate() throws IOException {
        JavaFile file = strategy.makeFile(packagePath, className, fields);
        file.writeTo(System.out);
        file.writeTo(filer);
    }
}
