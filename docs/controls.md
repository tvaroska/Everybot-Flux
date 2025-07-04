# Controls and Driver Interface

This document details the controller mappings, driver interface, and control schemes for the Everybot 2025 robot.

## Controller Configuration

### Hardware Setup
- **Driver Controller**: Xbox Controller on port 0
- **Operator Controller**: Xbox Controller on port 0 (can be same as driver for single-operator mode)

### Controller Layout

```
Xbox Controller Layout:
        LB          RB
    LT      (LS)      RT
         ╭─────╮
    ╭────┤     ├────╮
    │    │     │    │
    │    └─────┘    │
    │               │
    │  Y         X  │
    │    ╲     ╱    │
    │      ╲ ╱      │
    │        X       │
    │      ╱ ╲      │
    │    ╱     ╲    │
    │  A         B  │
    │               │
    │    ╭─────╮    │
    │    │ D-PAD│   │
    │    └─────┘    │
    │               │
    │      (RS)     │
    │               │
    └───────────────┘
         Back  Start
```

## Driver Controls

### Primary Driving (Left Stick + Right Stick)
- **Left Stick Y-Axis**: Forward/Backward movement
  - Push up = Move forward
  - Pull down = Move backward
  - Applied through sensitivity curve for smooth control

- **Right Stick X-Axis**: Left/Right turning
  - Push right = Turn right
  - Push left = Turn left  
  - Applied through sensitivity curve for precise steering

### Slow Mode (Right Bumper)
- **Function**: Reduces drive speed for precision movements
- **When Held**: 
  - Linear speed reduced to 40% (`SLOW_MODE_MOVE`)
  - Turn speed reduced to 40% (`SLOW_MODE_TURN`)
- **Use Cases**: 
  - Precise positioning
  - Approaching game pieces
  - End-game climbing alignment

### Parameter Update (Back Button)
- **Function**: Reloads tuning parameters from SmartDashboard
- **Usage**: Hold to refresh all subsystem parameters during practice

## Operator Controls

### Algae Manipulation

#### Intake (Right Bumper)
- **Function**: Runs algae intake sequence
- **While Held**: Both roller and shooter motors spin inward
- **Speed**: 80% intake speed
- **Use**: Collecting algae from field or loading station

#### Outtake (Right Trigger > 20%)
- **Function**: Ejects algae from robot
- **While Held**: Both roller and shooter motors spin outward  
- **Speed**: 80% output speed
- **Use**: Clearing jams or rejecting algae

### Arm Control

#### Arm Up (Left Bumper)
- **Function**: Raises arm to scoring position
- **Type**: Toggle command (press once to activate)
- **Duration**: Automatically stops when target reached
- **Use**: Preparing for L1 coral scoring

#### Arm Down (Left Trigger > 20%)  
- **Function**: Lowers arm to intake position
- **Type**: Toggle command (press once to activate)
- **Duration**: Automatically stops when target reached
- **Use**: Returning to ground-level operations

### Shooting Controls

The four face buttons provide different shooting presets:

#### A Button - Close Range Shooting
- **Upper Flywheel**: 2110 RPM
- **Lower Flywheel**: -1950 RPM
- **Use**: Close-range algae scoring

#### B Button - Medium-Close Range
- **Upper Flywheel**: 2200 RPM  
- **Lower Flywheel**: -2200 RPM
- **Use**: Medium-close algae scoring

#### Y Button - Medium-Far Range
- **Upper Flywheel**: 2800 RPM
- **Lower Flywheel**: -2800 RPM
- **Use**: Medium-far algae scoring

#### X Button - Long Range
- **Upper Flywheel**: 3000 RPM
- **Lower Flywheel**: -3000 RPM  
- **Use**: Long-range algae scoring

### Coral Handling

#### Coral Out (Right Trigger while arm is up)
- **Function**: Scores coral into empty L1 processor
- **Speed**: 40% output speed
- **Coordination**: Works with arm positioning

#### Coral Stack (Alternative configuration)
- **Function**: Stacks coral into occupied L1 processor
- **Speed**: 100% output speed for aggressive placement

### Climber Controls

#### Climber Up (D-Pad Up)
- **Function**: Extends climber to lift robot
- **While Held**: Climber motor runs at 50% up speed
- **Safety**: Monitor current draw and mechanism limits

