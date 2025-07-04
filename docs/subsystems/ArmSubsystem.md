# ArmSubsystem Documentation

## Purpose and Functionality

The `ArmSubsystem` controls the robot's arm mechanism used for positioning game pieces and manipulating objects. It features precise position control using PID feedback with encoder position sensing. The arm can move to predefined positions (up, down, mid) and hold those positions automatically. This subsystem is essential for scoring game pieces at different heights and levels.

## Hardware Components Used

### Motors
- **2 x SparkMax Motor Controllers** running brushless motors
  - Primary Arm Motor (CAN ID: 4)
  - Secondary Arm Motor (CAN ID: 5) - configured to follow the primary motor with inversion
  
### Sensors
- **Relative Encoder**: Built-in encoder from the primary arm motor for position feedback
- **Custom PID Controller**: Custom `PIDCtrl` class for position control

### Configuration
- **Motor Type**: Brushless motors
- **Voltage Compensation**: 10V for consistent performance
- **Current Limiting**: 40A smart current limit
- **Idle Mode**: Coast mode for smooth operation
- **Gear Ratio**: 60:1 (20:1 motor gearbox + 16:48 chain reduction)

## Key Methods and Parameters

### Constructor: `ArmSubsystem()`
Initializes the arm motors, encoder, and PID controller. Sets up motor following configuration and applies safety parameters.

### `run(double speed)`
Manually drives the arm at a specified speed.

**Parameters:**
- `speed` (double): Motor speed from -1.0 to 1.0, with 0 stopping the arm

**Usage Example:**
```java
// Move arm up at 30% speed
armSubsystem.run(0.3);

// Move arm down at 50% speed  
armSubsystem.run(-0.5);

// Stop the arm
armSubsystem.run(0.0);
```

