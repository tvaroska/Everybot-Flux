// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.Constants.RollerConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.RollerSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

/** A command to remove (score or pass) Algae. */
public class AlgieShootCommand extends Command {
  private final RollerSubsystem m_roller;
  private final ShooterSubsystem m_shooter;

  private int intakeBackTime = ShooterConstants.IntakeBackTime;
  private int shootWaitTime = ShooterConstants.ShootWaitTime;
  private int shootStartTime = ShooterConstants.ShootStartTime;
  private int shootEndTime = ShooterConstants.ShootFinishTime;

  private double speedBackoff = RollerConstants.SpeedBackoff;
  private double shooterOut = ShooterConstants.SHOOT_ALGAE_OUT;
  private double rollerIn = ShooterConstants.ALGAE_IN;

  private int preset = 0;
  private double upSpeeds[] = { ShooterConstants.SpeedUp1, ShooterConstants.SpeedUp2, ShooterConstants.SpeedUp3, ShooterConstants.SpeedUp4 };
  private double downSpeeds[] = { ShooterConstants.SpeedDown1, ShooterConstants.SpeedDown2, ShooterConstants.SpeedDown3, ShooterConstants.SpeedDown4 };

  private int execCounter = 0;

  /**
   * Shoot the Algae int to the net. 
   *
   * @param roller The subsystem used by this command.
   */
  public AlgieShootCommand(ShooterSubsystem shooter, RollerSubsystem roller, int presetNumber) {
    m_roller = roller;
    m_shooter = shooter;
    preset = presetNumber;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(roller);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    //TODO REM
    getParams();

    execCounter = 0;
    m_roller.init();
    m_shooter.init(upSpeeds[preset], downSpeeds[preset]);
    System.out.println("Shooter initialized");
/*
    // PID coefficients
    kP = 0.1; 
    kI = 1e-4;
    kD = 1; 
    kIz = 0; 
    kFF = 0; 
    kMaxOutput = 1; 
    kMinOutput = -1;

    // set PID coefficients
    m_pidController.setP(kP);
    m_pidController.setI(kI);
    m_pidController.setD(kD);
    m_pidController.setIZone(kIz);
    m_pidController.setFF(kFF);
    m_pidController.setOutputRange(kMinOutput, kMaxOutput);
    */
    }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    int time = execCounter++ * Constants.TimePeriodMsec;

//    if (execCounter == 0) System.out.println("Shooting 0");

//    System.out.println("Shooting Time " + intakeBackTime + ", " + shootWaitTime + + shootStartTime + ", " + shootEndTime);

