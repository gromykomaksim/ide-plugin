package log.it.plugin;

import com.intellij.psi.*;
import com.intellij.psi.augment.PsiAugmentProvider;
import com.intellij.psi.impl.source.PsiClassImpl;
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

        if (element instanceof PsiClassImpl && type == PsiMethod.class) {
            var currentClass = (PsiClassImpl) element;

            return (List<Psi>)(new MethodBuilderService(currentClass).buildMethods());
        }

        return emptyResult;
    }
}
