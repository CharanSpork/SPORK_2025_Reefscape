# Encoder Position Calculations

## String Coder Analysis
- **Original string coder factor**: 80/5 = 16 inches per volt
- **String coder range**: 0-5V = 0-80 inches
- **Current positions used**:
  - A Button: 49 inches (highest)
  - B Button: 28.5 inches (medium-high)
  - Y Button: 14 inches (medium)
  - X Button: 3.3 inches (lowest)

## Encoder System Configuration
- **Gear ratio**: 10:1 (motor rotations per elevator movement)
- **Pulley diameter**: 2.0 inches
- **Conversion factor**: (π × 2.0) / 10.0 = 0.628 inches per motor rotation

## Recommended Encoder Positions

The encoder positions should match the string coder positions in inches:

### Current Controller Bindings (Keep These Values)
```java
// These positions are already correct in inches
aButton.onTrue(new SetElevatorHeightCommandWithEncoder(elevator, 49, false));    // Highest
bButton.onTrue(new SetElevatorHeightCommandWithEncoder(elevator, 28.5, false));  // Medium-high
yButton.onTrue(new SetElevatorHeightCommandWithEncoder(elevator, 14, false));    // Medium
xButton.onTrue(new SetElevatorHeightCommandWithEncoder(elevator, 3.3, false));   // Lowest
```

## Why These Values Work

1. **Same units**: Both string coder and encoder system use inches
2. **Same range**: 0-80 inches total travel
3. **Same positions**: The controller bindings already use the correct inch values
4. **No conversion needed**: The EncoderPositionReader handles the conversion internally

## Calibration Steps

1. **Reset position** when elevator is at bottom:
   ```java
   elevator.resetPosition();
   ```

2. **Test each position**:
   - Move to each button position
   - Verify the elevator reaches the correct height
   - Adjust gear ratio or pulley diameter if needed

3. **Fine-tune if necessary**:
   - If positions are too high/low, adjust the gear ratio
   - If positions are too close/far apart, adjust the pulley diameter

## Expected Behavior

- **X Button (3.3")**: Elevator at lowest position
- **Y Button (14")**: Elevator at medium height
- **B Button (28.5")**: Elevator at medium-high position  
- **A Button (49")**: Elevator at highest position

The encoder system should provide the same functionality as the string coder with better reliability and accuracy.
