# RollerSubsystem Documentation

## Purpose and Functionality

The `RollerSubsystem` controls the robot's roller mechanism used for manipulating and transporting game pieces. The roller system can intake objects, eject them, and help with game piece positioning. It features two motors in a follower configuration for increased torque and reliability.

## Hardware Components Used

### Motors
- **2 x SparkMax Motor Controllers** running brushless motors
  - Primary Roller Motor (CAN ID: 14)
  - Secondary Roller Motor (CAN ID: 15) - configured to follow the primary motor with inversion

### Sensors
- **Relative Encoder**: Built-in encoder from the primary roller motor for position and velocity feedback

### Configuration
- **Motor Type**: Brushless motors
- **Voltage Compensation**: 10V for consistent performance
- **Current Limiting**: 40A smart current limit
- **Idle Mode**: Brake mode to hold position when stopped
- **Motor Following**: Secondary motor follows primary with inversion

## Key Methods and Parameters

### Constructor: `RollerSubsystem()`
Initializes the roller motors, encoder, and PID controller. Sets up motor following configuration and applies safety parameters.

### `runRoller(double speed)`
Controls the roller motors at a specified speed for game piece manipulation.

**Parameters:**
- `speed` (double): Motor speed from -1.0 to 1.0, with 0 stopping the roller

**Usage Example:**
```java
// Run roller to intake game pieces (positive direction)
rollerSubsystem.runRoller(0.8);

// Run roller to eject game pieces (negative direction)  
rollerSubsystem.runRoller(-0.6);

// Stop the roller
rollerSubsystem.runRoller(0.0);
```

### `init()`
Initializes the encoder position and resets PID control variables. Should be called before starting any roller operations.

### `stop()`
Immediately stops the roller motor.

### Control Methods
- `putParams()`: Publishes parameters to SmartDashboard (currently empty implementation)
- `getParams()`: Retrieves parameters from SmartDashboard (currently empty implementation)

## Constants Used from Constants Class

All constants are defined in `Constants.RollerConstants`:

### Motor Configuration
- `ROLLER_MOTOR_ID = 14`: CAN ID for primary roller motor
- `ROLLER_MOTOR_IDR = 15`: CAN ID for secondary roller motor  
- `ROLLER_MOTOR_CURRENT_LIMIT = 40`: Smart current limit in amps
- `ROLLER_MOTOR_VOLTAGE_COMP = 10`: Voltage compensation in volts

### Operational Speeds for Different Game Pieces

#### Algae Manipulation
- `ROLLER_ALGAE_IN = 0.8`: Speed for taking in algae pieces
- `ROLLER_ALGAE_OUT = -0.8`: Speed for ejecting algae pieces

#### Coral Manipulation  
- `ROLLER_CORAL_OUT = -0.4`: Speed for ejecting coral pieces
- `ROLLER_CORAL_STACK = -1`: Maximum speed for coral stacking operations

#### General Operations
- `SpeedIn = 0.8`: Standard intake speed
- `SpeedOut = -0.8`: Standard outtake speed
- `SpeedShoot = 0.4`: Speed for shooting/launching game pieces
- `SpeedBackoff = 0.2`: Slow speed for precise positioning

## Special Features

### Motor Following Configuration
The roller uses a leader-follower configuration:
- Secondary motor automatically follows the primary motor with inversion
- This provides increased torque for handling game pieces
- Reduces CAN bus traffic compared to controlling motors separately
- Ensures synchronized operation

### Brake Mode Operation
The roller motors are configured in brake mode to:
- Hold position when stopped
- Prevent game pieces from slipping when not actively controlled
- Provide better control during precise operations

### Debug Output
The subsystem prints diagnostic information including:
- Applied output values for both motors
- Helps with troubleshooting motor synchronization
- Useful for performance monitoring

### Encoder Feedback
Built-in encoder provides:
- Position tracking for precise control
- Velocity feedback for monitoring performance
- Foundation for future PID control implementation

