# Setup and Installation Guide

This guide will help you set up the development environment and deploy the Everybot 2025 code to your robot.

## Prerequisites

### Software Requirements

- **Java Development Kit (JDK) 17** or later
- **WPILib 2025** - Download from [WPILib Releases](https://github.com/wpilibsuite/allwpilib/releases)
- **Visual Studio Code** with WPILib extension
- **Git** for version control
- **Gradle** (included with WPILib)

### Hardware Requirements

- RoboRIO 2.0
- Compatible motor controllers (Spark MAX, Talon SRX, etc.)
- Driver Station laptop
- Radio for robot communication

## Installation Steps

### 1. Install WPILib

1. Download the WPILib installer for your operating system
2. Run the installer and follow the setup wizard
3. Install VS Code and the WPILib extension when prompted
4. Verify installation by opening WPILib VS Code

### 2. Clone the Repository

```bash
git clone <repository-url>
cd Everybot-Flux
```

### 3. Configure Team Number

1. Open the project in VS Code
2. Press `Ctrl+Shift+P` (or `Cmd+Shift+P` on macOS)
3. Type "WPILib: Set Team Number"
4. Enter your FRC team number

### 4. Build the Project

```bash
./gradlew build
```

Or use the VS Code command palette:
- Press `Ctrl+Shift+P`
- Type "WPILib: Build Robot Code"

### 5. Deploy to Robot

#### Prerequisites for Deployment
- RoboRIO must be imaged with the correct version
- Robot must be connected to your development machine
- Driver Station should show robot communication

#### Deploy Steps
```bash
./gradlew deploy
```

Or use VS Code:
- Press `Ctrl+Shift+P`
- Type "WPILib: Deploy Robot Code"

## Development Workflow

### Building
```bash
./gradlew build          # Build the project
./gradlew clean build    # Clean and build
```

### Testing
```bash
./gradlew test           # Run unit tests
```

### Simulation
```bash
./gradlew simulateJava   # Run robot simulation
```

Or use VS Code:
- Press `Ctrl+Shift+P`
- Type "WPILib: Simulate Robot Code"

## Project Structure

```
Everybot-Flux/
├── src/main/java/frc/robot/     # Main robot code
│   ├── Robot.java               # Main robot class
│   ├── RobotContainer.java      # Subsystem and command container
│   ├── Constants.java           # Configuration constants
│   ├── autos/                   # Autonomous commands
│   ├── commands/                # Robot commands
│   └── subsystems/              # Robot subsystems
├── src/main/deploy/             # Files deployed to robot
├── build.gradle                 # Gradle build configuration
├── gradlew                      # Gradle wrapper (Unix)
├── gradlew.bat                  # Gradle wrapper (Windows)
└── vendordeps/                  # Vendor library dependencies
```

## Configuration

### Motor Controller IDs
Review and update motor controller IDs in `Constants.java` to match your robot's wiring:

```java
public static final class DriveConstants {
    public static final int LEFT_LEADER_ID = 18;
    public static final int LEFT_FOLLOWER_ID = 19;
    public static final int RIGHT_LEADER_ID = 10;
    public static final int RIGHT_FOLLOWER_ID = 11;
}
```

### Controller Ports
Verify controller ports match your setup:

```java
public static final class OperatorConstants {
    public static final int DRIVER_CONTROLLER_PORT = 0;
    public static final int OPERATOR_CONTROLLER_PORT = 0;
}
```

## Troubleshooting

### Common Issues

1. **Build Failures**
   - Ensure Java 17 is installed and configured
   - Check internet connection for dependency downloads
   - Run `./gradlew clean build`

2. **Deploy Failures**
   - Verify robot connection in Driver Station
   - Check team number configuration
   - Ensure RoboRIO is properly imaged

3. **Simulation Issues**
   - Install desktop simulation libraries
   - Check `includeDesktopSupport = true` in build.gradle

### Getting Help

- Check the [WPILib Documentation](https://docs.wpilib.org/)
- Visit the [FRC Discord](https://discord.gg/frc)
- Review the [Chief Delphi](https://www.chiefdelphi.com/) forums

## Next Steps

After successful installation:
1. Review the [Architecture Overview](architecture.md)
2. Understand the [Subsystems](subsystems/) documentation
3. Learn about [Controls](controls.md) and driver interface
4. Explore [Autonomous](autonomous.md) routines