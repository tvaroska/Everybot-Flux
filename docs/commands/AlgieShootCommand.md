# AlgieShootCommand

## Purpose and Functionality
The `AlgieShootCommand` is a sophisticated command designed to accurately shoot algae game pieces into scoring targets. It implements a complex timed sequence involving roller backoff, shooter wheel acceleration, and coordinated algae feeding for precise scoring.

## Subsystems Required/Used
- **Primary**: `RollerSubsystem` - Controls algae feeding and backoff
- **Primary**: `ShooterSubsystem` - Controls shooter wheel speed and acceleration
- **Integration**: SmartDashboard for real-time parameter tuning

## Parameters and Constructor Details
```java
public AlgieShootCommand(ShooterSubsystem shooter, RollerSubsystem roller, int presetNumber)
```

**Parameters:**
- `shooter`: The ShooterSubsystem instance for wheel control
- `roller`: The RollerSubsystem instance for algae feeding
- `presetNumber`: Preset configuration (0-3) for different shooting scenarios

**Dependencies:**
- Requires exclusive access to the RollerSubsystem via `addRequirements(roller)`
- Note: ShooterSubsystem not in requirements (allows parallel operation)

## Key Methods

### initialize()
- **Purpose**: Sets up shooting sequence parameters and initializes subsystems
- **Implementation**: 
  - Calls `getParams()` to retrieve SmartDashboard values
  - Resets execution counter
  - Initializes roller and shooter with preset speeds
  - Sets up PID control parameters (commented)

### execute()
- **Purpose**: Manages the timed shooting sequence
- **Implementation**: Complex state machine with four phases:
  1. **Backoff Phase** (0 to `intakeBackTime`): Reverses roller to prevent jamming
  2. **Wait Phase** (`intakeBackTime` to `shootWaitTime`): Stops roller, allows shooter to spin up
  3. **Acceleration Phase** (`shootWaitTime` to `shootStartTime`): Runs shooter at target speed
  4. **Shooting Phase** (`shootStartTime` to `shootEndTime`): Coordinates shooter and roller

### end(boolean interrupted)
- **Purpose**: Safely stops all subsystems when shooting ends
- **Implementation**: Stops both roller and shooter motors

### isFinished()
- **Purpose**: Determines when shooting sequence is complete
- **Implementation**: Returns `true` when execution time exceeds `shootEndTime`

## Usage Examples and When to Use

### Typical Usage
```java
// Create preset shooting commands
AlgieShootCommand closeShot = new AlgieShootCommand(shooter, roller, 0);    // Preset 1
AlgieShootCommand midShot = new AlgieShootCommand(shooter, roller, 1);      // Preset 2
AlgieShootCommand longShot = new AlgieShootCommand(shooter, roller, 2);     // Preset 3
AlgieShootCommand maxShot = new AlgieShootCommand(shooter, roller, 3);      // Preset 4

// Bind to controller buttons
operatorController.a().onTrue(closeShot);
operatorController.b().onTrue(midShot);
operatorController.x().onTrue(longShot);
operatorController.y().onTrue(maxShot);
```

### When to Use
- **Autonomous Scoring**: Precise algae shooting during autonomous period
- **Teleop Scoring**: Accurate scoring from various field positions
- **High-Precision Shots**: When accuracy is more important than speed
- **Competition Play**: Reliable scoring mechanism for match play

### Preset Configurations
- **Preset 0**: Close-range shots (SpeedUp1: 2110 RPM, SpeedDown1: -1950 RPM)
- **Preset 1**: Medium-range shots (SpeedUp2: 2200 RPM, SpeedDown2: -2200 RPM)
- **Preset 2**: Long-range shots (SpeedUp3: 2800 RPM, SpeedDown3: -2800 RPM)
- **Preset 3**: Maximum-range shots (SpeedUp4: 3000 RPM, SpeedDown4: -3000 RPM)

## Special Features and State Management

### Timed Sequence Control
The command implements a sophisticated timing system:

```java
// Default timing values (milliseconds)
IntakeBackTime: 300ms    // Roller backoff duration
ShootWaitTime: 500ms     // Wait for shooter acceleration
ShootStartTime: 2000ms   // Begin coordinated shooting
ShootFinishTime: 3000ms  // Complete shooting sequence
```

### SmartDashboard Integration
- **Real-time Tuning**: Parameters can be adjusted during operation
- **Parameter Validation**: Ensures timing values maintain logical sequence
- **Preset Management**: Individual speed settings for each preset
- **Debug Information**: Timing and speed values displayed for monitoring

### Safety Features
- **Sequential Timing**: Prevents overlapping operations that could cause jams
- **Parameter Validation**: Ensures timing values are logically consistent
- **Automatic Cleanup**: Stops all motors when command ends
- **Preset Boundaries**: Speed values clamped to safe operating ranges

### State Machine Implementation
1. **Initialization**: Load parameters, reset counters, initialize subsystems
2. **Backoff**: Reverse roller to prevent algae jamming (300ms)
3. **Wait**: Allow shooter wheels to reach target speed (200ms)
4. **Acceleration**: Shooter runs at full speed (1500ms)
5. **Shooting**: Coordinated roller feeding with shooter (1000ms)
6. **Completion**: Automatic termination and cleanup

### Performance Characteristics
- **Accuracy**: High precision through timed coordination
- **Reliability**: Robust sequence handling prevents jams
- **Flexibility**: Four presets cover various shooting scenarios
- **Tuning**: Real-time parameter adjustment via SmartDashboard

### Advanced Features
- **PID Control**: Commented implementation for future speed control
- **Sensor Integration**: Framework for closed-loop shooting control
- **Parameter Persistence**: SmartDashboard values maintained between runs
- **Debug Output**: Console logging for troubleshooting

### Configuration Management
```java
// Parameter retrieval from SmartDashboard
public void getParams() {
    // Speed settings for current preset
    upSpeeds[preset] = SmartDashboard.getNumber("Set RPM Up " + (preset + 1), default);
    downSpeeds[preset] = SmartDashboard.getNumber("Set RPM Down " + (preset + 1), default);
    
    // Timing parameters
    intakeBackTime = (int) SmartDashboard.getNumber("Backoff Time", default);
    // ... additional parameter loading
}
```