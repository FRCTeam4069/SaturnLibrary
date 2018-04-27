package frc.team4069.saturn.lib.hid

/**
 * Represents a button on a human interface device
 */
enum class ButtonType(val id: Int) {
    A(1),
    B(2),
    X(3),
    Y(4),
    BUMPER_LEFT(5),
    BUMPER_RIGHT(6),
    BACK(7),
    START(8)
}