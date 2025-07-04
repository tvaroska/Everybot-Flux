# Robot Subsystems Overview

This directory contains comprehensive documentation for all robot subsystems in the Everybot-Flux project. Each subsystem is responsible for controlling specific robot functionality and hardware components.

## Subsystem Summary

| Subsystem | Purpose | Motors | Control Type | Key Features |
|-----------|---------|---------|--------------|--------------|
| [DriveSubsystem](DriveSubsystem.md) | Robot locomotion | 4 x SparkMax (Brushless) | Differential Drive | Arcade/Tank drive, Voltage compensation |
| [ArmSubsystem](ArmSubsystem.md) | Arm positioning | 2 x SparkMax (Brushless) | PID Position Control | Preset positions, Real-time tuning |
| [RollerSubsystem](RollerSubsystem.md) | Game piece manipulation | 2 x SparkMax (Brushless) | Open-loop Speed | Brake mode, Follower config |
| [ShooterSubsystem](ShooterSubsystem.md) | Game piece launching | 3 x SparkMax (Brushless) | PID Velocity Control | Dual-wheel control, Multiple presets |
| [ClimberSubsystem](ClimberSubsystem.md) | End-game climbing | 1 x SparkMax (Brushed) | Open-loop Speed | Winch mechanism, Brake mode |

## Common Features Across Subsystems

### Motor Controllers
- **SparkMax Controllers**: All subsystems use REV Robotics SparkMax motor controllers
- **CAN Bus Communication**: All motors communicate via CAN bus with configurable IDs
- **Voltage Compensation**: All subsystems implement voltage compensation for consistent performance
- **Current Limiting**: Smart current limiting protects motors and prevents breaker trips
- **Persistent Configuration**: Motor settings persist through power cycles

### Safety Features
- **Current Limiting**: Prevents motor damage and breaker trips
- **Voltage Compensation**: Maintains consistent behavior across battery voltages
- **Brake/Coast Modes**: Appropriate idle modes for each subsystem's needs
- **CAN Timeouts**: Proper timeout settings for reliable communication

### Control Methods
- **Open-loop Control**: Direct speed control (Drive, Roller, Climber)
- **PID Position Control**: Precise positioning (Arm)
- **PID Velocity Control**: Accurate speed regulation (Shooter)

## Hardware Configuration Summary

### CAN ID Assignments
```
Drive System:
- Left Leader: 18    - Left Follower: 19
- Right Leader: 10   - Right Follower: 11

Arm System:
- Primary Arm: 4     - Secondary Arm: 5

Roller System:
- Primary Roller: 14 - Secondary Roller: 15

Shooter System:
- Upper Motor: 8     - Lower Motor: 6
- Lower Motor R: 7

Climber System:
- Climber Motor: 17
```

### Power Requirements
| Subsystem | Current Limit | Voltage Compensation |
|-----------|---------------|---------------------|
| Drive | 60A per motor | 12V |
| Arm | 40A per motor | 10V |
| Roller | 40A per motor | 10V |
| Shooter | 40A per motor | 10V |
| Climber | 40A | 12V |

## Control Integration

### Command-Based Framework
All subsystems are designed to work with WPILib's command-based programming framework:
- Extend `SubsystemBase` for integration
- Implement `periodic()` methods (currently empty for most)
- Support command requirements and scheduling
- Compatible with command groups and autonomous routines

### SmartDashboard Integration
Several subsystems support real-time parameter tuning:
- **ArmSubsystem**: PID parameters, speeds, positions
- **ShooterSubsystem**: PID parameters, RPM settings, tolerances
- Real-time monitoring of performance metrics
- Live tuning during testing and matches

## Usage Patterns

### Initialization
```java
// Basic subsystem initialization
subsystem.init();

// Advanced initialization with parameters
shooterSubsystem.init(2500, -2500);
armSubsystem.setAngle(true);
```

### Basic Control
```java
// Direct speed control
driveSubsystem.driveArcade(speed, rotation, squared);
rollerSubsystem.runRoller(speed);
climberSubsystem.runClimber(speed);

// Position/velocity control
armSubsystem.runToPosition(position);
shooterSubsystem.run(target);
```

### Safety Stops
```java
// Stop all subsystems
subsystem.stop();
```

## Maintenance and Monitoring

### Regular Checks
- Verify CAN ID assignments match physical wiring
- Monitor motor temperatures during operation
- Check current consumption for signs of mechanical issues
- Validate encoder readings for position-controlled subsystems

### Performance Monitoring
- SmartDashboard telemetry for real-time diagnostics
- Console debug output for detailed analysis
- Current monitoring for motor health assessment
- Position/velocity tracking for control system validation

## Future Enhancements

### Potential Improvements
- **Position Control for Roller**: Add encoder-based position control
- **Enhanced Arm Control**: Implement feed-forward and gravity compensation
- **Shooter Optimization**: Auto-tuning based on distance/target
- **Climber Sensing**: Add limit switches or encoders for position feedback
- **Integrated Safety**: Cross-subsystem safety interlocks

### Code Quality
- Implement complete `atSetPoint()` methods where needed
- Add comprehensive error handling and recovery
- Standardize parameter validation across subsystems
- Enhance documentation with more usage examples

For detailed information about each subsystem, please refer to the individual documentation files linked in the summary table above.