#### Climber Down (D-Pad Down)  
- **Function**: Retracts climber to lower robot
- **While Held**: Climber motor runs at 50% down speed
- **Safety**: Control descent speed carefully

## Control Modes

### Single Driver Mode
For teams using only one driver:
- Use driver controller for all functions
- Consider remapping arm controls to more accessible buttons
- May want to change slow mode to B button for easier access

### Dual Driver Mode (Default)
- Driver handles locomotion and some basic functions
- Operator handles all manipulation tasks
- Provides better task separation and faster reactions

## Sensitivity and Response Curves

### Joystick Sensitivity System
The robot uses a sophisticated sensitivity system with three zones:

#### Dead Zone (0-2%)
- **Purpose**: Eliminates joystick drift
- **Behavior**: No robot movement
- **Tunable**: `Threshold` parameter

#### Linear Zone (2-80%)  
- **Purpose**: Precise low-speed control
- **Behavior**: Direct proportional response
- **Tunable**: `LinCoef` parameter controls slope

#### Quadratic Zone (80-100%)
- **Purpose**: Full-speed operation with smooth transition
- **Behavior**: Squared response for aggressive movement
- **Tunable**: `CuspX` parameter sets transition point

### Speed Limiting
- **Linear Movement**: Limited to `SpeedLimitX` (default 100%)
- **Rotational Movement**: Limited to `SpeedLimitRot` (default 70%)
- **Purpose**: Prevents over-aggressive driving while maintaining control

## SmartDashboard Tuning

### Real-Time Adjustable Parameters

#### Drive Sensitivity
- **Linear Sensitivity**: Adjusts response in linear zone
- **Zero Zone**: Sets dead zone size
- **Quadric Zone**: Sets transition point to quadratic response
- **Speed**: Maximum linear speed
- **Turn Speed**: Maximum rotation speed

#### Usage
1. Open SmartDashboard during practice
2. Adjust parameters while driving
3. Test different values for comfort
4. Press Back button on driver controller to apply changes
5. Record optimal values for competition

## Control Best Practices

### For Drivers
1. **Practice Smooth Inputs**: Use gradual stick movements
2. **Use Slow Mode**: Leverage for precision tasks
3. **Learn Sensitivity Zones**: Understand how each zone feels
4. **Coordinate with Operator**: Communicate intentions clearly

### For Operators  
1. **Know Your Presets**: Memorize which button for which distance
2. **Watch the Field**: Choose appropriate shooting preset
3. **Sequence Operations**: Coordinate arm and roller actions
4. **Emergency Stop**: Know how to quickly stop all operations

### Competition Tips
1. **Warm Up Controllers**: Check all buttons before matches
2. **Verify Mappings**: Ensure controls work as expected
3. **Have Backup**: Prepare backup controllers
4. **Practice Scenarios**: Rehearse common action sequences

## Control Flow Examples

### Algae Scoring Sequence
1. Position robot using drive controls
2. Hold Right Bumper to intake algae
3. Select appropriate shooting preset (A/B/Y/X)
4. Shooter automatically manages the firing sequence

### Coral Scoring Sequence  
1. Position robot near L1 processor
2. Press Left Bumper to raise arm
3. Drive forward to align with processor
4. Use coral manipulation controls as needed
5. Press Left Trigger to lower arm when complete

### End-Game Climbing
1. Position robot under chain using slow mode
2. Use D-Pad Up to extend climber and grab chain
3. Continue extending to lift robot
4. Use D-Pad Down to control descent if needed

## Troubleshooting Controls

### Common Issues

#### Robot Not Responding to Sticks
- Check controller connection in Driver Station
- Verify controller port assignments
- Test with different controller

#### Erratic Movement
- Check joystick calibration
- Adjust dead zone settings
- Look for interference or damage

#### Commands Not Triggering
- Verify button mappings in code
- Check for conflicting commands
- Test buttons individually

#### Inconsistent Speed
- Review sensitivity curve settings
- Check battery voltage effects
- Verify motor current limits

### Diagnostic Steps
1. **Driver Station**: Check controller status
2. **SmartDashboard**: Monitor joystick values
3. **Test Mode**: Use to verify individual subsystems
4. **Code Review**: Confirm bindings match documentation