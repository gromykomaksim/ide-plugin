package com.example.testplugin;

import com.intellij.lang.ASTNode;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.psi.*;
import com.intellij.psi.impl.light.LightMethodBuilder;
import com.intellij.psi.impl.light.LightModifierList;
import com.intellij.psi.impl.light.LightTypeParameterListBuilder;
import org.jetbrains.annotations.NotNull;

public class LightMethodBuilderExtension extends LightMethodBuilder {
    public LightMethodBuilderExtension(@NotNull PsiManager manager, @NotNull String name) {
        super(manager, JavaLanguage.INSTANCE, name);
    }

    private ASTNode myASTNode;
    private PsiMethod myMethod;
    private String myBody;

    public LightMethodBuilderExtension withBodyText(@NotNull String bodyText) {
        myBody = bodyText;

        return this;
    }

    @Override
    public ASTNode getNode() {
        if (null == myASTNode) {
            final PsiElement myPsiMethod = getOrCreateMyPsiMethod();
            myASTNode = null == myPsiMethod ? null : myPsiMethod.getNode();
        }
        return myASTNode;
    }

    private PsiElement getOrCreateMyPsiMethod() {
        if (null == myMethod) {
            myMethod = rebuildMethodFromString();
        }
        return myMethod;
    }

    private PsiMethod rebuildMethodFromString() {
        PsiMethod result;
        try {
            var manager = getContainingClass().getManager();
            StringBuilder methodTextPresentation = new StringBuilder();

            methodTextPresentation.append(getModifierList().getText());
            methodTextPresentation.append(' ');
            methodTextPresentation.append(getReturnType().getCanonicalText());
            methodTextPresentation.append(' ');
            methodTextPresentation.append(getName());
            methodTextPresentation.append("(){\n");
            methodTextPresentation.append(myBody);
            methodTextPresentation.append("}\n");

            PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(manager.getProject());

            result = elementFactory.createMethodFromText(methodTextPresentation.toString(), getContainingClass());
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return result;
    }
}
