# CoralStackCommand

## Purpose and Functionality
The `CoralStackCommand` is a specialized command designed for coral stacking operations in the L1 scoring area. It's specifically intended for use when coral is already present in L1, providing the additional power needed to push new coral pieces past existing ones for effective stacking.

## Subsystems Required/Used
- **Primary**: `RollerSubsystem` - Controls the coral stacking mechanism

## Parameters and Constructor Details
```java
public CoralStackCommand(RollerSubsystem roller)
```

**Parameters:**
- `roller`: The RollerSubsystem instance used for coral stacking operations

**Dependencies:**
- Requires exclusive access to the RollerSubsystem via `addRequirements(roller)`

## Key Methods

### initialize()
- **Purpose**: Called when command is first scheduled
- **Implementation**: Currently empty - no initialization required
- **Behavior**: Immediate operation without setup delay

### execute()
- **Purpose**: Called continuously while command is active (~50 times per second)
- **Implementation**: Runs the roller at `RollerConstants.ROLLER_CORAL_STACK` speed (-1.0)
- **Behavior**: Continuously pushes coral with maximum power while command is active

### end(boolean interrupted)
- **Purpose**: Called when command ends or is interrupted
- **Implementation**: Stops the roller by setting speed to 0
- **Safety**: Ensures roller doesn't continue running after stacking attempt

### isFinished()
- **Purpose**: Determines when command should end
- **Implementation**: Always returns `false`
- **Behavior**: Command runs indefinitely until manually interrupted

## Usage Examples and When to Use

### Typical Usage
```java
// Create command instance
CoralStackCommand coralStack = new CoralStackCommand(robotContainer.getRollerSubsystem());

// Bind to controller button for manual stacking
operatorController.b().whileTrue(coralStack);

// Use in autonomous sequence for multiple coral scoring
Commands.sequence(
    new ArmDownCommand(arm),                    // Position for L1 access
    new CoralOutCommand(roller).withTimeout(1.5),  // Score first coral
    Commands.waitSeconds(0.2),                  // Brief pause
    coralStack.withTimeout(2.0),               // Stack additional coral
    new ArmUpCommand(arm)                       // Return to travel position
).schedule();
```

### When to Use
- **L1 Stacking**: When coral already exists in L1 and you need to add more
- **Multiple Coral Scoring**: Pushing additional coral pieces into occupied L1
- **Dense Packing**: Maximizing coral density in L1 areas
- **Competition Strategy**: High-value stacking for maximum points
- **Overcoming Resistance**: When existing coral creates resistance

### Stacking Scenarios
```java
// Multi-coral autonomous sequence
Commands.sequence(
    // Position for L1 access
    new ArmDownCommand(arm),
    
    // Score first coral (normal power)
    new CoralOutCommand(roller).withTimeout(1.5),
    
    // Stack second coral (high power)
    coralStack.withTimeout(2.0),
    
    // Stack third coral if available
    coralStack.withTimeout(2.0),
    
    // Return to travel position
    new ArmUpCommand(arm)
).schedule();
```

### Not Recommended For
- **Empty L1**: Use `CoralOutCommand` for initial coral placement
- **Single Coral**: Normal `CoralOutCommand` is sufficient
- **Algae Operations**: This command is coral-specific

## Special Features and State Management

### High-Power Operation
- **Maximum Speed**: `ROLLER_CORAL_STACK` (-1.0) provides full power for stacking
- **Overcoming Resistance**: Sufficient power to push through existing coral
- **Dense Packing**: Enables tight coral stacking for maximum points

### Conditional Usage
- **Existing Coral Required**: Documentation explicitly states "when there is already coral in L1"
- **Reliability Warning**: "May be less reliable if there is no coral already in L1"
- **Strategic Application**: Best used as second/third coral in sequence

### Safety Features
- **Automatic Stop**: Motor stops when command ends or is interrupted
- **Subsystem Protection**: Exclusive access prevents conflicting commands
- **Operator Control**: Manual stop capability via button release

