package frc.team4069.saturn.lib

import edu.wpi.first.wpilibj.shuffleboard.EventImportance
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard


/**
 * Top level helper functions for logging using Shuffleboard event markers
 */

fun info(message: String) = Shuffleboard.addEventMarker(message, EventImportance.kLow)

fun debug(message: String) = Shuffleboard.addEventMarker(message, EventImportance.kTrivial)

fun warn(message: String) = Shuffleboard.addEventMarker(message, EventImportance.kNormal)

fun error(message: String) = Shuffleboard.addEventMarker(message, EventImportance.kHigh)

fun fatal(message: String) = Shuffleboard.addEventMarker(message, EventImportance.kCritical)
