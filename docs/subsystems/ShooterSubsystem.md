# ShooterSubsystem Documentation

## Purpose and Functionality

The `ShooterSubsystem` controls the robot's shooter mechanism for launching game pieces at high speed and accuracy. It features a dual-motor flywheel system with independent upper and lower wheels that can spin at different speeds to control trajectory. The subsystem uses sophisticated PID velocity control to maintain precise wheel speeds for consistent shooting performance.

## Hardware Components Used

### Motors
- **3 x SparkMax Motor Controllers** running brushless motors
  - Upper Shooter Motor (CAN ID: 8) - primary upper wheel motor
  - Lower Shooter Motor (CAN ID: 6) - primary lower wheel motor  
  - Lower Shooter Motor R (CAN ID: 7) - secondary lower wheel motor (follows primary)

### Sensors
- **2 x Relative Encoders**: Built-in encoders from both primary motors for velocity feedback
  - Upper encoder for upper wheel speed control
  - Lower encoder for lower wheel speed control

### Configuration
- **Motor Type**: Brushless motors
- **Voltage Compensation**: 10V for consistent performance
- **Current Limiting**: 40A smart current limit
- **Idle Mode**: Coast mode for smooth spinning
- **Control Method**: Velocity PID control for precise speed regulation

## Key Methods and Parameters

### Constructor: `ShooterSubsystem()`
Initializes shooter motors, encoders, and dual PID controllers. Sets up motor configuration and safety parameters.

### `init(double upRPM, double downRPM)`
Initializes the shooter system with target RPM values for both wheels.

**Parameters:**
- `upRPM` (double): Target RPM for upper wheel (positive value)
- `downRPM` (double): Target RPM for lower wheel (negative value for opposite rotation)

**Usage Example:**
```java
// Initialize for high-speed shooting
shooterSubsystem.init(2500, -2500);

// Initialize for close-range shooting  
shooterSubsystem.init(2000, -2000);
```

### `run(double target)`
Main control method that maintains target wheel speeds using PID control. Must be called continuously during shooting operations.

**Parameters:**
- `target` (double): Not directly used - speeds are set by `init()` method

### `runRaw(double target)`
Runs both motors at the same raw speed without PID control.

**Parameters:**
- `target` (double): Motor speed from -1.0 to 1.0

**Usage Example:**
```java
// Run both wheels at 50% speed for testing
shooterSubsystem.runRaw(0.5);
```

### `runDown(double target)`
Runs only the lower wheel at a specified speed.

**Parameters:**
- `target` (double): Motor speed for lower wheel (automatically inverted)

### `atSetPoint()`
Returns whether both wheels are at their target speeds within tolerance.

**Returns:**
- `boolean`: true if both wheels are within speed tolerance

### `stop()`
Immediately stops all shooter motors.

## Constants Used from Constants Class

All constants are defined in `Constants.ShooterConstants`:

### Motor Configuration
- `UP_MOTOR_ID = 8`: CAN ID for upper shooter motor
- `DOWN_MOTOR_ID = 6`: CAN ID for lower shooter motor
- `DOWN_MOTOR_IDR = 7`: CAN ID for secondary lower shooter motor
- `SHOOT_MOTOR_CURRENT_LIMIT = 40`: Smart current limit in amps
- `SHOOT_MOTOR_VOLTAGE_COMP = 10`: Voltage compensation in volts

### Speed Presets (RPM)
- `SpeedUp = 2500`: Default upper wheel speed
- `SpeedDown = -2500`: Default lower wheel speed

#### Shooting Modes
- `SpeedUp1 = 2110` / `SpeedDown1 = -1950`: Close range shooting
- `SpeedUp2 = 2200` / `SpeedDown2 = -2200`: Medium range shooting  
- `SpeedUp3 = 2800` / `SpeedDown3 = -2800`: Long range shooting
- `SpeedUp4 = 3000` / `SpeedDown4 = -3000`: Maximum range shooting

### PID Control Parameters
- `kP = 0.4`: Proportional gain for velocity control
- `kD = 0.00008`: Derivative gain for stability
- `kI = 0.001`: Integral gain for steady-state accuracy
- `kV = 0.0`: Velocity feedforward (currently disabled)
- `ControlOutputMax = 1.0`: Maximum control output
- `ControlOutputMin = 0.1`: Minimum control output to overcome friction
- `PositionDelta = 0.01`: Velocity tolerance (relative)
- `RPMDelta = 0.1`: Velocity tolerance (absolute)

### Timing Constants
- `ShootWaitDelay = 200`: Delay before shooting starts (ms)
- `ShootStartDelay = 1500`: Time to reach shooting speed (ms)
- `ShootFinishDelay = 1000`: Time to maintain shooting speed (ms)
- `IntakeBackTime = 300`: Time for intake backing off (ms)

## Special Features

### Dual PID Velocity Control
Each wheel has its own PID controller for independent speed control:
- **Upper PID Controller**: Maintains upper wheel at target RPM
- **Lower PID Controller**: Maintains lower wheel at target RPM
- **Feed-forward**: Optional velocity feed-forward (kV) for improved response
- **Output Limiting**: Control outputs clamped to safe operational ranges

