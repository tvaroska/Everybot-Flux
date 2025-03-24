// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final class DriveConstants {
    // 1st bot: 1, 2, 3, 4 -- 2nd bot: 18, 19, 10, 11
    public static final int LEFT_LEADER_ID = 18;
    public static final int LEFT_FOLLOWER_ID = 19;
    public static final int RIGHT_LEADER_ID = 10;
    public static final int RIGHT_FOLLOWER_ID = 11;

    public static final int DRIVE_MOTOR_CURRENT_LIMIT = 60;
    public static final double DRIVE_MOTOR_VOLTAGE_COMP = 12;
    public static final double SPEED_LIMIT = 0.8;
    public static final double SLOW_MODE_MOVE = 0.5;
    public static final double SLOW_MODE_TURN = 0.6;
  }

  public static final class RollerConstants {
    public static final int ROLLER_MOTOR_ID = 14;
    public static final int ROLLER_MOTOR_IDR = 15;
    public static final int ROLLER_MOTOR_CURRENT_LIMIT = 40;
    public static final double ROLLER_MOTOR_VOLTAGE_COMP = 10;

    public static final double ROLLER_ALGAE_IN = 0.8;
    public static final double ROLLER_ALGAE_OUT = -0.8;

    public static final double ROLLER_CORAL_OUT = -0.4;
    public static final double ROLLER_CORAL_STACK = -1;
  }

  public static final class ShooterConstants {
    public static final int UP_MOTOR_ID = 6;  // Down
    public static final int UP_MOTOR_IDR = 7;
    public static final int DOWN_MOTOR_ID = 8;
    public static final int DOWN_MOTOR_IDR = 9;
    public static final int SHOOT_MOTOR_CURRENT_LIMIT = 40;
    public static final double SHOOT_MOTOR_VOLTAGE_COMP = 10;

    public static final double ALGAE_IN = -0.4;
    public static final double ALGAE_OUT = 0.2;

    public static final double SHOOT_ALGAE_IN = 0.4;
    public static final double SHOOT_ALGAE_OUT = -0.8;

    public static final double SHOOT_CORAL_OUT = 0.1;
    public static final double SHOOT_CORAL_STACK = -1;
  }

  public static final class ArmConstants {
    public static final int ARM_MOTOR_ID = 4; // Right
    public static final int ARM_MOTOR_IDR = 5;
    public static final int ARM_MOTOR_CURRENT_LIMIT = 20;
    public static final double ARM_MOTOR_VOLTAGE_COMP = 10;
    public static final double ARM_SPEED_DOWN = 0.4;
    public static final double ARM_SPEED_UP = -0.4;
    public static final double ARM_HOLD_DOWN = 0.2;
    public static final double ARM_HOLD_UP = -0.2;
  }

  public static final class ClimberConstants {
    public static final int CLIMBER_MOTOR_ID = 17;
    public static final int CLIMBER_MOTOR_CURRENT_LIMIT = 40;
    public static final double CLIMBER_MOTOR_VOLTAGE_COMP = 12;
    public static final double CLIMBER_SPEED_DOWN = -0.5;
    public static final double CLIMBER_SPEED_UP = 0.5;
  }

  public static final class OperatorConstants {
    public static final int DRIVER_CONTROLLER_PORT = 0;
    public static final int OPERATOR_CONTROLLER_PORT = 0;
  }
}
