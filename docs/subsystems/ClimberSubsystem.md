# ClimberSubsystem Documentation

## Purpose and Functionality

The `ClimberSubsystem` controls the robot's climbing mechanism used for end-game climbing operations. This subsystem operates a winch-style climber that can extend and retract to lift the robot onto game field structures. The climber is essential for earning climb points during the final phase of matches.

## Hardware Components Used

### Motors
- **1 x SparkMax Motor Controller** running a brushed motor
  - Climber Motor (CAN ID: 17)

### Configuration
- **Motor Type**: Brushed motor (Note: declared as brushed in constructor despite comment mentioning brushless)
- **Voltage Compensation**: 12V for consistent performance across battery voltages
- **Current Limiting**: 40A smart current limit to prevent damage
- **Idle Mode**: Brake mode to hold position when stopped
- **Mechanism Type**: Winch-style climber with directional control

## Key Methods and Parameters

### Constructor: `ClimberSubsystem()`
Initializes the climber motor with appropriate safety and performance configurations.

### `runClimber(double speed)`
Controls the climber motor at a specified speed for climbing operations.

**Parameters:**
- `speed` (double): Motor speed from -1.0 to 1.0, with 0 stopping the climber

**Usage Example:**
```java
// Extend climber (direction depends on winch winding)
climberSubsystem.runClimber(0.5);

// Retract climber  
climberSubsystem.runClimber(-0.5);

// Stop climber and hold position
climberSubsystem.runClimber(0.0);
```

**Important Note**: The actual direction (extend vs retract) depends on how the winch cable is wound. Positive values may extend or retract depending on the mechanical setup.

## Constants Used from Constants Class

All constants are defined in `Constants.ClimberConstants`:

### Motor Configuration
- `CLIMBER_MOTOR_ID = 17`: CAN ID for the climber motor
- `CLIMBER_MOTOR_CURRENT_LIMIT = 40`: Smart current limit in amps
- `CLIMBER_MOTOR_VOLTAGE_COMP = 12`: Voltage compensation in volts

### Operational Speeds
- `CLIMBER_SPEED_DOWN = -0.5`: Standard speed for downward movement
- `CLIMBER_SPEED_UP = 0.5`: Standard speed for upward movement

## Special Features

### Brake Mode Operation
The climber motor is configured in brake mode to:
- Hold the robot's position when climbing is stopped
- Prevent the robot from falling during climb operations
- Provide safety during power interruptions
- Maintain climb position between movements

### Voltage Compensation
Set to 12V to ensure:
- Consistent climbing performance throughout the match
- Predictable behavior regardless of battery charge level
- Reliable operation during high-current draw situations

### Current Limiting
40A current limit provides:
- Protection against motor burnout during stalls
- Prevention of breaker trips during high-load operations
- Safety during unexpected mechanical resistance

### Simple Control Interface
The subsystem provides a straightforward control method:
- Single method for all climbing operations
- Direction controlled by sign of speed parameter
- No complex state management required
- Compatible with both manual and automated control

## Usage Examples

### Basic Climbing Command
```java
public class ClimbCommand extends CommandBase {
    private final ClimberSubsystem climberSubsystem;
    private final double speed;
    
    public ClimbCommand(ClimberSubsystem climberSubsystem, double speed) {
        this.climberSubsystem = climberSubsystem;
        this.speed = speed;
        addRequirements(climberSubsystem);
    }
    
    @Override
    public void execute() {
        climberSubsystem.runClimber(speed);
    }
    
    @Override
    public void end(boolean interrupted) {
        climberSubsystem.runClimber(0.0); // Stop and hold position
    }
}
```

### Preset Climbing Commands
```java
// Climb up command
public class ClimbUpCommand extends ClimbCommand {
    public ClimbUpCommand(ClimberSubsystem climberSubsystem) {
        super(climberSubsystem, ClimberConstants.CLIMBER_SPEED_UP);
    }
}

// Climb down command  
public class ClimbDownCommand extends ClimbCommand {
    public ClimbDownCommand(ClimberSubsystem climberSubsystem) {
        super(climberSubsystem, ClimberConstants.CLIMBER_SPEED_DOWN);
    }
}
```

