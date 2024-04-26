package log.it.plugin;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.*;
import com.intellij.psi.*;
import com.intellij.ui.JBColor;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;


public class CompletionProviderExtension extends CompletionProvider<CompletionParameters> {
    @Override
    public void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
        result.runRemainingContributors(parameters, completionResult -> {
            LookupElement lookupElement = completionResult.getLookupElement();
            PsiElement psiElement = lookupElement.getPsiElement();

            if (psiElement instanceof LightMethodBuilderExtension) {
                LookupElement finalLookupElement = LookupElementDecorator.withRenderer(lookupElement, new LookupElementRenderer<>() {
                    @Override
                    public void renderElement(LookupElementDecorator<LookupElement> element, LookupElementPresentation presentation) {
                        lookupElement.renderElement(presentation);

                        presentation.setItemTextForeground(JBColor.ORANGE);
                    }
                });
                result.passResult(completionResult.withLookupElement(finalLookupElement));
            } else {
                result.passResult(completionResult);
            }

            result.passResult(completionResult);
        });

//        Util.printExecTime("Customization complete", mark1);
    }
}
