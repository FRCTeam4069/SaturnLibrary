package frc.team4069.saturn.lib.mathematics.units

fun <T: Key> hypot(a: SIUnit<T>, b: SIUnit<T>) = SIUnit<T>(kotlin.math.hypot(a.value, b.value))

fun <T: Key> sign(value: SIUnit<T>) = kotlin.math.sign(value.value)

fun <T: Key> abs(value: SIUnit<T>) = value.absoluteValue

fun <T: Key> sqrt(value: SIUnit<Mult<T, T>>) = SIUnit<T>(kotlin.math.sqrt(value.value))

