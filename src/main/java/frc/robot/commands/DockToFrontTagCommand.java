package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.vision.VisionIOLimelight;
import frc.robot.subsystems.drive.Drive;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class DockToFrontTagCommand {
    public static Command create(VisionIOLimelight vision, Drive drive) {
        return Commands.run(
            () -> {
                var frontTag = vision.getTagDirectlyInFront();
                if (frontTag.isPresent()) {
                    VisionIOLimelight.PoseObservation targetTag = frontTag.get();

                    // Align horizontally
                    double tx = targetTag.pose().getTranslation().getX(); // Horizontal offset
                    if (Math.abs(tx) > 0.1) { // Threshold for alignment
                        drive.runVelocity(new ChassisSpeeds(0.0, 0.0, tx * 0.1)); // Proportional control for rotation
                    }

                    // Approach target
                    double distance = calculateDistance(targetTag.pose());
                    if (distance > 0.5) {
                        drive.runVelocity(new ChassisSpeeds(0.5, 0.0, 0.0)); // Drive forward at half speed
                    } else {
                        drive.stop();
                    }
                } else {
                    drive.stop();
                }
            },
            drive // Declare subsystem dependency
        );
    }

    private static double calculateDistance(Pose3d pose) {
        // Calculate Euclidean distance in the XZ plane (ignoring Y for horizontal distance)
        return Math.sqrt(Math.pow(pose.getX(), 2) + Math.pow(pose.getZ(), 2));
    }
}
