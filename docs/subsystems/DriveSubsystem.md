# DriveSubsystem Documentation

## Purpose and Functionality

The `DriveSubsystem` is responsible for controlling the robot's drivetrain. It implements a differential drive system (tank-style drive) that allows the robot to move forward/backward and turn left/right. The subsystem provides both arcade drive and tank drive control modes for maximum flexibility in robot operation.

## Hardware Components Used

### Motors
- **4 x SparkMax Motor Controllers** running brushless motors
  - Left Leader Motor (CAN ID: 18)
  - Left Follower Motor (CAN ID: 19) 
  - Right Leader Motor (CAN ID: 10)
  - Right Follower Motor (CAN ID: 11)

### Configuration
- **Motor Type**: Brushless motors
- **Drive Configuration**: Differential Drive (tank-style)
- **Motor Following**: Follower motors are configured to follow their respective leader motors
- **Voltage Compensation**: 12V for consistent performance across battery voltage levels
- **Current Limiting**: 60A smart current limit to prevent breaker trips
- **Left Side Inversion**: Left side motors are inverted so positive values drive both sides forward

## Key Methods and Parameters

### Constructor: `DriveSubsystem()`
Initializes the drivetrain with proper motor configuration, sets up differential drive, and configures motor parameters.

### `driveArcade(double xSpeed, double zRotation, boolean squared)`
Controls the robot using arcade drive style where one axis controls forward/backward movement and another controls turning.

**Parameters:**
- `xSpeed` (double): Forward/backward speed (-1.0 to 1.0)
- `zRotation` (double): Turning speed (-1.0 to 1.0) 
- `squared` (boolean): Whether to square the controller inputs for finer control at low speeds

**Usage Example:**
```java
// Move forward at 50% speed with no turning, with squared inputs
driveSubsystem.driveArcade(0.5, 0.0, true);

// Turn left at 30% speed with no forward movement
driveSubsystem.driveArcade(0.0, -0.3, false);
```

### `driveTank(double leftSpeed, double rightSpeed, boolean squared)`
Controls the robot using tank drive style where each joystick controls one side of the drivetrain independently.

**Parameters:**
- `leftSpeed` (double): Speed for left side motors (-1.0 to 1.0)
- `rightSpeed` (double): Speed for right side motors (-1.0 to 1.0)
- `squared` (boolean): Whether to square the controller inputs

**Usage Example:**
```java
// Drive straight forward at 50% speed
driveSubsystem.driveTank(0.5, 0.5, true);

// Turn in place - left side backward, right side forward
driveSubsystem.driveTank(-0.3, 0.3, false);
```

## Constants Used from Constants Class

All constants are defined in `Constants.DriveConstants`:

### Motor CAN IDs
- `LEFT_LEADER_ID = 18`: CAN ID for left leader motor
- `LEFT_FOLLOWER_ID = 19`: CAN ID for left follower motor  
- `RIGHT_LEADER_ID = 10`: CAN ID for right leader motor
- `RIGHT_FOLLOWER_ID = 11`: CAN ID for right follower motor

### Motor Configuration
- `DRIVE_MOTOR_CURRENT_LIMIT = 60`: Smart current limit in amps
- `DRIVE_MOTOR_VOLTAGE_COMP = 12`: Voltage compensation level in volts
- `SPEED_LIMIT = 0.8`: Maximum drive speed multiplier
- `SLOW_MODE_MOVE = 0.4`: Reduced speed for precise movement
- `SLOW_MODE_TURN = 0.4`: Reduced speed for precise turning

### Autonomous Constants
- `AUTO_MODE_SPEED = -0.3`: Default autonomous drive speed
- `AUTO_MODE_TIME = 0.6`: Default autonomous drive time in seconds

## Special Features

### Voltage Compensation
The drivetrain uses voltage compensation set to 12V to ensure consistent robot performance regardless of battery charge level. This helps maintain predictable robot behavior throughout a match.

### Current Limiting
Smart current limiting is set to 60A per motor to prevent breaker trips and protect the motors from damage during high-load situations like pushing against obstacles.

### Motor Following Configuration
The subsystem uses a leader-follower configuration where:
- Left follower motor automatically follows the left leader motor
- Right follower motor automatically follows the right leader motor
- This ensures synchronized movement and reduces CAN bus traffic

### Input Squaring
Both drive methods support input squaring, which provides:
- Finer control at low speeds for precise movements
- More aggressive response at high speeds
- Better "feel" for drivers used to traditional RC car controls

## Usage Examples

### Basic Arcade Drive Setup
```java
public class DriveCommand extends CommandBase {
    private final DriveSubsystem driveSubsystem;
    private final Supplier<Double> speedSupplier;
    private final Supplier<Double> rotationSupplier;
    
    public DriveCommand(DriveSubsystem driveSubsystem, 
                       Supplier<Double> speed, 
                       Supplier<Double> rotation) {
        this.driveSubsystem = driveSubsystem;
        this.speedSupplier = speed;
        this.rotationSupplier = rotation;
        addRequirements(driveSubsystem);
    }
    
    @Override
    public void execute() {
        driveSubsystem.driveArcade(
            speedSupplier.get(),
            rotationSupplier.get(),
            true  // Use squared inputs for better control
        );
    }
}
```

### Tank Drive with Speed Limiting
```java
// Apply speed limiting for slow mode
double leftSpeed = leftJoystick.getY() * DriveConstants.SLOW_MODE_MOVE;
double rightSpeed = rightJoystick.getY() * DriveConstants.SLOW_MODE_MOVE;
driveSubsystem.driveTank(leftSpeed, rightSpeed, false);
```

### Emergency Stop
```java
// Stop all drive motors immediately
driveSubsystem.driveArcade(0.0, 0.0, false);
// or
driveSubsystem.driveTank(0.0, 0.0, false);
```

## Technical Notes

- The subsystem extends `SubsystemBase` and can be used with WPILib's command-based programming framework
- CAN timeout is set to 250ms during initialization to allow for proper motor configuration
- The left side is inverted to ensure positive values drive the robot forward
- Motor configuration uses `ResetMode.kResetSafeParameters` and `PersistMode.kPersistParameters` for reliable operation
- The `periodic()` method is empty as no continuous monitoring is required for basic drive operations