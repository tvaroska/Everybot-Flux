# Autonomous Documentation

This document covers the autonomous capabilities of the Everybot 2025 robot, including available autonomous routines, configuration, and development guidelines.

## Available Autonomous Modes

### 1. Drive Forward Auto (`DriveForwardAuto.java`)

**Purpose**: Simple mobility autonomous that moves the robot forward to leave the starting zone.

**Duration**: 0.6 seconds
**Speed**: 30% reverse (moves forward due to motor configuration)
**Subsystems Used**: DriveSubsystem only

**Strategy**:
- Provides guaranteed mobility points
- Simple and reliable
- Good fallback option for competition

**Code Example**:
```java
public final DriveForwardAuto m_driveForwardAuto = new DriveForwardAuto(m_drive);
```

### 2. Simple Coral Auto (`SimpleCoralAuto.java`)

**Purpose**: Comprehensive autonomous routine for coral manipulation and scoring.

**Note**: Currently commented out in `RobotContainer.java` but available for activation.

**Sequence Overview**:
1. **Phase 1**: Initial coral collection from loading station
2. **Phase 2**: Navigate to L1 processor  
3. **Phase 3**: Score coral using arm and roller systems
4. **Phase 4**: Repeat or move to strategic position

**Subsystems Used**: 
- DriveSubsystem (navigation)
- RollerSubsystem (coral manipulation)
- ArmSubsystem (scoring position)

## Autonomous Selection

### SmartDashboard Integration

The autonomous selection is managed through a `SendableChooser` that appears on the SmartDashboard:

```java
SendableChooser<Command> m_chooser = new SendableChooser<>();
```

### Default Configuration

Currently configured in `RobotContainer.java`:

```java
// Default autonomous mode
m_chooser.setDefaultOption("Auto Drive", m_driveForwardAuto);

// Additional options (when enabled)
// m_chooser.setDefaultOption("Coral Auto", m_simpleCoralAuto);
```

### Selection Process

1. **During Practice**: Test different modes on SmartDashboard
2. **Before Match**: Select appropriate mode based on:
   - Alliance strategy
   - Robot condition
   - Field conditions
   - Competition phase (qualification vs elimination)

## Drive Forward Auto - Detailed Analysis

### Implementation

```java
public class DriveForwardAuto extends SequentialCommandGroup {
  public DriveForwardAuto(DriveSubsystem driveSubsystem) {
    addCommands(
      new RunCommand(
        () -> driveSubsystem.drive(DriveConstants.AUTO_MODE_SPEED, 0), 
        driveSubsystem)
        .withTimeout(DriveConstants.AUTO_MODE_TIME)
    );
  }
}
```

### Key Parameters

- **Speed**: `AUTO_MODE_SPEED = -0.3` (30% reverse)
- **Duration**: `AUTO_MODE_TIME = 0.6` seconds
- **Distance**: Approximately 3-4 feet (varies by battery voltage)

### Advantages

- **Reliability**: Minimal complexity, very reliable
- **Speed**: Quick execution leaves time for other actions
- **Safety**: Low speed reduces collision risk
- **Simplicity**: Easy to understand and debug

### Limitations

- **Scoring**: Provides no game piece scoring
- **Strategy**: Doesn't contribute to alliance scoring strategy
- **Positioning**: May not end in optimal field position

## Simple Coral Auto - Detailed Analysis

### Purpose and Strategy

This autonomous mode demonstrates advanced robot capabilities:
- Autonomous game piece manipulation
- Coordinated subsystem operation
- Strategic field positioning

### Expected Subsystem Coordination

Based on the constructor signature, this autonomous would:

1. **Navigation** (`DriveSubsystem`):
   - Move to coral collection position
   - Navigate to L1 processor
   - Position for optimal scoring

2. **Coral Collection** (`RollerSubsystem`):
   - Activate rollers for coral pickup
   - Maintain coral control during navigation
   - Release coral for scoring

3. **Scoring Preparation** (`ArmSubsystem`):
   - Raise arm to scoring position
   - Maintain position during scoring
   - Return to travel position

### Implementation Pattern

```java
public final SimpleCoralAuto m_simpleCoralAuto = 
    new SimpleCoralAuto(m_drive, m_roller, m_arm);
```

## Autonomous Development Guidelines

### Creating New Autonomous Modes

#### 1. Structure Planning
```java
public class NewAutonomous extends SequentialCommandGroup {
    public NewAutonomous(Subsystem1 sub1, Subsystem2 sub2) {
        addCommands(
            // Step 1: Initial setup
            new Command1(sub1).withTimeout(1.0),
            
            // Step 2: Main action
            new ParallelCommandGroup(
                new Command2(sub1),
                new Command3(sub2)
            ),
            
            // Step 3: Completion
            new Command4(sub1, sub2)
        );
    }
}
```

