package com.example.testplugin;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassImpl;

import java.util.*;

public class MethodBuilderService {
    private final PsiClass containingClass;

    public MethodBuilderService(PsiClass containingClass) {
        this.containingClass = containingClass;
    }

    public List<PsiMethod> buildMethods() {
        List<PsiMethod> result = new ArrayList<>();

        for (var annotation : containingClass.getAnnotations()) {

            if (AnnotationEnum.HELLO_WORLD.getFqn().equals(annotation.getQualifiedName())) {
                var helloWorldMethod = buildHelloWorldMethod(annotation);
                if (!PsiUtil.hasAlreadyDeclaredMethod(containingClass, helloWorldMethod)) {
                    result.add(helloWorldMethod);
                }
            }
        }

        if (containingClass instanceof PsiClassImpl) {
            var methods = ((PsiClassImpl) containingClass).getOwnMethods();

            for (var method : methods) {
                if (isLogItMethod(method)) {
                    var logItMethod = buildLogITMethod(method, true);
                    if (!PsiUtil.hasAlreadyDeclaredMethod(containingClass, logItMethod)) {
                        result.add(logItMethod);
                    }
                }
            }
        }
        addLogItMethodsToInterfaceIfNeeded(containingClass, result);

        return result;
    }

    private boolean isLogItMethod(PsiMethod method) {
        var annotations = method.getAnnotations();
        for (var annotation : annotations) {
            if (AnnotationEnum.LOG_IT.getFqn().equals(annotation.getQualifiedName())) {
                return true;
            }
        }
        return false;
    }

    private void addLogItMethodsToInterfaceIfNeeded(PsiClass psiClass, List<PsiMethod> result) {
        var classesImplementors = new HashSet<PsiClass>();

        try {
            var highestParent = PsiService.getHighestParent(psiClass);
            if (highestParent == null) return;

            fillClassesImplementors(highestParent, classesImplementors);
            classesImplementors.forEach(
                    currentClass -> {
                        var methods = ((PsiClassImpl) currentClass).getOwnMethods();

                        methods.forEach(
                                method -> {
                                    if (isLogItMethod(method)) {
                                        if (PsiUtil.hasAlreadyDeclaredMethod(containingClass, method)) {
                                            var abstractLogItMethod = PsiUtil.getSameMethodFromClass(containingClass, method);
                                            if (abstractLogItMethod == null) return;

                                            var logItMethod = buildLogITMethod(abstractLogItMethod, false);

                                            if (PsiUtil.hasAlreadyDeclaredMethod(containingClass, method)
                                                    && !PsiUtil.hasAlreadyDeclaredMethod(containingClass, logItMethod)) {
                                                System.out.println("Log it method built");
                                                result.add(logItMethod);
                                            }
                                        }
                                    }
                                }
                        );
                    }
            );
        } catch (Throwable e) {
            System.out.println("fail");
            e.printStackTrace();
        }
    }

    private void fillClassesImplementors(PsiElement currentNode, Set<PsiClass> classesImplementors) {
        if (currentNode instanceof PsiClass) {
            var currentClass = ((PsiClass) currentNode);

            if (PsiUtil.containsInImplements(currentClass, containingClass)) {
                classesImplementors.add(currentClass);
            }
        }
        for (var childNode : currentNode.getChildren()) {
            fillClassesImplementors(childNode, classesImplementors);
        }
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

        return methodBuilder;
    }

    private PsiMethod buildLogITMethod(PsiMethod prototype, boolean withBody) {
        var manager = containingClass.getManager();

        var methodBuilder = new LightMethodBuilderExtension(manager, prototype.getName());

        if (withBody) {
            methodBuilder
                    .withBodyText("return null;");
        }

        methodBuilder.addModifier(PsiModifier.PUBLIC)
                .setMethodReturnType(prototype.getReturnType())
                .setContainingClass(containingClass)
                .setNavigationElement(prototype);

        for (var exception : prototype.getThrowsList().getReferencedTypes()) {
            methodBuilder.addException(exception);
        }
        for (var param : prototype.getParameterList().getParameters()) {
            methodBuilder.addParameter(param);
        }

        methodBuilder.addParameter("currentUserId", "java.lang.String");

        return methodBuilder;
    }
}
