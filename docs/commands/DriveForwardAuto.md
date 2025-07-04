# DriveForwardAuto

## Purpose and Functionality
The `DriveForwardAuto` is a simple autonomous command that drives the robot forward for a specified duration. This command provides a basic autonomous routine that can be used for mobility points, leaving the starting zone, or as a foundation for more complex autonomous sequences.

## Subsystems Required/Used
- **Primary**: `DriveSubsystem` - Controls robot movement and navigation

## Parameters and Constructor Details
```java
public DriveForwardAuto(DriveSubsystem drive)
```

**Parameters:**
- `drive`: The DriveSubsystem instance for autonomous driving control

**Dependencies:**
- Requires exclusive access to the DriveSubsystem via `addRequirements(m_drive)`

## Key Methods

### initialize()
- **Purpose**: Prepares the autonomous routine for execution
- **Implementation**: 
  - Restarts the internal timer using `timer.restart()`
  - Clears any previous timer state for reliable operation
- **Behavior**: Ensures clean start regardless of previous command usage

### execute()
- **Purpose**: Controls robot movement during autonomous execution
- **Implementation**: 
  - Checks elapsed time against `drive_seconds` threshold
  - Drives forward at `AUTO_MODE_SPEED` (30% power) when time < threshold
  - Stops driving when time threshold is exceeded
- **Behavior**: Provides timed forward movement with automatic stopping

### end(boolean interrupted)
- **Purpose**: Safely terminates autonomous routine
- **Implementation**:
  - Stops drive motors with `driveArcade(0.0, 0.0, false)`
  - Stops the internal timer
- **Safety**: Ensures robot stops moving when command completes

### isFinished()
- **Purpose**: Determines when autonomous routine is complete
- **Implementation**: Returns `true` when `timer.get() >= drive_seconds`
- **Behavior**: Automatic termination after specified duration

## Usage Examples and When to Use

### Typical Usage
```java
// Create autonomous command
DriveForwardAuto driveAuto = new DriveForwardAuto(robotContainer.getDriveSubsystem());

// Set as autonomous command
robotContainer.setAutonomousCommand(driveAuto);

// Use in autonomous selector
SendableChooser<Command> autoChooser = new SendableChooser<>();
autoChooser.setDefaultOption("Drive Forward", driveAuto);
autoChooser.addOption("Do Nothing", Commands.none());
```

### When to Use
- **Mobility Points**: Earning autonomous mobility points by leaving starting zone
- **Simple Autonomous**: Basic autonomous when complex routines aren't ready
- **Backup Strategy**: Reliable fallback when main autonomous fails
- **Testing**: Basic movement testing and calibration
- **Competition Safety**: Guaranteed movement for minimum autonomous points

### Autonomous Sequence Integration
```java
// Use as part of larger autonomous
Commands.sequence(
    driveAuto,                                    // Drive forward
    new ArmDownCommand(arm),                      // Prepare for scoring
    new CoralOutCommand(roller).withTimeout(2.0), // Score game piece
    new ArmUpCommand(arm)                         // Return to travel position
).schedule();
```

## Special Features and State Management

### Timer-Based Control
```java
// Internal timing mechanism
private Timer timer = new Timer();
private double drive_seconds = DriveConstants.AUTO_MODE_TIME; // 0.6 seconds
```

**Timer Features:**
- **Automatic Reset**: `timer.restart()` ensures clean start
- **Precision Timing**: WPILib Timer provides accurate timing
- **State Tracking**: Current time available via `timer.get()`

### Configurable Parameters
```java
// Configuration from DriveConstants
AUTO_MODE_SPEED: -0.3    // 30% reverse speed (negative = backward)
AUTO_MODE_TIME: 0.6      // 0.6 seconds duration
```

**Parameter Notes:**
- **Speed**: Negative value (-0.3) drives robot backward
- **Duration**: Short time (0.6s) for controlled movement
- **Safety**: Conservative values prevent over-travel

### State Machine Implementation
1. **Initialization**: Timer reset, ready to begin movement
2. **Driving Phase**: Forward movement at constant speed
3. **Completion**: Automatic stop when time threshold reached
4. **Cleanup**: Motors stopped, timer stopped

### Safety Features
- **Timeout Protection**: Automatic termination prevents infinite running
- **Speed Limiting**: Conservative 30% speed for safe operation
- **Clean Shutdown**: Explicit motor stopping in `end()` method
- **Predictable Behavior**: Simple, testable autonomous routine

