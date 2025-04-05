// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.Constants.ArmConstants;
import frc.robot.subsystems.ArmSubsystem;
import edu.wpi.first.wpilibj2.command.Command;

/** An ArmUpCommand that uses an Arm subsystem. */
public class ArmUpCommand extends Command {
  private final ArmSubsystem m_arm;

  private int execCounter = 0;

  /**
   * Powers the arm up, when finished passively holds the arm up.
   * 
   * We recommend that you use this to only move the arm into the hardstop
   * and let the passive portion hold the arm up.
   *
   * @param arm The subsystem used by this command.
   */
  public ArmUpCommand(ArmSubsystem arm) {
    m_arm = arm;
    addRequirements(arm);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_arm.init();
    m_arm.setAngle(true);
    execCounter = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    int time = execCounter++ * Constants.TimePeriodMsec;

    if (Constants.ArmUsePulse) {
      if (time < ArmConstants.ARM_TIME_UP) {
//        System.out.println("ARM UP");
        m_arm.run(ArmConstants.ARM_SPEED_UP);
      }
      else if (time < ArmConstants.ARM_TIME_UP_BRAKE1) {
//        System.out.println("ARM WAIT");
      }
      else if (time < ArmConstants.ARM_TIME_UP_BRAKE2) {
//        System.out.println("ARM BRAKE");
        m_arm.run(ArmConstants.ARM_SPEED_UP_BRAKE);
      }
    }
    else {
      m_arm.runToPosition(ArmConstants.AngleUp);
    }

    // if (execCounter == 0)
    //   System.out.println("ShootCmd Start: " + time);

    // if (time > 0 && time < intakeBackTime) {
    //   System.out.println("Shooter intake: " + time);
    //   m_roller.runRoller(rollerOut);
    // }
  }

  // Called once the command ends or is interrupted.
  // Here we run a command that will hold the arm up after to ensure the arm does
  // not drop due to gravity.
  @Override
  public void end(boolean interrupted) {
    m_arm.run(0);
//    m_arm.run(ArmConstants.ARM_HOLD_UP);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (Constants.ArmUsePulse)
      return execCounter * Constants.TimePeriodMsec >= ArmConstants.ARM_TIME_UP_BRAKE2;
    else
      return execCounter * Constants.TimePeriodMsec >= ArmConstants.ArmTimeUp;
//    return false;
  }
}
