package frc.robot.games.reefscape2025;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.common.subsystems.stringcoder.EncoderPositionReader;

/**
 * ElevatorSubsystem with encoder-based position feedback.
 * This is an alternative implementation that uses motor encoders instead of string coder.
 * Use this when your string coder is broken or unavailable.
 */
public class ElevatorSubsystemWithEncoder extends SubsystemBase {
    public static class ElevatorConstants {
        public static final int spark_channel_nine = 9;
        public static final int spark_channel_ten = 10;
        
        // Encoder configuration - adjust these values based on your elevator mechanics
        public static final double gear_ratio = 12.0; // Example: 10:1 gear ratio
        public static final double pulley_diameter_inches = 1.8880; // Example: 2-inch diameter pulley
        
        public static final double elevator_speed = 0.4;
        public static final double error = 0.5;
    }

    private final SparkMax elevator_spark_nine;
    private final SparkMax elevator_spark_ten;
    private final EncoderPositionReader positionReader;
    private final double elevator_speed;
    private final double error;
    private double current_height;

    public ElevatorSubsystemWithEncoder() {
        elevator_spark_nine = new SparkMax(ElevatorConstants.spark_channel_nine, MotorType.kBrushless);
        elevator_spark_ten = new SparkMax(ElevatorConstants.spark_channel_ten, MotorType.kBrushless);

        // Get the encoder from the first motor and create position reader
        RelativeEncoder encoder = elevator_spark_nine.getEncoder();
        positionReader = new EncoderPositionReader(
            encoder, 
            ElevatorConstants.gear_ratio, 
            ElevatorConstants.pulley_diameter_inches
        );
        
        elevator_speed = ElevatorConstants.elevator_speed;
        error = ElevatorConstants.error;
        current_height = positionReader.getDistance();
    }

    @Override
    public void periodic() {
        updateHeight();
    }

    public void updateHeight() {
        current_height = positionReader.getDistance();
    }

    public void setElevatorSpeed(double speed) {
        elevator_spark_nine.set(speed);     // Motor 9: Positive up, negative down
        elevator_spark_ten.set(-speed);     // Motor 10: Negative up, positive down
    }

    public double getHeight() {
        return current_height;
    }

    public double getError() {
        return error;
    }

    public double getElevatorSpeed() {
        return elevator_speed;
    }

    /**
     * Reset the elevator position to zero.
     * Call this when the elevator is at the bottom position.
     */
    public void resetPosition() {
        positionReader.resetZeroPosition();
        current_height = 0.0;
    }

    /**
     * Get the raw encoder position for debugging.
     *
     * @return Current encoder position in rotations
     */
    public double getRawEncoderPosition() {
        return positionReader.getEncoderPosition();
    }

    /**
     * Get the zero position for debugging.
     *
     * @return Zero position in encoder rotations
     */
    public double getZeroPosition() {
        return positionReader.getZeroPosition();
    }
}