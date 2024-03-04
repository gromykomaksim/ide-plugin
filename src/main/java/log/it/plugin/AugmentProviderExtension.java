package log.it.plugin;

import com.intellij.psi.*;
import com.intellij.psi.augment.PsiAugmentProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class AugmentProviderExtension extends PsiAugmentProvider {
    @org.jetbrains.annotations.NotNull
    @Override
    public <Psi extends PsiElement> List<Psi> getAugments(@org.jetbrains.annotations.NotNull PsiElement element,
                                                          @NotNull final Class<Psi> type,
                                                          @Nullable String nameHint) {
        final List<Psi> emptyResult = Collections.emptyList();

        if (element instanceof PsiClass && type == PsiMethod.class) {
            var currentClass = (PsiClass) element;

            return (List<Psi>)(new MethodBuilderService(currentClass).buildMethods());
        }

        return emptyResult;
    }
}
