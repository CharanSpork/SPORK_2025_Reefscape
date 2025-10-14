# Encoder-Based Elevator Solution

This solution provides an alternative to the broken string coder by using motor encoders for elevator position feedback.

## Files Created

### Core Classes
- **`EncoderPositionReader.java`** - Encoder-based position reader that replaces StringCoderReader
- **`ElevatorSubsystemWithEncoder.java`** - New elevator subsystem using encoder feedback
- **`SetElevatorHeightCommandWithEncoder.java`** - Command for setting elevator height with encoder
- **`TestElevatorEncoderCommand.java`** - Test command for calibration and verification

## How to Use

### 1. Replace Your ElevatorSubsystem
Instead of using the original `ElevatorSubsystem`, use `ElevatorSubsystemWithEncoder`:

```java
// OLD: Original string coder version
ElevatorSubsystem elevator = new ElevatorSubsystem();

// NEW: Encoder-based version
ElevatorSubsystemWithEncoder elevator = new ElevatorSubsystemWithEncoder();
```

### 2. Update Your Commands
Use the encoder-specific commands:

```java
// OLD: String coder command
new SetElevatorHeightCommand(elevator, 10.0, false);

// NEW: Encoder command
new SetElevatorHeightCommandWithEncoder(elevator, 10.0, false);
```

### 3. Calibrate the System
Before using, you need to calibrate the encoder system:

```java
// Reset position when elevator is at bottom
elevator.resetPosition();

// Test the system
new TestElevatorEncoderCommand(elevator, false);
```

## Configuration

### Adjust These Constants in ElevatorSubsystemWithEncoder:

```java
public static class ElevatorConstants {
    // Adjust based on your elevator mechanics
    public static final double gear_ratio = 10.0; // Motor rotations per elevator movement
    
    // Adjust based on your pulley size
    public static final double pulley_diameter_inches = 2.0; // Pulley diameter in inches
}
```

## Advantages

- ✅ **No hardware changes** - Uses existing SparkMax motors
- ✅ **More reliable** - Motor encoders are more robust than string coders
- ✅ **Better accuracy** - Higher resolution than analog string coders
- ✅ **Easy to implement** - Drop-in replacement for string coder system
- ✅ **Maintains compatibility** - Same interface as original system

## Testing

1. **Deploy the code** to your robot
2. **Reset position** when elevator is at bottom: `elevator.resetPosition()`
3. **Test position readings** using `TestElevatorEncoderCommand`
4. **Adjust parameters** if position readings are incorrect
5. **Use normally** with your existing elevator commands

## Troubleshooting

### Position readings are wrong
- Check your `gear_ratio` and `pulley_diameter_inches` values
- Verify the encoder is reading correctly: `getRawEncoderPosition()`

### Position resets unexpectedly
- Make sure you're not calling `resetPosition()` accidentally
- Check for encoder disconnections

### PID control doesn't work
- Verify `getHeight()` returns correct values
- Check that the position units match your PID setpoints

## Migration Guide

To switch from string coder to encoder:

1. **Replace subsystem instantiation**:
   ```java
   // Change this
   ElevatorSubsystem elevator = new ElevatorSubsystem();
   
   // To this
   ElevatorSubsystemWithEncoder elevator = new ElevatorSubsystemWithEncoder();
   ```

2. **Update command usage**:
   ```java
   // Change this
   new SetElevatorHeightCommand(elevator, height, stopper);
   
   // To this
   new SetElevatorHeightCommandWithEncoder(elevator, height, stopper);
   ```

3. **Add calibration**:
   ```java
   // Reset position when elevator is at bottom
   elevator.resetPosition();
   ```

That's it! Your elevator will now use motor encoders instead of the broken string coder.
