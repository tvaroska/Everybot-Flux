# Usage Examples and Tutorials

This document provides practical examples and tutorials for working with the Everybot 2025 robot code.

## Getting Started Examples

### 1. Basic Robot Operation

#### Enabling the Robot
```java
// In Robot.java - This happens automatically
@Override
public void robotInit() {
    // Robot container initializes all subsystems
    m_robotContainer = new RobotContainer();
    
    // Usage tracking (keep this line)
    HAL.report(tResourceType.kResourceType_Framework, 11);
}
```

#### Basic Driving
```java
// This is already set up in RobotContainer.java
m_drive.setDefaultCommand(new DriveCommand(m_drive,
    () -> sensitivityPos.transfer(m_driverController.getLeftY()),
    () -> sensitivityRot.transfer(m_driverController.getRightX()),
    () -> false));
```

### 2. Adding a New Command

#### Step 1: Create the Command Class
```java
// File: src/main/java/frc/robot/commands/NewExampleCommand.java
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ExampleSubsystem;

public class NewExampleCommand extends CommandBase {
    private final ExampleSubsystem m_subsystem;
    private final double m_speed;
    
    public NewExampleCommand(ExampleSubsystem subsystem, double speed) {
        m_subsystem = subsystem;
        m_speed = speed;
        addRequirements(m_subsystem);
    }
    
    @Override
    public void initialize() {
        System.out.println("NewExampleCommand started");
    }
    
    @Override
    public void execute() {
        m_subsystem.setSpeed(m_speed);
    }
    
    @Override
    public void end(boolean interrupted) {
        m_subsystem.stop();
        System.out.println("NewExampleCommand ended");
    }
    
    @Override
    public boolean isFinished() {
        return false; // Runs until interrupted
    }
}
```

#### Step 2: Add to RobotContainer
```java
// In RobotContainer.java

// Add to imports
import frc.robot.commands.NewExampleCommand;

// Add as instance variable
private final NewExampleCommand m_newExampleCommand;

// Initialize in constructor
public RobotContainer() {
    m_newExampleCommand = new NewExampleCommand(m_exampleSubsystem, 0.5);
    configureBindings();
}

// Add to configureBindings()
private void configureBindings() {
    // Existing bindings...
    
    // Bind to a button (example: operator controller start button)
    m_operatorController.start().whileTrue(m_newExampleCommand);
}
```

### 3. Creating a New Subsystem

#### Step 1: Create the Subsystem Class
```java
// File: src/main/java/frc/robot/subsystems/ExampleSubsystem.java
package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ExampleConstants;

public class ExampleSubsystem extends SubsystemBase {
    private final CANSparkMax m_motor;
    
    public ExampleSubsystem() {
        m_motor = new CANSparkMax(ExampleConstants.MOTOR_ID, MotorType.kBrushless);
        m_motor.setSmartCurrentLimit(ExampleConstants.CURRENT_LIMIT);
        m_motor.enableVoltageCompensation(ExampleConstants.VOLTAGE_COMP);
        m_motor.setIdleMode(CANSparkMax.IdleMode.kBrake);
    }
    
    public void setSpeed(double speed) {
        m_motor.set(speed);
    }
    
    public void stop() {
        m_motor.set(0);
    }
    
    public double getCurrent() {
        return m_motor.getOutputCurrent();
    }
    
    @Override
    public void periodic() {
        // This runs every 20ms
        // Add telemetry or periodic updates here
    }
}
```

#### Step 2: Add Constants
```java
// In Constants.java, add new inner class
public static final class ExampleConstants {
    public static final int MOTOR_ID = 20;
    public static final int CURRENT_LIMIT = 40;
    public static final double VOLTAGE_COMP = 12.0;
    public static final double DEFAULT_SPEED = 0.5;
}
```

#### Step 3: Add to RobotContainer
```java
// In RobotContainer.java

// Add to imports
import frc.robot.subsystems.ExampleSubsystem;

// Add as instance variable
public final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
```

## Practical Tutorials

### Tutorial 1: Tuning Drive Sensitivity

The robot uses a sophisticated sensitivity system for smooth control:

#### Understanding the Sensitivity Curve
```java
// In RobotContainer.java - these create the sensitivity objects
private final Sensitivity sensitivityPos = 
    new Sensitivity(OperatorConstants.Threshold, OperatorConstants.CuspX, 
                   OperatorConstants.LinCoef, OperatorConstants.SpeedLimitX);

private final Sensitivity sensitivityRot = 
    new Sensitivity(OperatorConstants.Threshold, OperatorConstants.CuspX, 
                   OperatorConstants.LinCoef, OperatorConstants.SpeedLimitRot);
```

