# Architecture Overview

The Everybot 2025 robot code follows the WPILib Command-Based Programming framework, providing a clean separation between robot hardware (subsystems) and robot actions (commands).

## Command-Based Architecture

### Core Components

#### Robot Class (`Robot.java`)
The main robot class that manages the overall robot lifecycle:
- Initializes the robot container during `robotInit()`
- Runs the command scheduler every 20ms
- Manages autonomous and teleop mode transitions
- Handles periodic updates for all subsystems

#### RobotContainer Class (`RobotContainer.java`)
The central hub that:
- Instantiates all subsystems and commands
- Configures controller bindings
- Manages autonomous command selection
- Handles SmartDashboard parameter updates

#### Constants Class (`Constants.java`)
Centralized configuration containing:
- Motor controller IDs and current limits
- PID controller gains
- Speed and timing constants
- Controller port assignments

## System Architecture

```
Robot.java
    ├── RobotContainer.java
    │   ├── Subsystems
    │   │   ├── DriveSubsystem
    │   │   ├── ArmSubsystem
    │   │   ├── RollerSubsystem
    │   │   ├── ShooterSubsystem
    │   │   └── ClimberSubsystem
    │   ├── Commands
    │   │   ├── Autonomous Commands
    │   │   ├── Teleop Commands
    │   │   └── Utility Commands
    │   └── Controllers
    │       ├── Driver Controller
    │       └── Operator Controller
    └── Constants.java
```

## Subsystems

Each subsystem encapsulates hardware components and provides methods for controlling them:

### DriveSubsystem
- **Purpose**: Tank drive locomotion
- **Hardware**: 4 motor controllers (2 per side)
- **Features**: Voltage compensation, current limiting, speed control

### ArmSubsystem
- **Purpose**: Arm positioning for game piece manipulation
- **Hardware**: 2 motor controllers with encoders
- **Features**: Position-based PID control, hold positions

### RollerSubsystem
- **Purpose**: Game piece intake and manipulation
- **Hardware**: 2 roller motors
- **Features**: Variable speed control for different game pieces

### ShooterSubsystem
- **Purpose**: Precision shooting mechanism
- **Hardware**: 4 motors (2 upper, 2 lower)
- **Features**: RPM-based PID control, configurable speeds

### ClimberSubsystem
- **Purpose**: End-game climbing
- **Hardware**: Single motor controller
- **Features**: Manual up/down control

## Command Types

### Autonomous Commands
- `DriveForwardAuto`: Simple forward driving routine
- `SimpleCoralAuto`: Coral manipulation sequence

### Teleop Commands
- `DriveCommand`: Manual drive control with sensitivity curves
- `AlgieInCommand/AlgieOutCommand`: Algae manipulation
- `AlgieShootCommand`: Automated shooting sequence
- `ArmUpCommand/ArmDownCommand`: Arm positioning
- `CoralOutCommand/CoralStackCommand`: Coral handling
- `ClimberUpCommand/ClimberDownCommand`: Climbing operations

## Control Flow

### Initialization
1. `Robot.robotInit()` creates `RobotContainer`
2. `RobotContainer` instantiates all subsystems
3. Command bindings are configured
4. Default commands are assigned to subsystems

### Periodic Operation
1. `Robot.robotPeriodic()` runs `CommandScheduler.run()`
2. Command scheduler polls button inputs
3. Subsystem periodic methods update hardware
4. Active commands execute their logic

### Mode Transitions
- **Autonomous**: Selected command from chooser is scheduled
- **Teleop**: Autonomous command is cancelled, default commands resume
- **Disabled**: All commands are cancelled

## Design Patterns

### Command Pattern
- Commands encapsulate specific robot actions
- Commands can be composed and sequenced
- Easy to test and modify individual behaviors

### Subsystem Pattern
- Hardware abstraction and encapsulation
- Prevents multiple commands from conflicting
- Centralizes hardware-specific logic

### Constants Pattern
- All configuration values in one location
- Easy tuning without code changes
- Type-safe parameter management

## Key Features

### Sensitivity Control
The `Sensitivity` class provides customizable joystick response curves:
- Dead zone elimination
- Linear and quadratic response regions
- Speed limiting for precision control

### Smart Dashboard Integration
Real-time parameter tuning through SmartDashboard:
- Motor speeds and PID gains
- Timing constants
- Control sensitivity settings

### Safety Features
- Current limiting on all motors
- Voltage compensation for consistent performance
- Graceful handling of mode transitions

## File Organization

```
src/main/java/frc/robot/
├── Robot.java              # Main robot class
├── RobotContainer.java     # System container and bindings
├── Constants.java          # Configuration constants
├── Main.java              # Application entry point
├── PIDCtrl.java           # PID controller utility
├── Sensitivity.java       # Joystick sensitivity curves
├── autos/                 # Autonomous commands
├── commands/              # Teleop and utility commands
└── subsystems/            # Hardware subsystems
```

This architecture provides excellent maintainability, testability, and extensibility for the robot code while following FRC best practices.