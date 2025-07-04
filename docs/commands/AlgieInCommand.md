# AlgieInCommand

## Purpose and Functionality
The `AlgieInCommand` is designed to intake algae game pieces into the robot. This command controls the roller subsystem to pull algae into the robot's intake mechanism at a specified speed.

## Subsystems Required/Used
- **Primary**: `RollerSubsystem` - Controls the intake roller mechanism
- **Secondary**: `ShooterSubsystem` - Passed as parameter but not actively used in current implementation

## Parameters and Constructor Details
```java
public AlgieInCommand(RollerSubsystem roller, ShooterSubsystem shooter)
```

**Parameters:**
- `roller`: The RollerSubsystem instance used for intake operations
- `shooter`: The ShooterSubsystem instance (for future functionality)

**Dependencies:**
- Requires exclusive access to the RollerSubsystem via `addRequirements(roller)`

## Key Methods

### initialize()
- **Purpose**: Called when command is first scheduled
- **Implementation**: Currently empty - no initialization required

### execute()
- **Purpose**: Called continuously while command is active (~50 times per second)
- **Implementation**: Runs the roller at `RollerConstants.ROLLER_ALGAE_IN` speed (0.8)
- **Behavior**: Continuously intakes algae while command is active

### end(boolean interrupted)
- **Purpose**: Called when command ends or is interrupted
- **Implementation**: Stops the roller by setting speed to 0
- **Safety**: Ensures roller doesn't continue running unintentionally

### isFinished()
- **Purpose**: Determines when command should end
- **Implementation**: Always returns `false`
- **Behavior**: Command runs indefinitely until manually interrupted

## Usage Examples and When to Use

### Typical Usage
```java
// Create command instance
AlgieInCommand intakeCommand = new AlgieInCommand(robotContainer.getRollerSubsystem(), 
                                                  robotContainer.getShooterSubsystem());

// Bind to controller button
driverController.rightBumper().whileTrue(intakeCommand);
```

### When to Use
- **Manual Teleop Control**: Bind to a button for driver-controlled algae intake
- **Autonomous Sequences**: Use as part of a larger autonomous command sequence
- **Game Piece Collection**: When robot needs to collect algae from the field

### Not Recommended For
- Coral intake (use CoralOutCommand instead)
- Autonomous shooting sequences (use AlgieShootCommand)

## Special Features and State Management

### Continuous Operation
- Command never finishes on its own (`isFinished()` returns `false`)
- Designed for button-held operation (whileTrue binding)
- Automatically stops when button is released

### Safety Features
- **Motor Stop**: Ensures roller stops when command ends
- **Subsystem Protection**: Uses `addRequirements()` to prevent conflicts

### Configuration
- **Speed Control**: Intake speed defined by `RollerConstants.ROLLER_ALGAE_IN` (0.8)
- **Direction**: Positive value indicates inward rotation

### State Transitions
1. **Idle** → **Intaking**: When command is scheduled
2. **Intaking** → **Idle**: When command is interrupted/cancelled
3. **No Automatic End**: Command requires external interruption

### Performance Characteristics
- **Response Time**: Immediate start/stop
- **Power Usage**: Moderate (40A current limit on roller motors)
- **Reliability**: Simple, robust design with minimal failure points