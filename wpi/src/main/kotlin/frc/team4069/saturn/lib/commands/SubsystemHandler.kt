package frc.team4069.saturn.lib.commands

internal object SubsystemHandler {
    private val registeredSubsystems = arrayListOf<SaturnSubsystem>()

    fun add(subsystem: SaturnSubsystem) {
        registeredSubsystems.add(subsystem)
    }

    fun lateInit() = registeredSubsystems.forEach(SaturnSubsystem::lateInit)

    fun autoReset() = registeredSubsystems.forEach(SaturnSubsystem::autoReset)

    fun teleopReset() = registeredSubsystems.forEach(SaturnSubsystem::teleopReset)

    fun setNeutral() = registeredSubsystems.forEach(SaturnSubsystem::setNeutral)
}