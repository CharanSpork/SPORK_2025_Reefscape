package frc.robot.common.subsystems.stringcoder;

import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Encoder-based position reader to replace StringCoderReader.
 * Uses a SparkMax motor's integrated encoder to calculate elevator position.
 */
public class EncoderPositionReader extends SubsystemBase {
    private final RelativeEncoder encoder;
    private final double gearRatio; // Gear ratio from motor to elevator
    private final double pulleyDiameter; // Diameter of the pulley/winch in inches
    private final double encoderToDistanceFactor; // Conversion factor: encoder rotations to distance
    
    private double zeroPosition = 0.0; // Encoder position when elevator is at bottom
    private double currentPosition = 0.0;

    /**
     * Constructor for the EncoderPositionReader.
     *
     * @param encoder The SparkMax encoder to use for position feedback
     * @param gearRatio Gear ratio from motor to elevator (motor rotations per elevator rotation)
     * @param pulleyDiameter Diameter of the pulley/winch in inches
     */
    public EncoderPositionReader(RelativeEncoder encoder, double gearRatio, double pulleyDiameter) {
        this.encoder = encoder;
        this.gearRatio = gearRatio;
        this.pulleyDiameter = pulleyDiameter;
        
        // Calculate conversion factor: encoder rotations -> distance in inches
        // Distance = (encoder rotations / gear ratio) * (pulley diameter * π)
        this.encoderToDistanceFactor = (Math.PI * pulleyDiameter) / gearRatio;
        
        // Set the current position as zero (bottom of elevator)
        zeroPosition = encoder.getPosition();
    }

    /**
     * Get the measured distance from the encoder.
     *
     * @return Distance in inches from the bottom position.
     */
    public double getDistance() {
        double encoderPosition = encoder.getPosition();
        currentPosition = (encoderPosition - zeroPosition) * encoderToDistanceFactor;
        return currentPosition;
    }

    /**
     * Reset the zero position to the current encoder position.
     * Call this when the elevator is at a known position (like bottom).
     */
    public void resetZeroPosition() {
        zeroPosition = encoder.getPosition();
        currentPosition = 0.0;
    }

    /**
     * Set the zero position to a specific encoder value.
     * Useful for calibration or when you know the exact encoder position.
     *
     * @param encoderPosition The encoder position to set as zero
     */
    public void setZeroPosition(double encoderPosition) {
        zeroPosition = encoderPosition;
        currentPosition = 0.0;
    }

    /**
     * Get the current encoder position (for debugging).
     *
     * @return Current encoder position in rotations
     */
    public double getEncoderPosition() {
        return encoder.getPosition();
    }

    /**
     * Get the zero position (for debugging).
     *
     * @return Zero position in encoder rotations
     */
    public double getZeroPosition() {
        return zeroPosition;
    }

    @Override
    public void periodic() {
        // Update the current position
        getDistance();
        
        // Optional: Log the current distance for debugging
        // System.out.println("Encoder Distance: " + currentPosition + " inches");
    }
}