package frc.team4069.saturn.lint

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalQuickFixBase
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.search.GlobalSearchScope
import org.apache.log4j.Level
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction

class CustomConstructorRobotLint : AbstractKotlinInspection() {

    init {
        LOG.setLevel(Level.DEBUG)
    }

    override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor>? {
        if (file !is KtFile) {
            return null
        }

        val problems = mutableSetOf<ProblemDescriptor>()

        val robotClass = JavaPsiFacade.getInstance(file.project)
                .findClass("edu.wpi.first.wpilibj.RobotBase", GlobalSearchScope.allScope(file.project)) ?: return null
        for (clazz in file.classes) {
            if(clazz.isInheritor(robotClass, true)) {
                for(cons in clazz.constructors) {
                    LOG.debug("We have a constructor $cons. This is its body ${cons.body}")
                    LOG.debug("here are its children")
                    if(cons.children.isNotEmpty()) {
                        manager.createProblemDescriptor(cons, "Robot initialization code in constructor (Use robotInit() instead).", RefactorOutConstructorQuickFix(), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, isOnTheFly)
                    }
                }
            }
        }
        return problems.toTypedArray()
    }

    override fun getDisplayName(): String {
        return "CustomConstructorRobotLint"
    }

    override fun getGroupDisplayName(): String {
        return "frc.team4069.saturn.lint"
    }

    inner class RefactorOutConstructorQuickFix : LocalQuickFixBase("Refactor out constructor") {
        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val constructorElement = descriptor.psiElement as PsiMethod
            val clazz = constructorElement.containingClass as KtClass
            for(decl in clazz.getBody()?.declarations ?: emptyList()) {
                if(decl is KtNamedFunction) {
                    if(decl.name == "robotInit") {
                        for(expr in constructorElement.body!!.statements) {
                            decl.add(expr)
                        }
                    }
                }
            }
        }
    }

    companion object {
        val LOG = Logger.getInstance(this::class.java.name)
    }
}