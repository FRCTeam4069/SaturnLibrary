package frc.team4069.saturn.lib.vision

import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.units.conversions.inch
import frc.team4069.saturn.lib.mathematics.units.degree
import frc.team4069.saturn.lib.nt.SaturnNetworkTable
import frc.team4069.saturn.lib.nt.delegate
import frc.team4069.saturn.lib.nt.get
import kotlin.properties.Delegates

class LimelightCamera {
    private val llTable = SaturnNetworkTable.getTable("limelight")

    private val _hasTargets = llTable["tv"]
    val hasTargets: Boolean get() = _hasTargets.value.double == 1.0

    val xOffset by llTable["tx"].delegate(0.0)

    val yOffset by llTable["ty"].delegate(0.0)

    val targetArea by llTable["ta"].delegate(0.0)

    val targetSkew by llTable["ts"].delegate(0.0)

    val pipelineLatency by llTable["tl"].delegate(0.0)

    val targetBBShort by llTable["tshort"].delegate(0.0)

    val targetBBLong by llTable["tlong"].delegate(0.0)

    val targetBBHorizontal by llTable["thor"].delegate(0.0)

    val targetBBVertical by llTable["tvert"].delegate(0.0)

    private val _cameraPose = llTable["camtran"]

    val cameraPose: Pose2d
        get() {
            val poseStr = _cameraPose.value.doubleArray

            return Pose2d(poseStr[2].inch, poseStr[0].inch, poseStr[4].degree)
        }


    private val _ledState = llTable["ledMode"]

    var ledState: LEDState by Delegates.observable(LEDState.PipelineValue) { _, _, new ->
        _ledState.setNumber(new.toInt())
    }

    private val _cameraMode = llTable["camMode"]

    var cameraMode: CameraMode by Delegates.observable(CameraMode.VisionProcessor) { _, _, new ->
        _cameraMode.setNumber(new.toInt())
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