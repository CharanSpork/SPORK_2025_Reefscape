package frc.robot.subsystems.vision;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import frc.robot.subsystems.drive.Drive;
import java.util.Optional;

/** Simulated IO implementation for Limelight vision system. */
public class VisionIOLimelightSim implements VisionIO {

    private final String name;
    private final Drive drive;
    private final Transform3d robotToCamera;

    private final VisionIOInputs inputs = new VisionIOInputs();

    /**
     * Constructs a simulated VisionIOLimelight.
     *
     * @param name Name of the camera (simulated).
     * @param drive Drive subsystem (used for pose information, if needed).
     * @param robotToCamera Transform from the robot to the camera.
     */
    public VisionIOLimelightSim(String name, Drive drive, Transform3d robotToCamera) {
        this.name = name;
        this.drive = drive;
        this.robotToCamera = robotToCamera;
    }

    @Override
    public void updateInputs(VisionIOInputs inputs) {
        // Simulate connected status
        inputs.connected = true;

        // Simulate target observation
        inputs.latestTargetObservation = new TargetObservation(
                Rotation2d.fromDegrees(0.0), // Simulated TX
                Rotation2d.fromDegrees(0.0)  // Simulated TY
        );

        // Simulate pose observations
        inputs.poseObservations = new PoseObservation[] {
            new PoseObservation(
                    System.currentTimeMillis() * 1.0e-3,  // Simulated timestamp
                    new Pose3d(2.0, 1.0, 0.0, new Rotation3d()), // Simulated pose
                    0.0,  // Ambiguity
                    1,    // Tag count
                    1.0,  // Average tag distance
                    PoseObservationType.MEGATAG_1
            )
        };

        // Simulate tag IDs
        inputs.tagIds = new int[] {1};
    }

    @Override
    public Optional<PoseObservation> getTagDirectlyInFront() {
        PoseObservation tagInFront = null;
        double smallestTx = Double.MAX_VALUE;

        for (PoseObservation observation : inputs.poseObservations) {
            double tx = observation.pose().getTranslation().getX();
            if (Math.abs(tx) < smallestTx) {
                tagInFront = observation;
                smallestTx = Math.abs(tx);
            }
        }

        return Optional.ofNullable(tagInFront);
    }
}