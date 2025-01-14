package frc.robot.util;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.vision.VisionIO;
import org.littletonrobotics.junction.Logger;

public class DockingController {
    private final Drive drive;
    private final VisionIO vision;
    private Pose2d targetPose;
    private boolean dockingActive = false;

    public DockingController(Drive drive, VisionIO vision) {
        this.drive = drive;
        this.vision = vision;
    }

    public void startDocking() {
        var robotPose = drive.getPose();
        var robotPose3d = new Pose3d(
            robotPose.getTranslation().getX(),
            robotPose.getTranslation().getY(),
            0.0, // Z-coordinate is zero for 2D to 3D conversion
            new Rotation3d(0.0, 0.0, robotPose.getRotation().getRadians())
        );

        var tagsInRange = vision.getTagsInRange(robotPose3d, 3.0);
        if (!tagsInRange.isEmpty()) {
            targetPose = tagsInRange.get(0).pose.toPose2d();
            dockingActive = true;

            Logger.recordOutput("Docking/TargetPose", new double[] {
                targetPose.getX(), targetPose.getY(), targetPose.getRotation().getDegrees()
            });
            System.out.println("Docking started!");
        } else {
            System.out.println("No visible AprilTags to dock to!");
            dockingActive = false;
        }
    }

    public void executeDocking() {
        if (!dockingActive || targetPose == null) {
            return; // Do nothing if docking isn't active
        }

        var currentPose = drive.getPose();
        if (isDocked(currentPose)) {
            drive.stop();
            dockingActive = false;
            System.out.println("Docking complete!");
            return;
        }

        var dockingSpeeds = calculateDockingSpeeds(currentPose, targetPose);
        drive.runVelocity(dockingSpeeds);
    }

    private boolean isDocked(Pose2d currentPose) {
        double distance = currentPose.getTranslation().getDistance(targetPose.getTranslation());
        double angleDifference = Math.abs(currentPose.getRotation().getRadians() - targetPose.getRotation().getRadians());
        
        // These are the distance tolerances for docking
        return distance < 0.1 && angleDifference < Math.toRadians(5.0);
    }

    private ChassisSpeeds calculateDockingSpeeds(Pose2d currentPose, Pose2d targetPose) {
        double dx = targetPose.getX() - currentPose.getX();
        double dy = targetPose.getY() - currentPose.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
    
        double targetRotation = targetPose.getRotation().getRadians();
        double currentRotation = currentPose.getRotation().getRadians();
        double angularError = targetRotation - currentRotation;
    
        // Normalize angular error to the range [-pi, pi]
        angularError = Math.atan2(Math.sin(angularError), Math.cos(angularError));
    
        double angleToTarget = Math.atan2(dy, dx);
        double angleDifference = angleToTarget - currentRotation;
    
        // Normalize angle difference to the range [-pi, pi]
        angleDifference = Math.atan2(Math.sin(angleDifference), Math.cos(angleDifference));
    
        // Proportional control for linear and angular speeds
        double linearSpeed = Math.cos(angleDifference) * distance * 0.5; // Scale for forward motion
        double angularSpeed = angularError * 1.0; // Adjust scaling factor as needed
    
        // Reduce linear speed if angular error is too large
        if (Math.abs(angularError) > Math.toRadians(10)) {
            linearSpeed *= 0.3; // Slow down significantly to focus on angular alignment
        }
    
        return new ChassisSpeeds(linearSpeed, 0.0, angularSpeed);
    }    

    public boolean isActive() {
        return dockingActive; // True if docking is still in progress
    }
}
