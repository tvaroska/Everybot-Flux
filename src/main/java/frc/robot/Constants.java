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
  public static int TimePeriodMsec = 20;  // in seconds
  public static double TimePeriod = 0.001 * TimePeriodMsec;  // in milliseconds
  public static boolean ArmUsePulse = true;

  public static final class DriveConstants {
    // 1st bot: 1, 2, 3, 4 -- 2nd bot: 18, 19, 10, 11
    public static final int LEFT_LEADER_ID = 18;
    public static final int LEFT_FOLLOWER_ID = 19;
    public static final int RIGHT_LEADER_ID = 10;
    public static final int RIGHT_FOLLOWER_ID = 11;

    public static final int DRIVE_MOTOR_CURRENT_LIMIT = 60;
    public static final double DRIVE_MOTOR_VOLTAGE_COMP = 12;
    public static final double SPEED_LIMIT = 0.8;
    public static final double SLOW_MODE_MOVE = 0.4;
    public static final double SLOW_MODE_TURN = 0.4;

    public static final double AUTO_MODE_SPEED = -0.3;
    public static final double AUTO_MODE_TIME = 0.8;  //
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

    public static final double SpeedIn = 0.8;
    public static final double SpeedOut = -0.8;
    public static final double SpeedShoot = 0.4;
    public static final double SpeedBackoff = 0.2;
  }

  public static final class ShooterConstants {
    public static final int UP_MOTOR_ID = 8;
    public static final int UP_MOTOR_IDR = 9;
    public static final int DOWN_MOTOR_ID = 6;
    public static final int DOWN_MOTOR_IDR = 7;

    public static final int SHOOT_MOTOR_CURRENT_LIMIT = 40;
    public static final double SHOOT_MOTOR_VOLTAGE_COMP = 10;

    public static final double ALGAE_IN = -0.4;
    public static final double ALGAE_OUT = 0.2;

    public static final double SHOOT_ALGAE_IN = 0.4;
    public static final double SHOOT_ALGAE_OUT = -0.8;

    public static final int ShootWaitDelay = 300;
    public static final int ShootStartDelay = 1500;
    public static final int ShootFinishDelay = 1000;
  
    public static final int IntakeBackTime = 300;
    public static final int ShootWaitTime = IntakeBackTime + ShootWaitDelay;
    public static final int ShootStartTime = ShootWaitTime + ShootStartDelay;
    public static final int ShootFinishTime = ShootStartTime + ShootFinishDelay;
  
    public static final double SpeedUp = 2500;
    public static final double SpeedDown = -2500;

    public static final double SpeedUp1 = 2100;
    public static final double SpeedUp2 = 2200;
    public static final double SpeedUp3 = 2800;
    public static final double SpeedUp4 = 3000;

    public static final double SpeedDown1 = -1900;
    public static final double SpeedDown2 = -2200;
    public static final double SpeedDown3 = -2800;
    public static final double SpeedDown4 = -3000;

    //2500/0.4/0.00008/0.001500
    //3500/2500/0.7/0.00008/0.100 / 0.002
    //1500/0.3/0.00003/0.100 / 0.001
    public static final double kP = 0.4;//0.2
    public static final double kD = 0.00008;//0.001
    public static final double kI = 0.001;
    public static final double kV = 0.0;

    public static final double ControlOutputMax = 1.0;
    public static final double ControlOutputMin = 0.1;

    public static final double PositionDelta = 0.01;  // Relative
    public static final double RPMDelta = 0.1;  // Absolute
  }

  public static final class ArmConstants {
    public static final int ARM_MOTOR_ID = 4;
    public static final int ARM_MOTOR_IDR = 5;

    public static final int ARM_MOTOR_CURRENT_LIMIT = 40;
    public static final double ARM_MOTOR_VOLTAGE_COMP = 10;

    // -0.99/630
    // Even point: -0.25 Weak: -0.35
    public static final double ARM_SPEED_DOWN = -0.4;//0.99;//Reduce!!!
    // Even point ALMOST: 0.2/350/330 sec
    public static final double ARM_SPEED_UP = 0.22;
    public static final double ARM_SPEED_UP_BRAKE = -0.1;
    public static final double ARM_HOLD_DOWN = -0.05;
    public static final double ARM_HOLD_UP = 0.05;

    public static final double ARM_TIME_DOWN = 630;
    public static final double ARM_TIME_UP = 360;
    public static final double ARM_TIME_UP_BRAKE1 = 620;
    public static final double ARM_TIME_UP_BRAKE2 = 760;

    public static final double ArmTimeUp = 2000;

    public static final double SpeedUp = -0.4;
    public static final double SpeedDown = 0.4;
    public static final double SpeedHold = 0.2;
    public static final double SpeedBackoff = 0.2;

    // Gear ratioo: 20:1, Chain ratio: 16:48, Overall ratio: 60:1
    public static final int GearRatio = 60;

    // Motor shaft rotations
    public static final double AngleUp = 18 * GearRatio / 360.0;
    public static final double AngleDown = -18 * GearRatio / 360.0;
    public static final double AngleMid = 9 * GearRatio / 360.0;

    public static final double kP = 0.1;
    public static final double kD = 0.0;//0.0004;
    public static final double kI = 0.0;//0.0005;

    public static final double ControlOutputMax = 0.4;
    public static final double ControlOutputMin = 0.05;

    public static final double PositionDelta = 0.01;  // Relative
    public static final double RPMDelta = 0.1;  // Absolute
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

    public static final double LinCoef = 0.2;
    public static final double Threshold = 0.02;
    public static final double CuspX = 0.8;
    public static final double SpeedLimitX = 1.0;
    public static final double SpeedLimitRot = 0.7;
    }
}
