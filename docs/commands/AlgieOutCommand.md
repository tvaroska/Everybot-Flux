# AlgieOutCommand

## Purpose and Functionality
The `AlgieOutCommand` is designed to eject algae game pieces from the robot. This command controls the roller subsystem to push algae out of the robot's intake mechanism. **Note: Not recommended for scoring coral game pieces.**

## Subsystems Required/Used
- **Primary**: `RollerSubsystem` - Controls the ejection roller mechanism
- **Secondary**: `ShooterSubsystem` - Passed as parameter but not actively used (commented out functionality)

## Parameters and Constructor Details
```java
public AlgieOutCommand(RollerSubsystem roller, ShooterSubsystem shooter)
```

**Parameters:**
- `roller`: The RollerSubsystem instance used for ejection operations
- `shooter`: The ShooterSubsystem instance (contains commented-out functionality)

**Dependencies:**
- Requires exclusive access to the RollerSubsystem via `addRequirements(roller)`

## Key Methods

### initialize()
- **Purpose**: Called when command is first scheduled
- **Implementation**: Currently empty - no initialization required

### execute()
- **Purpose**: Called continuously while command is active (~50 times per second)
- **Implementation**: Runs the roller at `RollerConstants.ROLLER_ALGAE_OUT` speed (-0.8)
- **Behavior**: Continuously ejects algae while command is active
- **Commented Code**: Contains disabled shooter functionality (`m_shooter.runDown(0.3)`)

### end(boolean interrupted)
- **Purpose**: Called when command ends or is interrupted
- **Implementation**: Stops the roller by setting speed to 0
- **Safety**: Ensures roller doesn't continue running unintentionally
- **Commented Code**: Contains disabled shooter stop functionality

### isFinished()
- **Purpose**: Determines when command should end
- **Implementation**: Always returns `false`
- **Behavior**: Command runs indefinitely until manually interrupted

## Usage Examples and When to Use

### Typical Usage
```java
// Create command instance
AlgieOutCommand ejectCommand = new AlgieOutCommand(robotContainer.getRollerSubsystem(), 
                                                   robotContainer.getShooterSubsystem());

// Bind to controller button
driverController.leftBumper().whileTrue(ejectCommand);
```

### When to Use
- **Algae Ejection**: Removing algae from the robot when not needed
- **Game Piece Passing**: Passing algae to alliance partners
- **Emergency Clearing**: Clearing jammed or unwanted algae
- **Field Reset**: Ejecting algae during autonomous or teleop

### Not Recommended For
- **Coral Scoring**: Use `CoralOutCommand` instead for coral game pieces
- **Precision Shooting**: Use `AlgieShootCommand` for accurate scoring

## Special Features and State Management

### Continuous Operation
- Command never finishes on its own (`isFinished()` returns `false`)
- Designed for button-held operation (whileTrue binding)
- Automatically stops when button is released

### Safety Features
- **Motor Stop**: Ensures roller stops when command ends
- **Subsystem Protection**: Uses `addRequirements()` to prevent conflicts
- **Direction Safety**: Negative speed ensures outward ejection

### Configuration
- **Speed Control**: Ejection speed defined by `RollerConstants.ROLLER_ALGAE_OUT` (-0.8)
- **Direction**: Negative value indicates outward rotation
- **Future Enhancement**: Commented shooter integration for potential upgrades

### State Transitions
1. **Idle** → **Ejecting**: When command is scheduled
2. **Ejecting** → **Idle**: When command is interrupted/cancelled
3. **No Automatic End**: Command requires external interruption

### Performance Characteristics
- **Response Time**: Immediate start/stop
- **Power Usage**: Moderate (40A current limit on roller motors)
- **Reliability**: Simple, robust design with minimal failure points
- **Speed**: Full reverse speed for effective algae ejection

### Future Enhancements
The command contains commented code for shooter integration:
```java
// Potential future functionality
// m_shooter.runDown(0.3);  // In execute()
// m_shooter.runDown(0);    // In end()
```
This suggests potential for enhanced algae handling with coordinated roller-shooter operation.