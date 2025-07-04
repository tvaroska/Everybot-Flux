# SimpleCoralAuto

## Purpose and Functionality
The `SimpleCoralAuto` is a comprehensive autonomous command that combines robot movement with coral scoring. It implements a two-stage sequence: first driving forward to position the robot, then ejecting coral into the L1 scoring area while maintaining proper arm position throughout the operation.

## Subsystems Required/Used
- **Primary**: `DriveSubsystem` - Controls robot movement and positioning
- **Primary**: `RollerSubsystem` - Controls coral ejection mechanism  
- **Primary**: `ArmSubsystem` - Maintains arm position during autonomous

## Parameters and Constructor Details
```java
public SimpleCoralAuto(DriveSubsystem drive, RollerSubsystem roller, ArmSubsystem arm)
```

**Parameters:**
- `drive`: The DriveSubsystem instance for autonomous movement
- `roller`: The RollerSubsystem instance for coral scoring
- `arm`: The ArmSubsystem instance for positioning control

**Dependencies:**
- Requires exclusive access to all three subsystems via `addRequirements()`
- Coordinates multiple subsystems for complete autonomous operation

## Key Methods

### initialize()
- **Purpose**: Prepares all subsystems for autonomous execution
- **Implementation**: 
  - Restarts internal timer with `timer.restart()`
  - Clears previous timer state for reliable operation
- **Behavior**: Ensures clean start for multi-stage autonomous sequence

### execute()
- **Purpose**: Manages the two-stage autonomous sequence
- **Implementation**: Complex state machine with three phases:

#### Phase 1: Arm Positioning (Continuous)
```java
// Always maintain arm in up position for roller clearance
m_arm.run(ArmConstants.ARM_HOLD_UP);
```

#### Phase 2: Drive Forward (0 to 3.25 seconds)
```java
if(timer.get() < drive_seconds) {
    m_drive.driveArcade(0.3, 0.0, false);  // 30% forward speed
}
```

#### Phase 3: Coral Ejection (3.25 to 4.5 seconds)
```java
else if(timer.get() > drive_seconds && timer.get() < exjest_seconds) {
    m_drive.driveArcade(0.0, 0.0, false);              // Stop driving
    m_roller.runRoller(RollerConstants.ROLLER_CORAL_OUT); // Eject coral
}
```

### end(boolean interrupted)
- **Purpose**: Safely terminates autonomous sequence
- **Implementation**:
  - Stops drive motors: `driveArcade(0.0, 0.0, false)`
  - Stops roller: `runRoller(0)`
  - Stops timer: `timer.stop()`
- **Safety**: Ensures all subsystems stop when autonomous completes

### isFinished()
- **Purpose**: Determines when autonomous sequence is complete
- **Implementation**: Returns `true` when `timer.get() >= exjest_seconds` (4.5s)
- **Behavior**: Automatic termination after coral ejection phase

## Usage Examples and When to Use

### Typical Usage
```java
// Create comprehensive autonomous command
SimpleCoralAuto coralAuto = new SimpleCoralAuto(
    robotContainer.getDriveSubsystem(),
    robotContainer.getRollerSubsystem(), 
    robotContainer.getArmSubsystem()
);

// Set as autonomous command
robotContainer.setAutonomousCommand(coralAuto);

// Use in autonomous selector
SendableChooser<Command> autoChooser = new SendableChooser<>();
autoChooser.setDefaultOption("Simple Coral Auto", coralAuto);
autoChooser.addOption("Drive Only", new DriveForwardAuto(drive));
```

### When to Use
- **Complete Autonomous**: Full autonomous routine with movement and scoring
- **Coral Scoring**: When robot starts with preloaded coral
- **Reliable Strategy**: Proven autonomous sequence for competition
- **Point Maximization**: Combines mobility and scoring points
- **Time-Efficient**: Complete sequence in under 5 seconds

### Competition Strategy
```java
// Strategic autonomous selection
public class AutoStrategy {
    public static Command getOptimalAuto(StartingPosition position, GameState state) {
        if (state.hasPreloadedCoral() && position.allowsL1Access()) {
            return new SimpleCoralAuto(drive, roller, arm);
        } else {
            return new DriveForwardAuto(drive);  // Fallback
        }
    }
}
```

## Special Features and State Management

### Multi-Phase Timing System
```java
// Timing configuration
private double drive_seconds = 3.25;    // Drive phase duration
private double exjest_seconds = 4.5;    // Total sequence duration
// Coral ejection duration = 4.5 - 3.25 = 1.25 seconds
```

### Continuous Arm Management
```java
// Arm positioning throughout autonomous
m_arm.run(ArmConstants.ARM_HOLD_UP);  // Constant 0.05 power to maintain position
```

**Benefits:**
- **Roller Clearance**: Keeps rollers properly positioned for coral ejection
- **Consistent Operation**: Maintains position regardless of sequence phase
- **Safety**: Prevents arm from interfering with other mechanisms

### State Machine Implementation
1. **Initialization**: Timer reset, all subsystems ready
2. **Drive Phase** (0-3.25s): Forward movement with arm positioning
3. **Stop & Score Phase** (3.25-4.5s): Stationary coral ejection
4. **Completion**: All subsystems stopped, autonomous complete

### Safety Features
- **Timeout Protection**: Automatic termination prevents infinite running
- **Subsystem Coordination**: Prevents conflicting subsystem commands
- **Clean Shutdown**: Explicit stopping of all motors and timer
- **Predictable Sequence**: Well-defined phases with clear transitions

