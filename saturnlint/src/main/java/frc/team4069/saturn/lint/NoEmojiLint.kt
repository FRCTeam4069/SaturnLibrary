package frc.team4069.saturn.lint

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection

class NoEmojiLint : AbstractKotlinInspection() {

    override fun getDisplayName(): String {
        return "NoEmojiLint"
    }

    override fun getGroupDisplayName(): String {
        return "frc.team4069.saturn.lint"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
            object : PsiElementVisitor() {
                override fun visitElement(element: PsiElement?) {
                    if(element is LeafPsiElement) {
                        checkNode(element, holder)
                    }
                }
            }

    private fun checkNode(element: LeafPsiElement, holder: ProblemsHolder) {
        val text = element.text
        if(text.codePoints().toArray().any { it > 0x1F600 }) {
            holder.registerProblem(element, "Emoji used in code")
        }
    }
}