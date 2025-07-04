# Configuration Guide

This guide covers all configurable parameters in the Everybot 2025 robot code, located primarily in the `Constants.java` file.

## Motor Controller Configuration

### Drive System (DriveConstants)

```java
// Motor Controller CAN IDs
public static final int LEFT_LEADER_ID = 18;      // Left side leader motor
public static final int LEFT_FOLLOWER_ID = 19;    // Left side follower motor  
public static final int RIGHT_LEADER_ID = 10;     // Right side leader motor
public static final int RIGHT_FOLLOWER_ID = 11;   // Right side follower motor

// Motor Protection
public static final int DRIVE_MOTOR_CURRENT_LIMIT = 60;    // Amps
public static final double DRIVE_MOTOR_VOLTAGE_COMP = 12;   // Volts

// Speed Control
public static final double SPEED_LIMIT = 0.8;              // Normal max speed (80%)
public static final double SLOW_MODE_MOVE = 0.4;           // Slow mode linear (40%)
public static final double SLOW_MODE_TURN = 0.4;           // Slow mode rotation (40%)

// Autonomous Settings
public static final double AUTO_MODE_SPEED = -0.3;         // Auto drive speed
public static final double AUTO_MODE_TIME = 0.6;           // Auto drive duration (seconds)
```

### Roller System (RollerConstants)

```java
// Motor Controller CAN IDs
public static final int ROLLER_MOTOR_ID = 14;      // Primary roller motor
public static final int ROLLER_MOTOR_IDR = 15;     // Secondary roller motor

// Motor Protection
public static final int ROLLER_MOTOR_CURRENT_LIMIT = 40;   // Amps
public static final double ROLLER_MOTOR_VOLTAGE_COMP = 10; // Volts

// Algae Handling Speeds
public static final double ROLLER_ALGAE_IN = 0.8;          // Intake speed
public static final double ROLLER_ALGAE_OUT = -0.8;        // Output speed

// Coral Handling Speeds  
public static final double ROLLER_CORAL_OUT = -0.4;        // L1 scoring speed
public static final double ROLLER_CORAL_STACK = -1;        // Stacking speed

// Alternative Speed Settings
public static final double SpeedIn = 0.8;                  // Generic intake
public static final double SpeedOut = -0.8;                // Generic output
public static final double SpeedShoot = 0.4;               // Shooting assist
public static final double SpeedBackoff = 0.2;             // Gentle reverse
```

### Shooter System (ShooterConstants)

```java
// Upper Wheel Motor CAN IDs
public static final int UP_MOTOR_ID = 8;           // Upper wheel leader
public static final int UP_MOTOR_IDR = 9;          // Upper wheel follower

// Lower Wheel Motor CAN IDs  
public static final int DOWN_MOTOR_ID = 6;         // Lower wheel leader
public static final int DOWN_MOTOR_IDR = 7;        // Lower wheel follower

// Motor Protection
public static final int SHOOT_MOTOR_CURRENT_LIMIT = 40;    // Amps
public static final double SHOOT_MOTOR_VOLTAGE_COMP = 10;  // Volts

// Algae Handling (Manual)
public static final double ALGAE_IN = -0.4;               // Intake assist
public static final double ALGAE_OUT = 0.2;               // Gentle eject

// Shooting Assistance
public static final double SHOOT_ALGAE_IN = 0.4;          // Load assist
public static final double SHOOT_ALGAE_OUT = -0.8;        // Shoot assist

// Shooting Sequence Timing (milliseconds)
public static final int ShootWaitDelay = 200;             // Prep delay
public static final int ShootStartDelay = 1500;           // Spin-up time  
public static final int ShootFinishDelay = 1000;          // Finish time
public static final int IntakeBackTime = 300;             // Backoff time

// Target RPM Settings
public static final double SpeedUp = 2500;                // Default upper RPM
public static final double SpeedDown = -2500;             // Default lower RPM

// Preset RPM Settings (4 levels)
public static final double SpeedUp1 = 2110;               // Close range upper
public static final double SpeedUp2 = 2200;               // Medium-close upper
public static final double SpeedUp3 = 2800;               // Medium-far upper  
public static final double SpeedUp4 = 3000;               // Long range upper

public static final double SpeedDown1 = -1950;            // Close range lower
public static final double SpeedDown2 = -2200;            // Medium-close lower
public static final double SpeedDown3 = -2800;            // Medium-far lower
public static final double SpeedDown4 = -3000;            // Long range lower

// PID Controller Settings
public static final double kP = 0.4;                      // Proportional gain
public static final double kD = 0.00008;                  // Derivative gain  
public static final double kI = 0.001;                    // Integral gain
public static final double kV = 0.0;                      // Velocity feedforward

// Control Limits
public static final double ControlOutputMax = 1.0;        // Max motor output
public static final double ControlOutputMin = 0.1;        // Min motor output
public static final double PositionDelta = 0.01;          // Position tolerance
public static final double RPMDelta = 0.1;                // RPM tolerance
```

### Arm System (ArmConstants)

