package frc.robot.util;

import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.vision.VisionIO;

/** Utility class for executing predefined robot actions. */
public class RobotActions {

    public static void executeDockToClosestAprilTag(Drive drive, VisionIO vision) {
        // Instantiate DockingController
        DockingController dockingController = new DockingController(drive, vision);

        // Start docking process
        dockingController.startDocking();

        // Continuously execute docking logic until complete
        while (true) {
            dockingController.executeDocking();

            // Check if docking is complete (dockToClosestAprilTag should manage its own termination)
            if (!isDockingActive(dockingController)) {
                break;
            }

            // Add a small delay to prevent busy looping (adjust as necessary)
            try {
                Thread.sleep(20); // 20ms delay (50Hz update rate)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Docking process interrupted!");
                break;
            }
        }

        System.out.println("Docking complete or process terminated!");
    }

    private static boolean isDockingActive(DockingController dockingController) {
        // This can be adjusted based on the DockingController's internal state logic
        return dockingController.isActive();
    }
}
