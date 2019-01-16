package frc.team4069.saturn.lib.motor

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel.MotorType

class SaturnMAX(id: Int, motorType: MotorType = MotorType.kBrushless) : CANSparkMax(id, motorType)