### Performance Characteristics
- **Total Duration**: 4.5 seconds complete sequence
- **Drive Distance**: Approximately 3-4 feet forward movement
- **Scoring Reliability**: 1.25 seconds dedicated to coral ejection
- **Point Value**: Mobility points + coral scoring points
- **Success Rate**: High reliability with simple, proven sequence

### Comparison with DriveForwardAuto
| Aspect | SimpleCoralAuto | DriveForwardAuto |
|--------|----------------|------------------|
| Duration | 4.5 seconds | 0.6 seconds |
| Subsystems | 3 (Drive, Roller, Arm) | 1 (Drive only) |
| Functionality | Movement + Scoring | Movement only |
| Complexity | Multi-phase | Single phase |
| Point Value | Mobility + Scoring | Mobility only |

### Field Positioning Requirements

#### Starting Position Considerations
- **Preloaded Coral**: Robot must start with coral in mechanism
- **L1 Access**: Final position must allow coral ejection into L1
- **Clear Path**: Drive path must be obstacle-free
- **Alliance Zone**: Movement must remain within legal areas

#### Positioning Strategy
```java
// Optimal starting positions for SimpleCoralAuto
public enum StartingPosition {
    CENTER_LINE,     // Direct path to nearest L1
    LEFT_STATION,    // Angled approach to L1
    RIGHT_STATION,   // Angled approach to L1
    BACKUP_POSITION  // Safe fallback position
}
```

### Customization and Variations

#### Timing Adjustments
```java
// Customizable timing parameters
private double drive_seconds = 3.25;    // Adjust for different distances
private double exjest_seconds = 4.5;    // Adjust for scoring reliability
private double drive_speed = 0.3;       // Adjust for field conditions
```

#### Direction Variations
```java
// Reverse direction version
public class ReverseCoralAuto extends SimpleCoralAuto {
    @Override
    public void execute() {
        m_arm.run(ArmConstants.ARM_HOLD_UP);
        
        if(timer.get() < drive_seconds) {
            m_drive.driveArcade(-0.3, 0.0, false);  // Negative for reverse
        } else if(timer.get() > drive_seconds && timer.get() < exjest_seconds) {
            m_drive.driveArcade(0.0, 0.0, false);
            m_roller.runRoller(RollerConstants.ROLLER_CORAL_OUT);
        }
    }
}
```

### Advanced Integration

#### Sensor-Enhanced Version
```java
// Enhanced with sensor feedback
public class SmartCoralAuto extends SimpleCoralAuto {
    private final DistanceSensor distanceSensor;
    private final BooleanSupplier coralSensor;
    
    @Override
    public void execute() {
        m_arm.run(ArmConstants.ARM_HOLD_UP);
        
        // Drive until distance target or timeout
        if(timer.get() < drive_seconds && distanceSensor.getDistance() > targetDistance) {
            m_drive.driveArcade(0.3, 0.0, false);
        }
        // Score until coral ejected or timeout
        else if(timer.get() < exjest_seconds && coralSensor.get()) {
            m_drive.driveArcade(0.0, 0.0, false);
            m_roller.runRoller(RollerConstants.ROLLER_CORAL_OUT);
        }
    }
}
```

#### Multi-Coral Version
```java
// Extended sequence for multiple coral
public class MultiCoralAuto extends SequentialCommandGroup {
    public MultiCoralAuto(DriveSubsystem drive, RollerSubsystem roller, ArmSubsystem arm) {
        addCommands(
            // Score first coral
            new SimpleCoralAuto(drive, roller, arm),
            
            // Move to second coral
            new DriveToCoralCommand(drive),
            
            // Collect and score second coral
            new CollectCoralCommand(roller, arm),
            new SimpleCoralAuto(drive, roller, arm)
        );
    }
}
```

### Troubleshooting Guide

#### Common Issues
- **Coral Not Ejecting**: Check roller direction, arm position, coral loading
- **Inconsistent Distance**: Verify drive speed, timer accuracy, field conditions
- **Arm Interference**: Adjust `ARM_HOLD_UP` value, check mechanical clearance
- **Timing Problems**: Calibrate timing values for specific field/robot combination

#### Performance Optimization
- **Distance Calibration**: Measure actual drive distance and adjust timing
- **Speed Tuning**: Optimize drive speed for accuracy vs. time efficiency
- **Ejection Timing**: Adjust coral ejection duration for reliability
- **Arm Position**: Fine-tune arm holding power for optimal clearance

### Competition Considerations

#### Match Strategy
- **Alliance Coordination**: Ensure autonomous doesn't interfere with partners
- **Field Conditions**: Account for varying field surface conditions
- **Game Piece Management**: Verify preloaded coral is properly positioned
- **Backup Plans**: Have fallback autonomous if primary fails

#### Reliability Factors
- **Battery Voltage**: Consistent autonomous performance across matches
- **Mechanical Wear**: Monitor subsystem performance throughout competition
- **Environmental**: Account for temperature, humidity effects on timing
- **Practice**: Extensive practice on actual field conditions

### Development Guidelines
- **Incremental Testing**: Test each phase separately before integration
- **Parameter Tuning**: Use SmartDashboard for real-time parameter adjustment
- **Safety First**: Always include timeout protection and clean shutdown
- **Documentation**: Maintain clear documentation of timing and positioning requirements