// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Dumpster;
import edu.wpi.first.wpilibj.PS5Controller;
import edu.wpi.first.wpilibj.XboxController;

public class Robot extends TimedRobot {
  SparkMax leftLeader;
  SparkMax leftFollower;
  SparkMax rightLeader;
  SparkMax rightFollower;
 // XboxController joystick;
  PS5Controller joystick;
  
  //private RobotContainer m_robotContainer;
 // class
  public static final Dumpster dumpster = new Dumpster();
  public Robot() {
    // Instantiate our RobotContainer. This will perform all our button bindings,
    // and put our
    // autonomous chooser on the dashboard.
    //m_robotContainer = new RobotContainer();

    // Initialize the SPARKs
    leftLeader = new SparkMax(25, MotorType.kBrushed);
    leftFollower = new SparkMax(17, MotorType.kBrushed);
    rightLeader = new SparkMax(15, MotorType.kBrushed);
    rightFollower = new SparkMax(11, MotorType.kBrushed);

    /*
     * Create new SPARK MAX configuration objects. These will store the
     * configuration parameters for the SPARK MAXes that we will set below.
     */
    SparkMaxConfig globalConfig = new SparkMaxConfig();
    SparkMaxConfig rightLeaderConfig = new SparkMaxConfig();
    SparkMaxConfig leftFollowerConfig = new SparkMaxConfig();
    SparkMaxConfig rightFollowerConfig = new SparkMaxConfig();

    /*
     * Set parameters that will apply to all SPARKs. We will also use this as
     * the left leader config.
     */
    globalConfig
        .smartCurrentLimit(50)
        .idleMode(IdleMode.kBrake);

    // Apply the global config and invert since it is on the opposite side
    rightLeaderConfig
        .apply(globalConfig)
        .inverted(true);

    // Apply the global config and set the leader SPARK for follower mode
    leftFollowerConfig
        .apply(globalConfig)
        .follow(leftLeader);

    // Apply the global config and set the leader SPARK for follower mode
    rightFollowerConfig
        .apply(globalConfig)
        .follow(rightLeader);

    /*
     * Apply the configuration to the SPARKs.
     *
     * kResetSafeParameters is used to get the SPARK MAX to a known state. This
     * is useful in case the SPARK MAX is replaced.
     *
     * kPersistParameters is used to ensure the configuration is not lost when
     * the SPARK MAX loses power. This is useful for power cycles that may occur
     * mid-operation.
     */
    leftLeader.configure(globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    leftFollower.configure(leftFollowerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    rightLeader.configure(rightLeaderConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    rightFollower.configure(rightFollowerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    // Initialize joystick
    //joystick = new XboxController(0);
    joystick = new PS5Controller(0);

  }

  @Override
  public void robotPeriodic() {
    // Display the applied output of the left and right side onto the dashboard
    SmartDashboard.putNumber("Left Out", leftLeader.getAppliedOutput());
    SmartDashboard.putNumber("Right Out", rightLeader.getAppliedOutput());

    CommandScheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    /**
     * Get forward and rotation values from the joystick. Invert the joystick's
     * Y value because its forward direction is negative.
     */
    double maxSpeed = 0.8;
    double maxRotation = 0.5;
    // ARCADE
    double forward = joystick.getLeftY()*maxSpeed;
    double rotation = -joystick.getRightX()*maxRotation;
    

    // DIFFERENTIAL
    // double left = joystick.getLeftY()*maxSpeed;
    // double right = joystick.getRightY()*maxSpeed;

    /*
     * Apply values to left and right side. We will only need to set the leaders
     * since the other motors are in follower mode.
     */
    // ARCADE
    leftLeader.set(forward + rotation);
    rightLeader.set(forward - rotation);

    // DIFFERENTIAL
    // leftLeader.set(left);
    // rightLeader.set(right);

    // DUMPSTER
    if(joystick.getR2ButtonPressed()){
      dumpster.activateCoral(0.6);
    }else if(joystick.getR2ButtonReleased()){
      dumpster.activateCoral(0);
    }
    
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void simulationInit() {
  }

  @Override
  public void simulationPeriodic() {
  }
}