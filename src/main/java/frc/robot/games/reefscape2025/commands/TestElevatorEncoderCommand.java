package frc.robot.games.reefscape2025.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.games.reefscape2025.ElevatorSubsystemWithEncoder;

/**
 * Test command to verify the encoder-based elevator position system.
 * This command helps you calibrate and test the new encoder implementation.
 */
public class TestElevatorEncoderCommand extends Command {
    private final ElevatorSubsystemWithEncoder elevator;
    private final boolean resetPosition;

    public TestElevatorEncoderCommand(ElevatorSubsystemWithEncoder elevator, boolean resetPosition) {
        this.elevator = elevator;
        this.resetPosition = resetPosition;
        
        addRequirements(elevator);
    }

    @Override
    public void initialize() {
        if (resetPosition) {
            System.out.println("Resetting elevator position to zero...");
            elevator.resetPosition();
        }
        
        System.out.println("=== Elevator Encoder Test ===");
        System.out.println("Current Height: " + elevator.getHeight() + " inches");
        System.out.println("Raw Encoder Position: " + elevator.getRawEncoderPosition() + " rotations");
        System.out.println("Zero Position: " + elevator.getZeroPosition() + " rotations");
    }

    @Override
    public void execute() {
        // Print current status every 0.5 seconds
        if (System.currentTimeMillis() % 500 < 50) {
            System.out.println("Height: " + String.format("%.2f", elevator.getHeight()) + 
                             " | Encoder: " + String.format("%.2f", elevator.getRawEncoderPosition()) + 
                             " | Zero: " + String.format("%.2f", elevator.getZeroPosition()));
        }
    }

    @Override
    public boolean isFinished() {
        return false; // Run indefinitely for testing
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("Elevator encoder test ended.");
    }
}