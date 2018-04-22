package frc.team4069.saturn.lint

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalQuickFixBase
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.search.searches.ReferencesSearch
import org.apache.log4j.Level
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedDeclaration

class IncorrectSpellingLint : AbstractKotlinInspection() {

    val spellingMap = mapOf(
            "meter" to "metre"
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

    override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor>? {
        if (file !is KtFile) {
            return null
        }

        val problems = mutableListOf<ProblemDescriptor>()
        file.findChildrenByClass(KtNamedDeclaration::class.java).forEach {
            logger.debug("We got one! $it")
        }



//        for (childNode in file.allChildren) {
//            when(childNode) {
//                is KtNamedDeclaration -> {
//                    checkNode(childNode, manager, isOnTheFly, problems)
//
//                    when (childNode) {
//                        is KtNamedFunction -> {
//                            for (node in childNode.valueParameters) {
//                                checkNode(node, manager, isOnTheFly, problems)
//                            }
//                            childNode.children.forEach {
//                                logger.debug("Function child $it")
//                            }
//
//                            for(node in childNode.children.find { it is KtBlockExpression }?.children
//                                    ?.filterIsInstance<KtNamedDeclaration>() ?: listOf()) {
//                                checkNode(node, manager, isOnTheFly, problems)
//                            }
//                        }
//                        is KtClass -> {
//                            for(node in childNode.primaryConstructorParameters) {
//                                checkNode(node, manager, isOnTheFly, problems)
//                            }
//
//                            for(cons in childNode.secondaryConstructors) {
//                                for(node in cons.valueParameters) {
//                                    checkNode(node, manager, isOnTheFly, problems)
//                                }
//                            }
//
//                            if(childNode.hasBody()) {
//                                val body = childNode.getBody()!!
//
//                            }
//                        }
//                    }
//                }
//
//                is KtConstructor<*> -> {
//                    for(node in childNode.getValueParameters()) {
//                        checkNode(node, manager, isOnTheFly, problems)
//                    }
//                }
//            }
//        }

//        traverseNode(file, manager, isOnTheFly, problems)

        return problems.toTypedArray()
    }

    private fun checkNode(node: KtNamedDeclaration, manager: InspectionManager, isOnTheFly: Boolean, problems: MutableList<ProblemDescriptor>) {
        val name = node.name!!

        for ((wrong, correct) in spellingMap) {
            val nameNode = node.nameIdentifier!!

            if (name.contains(wrong, true)) {
                problems.add(manager.createProblemDescriptor(
                        nameNode,
                        "Non-conforming spelling of $correct",
                        SpellingQuickFix(wrong),
                        ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                        isOnTheFly
                ))
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
                }
            }

        }
    }
}