#### Real-Time Tuning Process
1. **Start Practice Mode**: Enable robot in practice mode
2. **Open SmartDashboard**: Launch SmartDashboard application
3. **Find Sensitivity Parameters**: Look for these values:
   - Linear Sensitivity
   - Zero Zone  
   - Quadric Zone
   - Speed
   - Turn Speed

4. **Adjust and Test**:
   ```java
   // Typical starting values
   Linear Sensitivity: 0.2   // Controls low-speed response
   Zero Zone: 0.02          // Dead zone size
   Quadric Zone: 0.8        // Where quadratic response starts
   Speed: 1.0               // Maximum forward/backward speed
   Turn Speed: 0.7          // Maximum turn speed
   ```

5. **Apply Changes**: Press Back button on driver controller to reload parameters

6. **Test Different Scenarios**:
   - Precise movements (use low values)
   - Quick movements (use high values)
   - Turn in place (adjust turn speed)

### Tutorial 2: Setting Up Autonomous

#### Creating a Simple Autonomous
```java
// File: src/main/java/frc/robot/autos/CustomAuto.java
package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.commands.ArmUpCommand;

public class CustomAuto extends SequentialCommandGroup {
    public CustomAuto(DriveSubsystem drive, ArmSubsystem arm) {
        addCommands(
            // Step 1: Wait for 1 second
            new WaitCommand(1.0),
            
            // Step 2: Raise arm while driving
            new ArmUpCommand(arm),
            new RunCommand(() -> drive.drive(-0.3, 0), drive)
                .withTimeout(2.0),
            
            // Step 3: Stop and wait
            new RunCommand(() -> drive.drive(0, 0), drive)
                .withTimeout(0.5)
        );
    }
}
```

#### Adding to Robot Selection
```java
// In RobotContainer.java

// Add import
import frc.robot.autos.CustomAuto;

// Add instance variable
public final CustomAuto m_customAuto;

// Initialize in constructor
public RobotContainer() {
    // After other initializations
    m_customAuto = new CustomAuto(m_drive, m_arm);
    
    configureBindings();
    
    // Add to chooser
    m_chooser.addOption("Custom Auto", m_customAuto);
    putParams();
}
```

### Tutorial 3: Advanced Command Composition

#### Parallel Commands
```java
// Running multiple subsystems simultaneously
public class ComplexCommand extends CommandBase {
    private final DriveSubsystem m_drive;
    private final ArmSubsystem m_arm;
    
    public ComplexCommand(DriveSubsystem drive, ArmSubsystem arm) {
        m_drive = drive;
        m_arm = arm;
        addRequirements(drive, arm);
    }
    
    @Override
    public void execute() {
        // Drive and move arm simultaneously
        m_drive.drive(-0.3, 0);        // Drive forward
        m_arm.setPosition(ArmConstants.AngleUp);  // Raise arm
    }
}
```

#### Sequential Commands with Conditions
```java
public class ConditionalAuto extends SequentialCommandGroup {
    public ConditionalAuto(DriveSubsystem drive, ArmSubsystem arm) {
        addCommands(
            // Drive forward
            new RunCommand(() -> drive.drive(-0.3, 0), drive)
                .withTimeout(1.0),
            
            // Conditional arm movement
            new ConditionalCommand(
                new ArmUpCommand(arm),     // If true
                new ArmDownCommand(arm),   // If false
                () -> getSensorValue() > 0.5  // Condition
            ),
            
            // Continue based on result
            new RunCommand(() -> drive.drive(0, 0), drive)
                .withTimeout(0.5)
        );
    }
    
    private double getSensorValue() {
        // Your sensor reading logic here
        return 1.0; // Example
    }
}
```

### Tutorial 4: SmartDashboard Integration

#### Adding Custom Telemetry
```java
// In any subsystem's periodic() method
@Override
public void periodic() {
    SmartDashboard.putNumber("Motor Speed", m_motor.get());
    SmartDashboard.putNumber("Motor Current", m_motor.getOutputCurrent());
    SmartDashboard.putBoolean("At Target", atTarget());
    SmartDashboard.putString("Subsystem State", getCurrentState());
}
```

#### Creating Tunable Parameters
```java
// In subsystem constructor
public ExampleSubsystem() {
    // Hardware initialization...
    
    // Put default values
    SmartDashboard.putNumber("Example Speed", ExampleConstants.DEFAULT_SPEED);
    SmartDashboard.putNumber("Example P Gain", ExampleConstants.kP);
}

// In periodic() method
@Override
public void periodic() {
    // Get updated values
    double speed = SmartDashboard.getNumber("Example Speed", 
                                          ExampleConstants.DEFAULT_SPEED);
    double pGain = SmartDashboard.getNumber("Example P Gain", 
                                           ExampleConstants.kP);
    
    // Apply values
    updatePIDGains(pGain, 0, 0);
}
```

