package frc.robot;

import frc.robot.games.reefscape2025.subsystems.drive.DriveConstants;

public class GlobalConstants {
    private GlobalConstants() {}

    // Sub-constants will be accessed as nested classes here
    public static class driveConstants extends DriveConstants {
        /*
        This class abstracts the game constants to allow access from
        the common libraries. To update or add constants files, simply
        import the additional constants classes and extend in the same way.
        */
    }

    public final class runMode {
        public static final Mode currentMode = Mode.REAL;
    
        public enum Mode {
            /** Running on a real robot. */
            REAL,
    
            /** Running a physics simulator. */
            SIM,
    
            /** Replaying from a log file. */
            REPLAY
        }
    }
}