// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants.RollerConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.RollerSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

/** A command to remove (score or pass) Algae. */
public class AlgieShootCommand extends Command {
  public static final int ExecTime = 20;

  public static final int ShootWaitDelay = 400;
  public static final int ShootStartDelay = 1000;
  public static final int ShootFinishDelay = 1000;

  public static final int IntakeBackTime = 400;
  public static final int ShootWaitTime = IntakeBackTime + ShootWaitDelay;
  public static final int ShootStartTime = ShootWaitTime + ShootStartDelay;
  public static final int ShootFinishTime = ShootStartTime + ShootFinishDelay;

  private final RollerSubsystem m_roller;

  private int execCounter = 0;

  private int intakeBackTime = IntakeBackTime;
  private int shootWaitTime = ShootWaitTime;
  private int shootStartTime = ShootStartTime;
  private int shootEndTime = ShootFinishTime;

  private double rollerOut = ShooterConstants.ALGAE_OUT;
  private double shooterOut = ShooterConstants.SHOOT_ALGAE_OUT;
  private double rollerIn = ShooterConstants.ALGAE_IN;

  /**
   * Shoot the Algae int to the net. 
   *
   * @param roller The subsystem used by this command.
   */
  public AlgieShootCommand(RollerSubsystem roller) {
    m_roller = roller;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(roller);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    execCounter = 0;
    m_roller.init();
    System.out.println("Shooter init");

    updateTimesDB();
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
    int time = execCounter * ExecTime;
    if (execCounter == 0)
      System.out.println("ShootCmd Start: " + time);

    if (time > 0 && time < intakeBackTime) {
      System.out.println("Shooter intake: " + time);
      m_roller.runRoller(rollerOut);
    }
    else if (time < shootWaitTime) {
      System.out.println("Shooter Wait: " + time);      
      m_roller.runRoller(0);
//      m_roller.stop();

      //m_roller.runShooter(ShooterConstants.SHOOT_ALGAE_OUT);
    }
    else if (time < shootStartTime) {
      System.out.println("Shooter Shoot: " + time);  
      m_roller.runShooter(shooterOut);
    }
    else if (time < shootEndTime) {
      System.out.println("Shooter Time: " + time);
      m_roller.runShooter(shooterOut);
      m_roller.runRoller(rollerIn);
    }
    else {
      m_roller.stop();
    }
//    System.out.println("Shooter running: " + time);
    execCounter++;
  }

  // Called once the command ends or is interrupted. This ensures the roller is not running when not intented.
  @Override
  public void end(boolean interrupted) {
    System.out.println("Shooter ended");
    m_roller.runRoller(0);
    m_roller.runShooter(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    int time = execCounter * ExecTime;
    return time > shootEndTime + ExecTime;
  }

  public void updateTimesDB() {
      int t1 = (int) SmartDashboard.getNumber("Intake In Time", AlgieShootCommand.IntakeBackTime);
      int t2 = (int) SmartDashboard.getNumber("Wait Time", AlgieShootCommand.ShootWaitTime);
      int t3 = (int) SmartDashboard.getNumber("Shoot Start Time", AlgieShootCommand.ShootStartTime);
      int t4 = (int) SmartDashboard.getNumber("Shoot End Time", AlgieShootCommand.ShootFinishTime);
      updateTimes(t1, t2, t3, t4);
  }

  public void updateTimes(int t1, int t2, int t3, int t4) {
    intakeBackTime = t1;
    shootWaitTime = t2;
    shootStartTime = t3;
    shootEndTime = t4;

    if (intakeBackTime < ExecTime)
      intakeBackTime = IntakeBackTime;
    if (shootWaitTime < intakeBackTime)
      shootWaitTime = intakeBackTime + ShootWaitTime;
    if (shootStartTime < shootWaitTime)
      shootStartTime = shootWaitTime + ShootStartTime;
    if (shootEndTime < shootStartTime)
      shootEndTime = shootStartTime + ShootFinishTime;
  }

  public void updateSpeed(double rollOut, double shootOut, double rollIn) {
    rollerOut = rollOut;
    shooterOut = shootOut;
    rollerIn = rollIn;
    rollerOut = Math.max(Math.min(rollerOut, 1), -1);
    shooterOut = Math.max(Math.min(shooterOut, 1), -1);
    rollerIn = Math.max(Math.min(rollerIn, 1), -1);
  }
}
