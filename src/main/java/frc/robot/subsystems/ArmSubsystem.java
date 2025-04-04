package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.RollerConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.Constants;
import frc.robot.PIDCtrl;

public class ArmSubsystem extends SubsystemBase {

    private final SparkMax armMotor;
    private final SparkMax armMotorR;
    private RelativeEncoder m_encoder;
    private final PIDCtrl pidCtrl;

    private double speedUp = ArmConstants.SpeedUp;
    private double speedDown = ArmConstants.SpeedDown;
    private double speedHold = ArmConstants.SpeedHold;

    private double angleUp = ArmConstants.AngleUp;
    private double angleDown = ArmConstants.AngleDown;
    private double angleMid = ArmConstants.AngleMid;

    // PID coefficients
    // 0.005/0.01/0.0001
    public double kP = ArmConstants.kP;
    public double kD = ArmConstants.kD; 
    public double kI = ArmConstants.kI;
    public double controlOutputMax = ArmConstants.ControlOutputMax;
    public double controlOutputMin = ArmConstants.ControlOutputMin;
    public double posDelta = ArmConstants.PositionDelta;
    public double rpmDelta = ArmConstants.RPMDelta;

    // public double kMaxOutput= 1;
    // public double kMinOutpu = -1;
    // public double kIz = 0;
    // public double kFF= 0;

    private double angleSet = 0;

    private double timeDelta;
    private double controlValue;
    private int execCounter = 0;
    private double time;
    private boolean done;

