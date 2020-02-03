package frc.team4069.saturn.lib.vision

import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import frc.team4069.saturn.lib.nt.SaturnNetworkTable
import frc.team4069.saturn.lib.nt.delegate
import frc.team4069.saturn.lib.nt.get
import kotlin.properties.Delegates

class LimelightCamera {
    private val llTable = SaturnNetworkTable.getTable("limelight")

    val hasTargets by llTable["tv"].delegate(false)

    val xOffset by llTable["tx"].delegate(0.0)

    val yOffset by llTable["ty"].delegate(0.0)

    val targetArea by llTable["ta"].delegate(0.0)

    val targetSkew by llTable["ts"].delegate(0.0)

    val pipelineLatency by llTable["tl"].delegate(0.0)

    val targetBBShort by llTable["tshort"].delegate(0.0)

    val targetBBLong by llTable["tlong"].delegate(0.0)

    val targetBBHorizontal by llTable["thor"].delegate(0.0)

    val targetBBVertical by llTable["tvert"].delegate(0.0)

    private val cameraPoseEntry = llTable["camtran"]

    val cameraPose: Pose2d
        get() {
            val poseStr = cameraPoseEntry.value.string.split(",")

            return Pose2d(poseStr[0].toDouble(), poseStr[1].toDouble(), Rotation2d.fromDegrees(poseStr[4].toDouble()))
        }


    private val ledEntry = llTable["ledMode"]

    var ledState: LEDState by Delegates.observable(LEDState.PipelineValue) { _, _, new ->
        ledEntry.setNumber(new.toInt())
    }

    private val camModeEntry = llTable["camMode"]

    var cameraMode: CameraMode by Delegates.observable(CameraMode.VisionProcessor) { _, _, new ->
        camModeEntry.setNumber(new.toInt())
    }

    var pipeline by llTable["pipeline"].delegate(0.0)

    enum class CameraMode {
        VisionProcessor,
        DriverMode;

        fun toInt(): Int = when(this) {
            VisionProcessor -> 0
            DriverMode -> 1
        }
    }

    enum class LEDState {
        PipelineValue,
        ForceOff,
        ForceBlink,
        ForceOn;

        fun toInt(): Int = when (this) {
            PipelineValue -> 0
            ForceOff -> 1
            ForceBlink -> 2
            ForceOn -> 3
        }
    }
}