package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkMaxAlternateEncoder;
import com.revrobotics.spark.SparkRelativeEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLimitSwitch;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.LimitSwitchConfig.Type;
import com.revrobotics.spark.SparkClosedLoopController;
//import com.revrobotics.spark.SparkPIDController;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.RollerConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.Constants;
import frc.robot.PIDCtrl;

public class ShooterSubsystem extends SubsystemBase {

    private final SparkMax upShooterMotor;
    private SparkMax upShooterMotorR;
    private SparkMax downShooterMotor;
    private SparkMax downShooterMotorR;

    private final RelativeEncoder upEncoder;
    private final RelativeEncoder downEncoder;

    private final PIDCtrl upPidCtrl;
    private final PIDCtrl downPidCtrl;
    private double upSetRPM = ShooterConstants.SpeedUp;
    private double downSetRPM = ShooterConstants.SpeedDown;

    private double timeDelta = Constants.TimePeriod;

    RelativeEncoder m_alternateEncoder;
    SparkClosedLoopController closedLoopCtrl;
    //private SparkPIDController m_pidController;
    private PIDController m_pidController;

    // PID coefficients
    public double kP = ShooterConstants.kP;
    public double kD = ShooterConstants.kD;
    public double kI = ShooterConstants.kI;
    public double kV = ShooterConstants.kV;
    public double controlOutputMax = ShooterConstants.ControlOutputMax;
    public double controlOutputMin = ShooterConstants.ControlOutputMin;
    public double posDelta = ShooterConstants.PositionDelta;
    public double rpmDelta = ShooterConstants.RPMDelta;

    // public double kIz = 0;
    // public double kFF= 0;
    // public double kMaxOutput= 1;
    // public double kMinOutput = -1;

    private int execCounter = 0;
    private double time = 0;
    private double controlValueUp = 0;
    private double controlValueDown = 0;
    private boolean atSpeed = false;

//    private static final SparkMaxAlternateEncoder.Type kAltEncType = SparkMaxAlternateEncoder.Type.kQuadrature;
    // private SparkMaxConfig motorConfig;
    // private SparkLimitSwitch forwardLimitSwitch;
    // private SparkLimitSwitch reverseLimitSwitch;
    

