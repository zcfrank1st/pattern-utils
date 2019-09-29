package com.chaos.utils.pattern.code;

import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

public class BuilderStrategy implements Strategy {
    @Override
    public JavaFile makeFile(String packagePath, String className, List<Field> fields) {
        ClassName originClazz = ClassName.get(packagePath, className);
        ClassName builder = ClassName.get(packagePath, className + ".Builder");

        List<FieldSpec> fieldSpecList = new ArrayList<>();
        List<MethodSpec> setMethodList = new ArrayList<>();
        List<MethodSpec> getMethodList = new ArrayList<>();

        MethodSpec.Builder mb = MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(builder, "builder");

        for (Field field : fields) {
            FieldSpec fieldSpec = FieldSpec
                    .builder(TypeName.get(field.getType()), field.getName())
                    .addModifiers(Modifier.PRIVATE)
                    .build();
            fieldSpecList.add(fieldSpec);

            MethodSpec setMethod = MethodSpec
                    .methodBuilder("set" + StringUtils.capitalize(field.getName()))
                    .addParameter(TypeName.get(field.getType()), field.getName())
                    .addStatement("this.$N = $N", field.getName(), field.getName())
                    .addStatement("return this")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(builder)
                    .build();

            setMethodList.add(setMethod);

            MethodSpec getMethod = MethodSpec
                    .methodBuilder("get" + StringUtils.capitalize(field.getName()))
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return $N", field.getName())
                    .returns(TypeName.get(field.getType()))
                    .build();

            getMethodList.add(getMethod);

            mb.addStatement("this.$N = builder.$N", field.getName(), field.getName());
        }

        TypeSpec clazz = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addFields(fieldSpecList)
                .addMethods(getMethodList)
                .addMethod(mb.build())
                .addType(TypeSpec.classBuilder("Builder")
                        .addModifiers(Modifier.STATIC)
                        .addFields(fieldSpecList)
                        .addMethods(setMethodList)
                        .addMethod(
                                MethodSpec
                                        .methodBuilder("build")
                                        .addModifiers(Modifier.PUBLIC)
                                        .addStatement("return new $T(this)", originClazz)
                                        .returns(originClazz)
                                        .build())
                        .build())
                .build();

        return JavaFile.builder(packagePath, clazz)
                .build();
    }
}
