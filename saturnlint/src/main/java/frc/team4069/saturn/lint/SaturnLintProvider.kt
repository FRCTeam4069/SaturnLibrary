package frc.team4069.saturn.lint

import com.intellij.codeInspection.InspectionToolProvider

class SaturnLintProvider : InspectionToolProvider {
    override fun getInspectionClasses(): Array<Class<out Any>> = arrayOf(IncorrectSpellingLint::class.java)
}