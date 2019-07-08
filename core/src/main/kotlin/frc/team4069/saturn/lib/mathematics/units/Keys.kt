package frc.team4069.saturn.lib.mathematics.units

/**
 * A method of encapsulating an arbitrary unit in a type constructor
 *
 * The object implementors of this class are 4/7 SI base units that are used in FRC
 * The class implementors represent compositions of those base units to represent some other derived value
 */
sealed class Key

object Meter : Key()
object Second : Key()
object Kilogram : Key()
object Ampere : Key()

/**
 * While not technically an SI base unit, angle measurements are very important for FRC, thus the radian
 * will be treated as a base unit by this library
 */
object Radian : Key()

/**
 * Represents the construct T/U. example usage is Velocity: Fraction<Length, Time>
 */
class Fraction<Numer: Key, Denom: Key> : Key()

/**
 * Represents the construct T * U. Example usage is Coulomb: Mult<Amp, Time>
 */
class Mult<LHS: Key, RHS: Key> : Key()

/**
 * Represents the construct 1/U. Example usage is Hertz: Inverse<Time>
 */
class Inverse<T: Key> : Key()