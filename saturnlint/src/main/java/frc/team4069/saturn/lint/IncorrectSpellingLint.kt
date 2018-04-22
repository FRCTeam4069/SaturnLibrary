package frc.team4069.saturn.lint

import com.intellij.codeInspection.LocalQuickFixBase
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.search.searches.ReferencesSearch
import org.apache.log4j.Level
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.psi.KtNamedDeclaration

class IncorrectSpellingLint : AbstractKotlinInspection() {

    val spellingMap = mapOf(
            "meter" to "metre",
            "organize" to "organise"
    )

    private val logger = Logger.getInstance(this::class.java)

    init {
        logger.setLevel(Level.DEBUG)
    }

    override fun getDisplayName(): String {
        return "IncorrectSpellingLint"
    }

    override fun getGroupDisplayName(): String {
        return "frc.team4069.saturn.lint"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor = object : PsiElementVisitor() {
        override fun visitElement(element: PsiElement?) {
            if (element is KtNamedDeclaration) {
                checkNode(element, holder)
            } else if (element is LeafPsiElement && element !is PsiWhiteSpace) {
                for ((wrong, correct) in spellingMap) {
                    if (element.text.contains(wrong, true)) {
                        holder.registerProblem(element, "Non-conforming spelling of $correct", SpellingQuickFix(wrong))
                    }
                }
            }
        }
    }

    private fun checkNode(node: KtNamedDeclaration, holder: ProblemsHolder) {
        val name = node.name ?: return

        for ((wrong, correct) in spellingMap) {
            val nameNode = node.nameIdentifier ?: return

            if (name.contains(wrong, true)) {
                holder.registerProblem(nameNode, "Non-conforming spelling of $correct", SpellingQuickFix(wrong))
            }
        }
    }

    inner class SpellingQuickFix(private val wrong: String) : LocalQuickFixBase("Correct spelling") {
        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val correct = spellingMap[wrong]!!
            val element = descriptor.psiElement


            val parent = element.parent
            when (parent) {
                is KtNamedDeclaration -> {
                    val newName = parent.name!!.laxReplace(wrong, correct)

                    for (ref in ReferencesSearch.search(parent).findAll()) {
                        ref.handleElementRename(newName)
                    }

                    parent.setName(newName)
                    return
                }
            }

            if (element is LeafPsiElement) {
                val newText = element.text.laxReplace(wrong, correct)
                element.replaceWithText(newText)
                return
            }
        }
    }
}

