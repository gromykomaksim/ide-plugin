package log.it.plugin;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassImpl;
import com.intellij.psi.search.searches.ClassInheritorsSearch;

import java.time.LocalDateTime;
import java.util.*;

public class MethodBuilderService {
    private final PsiClassImpl containingClass;
    private final LocalDateTime currentTimeMark = LocalDateTime.now();

    public MethodBuilderService(PsiClassImpl containingClass) {
        System.out.println(containingClass + " init");
        this.containingClass = containingClass;
    }

    public List<PsiMethod> buildMethods() {
        List<PsiMethod> result = new ArrayList<>();

        var methods = containingClass.getOwnMethods();

        for (var method : methods) {
            if (isLogItMethod(method)) {
                var logItMethod = buildLogITMethod(method, true);
                if (!PsiUtil.hasAlreadyDeclaredMethod(containingClass, logItMethod)) {
                    result.add(logItMethod);
                }
            }
        }

        Util.printExecTime("Additional methods built", currentTimeMark);

        addLogItMethodsToInterfaceIfNeeded(containingClass, result);

        Util.printExecTime("Whole process is finished", currentTimeMark);

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

        try {
            var highestParent = PsiService.getHighestParent(psiClass);
            if (highestParent == null) return;

            var classImplementors = ClassInheritorsSearch.search(psiClass);
            Util.printExecTime("Implementors found", currentTimeMark);
            classImplementors.forEach(
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
                                                result.add(logItMethod);
                                            }
                                        }
                                    }
                                }
                        );
                    }
            );
            Util.printExecTime("Interface methods built", currentTimeMark);
        } catch (Throwable e) {
            Util.printExecTime("fail", currentTimeMark);
            e.printStackTrace();
        }
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