## Usage Examples

### Basic Roller Control Command
```java
public class RunRollerCommand extends CommandBase {
    private final RollerSubsystem rollerSubsystem;
    private final double speed;
    
    public RunRollerCommand(RollerSubsystem rollerSubsystem, double speed) {
        this.rollerSubsystem = rollerSubsystem;
        this.speed = speed;
        addRequirements(rollerSubsystem);
    }
    
    @Override
    public void initialize() {
        rollerSubsystem.init();
    }
    
    @Override
    public void execute() {
        rollerSubsystem.runRoller(speed);
    }
    
    @Override
    public void end(boolean interrupted) {
        rollerSubsystem.stop();
    }
}
```

### Game Piece Specific Commands
```java
// Intake algae pieces
public class IntakeAlgaeCommand extends RunRollerCommand {
    public IntakeAlgaeCommand(RollerSubsystem rollerSubsystem) {
        super(rollerSubsystem, RollerConstants.ROLLER_ALGAE_IN);
    }
}

// Eject coral pieces
public class EjectCoralCommand extends RunRollerCommand {
    public EjectCoralCommand(RollerSubsystem rollerSubsystem) {
        super(rollerSubsystem, RollerConstants.ROLLER_CORAL_OUT);
    }
}

// Coral stacking operation
public class StackCoralCommand extends RunRollerCommand {
    public StackCoralCommand(RollerSubsystem rollerSubsystem) {
        super(rollerSubsystem, RollerConstants.ROLLER_CORAL_STACK);
    }
}
```

### Variable Speed Control
```java
public class VariableRollerCommand extends CommandBase {
    private final RollerSubsystem rollerSubsystem;
    private final Supplier<Double> speedSupplier;
    
    public VariableRollerCommand(RollerSubsystem rollerSubsystem, 
                                Supplier<Double> speedSupplier) {
        this.rollerSubsystem = rollerSubsystem;
        this.speedSupplier = speedSupplier;
        addRequirements(rollerSubsystem);
    }
    
    @Override
    public void execute() {
        double speed = speedSupplier.get();
        // Apply deadband
        if (Math.abs(speed) < 0.1) {
            speed = 0.0;
        }
        rollerSubsystem.runRoller(speed);
    }
    
    @Override
    public void end(boolean interrupted) {
        rollerSubsystem.stop();
    }
}
```

### Timed Roller Operation
```java
public class TimedRollerCommand extends CommandBase {
    private final RollerSubsystem rollerSubsystem;
    private final double speed;
    private final Timer timer;
    private final double duration;
    
    public TimedRollerCommand(RollerSubsystem rollerSubsystem, 
                             double speed, double duration) {
        this.rollerSubsystem = rollerSubsystem;
        this.speed = speed;
        this.duration = duration;
        this.timer = new Timer();
        addRequirements(rollerSubsystem);
    }
    
    @Override
    public void initialize() {
        rollerSubsystem.init();
        timer.restart();
    }
    
    @Override
    public void execute() {
        rollerSubsystem.runRoller(speed);
    }
    
    @Override
    public boolean isFinished() {
        return timer.hasElapsed(duration);
    }
    
    @Override
    public void end(boolean interrupted) {
        rollerSubsystem.stop();
    }
}
```

## Technical Notes

- The subsystem includes infrastructure for PID control but it's not currently implemented in the main control path
- Encoder position is reset during initialization for consistent starting conditions
- The secondary motor is configured to follow with inversion to ensure proper rotation direction
- CAN timeout is set to 250ms during initialization for reliable motor configuration
- Motor configuration uses `ResetMode.kResetSafeParameters` and `PersistMode.kPersistParameters`
- Debug output helps monitor motor synchronization between primary and secondary motors
- The subsystem is designed to work with the WPILib command-based programming framework
- Brake mode prevents game pieces from falling out when the roller is stopped
- Current limiting protects motors and prevents breaker trips during high-load operations