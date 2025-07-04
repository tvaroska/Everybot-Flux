# ClimberUpCommand

## Purpose and Functionality
The `ClimberUpCommand` controls the climber mechanism to retract or raise the climbing system. The actual direction (extending vs retracting) depends on how the winch is wound, making this command adaptable to different climbing mechanism configurations.

## Subsystems Required/Used
- **Primary**: `ClimberSubsystem` - Controls climber motor and winch mechanism

## Parameters and Constructor Details
```java
public ClimberUpCommand(ClimberSubsystem climber)
```

**Parameters:**
- `climber`: The ClimberSubsystem instance for climbing mechanism control

**Dependencies:**
- Requires exclusive access to the ClimberSubsystem via `addRequirements(climber)`

## Key Methods

### initialize()
- **Purpose**: Called when command is first scheduled
- **Implementation**: Currently empty - no initialization required
- **Behavior**: Immediate operation without setup delay

### execute()
- **Purpose**: Called continuously while command is active (~50 times per second)
- **Implementation**: Runs climber motor at `ClimberConstants.CLIMBER_SPEED_UP` (+0.5)
- **Behavior**: Continuously operates climber mechanism while command is active

### end(boolean interrupted)
- **Purpose**: Called when command ends or is interrupted
- **Implementation**: Stops the climber motor by setting speed to 0
- **Safety**: Ensures climber doesn't continue running after button release

### isFinished()
- **Purpose**: Determines when command should end
- **Implementation**: Always returns `false`
- **Behavior**: Command runs indefinitely until manually interrupted

## Usage Examples and When to Use

### Typical Usage
```java
// Create command instance
ClimberUpCommand climberUp = new ClimberUpCommand(robotContainer.getClimberSubsystem());

// Bind to controller button for manual control
operatorController.povUp().whileTrue(climberUp);

// Alternative binding for driver control
driverController.leftTrigger(0.5).whileTrue(climberUp);
```

### When to Use
- **Endgame Climbing**: Primary use during the final 30 seconds of match
- **Climber Retraction**: Retracting climbing mechanism from extended position
- **Robot Lifting**: Pulling robot up to climbing position
- **Practice/Testing**: Testing climber mechanism functionality
- **Setup/Calibration**: Positioning climber for maintenance or setup

### Climbing Sequence Example
```java
// Typical climbing sequence
Commands.sequence(
    // Step 1: Extend climber to reach chain/bar
    new ClimberUpCommand(climber).withTimeout(2.0),
    Commands.waitSeconds(0.5),                          // Allow operator to hook
    // Step 2: Retract to pull robot up
    new ClimberDownCommand(climber).withTimeout(3.0)
).schedule();

// Alternative: Manual two-button control
// operatorController.povUp().whileTrue(climberUp);     // Extend/reach
// operatorController.povDown().whileTrue(climberDown); // Retract/lift
```

## Special Features and State Management

### Continuous Operation
- **Never Finishes**: `isFinished()` returns `false` for manual control
- **Button-Held Operation**: Designed for `whileTrue()` command binding
- **Immediate Response**: Starts/stops instantly with button press/release

### Safety Features
- **Automatic Stop**: Motor stops when command ends or is interrupted
- **Current Limiting**: 40A current limit prevents motor damage
- **Voltage Compensation**: 12V compensation for consistent performance
- **Subsystem Protection**: Exclusive access prevents conflicting commands

### Configuration
```java
// Constants from ClimberConstants
CLIMBER_SPEED_UP: +0.5          // Motor speed (positive direction)
CLIMBER_MOTOR_CURRENT_LIMIT: 40 // 40 amp current protection
CLIMBER_MOTOR_VOLTAGE_COMP: 12  // 12V voltage compensation
```

### Winch Direction Dependency
The command documentation notes that operation "can change based on how the winch is wound":

- **Standard Winding**: Positive speed may retract climber mechanism
- **Reverse Winding**: Positive speed may extend climber mechanism
- **Team Specific**: Direction depends on mechanical implementation

### State Transitions
1. **Idle** → **Operating**: When command is scheduled (button pressed)
2. **Operating** → **Idle**: When command is interrupted (button released)
3. **Continuous Loop**: Remains in operating state while button held

### Performance Characteristics
- **Speed**: Moderate speed (+0.5) for controlled operation
- **Torque**: High torque capability for lifting robot weight
- **Response**: Immediate start/stop response
- **Endurance**: Designed for sustained operation during climbing
- **Power**: Sufficient for supporting/lifting 125+ lb robot

### Integration with ClimberDownCommand
| Aspect | ClimberUpCommand | ClimberDownCommand |
|--------|------------------|-------------------|
| Speed | +0.5 | -0.5 |
| Direction | Up/Retract* | Down/Extend* |
| Use Case | Raising/Retraction | Lowering/Extension |
| Timing | Manual Control | Manual Control |

*Actual direction depends on winch winding

### Operational Considerations

#### Climbing Strategy
- **Two-Stage Process**: Often used with ClimberDownCommand in sequence
- **Initial Extension**: May be first step to reach climbing structure
- **Final Retraction**: May be final step to lift robot off ground
- **Operator Coordination**: Requires coordination with human player/driver

#### Safety Guidelines
- **Never Leave Running**: Command designed to stop when button released
- **Current Monitoring**: Watch for high current indicating binding/jamming
- **Mechanical Limits**: Be aware of climber travel limits
- **Load Awareness**: Monitor robot weight and climber capacity

### Competition Context

#### FRC Climbing Rules
- **Endgame Only**: Climbing typically allowed only in final 30 seconds
- **Height Requirements**: Must achieve specific height for points
- **Multiple Robots**: May need to coordinate with alliance partners
- **Time Pressure**: Limited time requires efficient operation

#### Strategic Considerations
- **Risk vs Reward**: Climbing adds points but risks penalty/failure
- **Practice Critical**: Requires extensive practice for consistency
- **Backup Plans**: Have alternative strategies if climbing fails
- **Communication**: Clear operator communication essential

### Mechanical Integration

#### Typical Climber Designs
- **Winch System**: Cable or chain wound on rotating drum
- **Linear Actuator**: Direct linear extension/retraction
- **Telescoping**: Nested tube system for compact storage
- **Hook Mechanism**: Grabbing/releasing climbing structure

#### Power Requirements
- **High Torque**: Must overcome robot weight and friction
- **Sustained Load**: May need to hold robot weight for extended time
- **Quick Response**: Fast extension/retraction for time efficiency
- **Reliable Operation**: Must work consistently under competition stress

### Troubleshooting Guide
- **No Movement**: Check power connections, current limits, motor health
- **Slow Operation**: Check battery voltage, mechanical binding
- **Inconsistent**: Check loose connections, worn mechanical parts
- **High Current**: Check for binding, overload, or mechanical issues