#### 2. Timing Considerations
- **Total Time Limit**: 15 seconds maximum
- **Execution Buffer**: Plan for 12-13 seconds to account for delays
- **Critical Actions First**: Score high-value actions early
- **Contingency Time**: Reserve time for unexpected delays

#### 3. Reliability Features
- **Timeout Commands**: Use `.withTimeout()` to prevent stuck states
- **Parallel Execution**: Run independent actions simultaneously
- **Error Recovery**: Plan for sensor failures or missed targets
- **Graceful Degradation**: Continue with reduced functionality if needed

### Testing and Validation

#### Simulation Testing
```bash
./gradlew simulateJava
```

#### Field Testing Protocol
1. **Static Testing**: Verify commands execute in sequence
2. **Movement Testing**: Test navigation without game pieces
3. **Manipulation Testing**: Test game piece handling while stationary
4. **Integration Testing**: Full autonomous execution
5. **Stress Testing**: Multiple consecutive runs
6. **Competition Simulation**: Test under match-like conditions

### Common Autonomous Patterns

#### Sequential Actions
```java
addCommands(
    new Command1(subsystem1),
    new Command2(subsystem2),
    new Command3(subsystem1)
);
```

#### Parallel Actions
```java
addCommands(
    new ParallelCommandGroup(
        new DriveCommand(driveSubsystem),
        new ArmUpCommand(armSubsystem)
    )
);
```

#### Conditional Actions
```java
addCommands(
    new ConditionalCommand(
        new Command1(subsystem1),  // If condition true
        new Command2(subsystem2),  // If condition false
        () -> sensor.getValue() > threshold
    )
);
```

#### Timed Sequences
```java
addCommands(
    new WaitCommand(1.0),                           // Wait 1 second
    new Command1(subsystem1).withTimeout(2.0),      // Max 2 seconds
    new Command2(subsystem2).until(() -> condition) // Until condition met
);
```

## Configuration for Competition

### Pre-Match Setup

1. **Mode Selection**: Choose appropriate autonomous via SmartDashboard
2. **Position Verification**: Ensure robot starts in correct position
3. **Sensor Check**: Verify all sensors are functioning
4. **Battery Check**: Ensure adequate battery voltage
5. **Alliance Coordination**: Confirm strategy with alliance partners

### Strategy Considerations

#### Qualification Matches
- **Consistency Over Points**: Choose reliable autonomous
- **Practice New Modes**: Test advanced autonomous safely
- **Data Collection**: Gather performance metrics

#### Elimination Matches  
- **Maximum Points**: Use highest-scoring reliable autonomous
- **Alliance Strategy**: Coordinate with alliance partners
- **Risk Assessment**: Balance risk vs reward for each mode

### Performance Monitoring

#### Key Metrics
- **Success Rate**: Percentage of successful executions
- **Points Scored**: Average autonomous points per match
- **Execution Time**: How long each phase takes
- **Failure Modes**: Common points of failure

#### SmartDashboard Monitoring
- Display autonomous status during execution
- Log key decision points and sensor values
- Monitor subsystem health during autonomous

## Troubleshooting Autonomous Issues

### Common Problems

#### Robot Doesn't Move
- **Check**: Drive command configuration
- **Verify**: Motor controller enable status
- **Test**: Manual drive in teleop mode

#### Commands Don't Execute
- **Check**: Command bindings and requirements
- **Verify**: Subsystem initialization
- **Test**: Individual commands in test mode

#### Inconsistent Performance
- **Check**: Battery voltage effects on timing
- **Verify**: Sensor reliability and calibration
- **Test**: Multiple consecutive runs

#### Robot Gets Stuck
- **Solution**: Add appropriate timeouts to all commands
- **Prevention**: Test edge cases and failure modes
- **Recovery**: Implement backup commands

### Diagnostic Tools

#### Driver Station
- Monitor autonomous enable/disable status
- Check for error messages and alerts
- Verify communication with robot

#### SmartDashboard  
- Display real-time autonomous progress
- Show sensor values and subsystem status
- Log decision points for post-match analysis

#### Code Debugging
- Use print statements to track execution
- Add SmartDashboard outputs for key variables
- Test individual commands in isolation

## Future Development

### Potential Enhancements

1. **Vision-Guided Autonomous**: Use camera for precise positioning
2. **Multi-Game Piece**: Handle multiple coral or algae pieces
3. **Adaptive Strategy**: Adjust based on alliance partner actions
4. **Advanced Scoring**: Implement L2/L3 processor scoring

### Development Priorities

1. **Reliability First**: Ensure basic modes work consistently
2. **Incremental Improvement**: Add complexity gradually  
3. **Team Capability**: Match complexity to team programming skills
4. **Testing Infrastructure**: Develop robust testing procedures

### Integration with Teleop

- **Seamless Transition**: Ensure smooth handoff to teleop control
- **Position Awareness**: End autonomous in favorable position
- **Subsystem State**: Leave subsystems in known, safe states
- **Driver Preparation**: Position robot for easy driver takeover