### Performance Characteristics
- **Duration**: 0.6 seconds of movement
- **Distance**: Approximately 1-2 feet (depends on robot characteristics)
- **Speed**: 30% power for controlled movement
- **Reliability**: Simple design minimizes failure points
- **Repeatability**: Consistent performance across runs

### Field Positioning Considerations

#### Starting Positions
The command works from any starting position but considerations include:
- **Direction**: Negative speed drives backward from starting orientation
- **Obstacles**: Ensure clear path for movement duration
- **Alliance Area**: Verify movement stays within allowed zones
- **Mobility Line**: Ensure movement crosses mobility line for points

#### Competition Strategy
```java
// Strategic positioning autonomous
public class PositionalAuto extends SequentialCommandGroup {
    public PositionalAuto(DriveSubsystem drive, /* other subsystems */) {
        addCommands(
            // Move to strategic position
            new DriveForwardAuto(drive),
            
            // Perform game-specific actions
            new CoralOutCommand(roller).withTimeout(2.0),
            
            // Optional return movement
            new DriveBackwardAuto(drive)
        );
    }
}
```

### Customization and Variations

#### Parameter Adjustment
```java
// Custom duration and speed
public class CustomDriveAuto extends DriveForwardAuto {
    private double customSpeed = -0.5;     // Different speed
    private double customTime = 1.0;       // Different duration
    
    @Override
    public void execute() {
        if(timer.get() < customTime) {
            m_drive.driveArcade(customSpeed, 0.0, false);
        }
    }
}
```

#### Direction Variations
```java
// Drive backward (positive speed)
public class DriveBackwardAuto extends DriveForwardAuto {
    @Override
    public void execute() {
        if(timer.get() < drive_seconds) {
            m_drive.driveArcade(Math.abs(DriveConstants.AUTO_MODE_SPEED), 0.0, false);
        }
    }
}
```

### Integration with Complex Autonomous

#### Multi-Stage Autonomous
```java
public class ComplexAuto extends SequentialCommandGroup {
    public ComplexAuto(DriveSubsystem drive, RollerSubsystem roller, ArmSubsystem arm) {
        addCommands(
            // Stage 1: Move to scoring position
            new DriveForwardAuto(drive),
            
            // Stage 2: Score preloaded game piece
            new ParallelCommandGroup(
                new ArmDownCommand(arm),
                Commands.waitSeconds(0.5).andThen(
                    new CoralOutCommand(roller).withTimeout(2.0)
                )
            ),
            
            // Stage 3: Return to safe position
            new ArmUpCommand(arm)
        );
    }
}
```

### Troubleshooting Guide

#### Common Issues
- **No Movement**: Check motor connections, drive subsystem configuration
- **Wrong Direction**: Verify speed sign and motor inversion settings
- **Inconsistent Distance**: Check battery voltage, wheel condition, field surface
- **Timer Issues**: Verify timer initialization and execution cycle timing

#### Performance Optimization
- **Speed Tuning**: Adjust `AUTO_MODE_SPEED` for optimal movement
- **Time Calibration**: Measure actual distance and adjust `AUTO_MODE_TIME`
- **Surface Adaptation**: Account for different field surface conditions
- **Battery Compensation**: Consider voltage compensation for consistent performance

### Advanced Enhancements

#### Sensor Integration
```java
// Distance-based stopping
public class DriveDistanceAuto extends Command {
    private final Encoder encoder;
    private final double targetDistance;
    
    @Override
    public boolean isFinished() {
        return encoder.getDistance() >= targetDistance || 
               timer.get() >= maxTime; // Safety timeout
    }
}
```

#### Gyro-Assisted Straight Driving
```java
// Straight-line driving with gyro correction
public class StraightDriveAuto extends DriveForwardAuto {
    private final Gyro gyro;
    private final double targetAngle;
    
    @Override
    public void execute() {
        if(timer.get() < drive_seconds) {
            double angleError = targetAngle - gyro.getAngle();
            double correction = angleError * 0.05; // P controller
            m_drive.driveArcade(AUTO_MODE_SPEED, correction, false);
        }
    }
}
```

### Competition Usage Notes
- **Reliability**: Simple, proven autonomous for competition use
- **Backup Strategy**: Excellent fallback when main autonomous has issues
- **Development**: Good starting point for autonomous development
- **Testing**: Useful for basic robot functionality testing