    /**
     * This subsytem that controls the arm.
     */
    public ArmSubsystem () {
        // Set up the arm motor as a brushed motor
        armMotor = new SparkMax(ArmConstants.ARM_MOTOR_ID, MotorType.kBrushless);
        armMotorR = new SparkMax(ArmConstants.ARM_MOTOR_IDR, MotorType.kBrushless);
        m_encoder = armMotor.getEncoder();
        pidCtrl = new PIDCtrl(kP, kI, kD, Constants.TimePeriod);

        // Set can timeout. Because this project only sets parameters once on
        // construction, the timeout can be long without blocking robot operation. Code
        // which sets or gets parameters during operation may need a shorter timeout.
        armMotor.setCANTimeout(250);

        // Create and apply configuration for arm motor. Voltage compensation helps
        // the arm behave the same as the battery
        // voltage dips. The current limit helps prevent breaker trips or burning out
        // the motor in the event the arm stalls.
        SparkMaxConfig config = new SparkMaxConfig();
        config.voltageCompensation(ArmConstants.ARM_MOTOR_VOLTAGE_COMP);
        config.smartCurrentLimit(ArmConstants.ARM_MOTOR_CURRENT_LIMIT);
        config.idleMode(IdleMode.kCoast);
        armMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        config.follow(ArmConstants.ARM_MOTOR_ID, true);
        armMotorR.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public void periodic() {
    }
    /** 
     * This is a method that makes the arm move at your desired speed
     *  Positive values make it spin forward and negative values spin it in reverse
     * 
     * @param speed motor speed from -1.0 to 1, with 0 stopping it
     */
    public void run(double speed){
        armMotor.set(speed);
//        armMotorR.set(-speed);
        double vel = m_encoder.getVelocity();
        double pos = m_encoder.getPosition();
        System.out.printf("ARM-RUN, -, %.2f, %.3f, -, %.4f%n", pos, vel, armMotor.getAppliedOutput());

        //TODO !!! Different
        // Arm: 0.3635346591472626 - -0.3569692075252533
        // Arm: 0.26277199387550354 - -0.37734365463256836
//        System.out.println("Arm: " + armMotor.getAppliedOutput() + " - " + armMotorR.getAppliedOutput());
    }

    public boolean atSetPoint() {
        return false;
    }

    public void init() {
        controlValue = 0;
        execCounter = 0;
        time = 0;
        done = false;
        m_encoder.setPosition(0);
        pidCtrl.reset();
    }

    public void stop() {
        armMotor.set(0);
    }

    public boolean isAtPosition(double target, double pos, double velosity) {
        return Math.abs(1 - pos / target) < posDelta && Math.abs(velosity) < rpmDelta;
    }

    public void setAngle(boolean up) {
        if (up)
            angleSet = angleUp;
        else
            angleSet = angleDown;
    }

    public void runToPosition(double position) {
        double t = Timer.getFPGATimestamp();
        double dt = time > 0 ? t - time : 0;
        time = t;

        // double rpm = SmartDashboard.getNumber("RPM", 500);
        // if (targetRPM != rpm)
        //     targetRPM = rpm;
        // if (targetRPM < 10)
        //     targetRPM = 500;

        position = angleSet;

        double positionAbs = Math.abs(position);
        double direction = Math.signum(position);

        double vel = m_encoder.getVelocity();
        double pos = m_encoder.getPosition();

        if (dt == 0 || Math.abs(controlValue) < controlOutputMin) {
            controlValue = direction * controlOutputMin;
            armMotor.set(controlValue);
            System.out.println("Arm Starting with Output: " + controlValue + ", " + armMotor.getAppliedOutput());
            return;
        }
        
        if (isAtPosition(position, pos, vel)) {
            armMotor.set(0);
            done = true;
            System.out.println("Arm At Set Point: " + pos + ", " + vel);
            return;
        }
    
        //TODO Sign
        controlValue = pidCtrl.calculateDif2(pos, position, dt);
        // if (position < 0)
        //     controlValue = -controlValue;

//        dif += kV * targetRPM;
//        controlValue = dif / targetRPM;
        double ctrlval = controlValue;

        controlValue = PIDCtrl.limitRange(controlValue, controlOutputMax);

        armMotor.set(controlValue);

        SmartDashboard.putNumber("Arm Position", pos);

//    System.out.println("PID, " + time + ", " + pos + ", " + vel + ", " + dif + ", " + ctrlval + ", " + controlValue + ", " + upShooterMotor.getAppliedOutput() + ", " + dt1);
        System.out.printf("ARM, %.2f, -, %.2f, %.3f, -, %.4f, %.4f, %.4f%n", 1000 * dt, pos, vel, ctrlval, controlValue, armMotor.getAppliedOutput());
    }

    public void putParams() {
        SmartDashboard.putNumber("Intake Speed Up", speedUp);
        SmartDashboard.putNumber("Intake Speed Down", speedDown);
        SmartDashboard.putNumber("Intake Speed Hold", speedHold);

        SmartDashboard.putNumber("Angle Up", angleUp);
        SmartDashboard.putNumber("Angle Down", angleDown);
        SmartDashboard.putNumber("Angle Mid", angleMid);

        SmartDashboard.putNumber("kP_In", kP);
        SmartDashboard.putNumber("kD_In", kD);
        SmartDashboard.putNumber("kI_In", kI);

        SmartDashboard.putNumber("MaxOutput_In", controlOutputMax);
        SmartDashboard.putNumber("MinOutput_In", controlOutputMin);
        SmartDashboard.putNumber("PosDelta_In", posDelta);
        SmartDashboard.putNumber("RpmDelta_In", rpmDelta);
    }

    public void getParams() {
        speedUp = SmartDashboard.getNumber("Intake Speed Up", Constants.ArmConstants.SpeedUp);
        speedDown = SmartDashboard.getNumber("Intake Speed Down", Constants.ArmConstants.SpeedDown);
        speedHold = SmartDashboard.getNumber("Intake Speed Hold", Constants.ArmConstants.SpeedHold);

        angleUp = SmartDashboard.getNumber("Angle Up", Constants.ArmConstants.AngleUp);
        angleDown = SmartDashboard.getNumber("Angle Down", Constants.ArmConstants.AngleDown);
        angleMid = SmartDashboard.getNumber("Angle Mid", Constants.ArmConstants.AngleMid);

        kP = SmartDashboard.getNumber("kP_In", Constants.ArmConstants.kP);
        kD = SmartDashboard.getNumber("kD_In", Constants.ArmConstants.kD);
        kI = SmartDashboard.getNumber("kI_In", Constants.ArmConstants.kI);
        controlOutputMax = SmartDashboard.getNumber("MaxOutput_Sh", ArmConstants.ControlOutputMax);
        controlOutputMin = SmartDashboard.getNumber("MinOutput_Sh", ArmConstants.ControlOutputMin);
        posDelta = SmartDashboard.getNumber("PosDelta_In", ArmConstants.PositionDelta);
        rpmDelta = SmartDashboard.getNumber("RpmDelta_In", ArmConstants.RPMDelta);

        if (kP < 0)
            kP = 0.01;

        pidCtrl.pid(kP, kD, kI);
    }
}