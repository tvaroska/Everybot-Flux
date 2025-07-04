# CoralOutCommand

## Purpose and Functionality
The `CoralOutCommand` is specifically designed to score coral game pieces into the L1 (Level 1) scoring area. This command provides controlled coral ejection with optimized speed and power settings for reliable L1 scoring.

## Subsystems Required/Used
- **Primary**: `RollerSubsystem` - Controls the coral ejection mechanism

## Parameters and Constructor Details
```java
public CoralOutCommand(RollerSubsystem roller)
```

**Parameters:**
- `roller`: The RollerSubsystem instance used for coral scoring operations

**Dependencies:**
- Requires exclusive access to the RollerSubsystem via `addRequirements(roller)`

## Key Methods

### initialize()
- **Purpose**: Called when command is first scheduled
- **Implementation**: Currently empty - no initialization required
- **Behavior**: Immediate operation without setup delay

### execute()
- **Purpose**: Called continuously while command is active (~50 times per second)
- **Implementation**: Runs the roller at `RollerConstants.ROLLER_CORAL_OUT` speed (-0.4)
- **Behavior**: Continuously ejects coral while command is active

### end(boolean interrupted)
- **Purpose**: Called when command ends or is interrupted
- **Implementation**: Stops the roller by setting speed to 0
- **Safety**: Ensures roller doesn't continue running after scoring attempt

### isFinished()
- **Purpose**: Determines when command should end
- **Implementation**: Always returns `false`
- **Behavior**: Command runs indefinitely until manually interrupted

## Usage Examples and When to Use

### Typical Usage
```java
// Create command instance
CoralOutCommand coralScore = new CoralOutCommand(robotContainer.getRollerSubsystem());

// Bind to controller button for manual scoring
operatorController.a().whileTrue(coralScore);

// Use in autonomous sequence
Commands.sequence(
    new ArmDownCommand(arm),           // Lower arm for L1 access
    coralScore.withTimeout(1.5),       // Score coral with timeout
    new ArmUpCommand(arm)              // Return to travel position
).schedule();
```

### When to Use
- **L1 Coral Scoring**: Primary use for scoring coral into Level 1 areas
- **Autonomous Scoring**: Automated coral scoring during autonomous period
- **Teleop Scoring**: Manual coral scoring during teleoperated period
- **Strategic Placement**: Positioning coral for alliance partner collection
- **Field Reset**: Clearing coral from robot during match

### Scoring Scenarios
```java
// Quick score during teleop
operatorController.a().whileTrue(coralScore);

// Autonomous L1 scoring sequence
Commands.sequence(
    // Drive to L1 position
    driveToL1Position,
    // Prepare for scoring
    new ArmDownCommand(arm),
    // Score the coral
    coralScore.withTimeout(2.0),
    // Return to neutral position
    new ArmUpCommand(arm)
).schedule();
```

## Special Features and State Management

### Continuous Operation
- **Manual Control**: Command never finishes on its own (`isFinished()` returns `false`)
- **Button-Held Operation**: Designed for `whileTrue()` command binding
- **Operator Control**: Allows driver/operator to control scoring duration

### Optimized for L1 Scoring
- **Speed Setting**: `ROLLER_CORAL_OUT` (-0.4) optimized for coral handling
- **Moderate Power**: Lower power than algae operations for gentle coral handling
- **Reliable Ejection**: Sufficient power for consistent coral scoring

### Safety Features
- **Automatic Stop**: Motor stops when command ends or is interrupted
- **Subsystem Protection**: Exclusive access prevents conflicting commands
- **Gentle Operation**: Moderate speed prevents game piece damage

### Configuration
```java
// From RollerConstants
ROLLER_CORAL_OUT: -0.4          // Coral ejection speed (negative = outward)
ROLLER_MOTOR_CURRENT_LIMIT: 40  // 40A current protection
ROLLER_MOTOR_VOLTAGE_COMP: 10   // 10V voltage compensation
```

### State Transitions
1. **Idle** → **Scoring**: When command is scheduled (button pressed)
2. **Scoring** → **Idle**: When command is interrupted (button released)
3. **Continuous Operation**: Remains active while button held

### Performance Characteristics
- **Speed**: Moderate (-0.4) for controlled coral ejection
- **Torque**: Sufficient for reliable coral movement
- **Response**: Immediate start/stop
- **Consistency**: Reliable scoring performance
- **Power Efficiency**: Optimized power usage for coral handling

### Comparison with Other Roller Commands
| Command | Speed | Purpose | Game Piece |
|---------|-------|---------|------------|
| CoralOutCommand | -0.4 | L1 Scoring | Coral |
| CoralStackCommand | -1.0 | L1 Stacking | Coral |
| AlgieOutCommand | -0.8 | Ejection | Algae |
| AlgieInCommand | +0.8 | Intake | Algae |

### Integration with Arm Commands

#### Typical Sequence for L1 Scoring
```java
// Complete L1 scoring sequence
Commands.sequence(
    // 1. Position arm for L1 access
    new ArmDownCommand(arm),
    
    // 2. Wait for arm to reach position
    Commands.waitSeconds(0.5),
    
    // 3. Score coral with timeout for safety
    new CoralOutCommand(roller).withTimeout(2.0),
    
    // 4. Return arm to travel position
    new ArmUpCommand(arm)
).schedule();
```

### Operational Considerations

#### Game Strategy
- **L1 Focus**: Specifically designed for L1 scoring area
- **Speed vs Accuracy**: Balanced speed for quick but accurate scoring
- **Multiple Coral**: Can be used repeatedly for multiple coral pieces
- **Alliance Coordination**: Useful for setting up alliance partner collection

#### Mechanical Considerations
- **Arm Position**: Typically requires arm in down position for L1 access
- **Robot Positioning**: Robot must be properly positioned relative to L1
- **Coral Orientation**: Works best when coral is properly oriented in robot
- **Clearance**: Ensure adequate clearance for coral ejection

### Troubleshooting Guide

#### Common Issues
- **Coral Not Ejecting**: Check roller direction, power settings, mechanical binding
- **Inconsistent Scoring**: Verify arm position, robot positioning, coral orientation
- **Low Power**: Check battery voltage, current limits, motor connections
- **Timing Issues**: Adjust timeout values, check sequence timing

#### Performance Optimization
- **Speed Tuning**: Adjust `ROLLER_CORAL_OUT` for optimal performance
- **Timeout Settings**: Set appropriate timeouts for autonomous use
- **Position Verification**: Ensure proper arm and robot positioning
- **Practice**: Regular practice improves consistency and speed

### Competition Usage
- **Point Scoring**: Direct points for L1 coral placement
- **Strategic Value**: Can enable alliance partner strategies
- **Time Efficiency**: Quick scoring for maximum points
- **Reliable Operation**: Consistent performance under competition pressure

### Advanced Integration
```java
// Smart scoring with sensor feedback
public class SmartCoralScore extends SequentialCommandGroup {
    public SmartCoralScore(ArmSubsystem arm, RollerSubsystem roller) {
        addCommands(
            // Prepare for scoring
            new ArmDownCommand(arm),
            
            // Score until sensor confirms coral ejection
            new CoralOutCommand(roller).until(() -> !coralSensor.get()),
            
            // Return to travel position
            new ArmUpCommand(arm)
        );
    }
}
```