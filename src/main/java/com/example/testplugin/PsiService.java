package com.example.testplugin;

import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.file.PsiJavaDirectoryImpl;

public class PsiService {
    public static PsiElement getHighestParent(PsiElement psiElement) {
        PsiElement currentElement = psiElement;
        var notSrc = true;
        while (notSrc) {
            if (currentElement == null) {
                return null;
            }
            if (currentElement instanceof PsiJavaDirectoryImpl) {
                notSrc = !((PsiJavaDirectoryImpl) currentElement).getName().equals("src");
            }
            currentElement = currentElement.getParent();
        }
        System.out.println("highest parent init");
        return currentElement;
    }
}
