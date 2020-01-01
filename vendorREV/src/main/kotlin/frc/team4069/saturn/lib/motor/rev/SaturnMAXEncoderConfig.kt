package frc.team4069.saturn.lib.motor.rev

/**
 * Configuration specifying which encoder will be exposed by SaturnMAX.
 */
sealed class SaturnMAXEncoderConfig {
    /**
     * Specifies that the desired encoder is the hall effect sensor built in to the NEO.
     */
    object HallEffectEncoder : SaturnMAXEncoderConfig()

    /**
     * Specifies that the desired encoder is an alternate encoder connected to the data port on the front of the Spark.
     */
    data class AlternateEncoder(val cpr: Int = 4096) : SaturnMAXEncoderConfig()
}