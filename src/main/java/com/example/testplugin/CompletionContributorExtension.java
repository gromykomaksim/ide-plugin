package com.example.testplugin;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PlainTextTokenTypes;

public class CompletionContributorExtension extends CompletionContributor {
    public CompletionContributorExtension () {
        System.out.println("Init");
        extend(CompletionType.BASIC, PlatformPatterns.not(PlatformPatterns.alwaysFalse()),
                new CompletionProviderExtension());
    }
}
