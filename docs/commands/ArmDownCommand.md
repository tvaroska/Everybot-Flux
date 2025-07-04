# ArmDownCommand

## Purpose and Functionality
The `ArmDownCommand` powers the arm down to a lower position, typically into the paracord stop. It implements a sophisticated timed sequence with power ramping and braking to ensure smooth, controlled movement while preventing damage to the mechanism.

## Subsystems Required/Used
- **Primary**: `ArmSubsystem` - Controls arm motor and position sensing
- **Integration**: SmartDashboard for real-time parameter tuning

## Parameters and Constructor Details
```java
public ArmDownCommand(ArmSubsystem arm)
```

**Parameters:**
- `arm`: The ArmSubsystem instance for arm control

**Dependencies:**
- Requires exclusive access to the ArmSubsystem via `addRequirements(arm)`

## Key Methods

### initialize()
- **Purpose**: Prepares arm for downward movement
- **Implementation**:
  - Calls `getParams()` to retrieve SmartDashboard tuning values
  - Initializes arm subsystem
  - Sets arm angle tracking to down position
  - Resets execution counter

### execute()
- **Purpose**: Manages the timed arm movement sequence
- **Implementation**: Supports two modes:
  - **Pulse Mode** (`Constants.ArmUsePulse = true`): Timed sequence with phases
  - **Position Mode** (`Constants.ArmUsePulse = false`): Direct position control

#### Pulse Mode Sequence:
1. **Power Phase** (0 to `timeDown`): Full downward power
2. **Wait Phase** (`timeDown` to `timeBreak1`): Pause for momentum settling
3. **Brake Phase** (`timeBreak1` to `timeBreak2`): Reverse power for controlled stop

### end(boolean interrupted)
- **Purpose**: Safely stops arm movement
- **Implementation**: Sets arm motor speed to 0
- **Commented**: Contains disabled holding power functionality

### isFinished()
- **Purpose**: Determines when arm movement is complete
- **Implementation**: Returns `true` when execution time reaches `timeBreak2`

## Usage Examples and When to Use

### Typical Usage
```java
// Create command instance
ArmDownCommand armDown = new ArmDownCommand(robotContainer.getArmSubsystem());

// Bind to controller button
operatorController.dpadDown().onTrue(armDown);

// Use in autonomous sequence
Commands.sequence(
    armDown,
    new CoralOutCommand(roller)
).schedule();
```

### When to Use
- **Coral Scoring Preparation**: Lower arm for coral ejection into L1
- **Defense Position**: Lower arm to protect intake during defensive play
- **Autonomous Sequences**: Coordinated arm positioning for scoring
- **Manual Operator Control**: Direct arm control during teleop

### Recommended Usage
- Use to move arm **into the paracord** mechanical stop
- Let the passive holding mechanism maintain position
- Avoid continuous re-running unless necessary

## Special Features and State Management

### Dual Mode Operation

#### Pulse Mode (Recommended)
```java
if (Constants.ArmUsePulse) {
    // Timed sequence with three phases
    if (time < timeDown) {
        m_arm.run(speed);           // Full power down
    } else if (time < timeBreak1) {
        // Pause phase - no power
    } else if (time < timeBreak2) {
        m_arm.run(speedBrake);      // Brake power
    }
}
```

#### Position Mode (Alternative)
```java
else {
    m_arm.runToPosition(ArmConstants.AngleDown);  // PID to target position
}
```

### SmartDashboard Integration
Real-time parameter tuning for optimal performance:

```java
// Tunable parameters
"Arm Down Speed"      : speed       // Main downward power
"Arm Down Brake"      : speedBrake  // Braking power
"Arm Down Time 1"     : timeDown    // Power phase duration
"Arm Down Brake Time" : timeBreak1  // Wait phase end
"Arm Down Time"       : timeBreak2  // Total sequence time
```

### Safety Features
- **Controlled Deceleration**: Brake phase prevents hard impacts
- **Power Limiting**: Current limits prevent motor damage
- **Position Feedback**: Angle tracking for precise control
- **Timeout Protection**: Automatic completion prevents infinite running

### Default Timing Configuration
```java
// Default values from ArmConstants
ARM_SPEED_DOWN: -0.4          // Primary downward speed
ARM_SPEED_DOWN_BRAKE: 0.2     // Braking speed (reverse)
ARM_TIME_DOWN: 340ms          // Power phase duration
ARM_TIME_DOWN_BRAKE1: 450ms   // Wait phase end
ARM_TIME_DOWN_BRAKE2: 700ms   // Total sequence time
```

### State Transitions
1. **Idle** → **Powering Down**: Command starts, applies downward force
2. **Powering Down** → **Coasting**: Power phase complete, momentum carries arm
3. **Coasting** → **Braking**: Apply reverse power for controlled stop
4. **Braking** → **Complete**: Sequence finished, arm in position

### Performance Characteristics
- **Speed**: Moderate speed for controlled movement
- **Accuracy**: Precise positioning with mechanical stops
- **Safety**: Multi-phase sequence prevents damage
- **Reliability**: Robust timing system with tunable parameters
- **Power Management**: Current-limited operation (40A limit)

### Mechanical Integration
- **Paracord Stop**: Provides physical limit for down position
- **Gear Reduction**: 60:1 ratio for high torque, precise control
- **Chain Drive**: 16:48 chain ratio for additional reduction
- **Position Sensing**: Encoder feedback for accurate positioning

### Tuning Guidelines
- **Increase `timeDown`**: If arm doesn't reach mechanical stop
- **Adjust `speedBrake`**: Fine-tune stopping control
- **Modify `timeBreak2`**: Ensure complete sequence timing
- **Test incrementally**: Small changes for safety