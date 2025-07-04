# Everybot Flux - Command Documentation

This directory contains comprehensive documentation for all robot commands in the Everybot Flux codebase. The commands are organized into functional categories for easy navigation and understanding.

## Command Categories

### 🎮 Drive Commands
Commands that control robot movement and navigation.

| Command | Purpose | Duration | Usage |
|---------|---------|----------|-------|
| [DriveCommand](DriveCommand.md) | Primary teleop driving control using arcade drive | Continuous | Default command for driver control |

### 🦾 Arm Control Commands  
Commands that manage arm positioning for different game operations.

| Command | Purpose | Duration | Usage |
|---------|---------|----------|-------|
| [ArmUpCommand](ArmUpCommand.md) | Raises arm to up position (hardstop) | 760ms | Intake preparation, travel position |
| [ArmDownCommand](ArmDownCommand.md) | Lowers arm to down position (paracord) | 700ms | Coral scoring preparation |

### 🌿 Algae Handling Commands
Commands specifically designed for algae game piece manipulation.

| Command | Purpose | Duration | Usage |
|---------|---------|----------|-------|
| [AlgieInCommand](AlgieInCommand.md) | Intakes algae into robot | Continuous | Manual intake control |
| [AlgieOutCommand](AlgieOutCommand.md) | Ejects algae from robot | Continuous | Manual ejection, passing |
| [AlgieShootCommand](AlgieShootCommand.md) | Precision algae shooting sequence | 3000ms | Autonomous/manual scoring |

### 🪸 Coral Handling Commands
Commands optimized for coral game piece scoring and manipulation.

| Command | Purpose | Duration | Usage |
|---------|---------|----------|-------|
| [CoralOutCommand](CoralOutCommand.md) | Scores coral into L1 areas | Continuous | L1 scoring operations |
| [CoralStackCommand](CoralStackCommand.md) | Stacks coral in occupied L1 | Continuous | Multi-coral L1 scoring |

### 🧗 Climber Commands
Commands for endgame climbing mechanism control.

| Command | Purpose | Duration | Usage |
|---------|---------|----------|-------|
| [ClimberUpCommand](ClimberUpCommand.md) | Operates climber in up direction* | Continuous | Climbing extension/retraction |
| [ClimberDownCommand](ClimberDownCommand.md) | Operates climber in down direction* | Continuous | Climbing extension/retraction |

*Direction depends on winch winding configuration

### 🤖 Autonomous Commands
Complete autonomous routines for competition use.

| Command | Purpose | Duration | Subsystems Used |
|---------|---------|----------|----------------|
| [DriveForwardAuto](DriveForwardAuto.md) | Simple forward movement for mobility | 600ms | Drive |
| [SimpleCoralAuto](SimpleCoralAuto.md) | Complete coral scoring sequence | 4500ms | Drive, Roller, Arm |

## Command Architecture

### Command Base Structure
All commands follow the standard WPILib Command framework:

```java
public class ExampleCommand extends Command {
    // Constructor with subsystem dependencies
    public ExampleCommand(Subsystem subsystem) {
        addRequirements(subsystem);
    }
    
    // Lifecycle methods
    public void initialize() { }          // Setup when scheduled
    public void execute() { }             // Called every 20ms while active  
    public void end(boolean interrupted) { } // Cleanup when finished
    public boolean isFinished() { }       // Termination condition
}
```

### Subsystem Integration
Commands interact with the following subsystems:

- **DriveSubsystem**: Robot movement and navigation
- **ArmSubsystem**: Arm positioning and control  
- **RollerSubsystem**: Game piece manipulation
- **ShooterSubsystem**: Precision shooting mechanisms
- **ClimberSubsystem**: Endgame climbing operations

### Common Patterns

#### Continuous Commands
Many commands run continuously until interrupted:
```java
public boolean isFinished() {
    return false;  // Never finishes, must be interrupted
}
```

#### Timed Commands  
Some commands use internal timers for sequences:
```java
public boolean isFinished() {
    return timer.get() >= targetTime;
}
```

#### Multi-Phase Commands
Complex commands implement state machines:
```java
public void execute() {
    if (phase1Condition) {
        // Phase 1 actions
    } else if (phase2Condition) {
        // Phase 2 actions
    }
}
```

