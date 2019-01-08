package frc.team4069.saturn.lib.motor

import frc.team4069.saturn.lib.mathematics.units.Time
import frc.team4069.saturn.lib.mathematics.units.millisecond
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnit
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitModel.Companion.kDefaultSensorUnitsPerRotation
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitSensorModel

class NativeSaturnSRX(id: Int, sensorUnitsPerRotation: NativeUnit = kDefaultSensorUnitsPerRotation,
                      timeout: Time = 10.millisecond) : SaturnSRX<NativeUnit>(id, NativeUnitSensorModel(sensorUnitsPerRotation), timeout)

