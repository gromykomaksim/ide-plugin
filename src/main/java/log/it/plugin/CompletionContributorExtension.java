package log.it.plugin;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.PlatformPatterns;

public class CompletionContributorExtension extends CompletionContributor {
    public CompletionContributorExtension () {
        extend(CompletionType.BASIC, PlatformPatterns.not(PlatformPatterns.alwaysFalse()),
                new CompletionProviderExtension());
    }
}