    if (time > 0 && time < intakeBackTime) {
//      System.out.println("Shooting Backoff: " + time);
      m_roller.runRoller(speedBackoff);
    }
    else if (time < shootWaitTime) {
//      System.out.println("Shooter Wait: " + time);      
      m_roller.runRoller(0);
    }
    else if (time < shootStartTime) {
//        System.out.println("Shooter Accelerating: " + time);  
      m_shooter.run(shooterOut);
    }
    else if (time < shootEndTime) {
      //|| m_shooter.atSetPoint()
//      System.out.println("Shooting Starts: " + time);
      m_shooter.run(shooterOut);
      m_roller.runRoller(rollerIn);
    }
    else {
      m_roller.stop();
    }
  }

  // Called once the command ends or is interrupted. This ensures the roller is not running when not intented.
  @Override
  public void end(boolean interrupted) {
    m_roller.runRoller(0);
    m_shooter.stop();
    System.out.println("Shooter ended");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    int time = execCounter * Constants.TimePeriodMsec;
    return time > shootEndTime;
  }

  public void putParams() {
    SmartDashboard.putNumber("Set RPM Up " + (preset + 1), upSpeeds[preset]);
    SmartDashboard.putNumber("Set RPM Down " + (preset + 1), downSpeeds[preset]);

    SmartDashboard.putNumber("Backoff Time", intakeBackTime);
    SmartDashboard.putNumber("Wait Time", shootWaitTime);
    SmartDashboard.putNumber("Shoot Start Time", shootStartTime);
    SmartDashboard.putNumber("Shoot End Time", shootEndTime);
    SmartDashboard.putNumber("Backoff Speed", speedBackoff);
  }

  public void getParams() {
    // int t1 = (int) SmartDashboard.getNumber("Intake In Time", ShooterConstants.IntakeBackTime);
    // int t2 = (int) SmartDashboard.getNumber("Wait Time", ShooterConstants.ShootWaitTime);
    // int t3 = (int) SmartDashboard.getNumber("Shoot Start Time", ShooterConstants.ShootStartTime);
    // int t4 = (int) SmartDashboard.getNumber("Shoot End Time", ShooterConstants.ShootFinishTime);
//      m_linCoef = SmartDashboard.getNumber("Linear Sensitivity", LinCoef);
    upSpeeds[preset] = SmartDashboard.getNumber("Set RPM Up " + (preset + 1), ShooterConstants.SpeedUp);
    downSpeeds[preset] = SmartDashboard.getNumber("Set RPM Down " + (preset + 1), ShooterConstants.SpeedDown);

    intakeBackTime = (int) SmartDashboard.getNumber("Backoff Time", ShooterConstants.IntakeBackTime);
    shootWaitTime = (int) SmartDashboard.getNumber("Wait Time", ShooterConstants.ShootWaitTime);
    shootStartTime = (int) SmartDashboard.getNumber("Shoot Start Time", ShooterConstants.ShootStartTime);
    shootEndTime = (int) SmartDashboard.getNumber("Shoot End Time", ShooterConstants.ShootFinishTime);
    speedBackoff = SmartDashboard.getNumber("Backoff Speed", RollerConstants.SpeedBackoff);

    if (intakeBackTime < Constants.TimePeriodMsec)
      intakeBackTime = ShooterConstants.IntakeBackTime;
    if (shootWaitTime < intakeBackTime)
      shootWaitTime = intakeBackTime + ShooterConstants.ShootWaitTime;
    if (shootStartTime < shootWaitTime)
      shootStartTime = shootWaitTime + ShooterConstants.ShootStartTime;
    if (shootEndTime < shootStartTime)
      shootEndTime = shootStartTime + ShooterConstants.ShootFinishTime;

    // rollerOut = rollOut;
    // shooterOut = shootOut;
    // rollerIn = rollIn;
    // rollerOut = Math.max(Math.min(rollerOut, 1), -1);
    // shooterOut = Math.max(Math.min(shooterOut, 1), -1);
    // rollerIn = Math.max(Math.min(rollerIn, 1), -1);
  }

/*
  public void updateTimes(int t1, int t2, int t3, int t4) {
    intakeBackTime = t1;
    shootWaitTime = t2;
    shootStartTime = t3;
    shootEndTime = t4;

    if (intakeBackTime < Constants.TimePeriodMsec)
      intakeBackTime = ShooterConstants.IntakeBackTime;
    if (shootWaitTime < intakeBackTime)
      shootWaitTime = intakeBackTime + ShooterConstants.ShootWaitTime;
    if (shootStartTime < shootWaitTime)
      shootStartTime = shootWaitTime + ShooterConstants.ShootStartTime;
    if (shootEndTime < shootStartTime)
      shootEndTime = shootStartTime + ShooterConstants.ShootFinishTime;
  }

  public void updateSpeed(double backoff, double shootOut, double rollIn) {
    speedBackoff = backoff;
    shooterOut = shootOut;
    rollerIn = rollIn;
    speedBackoff = Math.max(Math.min(speedBackoff, 1), -1);
    shooterOut = Math.max(Math.min(shooterOut, 1), -1);
    rollerIn = Math.max(Math.min(rollerIn, 1), -1);
  }
*/
}
