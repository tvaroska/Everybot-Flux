# DriveCommand

## Purpose and Functionality
The `DriveCommand` provides the primary driving control for the robot during teleoperated periods. It implements arcade drive by default, taking joystick inputs and translating them into motor commands for robot movement. This command serves as the default driving interface for human operators.

## Subsystems Required/Used
- **Primary**: `DriveSubsystem` - Controls left and right drive motor groups

## Parameters and Constructor Details
```java
public DriveCommand(DriveSubsystem driveSubsystem, 
                   DoubleSupplier xSpeed, 
                   DoubleSupplier zRotation, 
                   BooleanSupplier squareInputs)
```

**Parameters:**
- `driveSubsystem`: The DriveSubsystem instance for motor control
- `xSpeed`: Supplier for forward/backward speed input (typically joystick Y-axis)
- `zRotation`: Supplier for rotational speed input (typically joystick X-axis)
- `squareInputs`: Supplier for input squaring toggle (typically button state)

**Dependencies:**
- Requires exclusive access to the DriveSubsystem via `addRequirements(m_drive)`

## Key Methods

### initialize()
- **Purpose**: Called when command is first scheduled
- **Implementation**: Currently empty - no initialization required
- **Behavior**: Command begins operation immediately

### execute()
- **Purpose**: Called continuously while command is active (~50 times per second)
- **Implementation**: Calls `m_drive.driveArcade()` with current joystick values
- **Behavior**: Translates joystick inputs to motor commands every cycle

### end(boolean interrupted)
- **Purpose**: Called when command ends or is interrupted
- **Implementation**: Currently empty - no cleanup required
- **Note**: DriveSubsystem handles motor stopping internally

### isFinished()
- **Purpose**: Determines when command should end
- **Implementation**: Always returns `false`
- **Behavior**: Command runs indefinitely as default drive control

## Usage Examples and When to Use

### Typical Usage
```java
// Create drive command with controller inputs
DriveCommand driveCommand = new DriveCommand(
    robotContainer.getDriveSubsystem(),
    () -> -driverController.getLeftY(),      // Forward/backward (inverted)
    () -> driverController.getRightX(),      // Left/right rotation
    () -> driverController.getRightBumper()  // Input squaring toggle
);

// Set as default command for drive subsystem
driveSubsystem.setDefaultCommand(driveCommand);
```

### Controller Binding Examples
```java
// Standard arcade drive setup
DriveCommand standardDrive = new DriveCommand(
    driveSubsystem,
    () -> -driverController.getLeftY(),      // Y-axis (inverted for forward)
    () -> driverController.getRightX(),      // X-axis for turning
    () -> false                              // No input squaring
);

// Advanced setup with input squaring
DriveCommand advancedDrive = new DriveCommand(
    driveSubsystem,
    () -> -driverController.getLeftY(),
    () -> driverController.getRightX(),
    () -> driverController.getRightBumper()  // Toggle input squaring
);

// Slow mode driving
DriveCommand slowDrive = new DriveCommand(
    driveSubsystem,
    () -> -driverController.getLeftY() * 0.5,  // 50% speed limit
    () -> driverController.getRightX() * 0.5,
    () -> true                                  // Always square inputs
);
```

### When to Use
- **Default Operation**: Set as default command for drive subsystem
- **Teleoperated Period**: Primary driving during human control
- **Practice/Testing**: Manual robot control for testing and practice
- **Competition Driving**: Main driving interface for drivers

## Special Features and State Management

### Arcade Drive Implementation
- **Forward/Backward**: Left joystick Y-axis controls forward/reverse motion
- **Rotation**: Right joystick X-axis controls turning/rotation
- **Differential Drive**: Translates arcade inputs to left/right wheel speeds
- **Real-time Control**: Immediate response to joystick movements

### Input Squaring Feature
```java
// Input squaring provides better handling control
m_drive.driveArcade(m_xSpeed.getAsDouble(), 
                   m_zRotation.getAsDouble(), 
                   m_squared.getAsBoolean());
```

**Benefits of Input Squaring:**
- **Fine Control**: Better precision at low speeds
- **Natural Feel**: More intuitive joystick response
- **Driver Preference**: Can be toggled based on driver comfort

### Continuous Operation
- **Never Finishes**: `isFinished()` returns `false` for continuous control
- **Default Command**: Typically set as default command for drive subsystem
- **Interruptible**: Can be interrupted by autonomous or other drive commands

