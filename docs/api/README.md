# API Reference

This directory contains comprehensive API documentation for all classes in the Everybot 2025 robot code.

## Documentation Structure

### Core Classes
- [Robot.md](Robot.md) - Main robot class managing lifecycle and modes
- [RobotContainer.md](RobotContainer.md) - Central hub for subsystems and commands
- [Constants.md](Constants.md) - Configuration constants and parameters
- [Main.md](Main.md) - Application entry point
- [PIDCtrl.md](PIDCtrl.md) - Custom PID controller implementation
- [Sensitivity.md](Sensitivity.md) - Joystick sensitivity curve implementation

### Subsystems
- [DriveSubsystem.md](DriveSubsystem.md) - Tank drive locomotion system
- [ArmSubsystem.md](ArmSubsystem.md) - Arm positioning mechanism
- [RollerSubsystem.md](RollerSubsystem.md) - Game piece manipulation rollers
- [ShooterSubsystem.md](ShooterSubsystem.md) - Precision shooting mechanism
- [ClimberSubsystem.md](ClimberSubsystem.md) - End-game climbing system

### Commands
- [DriveCommand.md](DriveCommand.md) - Primary teleop driving
- [AlgieInCommand.md](AlgieInCommand.md) - Algae intake operation
- [AlgieOutCommand.md](AlgieOutCommand.md) - Algae ejection operation
- [AlgieShootCommand.md](AlgieShootCommand.md) - Automated shooting sequence
- [ArmUpCommand.md](ArmUpCommand.md) - Arm raising operation
- [ArmDownCommand.md](ArmDownCommand.md) - Arm lowering operation
- [CoralOutCommand.md](CoralOutCommand.md) - Coral L1 scoring
- [CoralStackCommand.md](CoralStackCommand.md) - Coral stacking operation
- [ClimberUpCommand.md](ClimberUpCommand.md) - Climber extension
- [ClimberDownCommand.md](ClimberDownCommand.md) - Climber retraction

### Autonomous Commands
- [DriveForwardAuto.md](DriveForwardAuto.md) - Simple mobility autonomous
- [SimpleCoralAuto.md](SimpleCoralAuto.md) - Coral manipulation autonomous

## API Documentation Conventions

### Class Documentation Format

Each API document follows this structure:

1. **Overview** - Purpose and functionality
2. **Constructor(s)** - Parameters and initialization
3. **Public Methods** - All public methods with parameters and return values
4. **Protected/Private Methods** - Internal implementation details
5. **Fields** - Member variables and their purposes
6. **Usage Examples** - Practical code examples
7. **Related Classes** - Dependencies and related components

### Method Documentation Format

```java
/**
 * Brief description of method purpose
 * 
 * @param parameterName Description of parameter
 * @return Description of return value
 * @throws ExceptionType Description of when exception is thrown
 */
public ReturnType methodName(ParameterType parameterName) {
    // Implementation
}
```

### Code Example Format

All code examples include:
- Complete, runnable code snippets
- Proper imports and context
- Comments explaining key concepts
- Error handling where appropriate

## Quick Reference

### Subsystem Creation Pattern
```java
// All subsystems follow this pattern
public class ExampleSubsystem extends SubsystemBase {
    // Hardware components
    private final CANSparkMax motor;
    
    // Constructor
    public ExampleSubsystem() {
        // Initialize hardware
    }
    
    // Control methods
    public void doSomething() { }
    
    // Periodic updates
    @Override
    public void periodic() { }
}
```

### Command Creation Pattern
```java
// All commands follow this pattern
public class ExampleCommand extends CommandBase {
    // Required subsystems
    private final ExampleSubsystem subsystem;
    
    // Constructor
    public ExampleCommand(ExampleSubsystem subsystem) {
        this.subsystem = subsystem;
        addRequirements(subsystem);
    }
    
    // Command lifecycle
    @Override public void initialize() { }
    @Override public void execute() { }
    @Override public void end(boolean interrupted) { }
    @Override public boolean isFinished() { return false; }
}
```

### Constants Organization Pattern
```java
public final class Constants {
    public static final class SubsystemConstants {
        public static final int MOTOR_ID = 1;
        public static final double SPEED = 0.5;
        // Other constants...
    }
}
```

## Navigation Guide

### By Functionality
- **Movement**: DriveSubsystem, DriveCommand, DriveForwardAuto
- **Game Pieces**: RollerSubsystem, ShooterSubsystem, Algae/Coral Commands
- **Positioning**: ArmSubsystem, ArmUp/DownCommands
- **End Game**: ClimberSubsystem, ClimberUp/DownCommands
- **Control**: Sensitivity, PIDCtrl, RobotContainer

### By Complexity
- **Beginner**: Constants, Main, Simple Commands
- **Intermediate**: Subsystems, Basic Commands, Robot
- **Advanced**: PIDCtrl, Sensitivity, Complex Commands, RobotContainer

### By Usage Frequency
- **Most Used**: Robot, RobotContainer, DriveSubsystem, Constants
- **Regular Use**: All Commands, Most Subsystems
- **Specialized**: PIDCtrl, Sensitivity, Autonomous Commands

## Development Guidelines

### Adding New API Documentation

When adding new classes to the codebase:

1. Create corresponding `.md` file in appropriate directory
2. Follow the established documentation format
3. Include comprehensive usage examples
4. Cross-reference related classes
5. Update this README with new entries

### Maintaining API Documentation

- **Code Changes**: Update documentation when changing public APIs
- **Version Control**: Keep documentation in sync with code
- **Examples**: Test all code examples for accuracy
- **Cross-References**: Maintain links between related documentation

### Documentation Standards

- **Clarity**: Write for team members of all skill levels
- **Completeness**: Document all public methods and important private ones
- **Examples**: Include practical, testable examples
- **Consistency**: Follow established patterns and formatting
- **Accuracy**: Ensure documentation matches current code implementation

This API reference serves as the definitive guide to the Everybot 2025 robot code implementation, providing detailed technical information for development, maintenance, and troubleshooting.