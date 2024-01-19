package com.example.testplugin;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.*;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.lang.Language;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.compiled.ClsMethodImpl;
import com.intellij.psi.impl.light.LightMethodBuilder;
import com.intellij.psi.impl.source.tree.java.PsiJavaTokenImpl;
import com.intellij.psi.tree.IElementType;
import com.intellij.ui.JBColor;
import com.intellij.util.PlatformIcons;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;


public class CompletionProviderExtension extends CompletionProvider<CompletionParameters> {
    @Override
    public void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
        System.out.println("Method starts");

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

        });
    }
}