### Safety Features
- **Subsystem Protection**: Exclusive access prevents conflicting drive commands
- **Input Validation**: DriveSubsystem handles input clamping and safety
- **Emergency Stop**: Can be interrupted for emergency situations

### Performance Characteristics
- **Response Time**: Immediate joystick-to-motor response (~20ms cycle)
- **Precision**: High precision control with optional input squaring
- **Reliability**: Simple, robust implementation with minimal failure points
- **Efficiency**: Direct input-to-output mapping for maximum responsiveness

### Integration with Drive Subsystem

#### Drive Modes
The command works with whatever drive mode is implemented in DriveSubsystem:
- **Arcade Drive**: Default implementation (forward/turn inputs)
- **Tank Drive**: Can be modified to support tank drive
- **Curvature Drive**: Alternative implementation option

#### Speed Limiting
```java
// Speed limiting handled in DriveSubsystem
public void driveArcade(double xSpeed, double zRotation, boolean squareInputs) {
    // Apply speed limits from DriveConstants
    xSpeed = MathUtil.clamp(xSpeed, -SPEED_LIMIT, SPEED_LIMIT);
    zRotation = MathUtil.clamp(zRotation, -SPEED_LIMIT, SPEED_LIMIT);
    
    // Optional input squaring
    if (squareInputs) {
        xSpeed = Math.copySign(xSpeed * xSpeed, xSpeed);
        zRotation = Math.copySign(zRotation * zRotation, zRotation);
    }
    
    // Send to differential drive
    differentialDrive.arcadeDrive(xSpeed, zRotation);
}
```

### Customization Options

#### Alternative Control Schemes
```java
// Tank drive modification
public class TankDriveCommand extends Command {
    public void execute() {
        m_drive.tankDrive(
            m_leftSpeed.getAsDouble(),
            m_rightSpeed.getAsDouble(),
            m_squared.getAsBoolean()
        );
    }
}

// Curvature drive alternative
public class CurvatureDriveCommand extends Command {
    public void execute() {
        m_drive.curvatureDrive(
            m_xSpeed.getAsDouble(),
            m_zRotation.getAsDouble(),
            m_quickTurn.getAsBoolean()
        );
    }
}
```

#### Input Filtering
```java
// Add deadband and rate limiting
DriveCommand filteredDrive = new DriveCommand(
    driveSubsystem,
    () -> MathUtil.applyDeadband(-driverController.getLeftY(), 0.05),
    () -> MathUtil.applyDeadband(driverController.getRightX(), 0.05),
    () -> driverController.getRightBumper()
);
```

### Competition Configuration

#### Driver Preferences
```java
// Configure for specific driver preferences
public class DriverConfig {
    public static final double SPEED_MULTIPLIER = 0.8;    // 80% max speed
    public static final double TURN_MULTIPLIER = 0.7;     // 70% max turn rate
    public static final double DEADBAND = 0.05;           // 5% deadband
    public static final boolean DEFAULT_SQUARED = true;   // Input squaring on
}
```

#### Multiple Driver Support
```java
// Switch between driver configurations
Command driver1Config = new DriveCommand(/* driver 1 settings */);
Command driver2Config = new DriveCommand(/* driver 2 settings */);

// Bind to controller selection
driverController.start().onTrue(
    Commands.runOnce(() -> driveSubsystem.setDefaultCommand(driver1Config))
);
```

### Troubleshooting Guide

#### Common Issues
- **No Response**: Check controller connections, joystick mappings
- **Inverted Controls**: Verify axis inversion and motor directions
- **Jerky Movement**: Check for mechanical binding, adjust deadbands
- **Poor Precision**: Enable input squaring, adjust speed limits

#### Performance Optimization
- **Input Tuning**: Adjust deadbands and scaling for driver comfort
- **Speed Limits**: Set appropriate maximum speeds for control
- **Input Squaring**: Enable for better low-speed control
- **Rate Limiting**: Add for smoother acceleration if needed

### Advanced Features

#### Sensor Integration
```java
// Example: Gyro-assisted driving
public class GyroAssistedDrive extends DriveCommand {
    @Override
    public void execute() {
        double gyroCorrection = calculateGyroCorrection();
        m_drive.driveArcade(
            m_xSpeed.getAsDouble(),
            m_zRotation.getAsDouble() + gyroCorrection,
            m_squared.getAsBoolean()
        );
    }
}
```

#### Field-Oriented Drive
```java
// Field-oriented driving capability
public void executeFieldOriented() {
    double robotAngle = gyro.getRotation2d().getRadians();
    // Transform joystick inputs to field coordinates
    // Apply field-oriented control
}
```