package frc.team4069.saturn.lint

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalQuickFixBase
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import org.apache.log4j.Level
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.psi.KtFile

class WpiImportLint : AbstractKotlinInspection() {

    init {
        LOG.setLevel(Level.DEBUG)
    }

    override fun getDisplayName(): String {
        return "WpiImportLint"
    }

    override fun getGroupDisplayName(): String {
        return "frc.team4069.saturn.lint"
    }

    override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor>? {
        if (file !is KtFile) {
            return null
        }

        val problems = mutableListOf<ProblemDescriptor>()

        val packageName = file.packageFqName.asString()

        for (import in file.importDirectives) {
            LOG.debug(import.importedFqName?.asString())
            if (import.importedFqName?.asString()?.contains("wpilibj") == true && !packageName.contains("lib")) {
                problems.add(manager.createProblemDescriptor(import,
                        "WPILib import directive outside of SaturnShell",
                        DeleteImport(),
                        ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                        isOnTheFly))
            }
        }

        return problems.toTypedArray()
    }

    inner class DeleteImport : LocalQuickFixBase("Remove import") {
        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            descriptor.psiElement.delete()
        }

    }

    companion object {
        val LOG = Logger.getInstance(this::class.java)
    }
}