#### Button Monitoring
```java
// In RobotContainer.java
public void putParams() {
    // Monitor button states
    SmartDashboard.putBoolean("A Button", m_operatorController.a().getAsBoolean());
    SmartDashboard.putBoolean("B Button", m_operatorController.b().getAsBoolean());
    
    // Monitor joystick values
    SmartDashboard.putNumber("Left Y", m_driverController.getLeftY());
    SmartDashboard.putNumber("Right X", m_driverController.getRightX());
}
```

## Common Patterns and Best Practices

### 1. Safe Motor Control
```java
// Always set current limits
m_motor.setSmartCurrentLimit(40);

// Use voltage compensation for consistency
m_motor.enableVoltageCompensation(12.0);

// Set appropriate idle mode
m_motor.setIdleMode(CANSparkMax.IdleMode.kBrake); // For positioning
m_motor.setIdleMode(CANSparkMax.IdleMode.kCoast); // For free spinning
```

### 2. Robust Command Design
```java
public class RobustCommand extends CommandBase {
    private Timer m_timer = new Timer();
    private boolean m_hasTimedOut = false;
    
    @Override
    public void initialize() {
        m_timer.restart();
        m_hasTimedOut = false;
    }
    
    @Override
    public void execute() {
        // Check for timeout
        if (m_timer.hasElapsed(5.0)) {
            m_hasTimedOut = true;
            return;
        }
        
        // Normal execution
        doWork();
    }
    
    @Override
    public boolean isFinished() {
        return m_hasTimedOut || workComplete();
    }
    
    @Override
    public void end(boolean interrupted) {
        // Always clean up
        cleanup();
        
        if (interrupted) {
            System.out.println("Command was interrupted");
        } else if (m_hasTimedOut) {
            System.out.println("Command timed out");
        } else {
            System.out.println("Command completed normally");
        }
    }
}
```

### 3. Error Handling
```java
public void safeMotorControl(double speed) {
    try {
        // Check bounds
        if (speed > 1.0) speed = 1.0;
        if (speed < -1.0) speed = -1.0;
        
        // Check for hardware faults
        if (m_motor.getFault(CANSparkMax.FaultID.kOvercurrent)) {
            System.err.println("Motor overcurrent detected!");
            speed = 0;
        }
        
        // Apply speed
        m_motor.set(speed);
        
    } catch (Exception e) {
        System.err.println("Motor control error: " + e.getMessage());
        m_motor.set(0); // Safe stop
    }
}
```

### 4. Testing and Debugging
```java
// Add debug modes to commands
public class DebugCommand extends CommandBase {
    private static final boolean DEBUG = true;
    
    @Override
    public void initialize() {
        if (DEBUG) System.out.println("DebugCommand initialized");
    }
    
    @Override
    public void execute() {
        if (DEBUG) {
            SmartDashboard.putNumber("Debug Value", getCurrentValue());
            SmartDashboard.putString("Debug State", getCurrentState());
        }
        
        // Normal execution
    }
}
```

## Advanced Examples

### Custom PID Controller Usage
```java
// Using the custom PIDCtrl class
public class AdvancedSubsystem extends SubsystemBase {
    private PIDCtrl m_pidController;
    
    public AdvancedSubsystem() {
        m_pidController = new PIDCtrl(
            "Advanced", 
            0.1,    // kP
            0.0,    // kI  
            0.001,  // kD
            1.0,    // max output
            0.1     // min output
        );
    }
    
    public void setTarget(double target) {
        m_pidController.setTarget(target);
    }
    
    @Override
    public void periodic() {
        double current = getCurrentPosition();
        double output = m_pidController.update(current);
        m_motor.set(output);
    }
}
```

### Complex Autonomous with Error Recovery
```java
public class RobustAuto extends SequentialCommandGroup {
    public RobustAuto(DriveSubsystem drive, ArmSubsystem arm, RollerSubsystem roller) {
        addCommands(
            // Phase 1: Setup with timeout
            new ParallelRaceGroup(
                new ArmUpCommand(arm),
                new WaitCommand(3.0) // Timeout after 3 seconds
            ),
            
            // Phase 2: Drive with error handling
            new DriveWithRecovery(drive, -0.3, 2.0),
            
            // Phase 3: Conditional actions
            new ConditionalCommand(
                new SuccessSequence(roller, arm),
                new FailureRecovery(drive),
                () -> arm.atTarget()
            )
        );
    }
}
```

This comprehensive guide provides practical examples for extending and customizing the Everybot 2025 robot code. Use these patterns as starting points for your own implementations.