### SmartDashboard Integration
Real-time monitoring and tuning capabilities:

#### `putParams()`
Publishes current parameters to SmartDashboard:
```java
shooterSubsystem.putParams();
```

#### `getParams()`
Retrieves updated parameters from SmartDashboard:
```java
shooterSubsystem.getParams();
```

Published parameters include:
- Target RPM values (Up/Down)
- PID gains (kP, kD, kI, kV)
- Control limits and tolerances
- Current motor speeds and thrust values

### Performance Monitoring
The subsystem provides extensive telemetry:
- Real-time wheel speeds (RPM)
- Motor current consumption
- PID control outputs
- Thrust/power levels
- Debug timing information

### Safety Features
- **Speed Validation**: Minimum speed limits prevent invalid configurations
- **Current Monitoring**: Real-time current tracking for motor health
- **Output Limiting**: PID outputs constrained to safe ranges
- **Automatic Speed Ramping**: Gradual acceleration to target speeds

## Usage Examples

### Basic Shooting Sequence
```java
public class ShootCommand extends CommandBase {
    private final ShooterSubsystem shooterSubsystem;
    private final double upSpeed;
    private final double downSpeed;
    private Timer timer;
    
    public ShootCommand(ShooterSubsystem shooterSubsystem, 
                       double upSpeed, double downSpeed) {
        this.shooterSubsystem = shooterSubsystem;
        this.upSpeed = upSpeed;
        this.downSpeed = downSpeed;
        this.timer = new Timer();
        addRequirements(shooterSubsystem);
    }
    
    @Override
    public void initialize() {
        shooterSubsystem.init(upSpeed, downSpeed);
        timer.restart();
    }
    
    @Override
    public void execute() {
        shooterSubsystem.run(0); // Target not used, set by init()
    }
    
    @Override
    public boolean isFinished() {
        return shooterSubsystem.atSetPoint() && 
               timer.hasElapsed(1.0); // Ensure stable for 1 second
    }
    
    @Override
    public void end(boolean interrupted) {
        shooterSubsystem.stop();
    }
}
```

### Preset Shooting Modes
```java
// Close range shot
public class CloseRangeShootCommand extends ShootCommand {
    public CloseRangeShootCommand(ShooterSubsystem shooterSubsystem) {
        super(shooterSubsystem, 
              ShooterConstants.SpeedUp1, 
              ShooterConstants.SpeedDown1);
    }
}

// Long range shot
public class LongRangeShootCommand extends ShootCommand {
    public LongRangeShootCommand(ShooterSubsystem shooterSubsystem) {
        super(shooterSubsystem, 
              ShooterConstants.SpeedUp3, 
              ShooterConstants.SpeedDown3);
    }
}
```

### Variable Speed Shooting
```java
public class VariableShootCommand extends CommandBase {
    private final ShooterSubsystem shooterSubsystem;
    private final Supplier<Double> upSpeedSupplier;
    private final Supplier<Double> downSpeedSupplier;
    
    public VariableShootCommand(ShooterSubsystem shooterSubsystem,
                               Supplier<Double> upSpeed,
                               Supplier<Double> downSpeed) {
        this.shooterSubsystem = shooterSubsystem;
        this.upSpeedSupplier = upSpeed;
        this.downSpeedSupplier = downSpeed;
        addRequirements(shooterSubsystem);
    }
    
    @Override
    public void initialize() {
        double upRPM = upSpeedSupplier.get();
        double downRPM = downSpeedSupplier.get();
        shooterSubsystem.init(upRPM, downRPM);
    }
    
    @Override
    public void execute() {
        shooterSubsystem.run(0);
    }
    
    @Override
    public void end(boolean interrupted) {
        shooterSubsystem.stop();
    }
}
```

### Shooter Warmup Command
```java
public class ShooterWarmupCommand extends CommandBase {
    private final ShooterSubsystem shooterSubsystem;
    
    public ShooterWarmupCommand(ShooterSubsystem shooterSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        addRequirements(shooterSubsystem);
    }
    
    @Override
    public void initialize() {
        // Start at medium speed for warmup
        shooterSubsystem.init(
            ShooterConstants.SpeedUp2, 
            ShooterConstants.SpeedDown2
        );
    }
    
    @Override
    public void execute() {
        shooterSubsystem.run(0);
    }
    
    @Override
    public boolean isFinished() {
        return shooterSubsystem.atSetPoint();
    }
    
    // Don't stop on end - keep spinning for quick shots
}
```

## Technical Notes

- **Velocity Control**: Uses RPM-based PID control rather than position control
- **Dual Motor Configuration**: Lower wheel uses two motors for increased power
- **Time-based PID**: Uses actual delta time for accurate control calculations
- **Output Saturation**: PID outputs are limited to prevent motor damage
- **Speed Validation**: Automatically corrects invalid speed configurations
- **Telemetry**: Extensive debug output for performance analysis and tuning
- **Coast Mode**: Motors coast when stopped to allow quick restarts
- **Current Monitoring**: Real-time current tracking helps identify mechanical issues
- **Parameter Persistence**: Motor configurations persist through power cycles
- **CAN Optimization**: Efficient CAN bus usage with appropriate timeouts