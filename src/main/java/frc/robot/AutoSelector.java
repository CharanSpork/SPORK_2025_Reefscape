package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;

public class AutoSelector {
    private SendableChooser<Command> autoChooser = new SendableChooser<>();
    
        public AutoSelector() {
            // Load all paths into the chooser
            
            autoChooser = AutoBuilder.buildAutoChooser();
        // autoChooser.setDefaultOption("Default Path", );

        // Add the chooser to Shuffleboard or SmartDashboard
        Shuffleboard.getTab("Autonomous").add("Auto Selector", autoChooser);
    }

    public Command getSelectedAuto() {
        return autoChooser.getSelected();
    }
}