### `runToPosition(double position)`
Drives the arm to a specific encoder position using PID control. This method should be called repeatedly (typically in a command's execute method) until the arm reaches the target position.

**Parameters:**
- `position` (double): Target position in encoder rotations

### `setAngle(boolean up)`
Sets the target angle for the arm to either the up position or down position.

**Parameters:**
- `up` (boolean): true for up position, false for down position

**Usage Example:**
```java
// Set target to up position
armSubsystem.setAngle(true);

// Set target to down position  
armSubsystem.setAngle(false);
```

### `init()`
Initializes the PID controller and resets all control variables. Should be called before starting position control.

### `stop()`
Immediately stops the arm motor.

### `atSetPoint()`
Returns whether the arm is currently at its target position.

**Returns:**
- `boolean`: Currently always returns false (implementation incomplete)

### `isAtPosition(double target, double pos, double velocity)`
Checks if the arm is at a target position within acceptable tolerances.

**Parameters:**
- `target` (double): Target position
- `pos` (double): Current position  
- `velocity` (double): Current velocity

**Returns:**
- `boolean`: true if within position and velocity tolerances

## Constants Used from Constants Class

All constants are defined in `Constants.ArmConstants`:

### Motor Configuration
- `ARM_MOTOR_ID = 4`: CAN ID for primary arm motor
- `ARM_MOTOR_IDR = 5`: CAN ID for secondary arm motor
- `ARM_MOTOR_CURRENT_LIMIT = 40`: Smart current limit in amps
- `ARM_MOTOR_VOLTAGE_COMP = 10`: Voltage compensation in volts

### Speed Settings
- `SpeedUp = -0.4`: Speed for moving arm up
- `SpeedDown = 0.4`: Speed for moving arm down  
- `SpeedHold = 0.2`: Speed for holding position
- `SpeedBackoff = 0.2`: Speed for backing off from position

### Position Settings (in motor shaft rotations)
- `AngleUp = 18 * GearRatio / 360.0`: Up position (18 degrees scaled by gear ratio)
- `AngleDown = -18 * GearRatio / 360.0`: Down position (-18 degrees scaled by gear ratio)  
- `AngleMid = 9 * GearRatio / 360.0`: Middle position (9 degrees scaled by gear ratio)
- `GearRatio = 60`: Overall gear reduction ratio

### PID Control Parameters
- `kP = 0.1`: Proportional gain
- `kD = 0.0`: Derivative gain (currently disabled)
- `kI = 0.0`: Integral gain (currently disabled)
- `ControlOutputMax = 0.4`: Maximum control output
- `ControlOutputMin = 0.05`: Minimum control output
- `PositionDelta = 0.01`: Position tolerance (relative)
- `RPMDelta = 0.1`: Velocity tolerance (absolute)

### Timing Constants
- `ArmTimeUp = 2000`: Time limit for arm up movement in milliseconds

## Special Features

### PID Position Control
The arm uses a custom PID controller (`PIDCtrl`) for precise position control:
- Proportional control provides response proportional to position error
- Derivative and integral terms are available but currently disabled
- Control output is limited to safe ranges to prevent damage
- Position and velocity tolerances ensure accurate positioning

### SmartDashboard Integration
The subsystem provides methods to tune parameters in real-time:

#### `putParams()`
Publishes current parameters to SmartDashboard for monitoring:
```java
armSubsystem.putParams();
```

#### `getParams()`  
Retrieves updated parameters from SmartDashboard for live tuning:
```java
armSubsystem.getParams();
```

Published parameters include:
- Speed settings (Up, Down, Hold)
- Angle positions (Up, Down, Mid)
- PID gains (kP, kD, kI)
- Control limits (Max/Min Output, Position/RPM Delta)

### Safety Features
- **Current Limiting**: 40A limit prevents motor damage
- **Voltage Compensation**: Maintains consistent behavior across battery voltages
- **Output Limiting**: Control outputs are clamped to safe ranges
- **Coast Mode**: Allows manual positioning when disabled

## Usage Examples

### Basic Arm Positioning Command
```java
public class ArmToPositionCommand extends CommandBase {
    private final ArmSubsystem armSubsystem;
    private final boolean upPosition;
    
    public ArmToPositionCommand(ArmSubsystem armSubsystem, boolean up) {
        this.armSubsystem = armSubsystem;
        this.upPosition = up;
        addRequirements(armSubsystem);
    }
    
    @Override
    public void initialize() {
        armSubsystem.init();
        armSubsystem.setAngle(upPosition);
    }
    
    @Override
    public void execute() {
        armSubsystem.runToPosition(0); // Position set by setAngle()
    }
    
    @Override
    public boolean isFinished() {
        return armSubsystem.atSetPoint();
    }
    
    @Override
    public void end(boolean interrupted) {
        armSubsystem.stop();
    }
}
```

### Manual Arm Control
```java
public class ManualArmCommand extends CommandBase {
    private final ArmSubsystem armSubsystem;
    private final Supplier<Double> speedSupplier;
    
    public ManualArmCommand(ArmSubsystem armSubsystem, Supplier<Double> speed) {
        this.armSubsystem = armSubsystem;
        this.speedSupplier = speed;
        addRequirements(armSubsystem);
    }
    
    @Override
    public void execute() {
        double speed = speedSupplier.get();
        // Apply deadband and scaling
        if (Math.abs(speed) < 0.1) {
            speed = 0.0;
        }
        armSubsystem.run(speed * 0.5); // Limit to 50% max speed
    }
    
    @Override
    public void end(boolean interrupted) {
        armSubsystem.stop();
    }
}
```

### Arm Initialization
```java
// Initialize arm for position control
armSubsystem.init();

// Set to up position and run until complete
armSubsystem.setAngle(true);
while (!armSubsystem.atSetPoint()) {
    armSubsystem.runToPosition(0);
    // This would typically be in a command's execute() method
}
```

## Technical Notes

- The encoder position is reset to 0 during initialization
- PID control uses time-based calculations with delta time for accurate control
- Debug output is printed to console showing position, velocity, and control values
- The secondary motor follows the primary motor with inversion for proper operation
- Gear ratio calculations convert between motor shaft rotations and actual arm angles
- Position control requires continuous calling of `runToPosition()` until target is reached
- SmartDashboard integration allows for real-time parameter tuning during testing