    /**
     * This subsytem that controls the roller.
     */
    public ShooterSubsystem () {
        upShooterMotor = new SparkMax(ShooterConstants.UP_MOTOR_ID, MotorType.kBrushless);
        downShooterMotor = new SparkMax(ShooterConstants.DOWN_MOTOR_ID, MotorType.kBrushless);
        downShooterMotorR = new SparkMax(ShooterConstants.DOWN_MOTOR_IDR, MotorType.kBrushless);

        upEncoder = upShooterMotor.getEncoder();
        downEncoder = downShooterMotor.getEncoder();

    // upShooterMotorR = new SparkMax(ShooterConstants.UP_MOTOR_IDR, MotorType.kBrushless);

    // Set can timeout. Because this project only sets parameters once on
    // construction, the timeout can be long without blocking robot operation. Code
    // which sets or gets parameters during operation may need a shorter timeout.

    upShooterMotor.setCANTimeout(250);
    downShooterMotor.setCANTimeout(250);
    downShooterMotorR.setCANTimeout(250);

    // Create and apply configuration for roller motor. Voltage compensation helps
    // the roller behave the same as the battery
    // voltage dips. The current limit helps prevent breaker trips or burning out
    // the motor in the event the roller stalls.
    SparkMaxConfig config = new SparkMaxConfig();
    config.voltageCompensation(RollerConstants.ROLLER_MOTOR_VOLTAGE_COMP);
    config.smartCurrentLimit(RollerConstants.ROLLER_MOTOR_CURRENT_LIMIT);
    config.idleMode(IdleMode.kCoast);
    SparkMaxConfig config1 = new SparkMaxConfig();
    config1.voltageCompensation(RollerConstants.ROLLER_MOTOR_VOLTAGE_COMP);
    config1.smartCurrentLimit(RollerConstants.ROLLER_MOTOR_CURRENT_LIMIT);
    config1.idleMode(IdleMode.kCoast);

    upShooterMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    downShooterMotor.configure(config1, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
//    config1.follow(ShooterConstants.DOWN_MOTOR_ID, true);
    downShooterMotorR.configure(config1, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    m_pidController = new PIDController(kP, kI, kD);
    //m_pidController.setIZone(kIz);
    m_pidController.setSetpoint(1);

    // set PID coefficients
    // m_pidController.setIZone(kIz);
    // m_pidController.setFF(kFF);
    // m_pidController.setOutputRange(kMinOutput, kMaxOutput);

        upPidCtrl = new PIDCtrl(kP, kD, kI, timeDelta);
        downPidCtrl = new PIDCtrl(kP, kD, kI, timeDelta);
    }

    @Override
    public void periodic() {
    }

    public boolean atSetPoint() {
        return atSpeed;
    }

    public void init(double upRPM, double downRPM) {
        System.out.println("Shooter Initializing");
        getParams();
        upSetRPM = upRPM;
        downSetRPM = downRPM;
        execCounter = 0;
        
        time = 0;
        controlValueUp = 0;
        controlValueDown = 0;
        atSpeed = false;

        upEncoder.setPosition(0);
        downEncoder.setPosition(0);

        upPidCtrl.reset();
        downPidCtrl.reset();
        // m_pidController.reset();
        // m_pidController.setSetpoint(10);
    }

    public void stop() {
        upShooterMotor.set(0);
        //upShooterMotor.stopMotor();
        downShooterMotor.set(0);
        downShooterMotorR.set(0);
    }

    public void runDown(double target) {
        // double velUp = upEncoder.getVelocity();
        // double velDown = downEncoder.getVelocity();
//        upShooterMotor.set(target);
        downShooterMotor.set(-target);
        // SmartDashboard.putNumber("Shooter RPM Up", velUp);
        // SmartDashboard.putNumber("Shooter RPM Down", velDown);
    }

    public void runRaw(double target) {
        double velUp = upEncoder.getVelocity();
        double velDown = downEncoder.getVelocity();
        upShooterMotor.set(target);
        downShooterMotor.set(-target);
        SmartDashboard.putNumber("Shooter RPM Up", velUp);
        SmartDashboard.putNumber("Shooter RPM Down", velDown);
    }
   
    //TODO Param?
    public void run(double target) {
        ++execCounter;
        int dtime = (int) (execCounter * 1000 * Constants.TimePeriod);

        double t = Timer.getFPGATimestamp();
        double dt = time > 0 ? t - time : 0;
        time = t;

//        downSetRPM = upSetRPM;
        double velUp = upEncoder.getVelocity();
        double velDown = downEncoder.getVelocity();

        double pos = upEncoder.getPosition();
        double pos1 = downEncoder.getPosition();
        //double pos2 = downEncoderR.getPosition();
        // double rpm = SmartDashboard.getNumber("RPM", 500);
        // if (targetRPM != rpm)
        //     targetRPM = rpm;
        // if (targetRPM < 50)
        //     targetRPM = 500;
//        System.out.printf("VEL, %.2f, -, %.2f%n", velUp, velDown);

        if (controlValueUp < controlOutputMin) {
            controlValueUp = controlOutputMin;
            controlValueDown = -controlOutputMin;
            System.out.printf("VEL, %.2f, -, %.2f%n", velUp, velDown);

            upShooterMotor.set(controlValueUp);
            downShooterMotor.set(controlValueDown);
            downShooterMotorR.set(-controlValueDown);
            System.out.printf("Applied Output: %.3f, %.3f, %.4f %n", controlValueUp, controlValueDown, upShooterMotor.getAppliedOutput());
            return;
        }

        double difUp = kV * upSetRPM + upPidCtrl.calculateDif(velUp, upSetRPM, dt);
        System.out.printf("calculateDif: %.3f, %.3f, %.2f, -, %.2f%n", velDown, downSetRPM, velUp, velDown);
        double difDown = kV * (-downSetRPM) + downPidCtrl.calculateDif(-velDown, -downSetRPM, dt);

        atSpeed = Math.abs(difUp / upSetRPM - 1) < posDelta && Math.abs(difDown / downSetRPM - 1) < posDelta;

        controlValueUp += difUp / upSetRPM;
        controlValueDown += difDown / downSetRPM;

        double ctrlvalUp = controlValueUp;
        double ctrlvalDown = controlValueDown;
    
        controlValueUp = PIDCtrl.limitSignedRange(controlValueUp, controlOutputMin, controlOutputMax);
        controlValueDown = PIDCtrl.limitSignedRange(controlValueDown, -controlOutputMax, -controlOutputMin);

//        upShooterMotor.setVoltage(ctrlSpeed);
        upShooterMotor.set(controlValueUp);
//        downShooterMotor.set(-controlValueUp);
        downShooterMotor.set(controlValueDown);
        downShooterMotorR.set(-controlValueDown);

        SmartDashboard.putNumber("Shooter RPM Up", velUp);
        SmartDashboard.putNumber("Shooter RPM Down", velDown);
        SmartDashboard.putNumber("Thrust", controlValueUp);

        System.out.printf("SHT, %d, %.2f, -, %.2f, %.3f, -, %.4f, %.4f, %.4f, %.4f%n", dtime, 1000 * dt, pos, velUp, difUp, ctrlvalUp, controlValueUp, upShooterMotor.getAppliedOutput());
        System.out.printf("SHT, %d, %.2f, -, %.2f, %.3f, -, %.4f, %.4f, %.4f, %.4f, %.4f%n", dtime, 1000 * dt, pos, velDown, difDown, ctrlvalDown, controlValueDown, downShooterMotor.getAppliedOutput(), downShooterMotorR.getAppliedOutput());
    }

/*
    public void runShooter1(double speed) {
        double setPoint = 50;

        double ctrlSpeed = m_pidController.calculate(m_encoder.getPosition(), setPoint);
//        m_pidController.setReference(rotations, CANSparkMax.ControlType.kPosition);
        System.out.println("Pos/Vel: " + m_encoder.getPosition() + " - " + m_encoder.getVelocity());
        System.out.println("PID1: " + ctrlSpeed);
//            SmartDashboard.putNumber("Applied Output", upShooterMotor.getAppliedOutput());
            //System.out.println("PID: " + m_pidController.calculate(m_encoder.getVelocity()));
            //System.out.println("Encoder A: " + m_alternateEncoder.getPosition() + "-" + m_alternateEncoder.getVelocity());
        //upShooterMotor.set(m_pidController.calculate(m_encoder.getPosition()));
        double coeff = SmartDashboard.getNumber("Coeff", 1);
        //ctrlSpeed *= coeff;
//        System.out.println("Speed: " + speed);
        double velUp = upEncoder.getVelocity();
        double velDown = downEncoder.getVelocity();

        if (controlValue == 0)
            controlValue = speed;
        double dif = pidCtrl.calculateDif(m_encoder.getPosition(), setPoint);
        controlValue += dif;
        System.out.println("PID Dif/Control: " + dif + " - " + controlValue);

        double limited = PIDCtrl.limitRange(controlValue, 1.0);

//        closedLoopCtrl.setReference(1000, ControlType.kVelocity);
//        closedLoopCtrl.setReference(50, ControlType.kPosition);

//        upShooterMotor.setVoltage(ctrlSpeed);
        upShooterMotor.set(controlValue);
//        upShooterMotor.setVoltage(controller.update(getState().getTargetAngle().getDegrees(), getCurrentAngle().getDegrees()));
        SmartDashboard.putNumber("Shhot RPM Up", vel);
        SmartDashboard.putNumber("Shhot RPM Up", vel);

        System.out.println("Applied Output: " + upShooterMotor.getAppliedOutput());            
    }
*/        
    public void putParams() {
        SmartDashboard.putNumber("Set RPM Up", upSetRPM);
        SmartDashboard.putNumber("Set RPM Down", downSetRPM);

        SmartDashboard.putNumber("kP_Sh", kP);
        SmartDashboard.putNumber("kD_Sh", kD);
        SmartDashboard.putNumber("kI_Sh", kI);
        SmartDashboard.putNumber("kV_Sh", kV);
        SmartDashboard.putNumber("MaxOutput_Sh", controlOutputMax);
        SmartDashboard.putNumber("MinOutput_Sh", controlOutputMin);
        SmartDashboard.putNumber("PosDelta_Sh", posDelta);
        SmartDashboard.putNumber("RpmDelta_Sh", rpmDelta);
    }

    public void getParams() {
        upSetRPM = SmartDashboard.getNumber("Set RPM Up", ShooterConstants.SpeedUp);
        downSetRPM = SmartDashboard.getNumber("Set RPM Down", ShooterConstants.SpeedDown);

        kP = SmartDashboard.getNumber("kP_Sh", ShooterConstants.kP);
        kD = SmartDashboard.getNumber("kD_Sh", ShooterConstants.kD);
        kI = SmartDashboard.getNumber("kI_Sh", ShooterConstants.kI);
        kV = SmartDashboard.getNumber("kV_Sh", ShooterConstants.kV);
        controlOutputMax = SmartDashboard.getNumber("MaxOutput_Sh", ShooterConstants.ControlOutputMax);
        controlOutputMin = SmartDashboard.getNumber("MinOutput_Sh", ShooterConstants.ControlOutputMin);
        posDelta = SmartDashboard.getNumber("PosDelta_Sh", ShooterConstants.PositionDelta);
        rpmDelta = SmartDashboard.getNumber("RpmDelta_Sh", ShooterConstants.RPMDelta);

        //TODOTODO!!!
        if (upSetRPM < 50)
            upSetRPM = 50;
        if (downSetRPM > -50)
            downSetRPM = -50;

        upPidCtrl.pid(kP, kD, kI);
        downPidCtrl.pid(kP, kD, kI);
    }
}