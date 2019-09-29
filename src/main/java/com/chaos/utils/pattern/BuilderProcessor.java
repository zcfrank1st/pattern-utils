package com.chaos.utils.pattern;

import com.chaos.utils.pattern.anno.Builder;
import com.chaos.utils.pattern.code.BuilderStrategy;
import com.chaos.utils.pattern.code.CodeGenerator;
import com.chaos.utils.pattern.code.Field;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author chaos
 */
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes({"com.chaos.utils.pattern.anno.Builder"})
public class BuilderProcessor extends AbstractProcessor {
    private Filer mFiler;
    private Messager mMessager;
    private Elements mElementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mMessager = processingEnvironment.getMessager();
        mElementUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(Builder.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Set<? extends Element> builderElements = roundEnv.getElementsAnnotatedWith(Builder.class);
        for (Element element : builderElements) {
            PackageElement packageElement = mElementUtils.getPackageOf(element);
            String pkName = packageElement.getQualifiedName().toString();
            note("package = " + pkName);
            String[] clazzNameArray = element.asType().toString().split("\\.");
            String clazzName = clazzNameArray[clazzNameArray.length - 1];
            note("clazzName = " + clazzName);
            List<Field> fieldList = new ArrayList<>();
            for (Element e : element.getEnclosedElements()) {
                if (e.getKind() == ElementKind.FIELD) {
                    Field field = new Field();
                    field.setType(e.asType().toString());
                    field.setName(e.getSimpleName().toString());
                }
            }

            CodeGenerator generator = new CodeGenerator(
                    new BuilderStrategy(),
                    pkName,
                    clazzName + "$",
                    fieldList,
                    mFiler);
            try {
                generator.generate();
            } catch (IOException e) {
                note(e.getMessage());
            }
            return true;
        }
        return false;
    }

    private void note(String msg) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
    }
}
