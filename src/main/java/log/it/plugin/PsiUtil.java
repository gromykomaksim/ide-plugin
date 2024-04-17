package log.it.plugin;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassImpl;
import com.intellij.psi.impl.source.PsiParameterImpl;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PsiUtil {
    public static boolean hasAlreadyDeclaredMethod(PsiClass psiClass, PsiMethod psiMethod) {
        var methods = ((PsiClassImpl) psiClass).getOwnMethods();
        for (var method : methods) {
            if (parameterListsEqual(method.getParameterList(), psiMethod.getParameterList())
                    && method.getName().equals(psiMethod.getName())) {
                return true;
            }
        }

        return false;
    }

    private static boolean parameterListsEqual(PsiParameterList paramList1, PsiParameterList paramList2) {
        var filteredParamList1 = Arrays.stream(paramList1.getChildren()).filter(x -> !(x instanceof PsiJavaToken)).map(x -> (PsiParameterImpl) x).sorted().collect(Collectors.toList());
        var filteredParamList2 = Arrays.stream(paramList2.getChildren()).filter(x -> !(x instanceof PsiJavaToken)).map(x -> (PsiParameterImpl) x).sorted().collect(Collectors.toList());

        return filteredParamList1.stream().anyMatch(
                param1 -> filteredParamList2.stream().anyMatch(
                        param2 -> param1.getType().equals(param2.getType())
                )
        );
    }

    public static PsiMethod getSameMethodFromClass(PsiClass psiClass, PsiMethod psiMethod) {
        var methods = ((PsiClassImpl) psiClass).getOwnMethods();
        for (var method : methods) {
            if (parameterListsEqual(method.getParameterList(), psiMethod.getParameterList())
                    && method.getName().equals(psiMethod.getName())) {
                return method;
            }
        }

        return null;
    }
}
