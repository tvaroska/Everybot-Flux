# Everybot-2025-Code Documentation

This documentation provides comprehensive information about the Everybot 2025 robot code, a FIRST Robotics Competition (FRC) robot built with Java and the WPILib framework.

## Table of Contents

- [Getting Started](setup-installation.md) - Installation and setup instructions
- [Architecture Overview](architecture.md) - Code structure and design patterns
- [Subsystems](subsystems/) - Detailed documentation for each robot subsystem
- [Commands](commands/) - Robot command implementations
- [Configuration](configuration.md) - Constants and configuration options
- [Autonomous](autonomous.md) - Autonomous mode documentation
- [Controls](controls.md) - Driver and operator control mappings
- [Troubleshooting](troubleshooting.md) - Common issues and solutions
- [API Reference](api/) - Complete API documentation

## Quick Links

- [Robot.java](../src/main/java/frc/robot/Robot.java) - Main robot class
- [Constants.java](../src/main/java/frc/robot/Constants.java) - Robot configuration constants
- [RobotContainer.java](../src/main/java/frc/robot/RobotContainer.java) - Robot subsystem and command container

## About This Robot

The Everybot 2025 is designed for the FIRST Robotics Competition game. It features:

- **Drive System**: Tank drive with dual motor controllers per side
- **Arm Mechanism**: Powered arm system for game piece manipulation
- **Roller/Intake System**: Dual roller system for algae and coral handling
- **Shooter System**: Precision shooting mechanism with PID control
- **Climber**: End-game climbing capability

## Framework Information

- **Language**: Java 17
- **Framework**: WPILib 2025.2.1
- **Build System**: Gradle
- **Command Pattern**: Command-based robot architecture