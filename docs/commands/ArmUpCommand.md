# ArmUpCommand

## Purpose and Functionality
The `ArmUpCommand` powers the arm up to a raised position, typically into the hardstop. It implements a timed sequence with power phases and braking to ensure smooth, controlled upward movement against gravity while preventing mechanism damage.

## Subsystems Required/Used
- **Primary**: `ArmSubsystem` - Controls arm motor and position sensing

## Parameters and Constructor Details
```java
public ArmUpCommand(ArmSubsystem arm)
```

**Parameters:**
- `arm`: The ArmSubsystem instance for arm control

**Dependencies:**
- Requires exclusive access to the ArmSubsystem via `addRequirements(arm)`

## Key Methods

### initialize()
- **Purpose**: Prepares arm for upward movement
- **Implementation**:
  - Initializes arm subsystem
  - Sets arm angle tracking to up position (`setAngle(true)`)
  - Resets execution counter to 0

### execute()
- **Purpose**: Manages the timed arm movement sequence
- **Implementation**: Supports two modes:
  - **Pulse Mode** (`Constants.ArmUsePulse = true`): Timed sequence with phases
  - **Position Mode** (`Constants.ArmUsePulse = false`): Direct position control

#### Pulse Mode Sequence:
1. **Power Phase** (0 to `ARM_TIME_UP`): Full upward power (360ms)
2. **Wait Phase** (`ARM_TIME_UP` to `ARM_TIME_UP_BRAKE1`): Pause for momentum (200ms)
3. **Brake Phase** (`ARM_TIME_UP_BRAKE1` to `ARM_TIME_UP_BRAKE2`): Reverse power for controlled stop (200ms)

### end(boolean interrupted)
- **Purpose**: Safely stops arm movement
- **Implementation**: Sets arm motor speed to 0
- **Commented**: Contains disabled holding power functionality

### isFinished()
- **Purpose**: Determines when arm movement is complete
- **Implementation**: 
  - **Pulse Mode**: Returns `true` when time exceeds `ARM_TIME_UP_BRAKE2` (760ms)
  - **Position Mode**: Returns `true` when time exceeds `ArmTimeUp` (2000ms)

## Usage Examples and When to Use

### Typical Usage
```java
// Create command instance
ArmUpCommand armUp = new ArmUpCommand(robotContainer.getArmSubsystem());

// Bind to controller button
operatorController.dpadUp().onTrue(armUp);

// Use in autonomous sequence
Commands.sequence(
    armUp,
    new AlgieInCommand(roller, shooter)
).schedule();
```

### When to Use
- **Intake Position**: Raise arm for algae intake operations
- **Travel Position**: Safe arm position during robot movement
- **Autonomous Sequences**: Coordinated arm positioning for game piece handling
- **Manual Operator Control**: Direct arm control during teleop
- **Competition Setup**: Positioning arm for match start

### Recommended Usage
- Use to move arm **into the hardstop** mechanical limit
- Let the passive holding mechanism maintain position
- Ideal for preparing robot for algae collection

## Special Features and State Management

### Dual Mode Operation

#### Pulse Mode (Default)
```java
if (Constants.ArmUsePulse) {
    if (time < ArmConstants.ARM_TIME_UP) {
        m_arm.run(ArmConstants.ARM_SPEED_UP);        // Full power up (0.22)
    } else if (time < ArmConstants.ARM_TIME_UP_BRAKE1) {
        // Wait phase - no power, momentum carries arm
    } else if (time < ArmConstants.ARM_TIME_UP_BRAKE2) {
        m_arm.run(ArmConstants.ARM_SPEED_UP_BRAKE);  // Brake power (-0.15)
    }
}
```

#### Position Mode (Alternative)
```java
else {
    m_arm.runToPosition(ArmConstants.AngleUp);  // PID to 18° position
}
```

### Timing Configuration
```java
// Timing constants from ArmConstants
ARM_TIME_UP: 360ms          // Primary power phase
ARM_TIME_UP_BRAKE1: 560ms   // Wait phase end (560-360 = 200ms wait)
ARM_TIME_UP_BRAKE2: 760ms   // Total sequence (760-560 = 200ms brake)
ArmTimeUp: 2000ms          // Position mode timeout
```

### Power Settings
```java
// Power constants from ArmConstants
ARM_SPEED_UP: 0.22          // Upward driving power
ARM_SPEED_UP_BRAKE: -0.15   // Downward braking power
ARM_HOLD_UP: 0.05          // Passive holding power (commented)
```

### State Transitions
1. **Idle** → **Powering Up**: Command starts, applies upward force against gravity
2. **Powering Up** → **Coasting**: Power phase complete, momentum carries arm upward
3. **Coasting** → **Braking**: Apply downward power for controlled deceleration
4. **Braking** → **Complete**: Sequence finished, arm reaches hardstop

### Safety Features
- **Controlled Deceleration**: Brake phase prevents hard impacts with hardstop
- **Power Limiting**: Current limits (40A) prevent motor damage
- **Position Feedback**: Encoder tracking for precise control
- **Timeout Protection**: Automatic completion prevents infinite running
- **Gravity Compensation**: Appropriate power levels for upward movement

### Performance Characteristics
- **Speed**: Moderate speed for controlled movement against gravity
- **Accuracy**: Precise positioning with mechanical hardstop
- **Safety**: Multi-phase sequence prevents mechanism damage
- **Reliability**: Robust timing system with proven parameters
- **Power Management**: Efficient current-limited operation

### Mechanical Integration
- **Hardstop**: Physical upper limit provides precise positioning
- **Gear Reduction**: 60:1 overall ratio (20:1 gearbox × 3:1 chain) for high torque
- **Encoder Feedback**: Position sensing for closed-loop control option
- **Gravity Loading**: System designed to work against gravitational force

### Position Control Details
When using position mode (`Constants.ArmUsePulse = false`):
- **Target Position**: `AngleUp` = 18° × 60:1 ratio = 1080 encoder counts
- **Control Method**: PID control to precise angle
- **Timeout**: 2000ms maximum execution time
- **Accuracy**: ±0.01 relative position tolerance

### Comparison with ArmDownCommand
| Aspect | ArmUpCommand | ArmDownCommand |
|--------|--------------|----------------|
| Power Level | 0.22 (moderate) | -0.4 (higher) |
| Brake Power | -0.15 | 0.2 |
| Total Time | 760ms | 700ms |
| Gravity | Works against | Works with |
| Mechanical Stop | Hardstop | Paracord |

### Operational Notes
- **Gravity Consideration**: Requires more sophisticated control than down movement
- **Hardstop Contact**: Designed to gently reach mechanical limit
- **Power Efficiency**: Optimized power levels for reliable operation
- **Sequence Timing**: Proven timing values from competition testing