## Configuration and Tuning

### Constants Integration
Commands use centralized constants for easy tuning:

```java
// Speed and timing constants
RollerConstants.ROLLER_ALGAE_IN = 0.8
ArmConstants.ARM_TIME_UP = 360ms
ShooterConstants.SpeedUp1 = 2110 RPM
```

### SmartDashboard Integration
Advanced commands support real-time tuning:

```java
// Example from AlgieShootCommand
speed = SmartDashboard.getNumber("Set RPM Up 1", defaultValue);
timing = SmartDashboard.getNumber("Shoot Start Time", defaultValue);
```

## Usage Guidelines

### Button Binding Patterns

#### Continuous Commands (whileTrue)
```java
// For commands that should run while button is held
controller.rightBumper().whileTrue(new AlgieInCommand(roller, shooter));
```

#### Instant Commands (onTrue)  
```java
// For commands that run once when button is pressed
controller.a().onTrue(new ArmUpCommand(arm));
```

#### Timed Commands with Safety
```java
// For autonomous with timeout protection
new AlgieShootCommand(shooter, roller, 0).withTimeout(5.0);
```

### Command Composition

#### Sequential Operations
```java
Commands.sequence(
    new ArmDownCommand(arm),
    new CoralOutCommand(roller).withTimeout(2.0),
    new ArmUpCommand(arm)
).schedule();
```

#### Parallel Operations
```java
Commands.parallel(
    new ArmUpCommand(arm),
    new AlgieInCommand(roller, shooter)
).schedule();
```

## Performance Characteristics

### Execution Timing
- **Command Scheduler**: ~50Hz (20ms cycle time)
- **Response Time**: Single-cycle response for most commands
- **Safety Timeouts**: Autonomous commands include timeout protection

### Resource Management
- **Subsystem Requirements**: Prevents conflicting commands
- **Memory Efficiency**: Lightweight command objects
- **Power Management**: Current limiting and voltage compensation

## Competition Best Practices

### Reliability
- **Simple Designs**: Prefer simple, proven command structures
- **Timeout Protection**: Always use timeouts in autonomous
- **Defensive Programming**: Handle edge cases and failures gracefully

### Performance
- **Tuning**: Use SmartDashboard for real-time parameter adjustment
- **Practice**: Extensive testing on competition-like conditions
- **Monitoring**: Log critical parameters for post-match analysis

### Strategy
- **Modular Design**: Commands can be combined for complex strategies
- **Fallback Options**: Simple commands as backup for complex ones
- **Alliance Coordination**: Consider impact on alliance partners

## Development Workflow

### Adding New Commands
1. **Identify Requirements**: Determine subsystems and functionality needed
2. **Create Command Class**: Follow standard WPILib patterns
3. **Implement Lifecycle**: Initialize, execute, end, and finish methods
4. **Add Safety Features**: Timeouts, bounds checking, error handling
5. **Document Thoroughly**: Update this documentation
6. **Test Extensively**: Verify behavior in all scenarios

### Testing Strategy
- **Unit Testing**: Test individual command behavior
- **Integration Testing**: Test command interactions
- **Competition Simulation**: Test under realistic conditions
- **Edge Cases**: Verify behavior during failures and interruptions

### Maintenance
- **Regular Review**: Keep documentation current with code changes
- **Performance Monitoring**: Track command execution times and success rates
- **Parameter Tuning**: Adjust constants based on competition experience
- **Code Quality**: Maintain clean, readable, well-commented code

## Related Documentation

- **[Subsystem Documentation](../subsystems/README.md)**: Detailed subsystem information
- **[Architecture Overview](../architecture.md)**: Overall robot code structure
- **[Setup Guide](../setup-installation.md)**: Development environment setup

## Quick Reference

### Emergency Procedures
- **Stop All Motors**: Disable robot or use emergency stop
- **Command Interruption**: Commands can be cancelled via scheduler
- **Subsystem Reset**: Restart robot code to reset all subsystems

### Common Troubleshooting
- **No Response**: Check subsystem requirements and command scheduling
- **Unexpected Behavior**: Verify constants and parameter values
- **Performance Issues**: Monitor current draw and voltage levels
- **Timing Problems**: Check cycle times and timeout values

For specific command details, click on any command name above to view its comprehensive documentation.