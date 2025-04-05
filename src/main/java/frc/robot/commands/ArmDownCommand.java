// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.RollerConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.ArmSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

/** An ArmDown command that uses an Arm subsystem. */
public class ArmDownCommand extends Command {
  private final ArmSubsystem m_arm;

  double speed = ArmConstants.ARM_SPEED_DOWN;
  double speedBrake = ArmConstants.ARM_SPEED_DOWN_BRAKE;

  double timeDown = ArmConstants.ARM_TIME_DOWN;
  double timeBreak1 = ArmConstants.ARM_TIME_DOWN_BRAKE1;
  double timeBreak2 = ArmConstants.ARM_TIME_DOWN_BRAKE2;

  private int execCounter = 0;

  /**
   * Powers the arm down, when finished passively holds the arm down.
   * 
   * We recommend that you use this to only move the arm into the paracord
   * and let the passive portion hold the arm down.
   *
   * @param arm The subsystem used by this command.
   */
  public ArmDownCommand(ArmSubsystem arm) {
    m_arm = arm;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(arm);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    getParams();
    m_arm.init();
    m_arm.setAngle(false);
    execCounter = 0;
  }


  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    int time = execCounter++ * Constants.TimePeriodMsec;

    if (Constants.ArmUsePulse) {
      if (time < timeDown) {
          m_arm.run(speed);
//          System.out.println("ARM DN " + speed);
      }
      else if (time < timeBreak1) {
//        System.out.println("ARM DN WAIT");
      }
      else if (time < timeBreak2) {
//s        System.out.println("ARM DN BRAKE");
        m_arm.run(speedBrake);
      }
    }
    else {
      m_arm.runToPosition(ArmConstants.AngleDown);
    }
  }

  // Called once the command ends or is interrupted.
  // Here we run arm down at low speed to ensure it stays down
  // When the next command is caled it will override this command
  @Override
  public void end(boolean interrupted) {
    m_arm.run(0);
 //   m_arm.run(ArmConstants.ARM_HOLD_DOWN);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
//    return execCounter++ * Constants.TimePeriodMsec >= ArmConstants.ARM_TIME_DOWN;
    return execCounter * Constants.TimePeriodMsec >= timeBreak2;
  }

  public void putParams() {
    SmartDashboard.putNumber("Arm Down Speed", speed);
    SmartDashboard.putNumber("Arm Down Brake", speedBrake);
    SmartDashboard.putNumber("Arm Down Time 1", timeDown);
    SmartDashboard.putNumber("Arm Down Brake Time", timeBreak1);
    SmartDashboard.putNumber("Arm Down Time", timeBreak2);
  }

  public void getParams() {
    speed = SmartDashboard.getNumber("Arm Down Speed", ArmConstants.ARM_SPEED_DOWN);
    speedBrake = SmartDashboard.getNumber("Arm Down Brake", ArmConstants.ARM_SPEED_DOWN_BRAKE);
    timeDown = SmartDashboard.getNumber("Arm Down Time 1", ArmConstants.ARM_TIME_DOWN);
    timeBreak1 = SmartDashboard.getNumber("Arm Down Brake Time", ArmConstants.ARM_TIME_DOWN_BRAKE1);
    timeBreak2 = SmartDashboard.getNumber("Arm Down Time", ArmConstants.ARM_TIME_DOWN_BRAKE2);
  }
}
