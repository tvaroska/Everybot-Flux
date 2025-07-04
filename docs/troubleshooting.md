# Troubleshooting Guide

This guide covers common issues, diagnostic procedures, and solutions for the Everybot 2025 robot code.

## Quick Diagnostic Checklist

### Before Each Practice/Match
- [ ] RoboRIO power LED is solid (not blinking)
- [ ] Driver Station shows robot communication
- [ ] Battery voltage above 12.0V
- [ ] All motor controllers have solid green LEDs
- [ ] Controllers are connected and responsive
- [ ] SmartDashboard shows expected values

### Driver Station Status
- [ ] **Communications**: Should show "Good" or green
- [ ] **Robot Code**: Should show "Good" or green  
- [ ] **Joysticks**: Controllers should appear in list
- [ ] **Battery**: Should show current voltage
- [ ] **CAN**: Should show no error messages

## Common Issues and Solutions

### 1. Robot Won't Enable

#### Symptoms
- Driver Station shows red "Robot Code" or "Communications"
- Enable button is grayed out
- Robot doesn't respond to controller input

#### Troubleshooting Steps
1. **Check Physical Connections**
   ```
   Power Chain: Battery → Main Breaker → PDP → RoboRIO
   Data Chain: Router → RoboRIO → Motor Controllers
   ```

2. **Verify RoboRIO Status**
   - Solid green power LED = Good
   - Blinking = Check power connections
   - Off = Check main breaker and battery

3. **Check Code Deployment**
   ```bash
   ./gradlew deploy
   ```
   - Look for successful deployment message
   - Verify team number matches robot configuration

4. **Driver Station Diagnostics**
   - Restart Driver Station software
   - Check Windows firewall settings
   - Verify robot radio configuration

### 2. Drive System Issues

#### Robot Drives in Circles

**Cause**: Motor direction or follower configuration incorrect

**Solution**:
```java
// In DriveSubsystem.java, check motor configuration
leftLeader.setInverted(false);
leftFollower.setInverted(false); 
rightLeader.setInverted(true);   // Usually inverted
rightFollower.setInverted(true); // Usually inverted

// Verify follower setup
leftFollower.follow(leftLeader);
rightFollower.follow(rightLeader);
```

#### Robot Doesn't Drive Straight

**Cause**: Motors not properly synchronized or mechanical issues

**Solutions**:
1. **Check Motor Configuration**
   ```java
   // Verify current limits are equal
   leftLeader.setSmartCurrentLimit(DriveConstants.DRIVE_MOTOR_CURRENT_LIMIT);
   rightLeader.setSmartCurrentLimit(DriveConstants.DRIVE_MOTOR_CURRENT_LIMIT);
   ```

2. **Verify Follower Setup**
   ```java
   // Ensure followers are properly configured
   leftFollower.follow(leftLeader);
   rightFollower.follow(rightLeader);
   ```

3. **Check Mechanical Issues**
   - Wheel pressure and wear
   - Drive belt tension
   - Bearing condition

#### Slow or Weak Driving

**Causes**: Low battery, current limiting, or mechanical drag

**Solutions**:
1. **Check Battery Voltage**
   - Should be above 12.0V under load
   - Replace if below 11.5V

2. **Review Current Limits**
   ```java
   // Increase if motors are current-limiting
   public static final int DRIVE_MOTOR_CURRENT_LIMIT = 60; // Try 80
   ```

3. **Check Mechanical Systems**
   - Lubricate drive components
   - Check for binding or excessive friction

### 3. Arm System Issues

#### Arm Drifts When Stopped

**Cause**: Insufficient hold power or mechanical backlash

**Solutions**:
1. **Increase Hold Power**
   ```java
   // In ArmSubsystem.java
   public static final double ARM_HOLD_UP = 0.05;   // Try 0.1
   public static final double ARM_HOLD_DOWN = -0.05; // Try -0.1
   ```

2. **Check Gear Backlash**
   - Mechanical adjustment may be needed
   - Consider adding brake or locking mechanism

#### Arm Oscillates or Shakes

**Cause**: PID tuning issues

**Solutions**:
1. **Reduce Proportional Gain**
   ```java
   public static final double kP = 0.1; // Try 0.05
   ```

