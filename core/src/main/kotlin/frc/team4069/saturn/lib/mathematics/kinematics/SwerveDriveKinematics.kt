/*
 * Copyright 2019 Lo-Ellen Robotics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// This file is a Kotlin translation of SwerveDriveKinematics from wpilibj 2020
package frc.team4069.saturn.lib.mathematics.kinematics

import frc.team4069.saturn.lib.mathematics.twodim.geometry.Rotation2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Translation2d
import frc.team4069.saturn.lib.mathematics.units.*
import frc.team4069.saturn.lib.mathematics.units.conversions.LinearVelocity
import org.ejml.simple.SimpleMatrix
import kotlin.math.hypot

class SwerveDriveKinematics(vararg val modules: Translation2d) {

    val numModules = modules.size
    private val inverseKinematics = SimpleMatrix(numModules * 2, 3)
    private val forwardKinematics: SimpleMatrix
    private var prevCoR = Translation2d()

    init {
        if(modules.size < 2) {
            throw IllegalArgumentException("A swerve drive requires at least 2 modules.")
        }

        for(i in 0 until numModules) {
            inverseKinematics.setRow(i * 2 + 0, 0, 1.0, 0.0, -modules[i].y.value)
            inverseKinematics.setRow(i * 2 + 1, 0, 0.0, 1.0, +modules[i].x.value)
        }
        forwardKinematics = inverseKinematics.pseudoInverse()
    }

    fun toSwerveModuleStates(chassisSpeeds: ChassisSpeeds, centerOfRotation: Translation2d = Translation2d()): List<SwerveModuleState> {
        if(centerOfRotation.x != prevCoR.x || centerOfRotation.y != prevCoR.y) {
            for(i in 0 until numModules) {
                inverseKinematics.setRow(i * 2 + 0, 0, 1.0, 0.0, -modules[i].y.value + centerOfRotation.y.value)
                inverseKinematics.setRow(i * 2 + 1, 0, 0.0, 1.0, +modules[i].x.value - centerOfRotation.x.value)
            }
            prevCoR = centerOfRotation
        }

        val chassisSpeedsVector = SimpleMatrix(3, 1)
        chassisSpeedsVector.setColumn(0, 0,
                chassisSpeeds.vx.value, chassisSpeeds.vy.value,
                chassisSpeeds.w.value)

        var modulesMatrix = inverseKinematics.mult(chassisSpeedsVector)
        val moduleStates = mutableListOf<SwerveModuleState>()

        for(i in 0 until numModules) {
            val x = modulesMatrix.get(i * 2, 0)
            val y = modulesMatrix.get(i * 2 + 1, 0)

            val speed = hypot(x, y)
            val angle = Rotation2d(x, y, true)
            moduleStates += SwerveModuleState(speed.meter.velocity, angle)
        }

        return moduleStates
    }

    fun toChassisSpeeds(vararg wheelStates: SwerveModuleState): ChassisSpeeds {
        if(wheelStates.size != numModules) {
            throw RuntimeException("no")
        }

        val moduleStatesMatrix = SimpleMatrix(numModules * 2, 1)
        for(i in 0 until numModules) {
            val module = wheelStates[i]
            moduleStatesMatrix.set(i * 2, 0, module.speed.value * module.angle.cos)
            moduleStatesMatrix.set(i * 2 + 1, module.speed.value * module.angle.sin)
        }

        val chassisSpeedVector = forwardKinematics.mult(moduleStatesMatrix)
        return ChassisSpeeds(chassisSpeedVector.get(0, 0).meter.velocity,
                chassisSpeedVector.get(1, 0).meter.velocity, chassisSpeedVector.get(2, 0).radian.velocity)
    }

    companion object {
        fun normalizeWheelSpeeds(moduleStates: List<SwerveModuleState>, attainableMaxSpeed: SIUnit<LinearVelocity>) {
            val maxSpeed = moduleStates.maxBy { it.speed.value }!!.speed
            if(maxSpeed > attainableMaxSpeed) {
                for(module in moduleStates) {
                    module.speed = module.speed / maxSpeed * attainableMaxSpeed
                }
            }
        }
    }
}
