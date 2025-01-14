package frc.robot.util;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.vision.VisionIO;
import frc.robot.commands.DriveCommands;
import frc.robot.subsystems.drive.Drive;
import java.util.concurrent.ExecutorService;

public class ControllerBindings {
    private final CommandXboxController controller;
    private final Drive drive;
    private final VisionIO vision;
    private final ExecutorService executorService;
    private final ControllerProfiles.ControllerProfile activeProfile;

    public ControllerBindings(CommandXboxController controller, Drive driveSubsystem, VisionIO vision, ExecutorService executorService) {
        this.controller = controller;
        this.drive = driveSubsystem;
        this.vision = vision;
        this.executorService = executorService;

        // Detect and set the active controller profile
        this.activeProfile = ControllerProfiles.detectControllerProfile();
    }

    /**
     * Configures button bindings dynamically based on the detected controller profile.
     * This is where we map our functional classes to controller buttons
     */
    public void configureButtonBindings() {
        drive.setDefaultCommand(DriveCommands.joystickDrive(
                drive,
                // Pay attention to the fact that some of these are inverted
                () -> controller.getRawAxis(activeProfile.leftYAxis), // Forward/backward
                () -> controller.getRawAxis(activeProfile.leftXAxis), // Strafe
                () -> -controller.getRawAxis(activeProfile.rightXAxis) // Rotation
            ));

        // Example: Button A - Drive forward at half speed
        controller.button(activeProfile.buttonA)
            .onTrue(Commands.runOnce(() -> executorService.submit(() -> {
                try {
                    System.out.println("Async docking started");
                    RobotActions.executeDockToClosestAprilTag(drive, vision);
                    System.out.println("Async docking completed");
                } catch (Exception e) {
                    System.err.println("Error during async docking: " + e.getMessage());
                }
            })));

        // Example: Button B - Stop the drive subsystem
        //controller.button(activeProfile.buttonB)
        //    .onTrue(null);

        // Example: Button X - Drive backward at half speed
        //controller.button(activeProfile.buttonX)
        //    .onTrue(null);

        // Example: Left Trigger - Rotate left
        //new Trigger(() -> controller.getRawAxis(activeProfile.leftTriggerAxis) > 0.5)
        //    .onTrue(null);

        // Example: Right Trigger - Rotate right
        //new Trigger(() -> controller.getRawAxis(activeProfile.leftTriggerAxis) > 0.5)
        //    .onTrue(null);

        // Right Bumper: Perform a toggle action
        //controller.button(activeProfile.rightBumper)
        //    .onTrue(null);
    }
}