2. **Add Derivative Gain**
   ```java
   public static final double kD = 0.0004; // Add small D term
   ```

3. **Check Encoder**
   - Verify encoder connections
   - Check for mechanical issues affecting feedback

#### Arm Moves Too Slowly

**Cause**: Low motor output limits or excessive load

**Solutions**:
1. **Increase Output Limits**
   ```java
   public static final double ControlOutputMax = 0.4; // Try 0.6
   ```

2. **Check Mechanical Load**
   - Verify arm balance and counterweights
   - Check for binding or excessive friction

### 4. Shooter System Issues

#### Inconsistent Shot Distance

**Cause**: PID tuning, mechanical issues, or power problems

**Solutions**:
1. **Tune PID Controllers**
   ```java
   // Start with P-only tuning
   public static final double kP = 0.4;  // Adjust until stable
   public static final double kI = 0.0;  // Add only if needed
   public static final double kD = 0.00008; // Add to reduce overshoot
   ```

2. **Check RPM Consistency**
   - Monitor actual vs target RPM on SmartDashboard
   - Look for RPM fluctuations during shooting

3. **Verify Power Supply**
   - Check battery voltage during shooting
   - Ensure adequate current limits

#### Shooter Won't Reach Target RPM

**Cause**: Insufficient power or mechanical issues

**Solutions**:
1. **Increase Current Limits**
   ```java
   public static final int SHOOT_MOTOR_CURRENT_LIMIT = 40; // Try 60
   ```

2. **Check Mechanical Systems**
   - Verify flywheel balance
   - Check bearing condition
   - Ensure proper belt tension

3. **Adjust Control Parameters**
   ```java
   public static final double ControlOutputMax = 1.0; // Ensure at maximum
   ```

### 5. Roller System Issues

#### Game Pieces Get Stuck

**Cause**: Speed mismatch or mechanical interference

**Solutions**:
1. **Adjust Roller Speeds**
   ```java
   // Try different speed combinations
   public static final double ROLLER_ALGAE_IN = 0.8;  // Try 0.6
   public static final double ROLLER_ALGAE_OUT = -0.8; // Try -0.6
   ```

2. **Check Roller Coordination**
   - Verify both rollers spin in correct directions
   - Check timing of roller activation

#### Inconsistent Game Piece Control

**Cause**: Speed tuning or mechanical issues

**Solutions**:
1. **Fine-tune Speeds**
   - Test different speeds for different game pieces
   - Consider separate speeds for different operations

2. **Check Mechanical Condition**
   - Verify roller surface condition
   - Check alignment and spacing

### 6. Controller Issues

#### Controls Not Responding

**Cause**: Controller connection, mapping, or code issues

**Solutions**:
1. **Check Controller Connection**
   - Verify controllers appear in Driver Station
   - Try different USB ports
   - Test with known good controller

2. **Verify Controller Mapping**
   ```java
   // In RobotContainer.java, check port assignments
   private final CommandXboxController m_driverController =
       new CommandXboxController(OperatorConstants.DRIVER_CONTROLLER_PORT);
   ```

3. **Test Individual Buttons**
   - Use SmartDashboard to monitor controller inputs
   - Verify buttons trigger expected commands

#### Erratic or Jittery Movement

**Cause**: Joystick calibration or sensitivity settings

**Solutions**:
1. **Adjust Dead Zone**
   ```java
   public static final double Threshold = 0.02; // Try 0.05
   ```

2. **Check Sensitivity Curve**
   ```java
   // In SmartDashboard, adjust sensitivity parameters
   SmartDashboard.putNumber("Linear Sensitivity", 0.2);
   SmartDashboard.putNumber("Zero Zone", 0.02);
   ```

### 7. CAN Bus Issues

#### Random Motor Controller Disconnections

**Cause**: CAN bus errors, wiring issues, or power problems

**Solutions**:
1. **Check CAN Wiring**
   - Verify CAN High/Low connections
   - Check for damaged or loose connections
   - Ensure proper termination resistors

2. **Review CAN IDs**
   ```java
   // Ensure no duplicate CAN IDs
   public static final int LEFT_LEADER_ID = 18;    // Unique
   public static final int LEFT_FOLLOWER_ID = 19;  // Unique
   ```