### Manual Climber Control
```java
public class ManualClimberCommand extends CommandBase {
    private final ClimberSubsystem climberSubsystem;
    private final Supplier<Double> speedSupplier;
    
    public ManualClimberCommand(ClimberSubsystem climberSubsystem, 
                               Supplier<Double> speedSupplier) {
        this.climberSubsystem = climberSubsystem;
        this.speedSupplier = speedSupplier;
        addRequirements(climberSubsystem);
    }
    
    @Override
    public void execute() {
        double speed = speedSupplier.get();
        
        // Apply deadband for controller input
        if (Math.abs(speed) < 0.1) {
            speed = 0.0;
        }
        
        // Scale speed for safety
        speed *= 0.75; // Limit to 75% max speed
        
        climberSubsystem.runClimber(speed);
    }
    
    @Override
    public void end(boolean interrupted) {
        climberSubsystem.runClimber(0.0);
    }
}
```

### Timed Climbing Operations
```java
public class TimedClimbCommand extends CommandBase {
    private final ClimberSubsystem climberSubsystem;
    private final double speed;
    private final double duration;
    private final Timer timer;
    
    public TimedClimbCommand(ClimberSubsystem climberSubsystem, 
                            double speed, double duration) {
        this.climberSubsystem = climberSubsystem;
        this.speed = speed;
        this.duration = duration;
        this.timer = new Timer();
        addRequirements(climberSubsystem);
    }
    
    @Override
    public void initialize() {
        timer.restart();
    }
    
    @Override
    public void execute() {
        climberSubsystem.runClimber(speed);
    }
    
    @Override
    public boolean isFinished() {
        return timer.hasElapsed(duration);
    }
    
    @Override
    public void end(boolean interrupted) {
        climberSubsystem.runClimber(0.0);
    }
}
```

### Sequential Climbing Command
```java
public class SequentialClimbCommand extends SequentialCommandGroup {
    public SequentialClimbCommand(ClimberSubsystem climberSubsystem) {
        addCommands(
            // Extend climber
            new TimedClimbCommand(climberSubsystem, 
                                ClimberConstants.CLIMBER_SPEED_UP, 2.0),
            
            // Brief pause
            new WaitCommand(0.5),
            
            // Retract to lift robot
            new TimedClimbCommand(climberSubsystem, 
                                ClimberConstants.CLIMBER_SPEED_DOWN, 3.0)
        );
    }
}
```

## Safety Considerations

### Current Monitoring
- Monitor motor current during operation to detect:
  - Mechanical binding or obstacles
  - Worn or damaged winch components
  - Excessive load conditions

### Position Awareness
- Be aware of climber extension limits to prevent:
  - Over-extension damage
  - Cable unwinding from winch
  - Collision with field elements

### Emergency Stop
- Always be prepared to stop climbing operations immediately
- Brake mode will hold position if power is lost
- Manual override should be available for emergency situations

### Operational Guidelines
- Test climber operation before each match
- Verify proper cable routing and tension
- Check for mechanical wear or damage
- Ensure adequate battery voltage for climbing

## Technical Notes

- **Motor Type Discrepancy**: Code comment mentions brushless but constructor uses `MotorType.kBrushed`
- **Brake Mode**: Essential for safety - prevents robot from falling when stopped
- **Voltage Compensation**: Higher than other subsystems (12V vs 10V) for climbing power requirements
- **Current Limit**: Same as other subsystems (40A) but may need monitoring during high-load climbing
- **No Encoder**: Simple open-loop control without position feedback
- **CAN Timeout**: 250ms timeout during initialization for reliable configuration
- **Persistent Configuration**: Motor settings persist through power cycles for reliability
- **Single Motor**: Uses only one motor - consider load capacity for robot weight
- **Direction Dependency**: Actual movement direction depends on mechanical winch configuration