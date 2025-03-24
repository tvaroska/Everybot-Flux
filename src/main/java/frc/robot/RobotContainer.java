// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.autos.DriveForwardAuto;
import frc.robot.autos.SimpleCoralAuto;
import frc.robot.commands.AlgieInCommand;
import frc.robot.commands.AlgieOutCommand;
import frc.robot.commands.AlgieShootCommand;
import frc.robot.commands.ArmDownCommand;
import frc.robot.commands.ArmUpCommand;
import frc.robot.commands.ClimberDownCommand;
import frc.robot.commands.ClimberUpCommand;
import frc.robot.commands.CoralOutCommand;
import frc.robot.commands.CoralStackCommand;
import frc.robot.commands.DriveCommand;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.RollerSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  // The robot's subsystems and commands are defined here...
  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.DRIVER_CONTROLLER_PORT);
  // You can remove this if you wish to have a single driver, note that you
  // may have to change the binding for left bumper.
  private final CommandXboxController m_operatorController = 
      new CommandXboxController(OperatorConstants.OPERATOR_CONTROLLER_PORT);

  // The autonomous chooser
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  public static final double LinCoef = 0.4;
  public static final double Threshold = 0.1;
  public static final double CuspX = 0.6;
  public static final double SpeedLimitX = 1.0;
  public static final double SpeedLimitRot = 0.7;


  double m_linCoef = LinCoef;
  double m_threshold = Threshold;
  double m_cuspX = CuspX;
  double m_speedLimitX = SpeedLimitX;
  double m_speedLimitRot = SpeedLimitRot;

  double m_speedLimit2 = 1.0;
  double m_speedLimit3 = Constants.DriveConstants.SPEED_LIMIT;

  private int intakeBackTime = AlgieShootCommand.IntakeBackTime;
  private int shootWaitTime = AlgieShootCommand.ShootWaitTime;
  private int shootStartTime = AlgieShootCommand.ShootStartTime;
  private int shootEndTime = AlgieShootCommand.ShootFinishTime;

  public final RollerSubsystem m_roller = new RollerSubsystem();
  public final ArmSubsystem m_arm = new ArmSubsystem();
  public final DriveSubsystem m_drive = new DriveSubsystem();
  public final ClimberSubsystem m_climber = new ClimberSubsystem();

 // public final SimpleCoralAuto m_simpleCoralAuto = new SimpleCoralAuto(m_drive, m_roller, m_arm);
  public final DriveForwardAuto m_driveForwardAuto = new DriveForwardAuto(m_drive);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Set up command bindings
    configureBindings();
    // Set the options to show up in the Dashboard for selecting auto modes. If you
    // add additional auto modes you can add additional lines here with
    // autoChooser.addOption
    //m_chooser.setDefaultOption("Coral Auto", m_simpleCoralAuto);
    m_chooser.addOption("Drive Forward Auto", m_driveForwardAuto);
    SmartDashboard.putData(m_chooser);
    SmartDashboard.putNumber("Linear coefficient", m_linCoef);
    SmartDashboard.putNumber("Speed limit", m_speedLimitX);
    SmartDashboard.putNumber("Rot Speed limit", m_speedLimitRot);
    SmartDashboard.putNumber("Threshold", m_threshold);
    SmartDashboard.putNumber("Linear Zone", m_cuspX);

    SmartDashboard.putNumber("Intake In Time", intakeBackTime);
    SmartDashboard.putNumber("Wait Time", shootWaitTime);
    SmartDashboard.putNumber("Shoot Start Time", shootStartTime);
    SmartDashboard.putNumber("Shoot End Time", shootEndTime);
    // SmartDashboard.putNumber("Wait Time", shootWaitTime);
    // SmartDashboard.putNumber("Shoot Start Time", shootStartTime);
    // SmartDashboard.putNumber("Shoot End Time", shootEndTime);
    // rollerOut = rollOut;
    // shooterOut = shootOut;
    // rollerIn = rollIn;
    }

  public void updateParams() {
    System.out.println("updateParams");
    m_linCoef = SmartDashboard.getNumber("Linear coefficient", LinCoef);
    m_speedLimitX = SmartDashboard.getNumber("Speed limit", SpeedLimitX);
    m_speedLimitRot = SmartDashboard.getNumber("Rot Speed limit", SpeedLimitRot);
    m_threshold = SmartDashboard.getNumber("Threshold", Threshold);
    m_cuspX = SmartDashboard.getNumber("Linear Zone", CuspX);
    
    if (m_cuspX > 0.9)
      m_cuspX = 0.9;
    if (m_cuspX < 0.0)
      m_cuspX = 0.0;
    if (m_threshold > m_cuspX)
      m_threshold = m_cuspX / 2;
    if (m_threshold < 0)
      m_threshold = 0;
 
  //    m_roller.updateParams();
//      updateTimes();
    }

    public void updateShooterParams() {

    }

    public void updateTimes() {
      intakeBackTime = (int) SmartDashboard.getNumber("Intake In Time", AlgieShootCommand.IntakeBackTime);
      shootWaitTime = (int) SmartDashboard.getNumber("Wait Time", AlgieShootCommand.ShootWaitTime);
      shootStartTime = (int) SmartDashboard.getNumber("Shoot Start Time", AlgieShootCommand.ShootStartTime);
      shootEndTime = (int) SmartDashboard.getNumber("Shoot End Time", AlgieShootCommand.ShootFinishTime);
    }
    /*
    public void updateSpeed(double rollOut, double shootOut, double rollIn) {
      rollerOut = rollOut;
      shooterOut = shootOut;
      rollerIn = rollIn;
      rollerOut = Math.max(Math.min(rollerOut, 1), -1);
      shooterOut = Math.max(Math.min(shooterOut, 1), -1);
      rollerIn = Math.max(Math.min(rollerIn, 1), -1);
    }
*/
  double sensitivity2(double x) {

    double xabs = Math.abs(x);
    if (xabs < m_threshold)
      return 0;
    else if (xabs <= m_cuspX) {
      //System.out.println("Lin " + m_linCoef + " x: " + x);
      return m_linCoef * x;
    }
    else {//if (xabs <= 1.0) {
      // double x2 = (xabs - m_speedStep1);
      // x2 *= x2;
      double x0 = m_cuspX;
      double g = (2.0 - x0) * x0;
      double denom = 1.0 - g;
      double a = (1.0 - m_linCoef) / denom;
      double c = a * x0 * x0;
      double b = (m_linCoef + (m_linCoef * x0 - 2.0) * x0) / denom;

      //System.out.println("Quad " + m_threshold + " x: " + x);
      xabs = a * xabs * xabs + b * xabs + c;
      x = x >= 0 ? xabs : -xabs;
    }
    return x;
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    m_operatorController.b().onTrue(new AlgieShootCommand(m_roller));

    /** 
     * Set the default command for the drive subsystem to an instance of the
     * DriveCommand with the values provided by the joystick axes on the driver
     * controller. The Y axis of the controller is inverted so that pushing the
     * stick away from you (a negative value) drives the robot forwards (a positive
     * value). Similarly for the X axis where we need to flip the value so the
     * joystick matches the WPILib convention of counter-clockwise positive
     */
    m_drive.setDefaultCommand(new DriveCommand(m_drive,
        () -> m_speedLimitX * sensitivity2(m_driverController.getLeftY()),
        () -> m_speedLimitRot * sensitivity2(m_driverController.getRightX()),
        () -> false));

//    if (true) return;
    /**
     * Holding the left bumper (or whatever button you assign) will multiply the speed
     * by a decimal to limit the max speed of the robot -> 
     * 1 (100%) from the controller * .9 = 90% of the max speed when held (we also square it)
     * 
     * Slow mode is very valuable for line ups and the deep climb 
     * 
     * When switching to single driver mode switch to the B button
     */
    // m_driverController.leftBumper().whileTrue(new DriveCommand(m_drive, 
    //     () -> -m_driverController.getLeftY() * DriveConstants.SLOW_MODE_MOVE,  
    //     () -> -m_driverController.getRightX() * DriveConstants.SLOW_MODE_TURN,
    //     () -> true));

    /**
     * Here we declare all of our operator commands, these commands could have been
     * written in a more compact manner but are left verbose so the intent is clear.
     */
    m_operatorController.rightBumper().whileTrue(new AlgieInCommand(m_roller));
    
    // Here we use a trigger as a button when it is pushed past a certain threshold
    m_operatorController.rightTrigger(.2).whileTrue(new AlgieOutCommand(m_roller));

    /**
     * The arm will be passively held up or down after this is used,
     * make sure not to run the arm too long or it may get upset!
     */
    m_operatorController.leftBumper().whileTrue(new ArmUpCommand(m_arm));
    m_operatorController.leftTrigger(.2).whileTrue(new ArmDownCommand(m_arm));

    /**
     * Used to score coral, the stack command is for when there is already coral
     * in L1 where you are trying to score. The numbers may need to be tuned, 
     * make sure the rollers do not wear on the plastic basket.
     */
    // m_operatorController.x().whileTrue(new CoralOutCommand(m_roller));
    // m_operatorController.y().whileTrue(new CoralStackCommand(m_roller));

//    m_operatorController.a().onTrue(new AlgieShootCommand(m_roller));

    /**
     * POV is a direction on the D-Pad or directional arrow pad of the controller,
     * the direction of this will be different depending on how your winch is wound
     */
    m_operatorController.pov(0).whileTrue(new ClimberUpCommand(m_climber));
    m_operatorController.pov(180).whileTrue(new ClimberDownCommand(m_climber));

    m_driverController.back().whileTrue(new Command() {
        @Override public void initialize() {
          updateParams();
        }
        // @Override public void execute() {
        //   System.out.println("execute");
        // }
      });
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
    public Command getAutonomousCommand() {
    // The selected command will be run in autonomous
    return m_chooser.getSelected();
  }

  double sensitivity(double x) {
    //m_speedLimit1 = SmartDashboard.getNumber("Drive speed limit1", 0.1);

    double xabs = Math.abs(x);
    if (xabs <= m_threshold) {
      return m_linCoef * x;
    }
    else {//if (xabs <= 1.0) {
      // double x2 = (xabs - m_speedStep1);
      // x2 *= x2;

      double c = m_linCoef * m_cuspX;
      double coef = (1 - c) / (1 - m_threshold);
      coef *= m_speedLimit2;
      xabs = coef * (xabs - m_threshold) + c;
      x = x >= 0 ? xabs : -xabs;
    }
    // else if (xabs <= 0.6) {
    //   // 0.32 = 0.4 * k
    //   //x = 0.4 * (x - 0.2) + 0.04;
    // }
    // else {
    //   // 0.2
    //   //x = m_speedLimit1 * (x - 0.6) + 0.2;
    // }
    return x;
  }

  static double sensitivity3(double x) {
    double xabs = Math.abs(x);
    if (xabs < 0.1)
      return 0;
    else if (xabs < 0.6)
      return 0.4 * Math.signum(x) * (xabs);
    else return 0;
    //return 0.5 * x * x * x;
  }

  void teleopInit() {
    m_roller.stop();
  }
}