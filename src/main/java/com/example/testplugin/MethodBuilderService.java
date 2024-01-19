package com.example.testplugin;

import com.intellij.execution.configurations.ParametersList;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiJavaTokenImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MethodBuilderService {
    private final PsiClass containingClass;

    public MethodBuilderService(PsiClass containingClass) {
        this.containingClass = containingClass;
    }

    public List<PsiMethod> buildMethods() {
        List<PsiMethod> result = new ArrayList<>();

        for (var annotation : containingClass.getAnnotations()) {
            PsiMethod builtMethod = null;

            if (AnnotationEnum.HELLO_WORLD.getFqn().equals(annotation.getQualifiedName())) {
                builtMethod = buildHelloWorldMethod(annotation);
            }

            if (builtMethod == null) continue;

            result.add(builtMethod);
        }

        return result;
    }

    private PsiMethod buildHelloWorldMethod(PsiElement navigationElement) {
        var manager = containingClass.getManager();

        var methodBuilder = new LightMethodBuilderExtension(manager, "helloWorld");

        methodBuilder
                .withBodyText("System.out.println(\"Hello world!\");\n")
                .addModifier(PsiModifier.PUBLIC)
                .setMethodReturnType(PsiType.VOID)
                .setContainingClass(containingClass)
                .setNavigationElement(navigationElement);


        if (hasAlreadyDeclaredMethod(methodBuilder)) {
            return null;
        }

        return methodBuilder;
    }

    private boolean hasAlreadyDeclaredMethod(PsiMethod psiMethod) {
        for (var child : containingClass.getChildren()) {
            if (child instanceof PsiMethod) {
                var currentMethod = (PsiMethod) child;

                if (parameterListsEqual(currentMethod.getParameterList(), psiMethod.getParameterList())
                        && currentMethod.getName().equals(psiMethod.getName())) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean parameterListsEqual(PsiParameterList paramList1, PsiParameterList paramList2) {
        var filteredParamList1 = Arrays.stream(paramList1.getChildren()).filter(x -> !(x instanceof PsiJavaToken)).sorted().collect(Collectors.toList());
        var filteredParamList2 = Arrays.stream(paramList2.getChildren()).filter(x -> !(x instanceof PsiJavaToken)).sorted().collect(Collectors.toList());

        return filteredParamList1.equals(filteredParamList2);
    }
}
