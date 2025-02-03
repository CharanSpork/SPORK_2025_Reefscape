package frc.robot.util;

import frc.robot.commands.ElevatorConstants;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.vision.VisionIO;
import frc.robot.util.ControllerProfiles;
import frc.robot.util.ControllerProfiles.ControllerProfile;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.ElevatorCommands;
import frc.robot.commands.ElevatorConstants;


/** Utility class for executing predefined robot actions. */
public class RobotActions {
    static ElevatorCommands elevator;

    public static void executeDockToClosestAprilTag(Drive drive, VisionIO vision) {
        DockingController dockingController = new DockingController(vision, drive);
        dockingController.initiateDocking();
    }
    // The functions below are for moving the elevator up and down to the level you want
    public static void movetoL1() {
        elevator.set_height(ElevatorConstants.L1);
    }
    public static void movetoL2() {
        elevator.set_height(ElevatorConstants.L2);
    }
    public static void movetoL3() {
        elevator.set_height(ElevatorConstants.L3);
    }
    public static void movetoL4() {
        elevator.set_height(ElevatorConstants.L4);
    }

}