```java
// Motor Controller CAN IDs
public static final int ARM_MOTOR_ID = 4;          // Primary arm motor
public static final int ARM_MOTOR_IDR = 5;         // Secondary arm motor

// Motor Protection
public static final int ARM_MOTOR_CURRENT_LIMIT = 40;     // Amps
public static final double ARM_MOTOR_VOLTAGE_COMP = 10;   // Volts

// Timed Control Speeds (Legacy)
public static final double ARM_SPEED_UP = 0.22;           // Raise speed
public static final double ARM_SPEED_UP_BRAKE = -0.15;    // Raise brake
public static final double ARM_SPEED_DOWN = -0.4;         // Lower speed  
public static final double ARM_SPEED_DOWN_BRAKE = 0.2;    // Lower brake

// Timed Control Timing (milliseconds)
public static final double ARM_TIME_UP = 360;             // Raise duration
public static final double ARM_TIME_UP_BRAKE1 = 560;      // First brake point
public static final double ARM_TIME_UP_BRAKE2 = 760;      // Final brake point
public static final double ARM_TIME_DOWN = 340;           // Lower duration
public static final double ARM_TIME_DOWN_BRAKE1 = 450;    // First brake point  
public static final double ARM_TIME_DOWN_BRAKE2 = 700;    // Final brake point

// Hold Speeds
public static final double ARM_HOLD_DOWN = -0.05;         // Hold down position
public static final double ARM_HOLD_UP = 0.05;            // Hold up position

// Position Control (Preferred)
public static final double SpeedUp = -0.4;                // Controlled raise
public static final double SpeedDown = 0.4;               // Controlled lower
public static final double SpeedHold = 0.2;               // Hold power
public static final double SpeedBackoff = 0.2;            // Gentle adjust

// Mechanical Configuration
public static final int GearRatio = 60;                   // Overall gear reduction

// Target Positions (motor rotations)
public static final double AngleUp = 18 * GearRatio / 360.0;     // Up position
public static final double AngleDown = -18 * GearRatio / 360.0;   // Down position  
public static final double AngleMid = 9 * GearRatio / 360.0;      // Mid position

// PID Controller Settings
public static final double kP = 0.1;                      // Proportional gain
public static final double kD = 0.0;                      // Derivative gain
public static final double kI = 0.0;                      // Integral gain

// Control Limits
public static final double ControlOutputMax = 0.4;        // Max motor output
public static final double ControlOutputMin = 0.05;       // Min motor output
public static final double PositionDelta = 0.01;          // Position tolerance
public static final double RPMDelta = 0.1;                // RPM tolerance
```

### Climber System (ClimberConstants)

```java
// Motor Controller CAN ID
public static final int CLIMBER_MOTOR_ID = 17;            // Climber motor

// Motor Protection  
public static final int CLIMBER_MOTOR_CURRENT_LIMIT = 40; // Amps
public static final double CLIMBER_MOTOR_VOLTAGE_COMP = 12; // Volts

// Control Speeds
public static final double CLIMBER_SPEED_DOWN = -0.5;     // Lower speed
public static final double CLIMBER_SPEED_UP = 0.5;        // Raise speed
```

## Controller Configuration

### Controller Ports (OperatorConstants)

```java
public static final int DRIVER_CONTROLLER_PORT = 0;       // Driver Xbox controller
public static final int OPERATOR_CONTROLLER_PORT = 0;     // Operator Xbox controller
```

### Sensitivity Control Settings

```java
public static final double LinCoef = 0.2;                 // Linear response coefficient
public static final double Threshold = 0.02;              // Dead zone threshold
public static final double CuspX = 0.8;                   // Quadratic transition point
public static final double SpeedLimitX = 1.0;             // Max linear speed
public static final double SpeedLimitRot = 0.7;           // Max rotation speed
```

## Global Settings

### Timing Configuration

```java
public static int TimePeriodMsec = 20;                     // Loop period (ms)
public static double TimePeriod = 0.001 * TimePeriodMsec;  // Loop period (seconds)
```

### Feature Toggles

```java
public static boolean ArmUsePulse = true;                  // Use pulse vs continuous arm control
```

## SmartDashboard Parameters

The following parameters can be tuned in real-time via SmartDashboard:

### Drive Sensitivity
- **Linear Sensitivity**: `LinCoef` - Controls linear response curve
- **Zero Zone**: `Threshold` - Dead zone size
- **Quadric Zone**: `CuspX` - Transition point between linear and quadratic
- **Speed**: `SpeedLimitX` - Maximum linear speed
- **Turn Speed**: `SpeedLimitRot` - Maximum rotation speed

### Subsystem Parameters
Each subsystem exposes additional tunable parameters through SmartDashboard. See individual subsystem documentation for details.

## Configuration Best Practices

### Motor IDs
- Use sequential numbering for related motors
- Document all CAN IDs in a central location
- Verify IDs match actual hardware configuration

### Current Limits
- Set appropriate limits based on motor specifications
- Consider mechanical load and duty cycle
- Monitor current draw during operation

### Speed Limits
- Start with conservative values
- Tune based on performance requirements
- Consider driver skill level and competition conditions

### PID Tuning
1. Start with P-only control
2. Add D gain to reduce oscillation
3. Add I gain only if steady-state error exists
4. Test thoroughly in match-like conditions

### Safety Considerations
- Always set reasonable current limits
- Use voltage compensation for consistent behavior
- Implement proper hold powers for gravity-affected mechanisms
- Test all emergency stop scenarios

## Tuning Workflow

1. **Initial Setup**: Configure basic motor IDs and current limits
2. **Basic Operation**: Verify all subsystems move in correct directions
3. **Speed Tuning**: Adjust speeds for optimal performance
4. **PID Tuning**: Tune closed-loop controllers if used
5. **Integration Testing**: Test all subsystems working together
6. **Competition Tuning**: Fine-tune based on match performance

## Common Configuration Issues

### Drive System
- **Symptom**: Robot drives in circles
- **Solution**: Check motor directions and follower configuration

### Arm System  
- **Symptom**: Arm drifts when stopped
- **Solution**: Increase hold power or check gear backlash

### Shooter System
- **Symptom**: Inconsistent shot distance
- **Solution**: Tune PID gains and verify wheel speeds

### General
- **Symptom**: Motors overheating
- **Solution**: Reduce current limits or duty cycle