3. **Monitor CAN Utilization**
   - Check Driver Station for CAN error messages
   - Reduce unnecessary CAN traffic if needed

### 8. Autonomous Issues

#### Autonomous Doesn't Start

**Cause**: Command selection or initialization issues

**Solutions**:
1. **Check Autonomous Selection**
   ```java
   // Verify autonomous chooser setup
   m_chooser.setDefaultOption("Auto Drive", m_driveForwardAuto);
   ```

2. **Test Command Independently**
   - Try running autonomous commands manually
   - Check for command requirements conflicts

#### Autonomous Stops Prematurely

**Cause**: Command timeouts or completion conditions

**Solutions**:
1. **Check Timeouts**
   ```java
   // Ensure reasonable timeouts
   .withTimeout(DriveConstants.AUTO_MODE_TIME) // Verify time value
   ```

2. **Review End Conditions**
   - Check `isFinished()` methods in commands
   - Verify sensor readings if used

### 9. Performance Issues

#### Robot Slow to Respond

**Cause**: Code performance, power issues, or excessive processing

**Solutions**:
1. **Check Loop Timing**
   - Monitor loop overruns in Driver Station
   - Optimize periodic methods if needed

2. **Review Power Management**
   - Check battery voltage under load
   - Verify all systems within current limits

#### Intermittent Functionality

**Cause**: Loose connections, power issues, or code bugs

**Solutions**:
1. **Physical Inspection**
   - Check all electrical connections
   - Look for damaged wires or components

2. **Code Review**
   - Look for race conditions or timing issues
   - Check for proper error handling

## Diagnostic Tools and Procedures

### SmartDashboard Monitoring

#### Essential Values to Monitor
```java
// Add these to subsystem periodic() methods
SmartDashboard.putNumber("Battery Voltage", getBatteryVoltage());
SmartDashboard.putNumber("Left Drive Current", leftLeader.getOutputCurrent());
SmartDashboard.putNumber("Right Drive Current", rightLeader.getOutputCurrent());
SmartDashboard.putBoolean("Arm At Target", armAtTarget());
SmartDashboard.putNumber("Shooter RPM", getShooterRPM());
```

#### Debugging Commands
```java
// Temporary debugging output
System.out.println("Debug: " + variableName + " = " + value);
```

### Driver Station Diagnostics

#### Log Viewer
- Check for error messages and warnings
- Monitor CAN device status
- Review power and voltage logs

#### Test Mode
- Use to test individual subsystems
- Verify motor directions and speeds
- Check sensor readings

### Hardware Testing

#### Motor Controller LEDs
- **Solid Green**: Normal operation
- **Blinking Green**: No CAN communication
- **Red**: Hardware fault or over-current
- **Orange**: Device in bootloader mode

#### Power Distribution Panel
- Check circuit breaker status
- Monitor current draw on critical circuits
- Verify proper voltage distribution

## Prevention and Maintenance

### Regular Maintenance
- **Weekly**: Check all electrical connections
- **Before Competition**: Full system test and backup
- **After Matches**: Review logs for issues
- **End of Season**: Document known issues and solutions

### Code Maintenance
- **Version Control**: Use git for all code changes
- **Documentation**: Keep documentation updated
- **Testing**: Regular testing of all subsystems
- **Backup**: Maintain working code backups

### Emergency Procedures

#### If Robot Becomes Unresponsive
1. **Immediately**: Hit emergency stop
2. **Check**: Physical safety of robot and people
3. **Disable**: Robot through Driver Station
4. **Diagnose**: Using systematic approach above

#### Competition Emergency Kit
- Spare controllers and cables
- Backup radio and ethernet cables
- Spare motor controllers and sensors
- Known good code backup
- Basic tools and electrical supplies

## Getting Help

### Internal Resources
- Check this documentation first
- Review code comments and documentation
- Test with known good hardware

### External Resources
- **WPILib Documentation**: https://docs.wpilib.org/
- **Chief Delphi Forums**: https://www.chiefdelphi.com/
- **FRC Discord**: https://discord.gg/frc
- **Vendor Documentation**: Check specific motor controller manuals

### Competition Support
- **Field Support**: For field-related issues
- **Technical Inspection**: For compliance questions
- **Volunteer Mentors**: For programming help
- **Team Resources**: Coordinate with other teams for parts/help