### Configuration
```java
// From RollerConstants
ROLLER_CORAL_STACK: -1.0        // Full power stacking speed
ROLLER_MOTOR_CURRENT_LIMIT: 40  // 40A current protection
ROLLER_MOTOR_VOLTAGE_COMP: 10   // 10V voltage compensation
```

### State Transitions
1. **Idle** → **Stacking**: When command is scheduled (button pressed)
2. **Stacking** → **Idle**: When command is interrupted (button released)
3. **High-Power Operation**: Maintains full power while active

### Performance Characteristics
- **Speed**: Maximum (-1.0) for overcoming coral resistance
- **Torque**: High torque for pushing through packed coral
- **Response**: Immediate start/stop
- **Power**: Full roller capability for challenging stacking
- **Efficiency**: Short bursts for maximum effectiveness

### Comparison with CoralOutCommand
| Aspect | CoralStackCommand | CoralOutCommand |
|--------|------------------|-----------------|
| Speed | -1.0 (Maximum) | -0.4 (Moderate) |
| Use Case | Stacking in occupied L1 | Initial L1 scoring |
| Power | Full power | Moderate power |
| Prerequisites | Coral already in L1 | Empty or any L1 |
| Reliability | Best with existing coral | Reliable in all conditions |

### Strategic Considerations

#### Point Maximization
- **Multiple Coral**: Enables scoring multiple coral pieces in same L1
- **Dense Packing**: Maximizes points per L1 area
- **Competitive Advantage**: Higher scoring potential than single coral

#### Timing Strategy
```java
// Optimal stacking sequence
Commands.sequence(
    // 1. Normal scoring for first coral
    new CoralOutCommand(roller).withTimeout(1.5),
    
    // 2. Brief pause to let first coral settle
    Commands.waitSeconds(0.3),
    
    // 3. High-power stacking for additional coral
    new CoralStackCommand(roller).withTimeout(2.0)
).schedule();
```

### Operational Guidelines

#### Best Practices
- **Verify Existing Coral**: Ensure coral is already in L1 before using
- **Sequential Operation**: Use after `CoralOutCommand` for best results
- **Timeout Usage**: Always use timeouts in autonomous to prevent infinite running
- **Power Monitoring**: Watch current draw for signs of jamming

#### Mechanical Considerations
- **High Current**: Monitor for current spikes indicating binding
- **Coral Jamming**: Risk of coral jamming with maximum power
- **Mechanism Stress**: High power may stress mechanical components
- **Positioning Critical**: Robot positioning crucial for effective stacking

### Troubleshooting Guide

#### Common Issues
- **Coral Jamming**: Reduce timeout duration, check mechanism alignment
- **Inconsistent Stacking**: Verify existing coral position, robot positioning
- **High Current Draw**: Check for mechanical binding, reduce operation time
- **Poor Reliability**: Ensure coral is already present before using

#### Performance Optimization
- **Timing Adjustment**: Optimize timeout values for consistent operation
- **Sequence Tuning**: Adjust pauses between commands
- **Position Verification**: Ensure optimal robot positioning
- **Power Management**: Monitor battery voltage during high-power operation

### Advanced Usage

#### Sensor-Based Stacking
```java
// Smart stacking with feedback
public class SmartCoralStack extends Command {
    private final RollerSubsystem roller;
    private final BooleanSupplier coralSensor;
    
    public void execute() {
        if (coralSensor.get()) {  // Coral detected in L1
            roller.runRoller(RollerConstants.ROLLER_CORAL_STACK);
        }
    }
    
    public boolean isFinished() {
        return !coralSensor.get() || timer.hasElapsed(2.0);
    }
}
```

#### Competition Strategy
- **Alliance Coordination**: Plan stacking with alliance partners
- **Time Management**: Balance stacking time vs other objectives
- **Risk Assessment**: Evaluate stacking risk vs guaranteed single coral
- **Practice Critical**: Extensive practice required for consistent stacking

### Safety Warnings
- **High Power Operation**: Full speed may cause mechanical stress
- **Current Monitoring**: Watch for overcurrent conditions
- **Operator Training**: Ensure operators understand proper usage
- **Emergency Stop**: Have emergency stop procedures ready