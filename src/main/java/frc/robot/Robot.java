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
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.PS5Controller;

public class Robot extends TimedRobot {
  SparkMax leftLeader;
  SparkMax leftFollower;
  SparkMax rightLeader;
  SparkMax rightFollower;

  SparkMax ps5_leftLeader;
  SparkMax ps5_leftFollower;
  SparkMax ps5_rightLeader;
  SparkMax ps5_rightFollower;


  XboxController joystick;
  PS5Controller joyPs5Controller;


  public Robot() {
    // Initialize the SPARKs
    leftLeader = new SparkMax(25, MotorType.kBrushed);
    leftFollower = new SparkMax(17, MotorType.kBrushed);
    rightLeader = new SparkMax(15, MotorType.kBrushed);
    rightFollower = new SparkMax(11, MotorType.kBrushed);

    ps5_leftLeader = new SparkMax(25, MotorType.kBrushed);
    ps5_leftFollower = new SparkMax(17, MotorType.kBrushed);
    ps5_rightLeader = new SparkMax(15, MotorType.kBrushed);
    ps5_rightFollower = new SparkMax(11, MotorType.kBrushed);

    /*
     * Create new SPARK MAX configuration objects. These will store the
     * configuration parameters for the SPARK MAXes that we will set below.
     */
    SparkMaxConfig globalConfig = new SparkMaxConfig();
    SparkMaxConfig rightLeaderConfig = new SparkMaxConfig();
    SparkMaxConfig leftFollowerConfig = new SparkMaxConfig();
    SparkMaxConfig rightFollowerConfig = new SparkMaxConfig();

    SparkMaxConfig ps5_globalConfig = new SparkMaxConfig();
    SparkMaxConfig ps5_rightLeaderConfig = new SparkMaxConfig();
    SparkMaxConfig ps5_leftFollowerConfig = new SparkMaxConfig();
    SparkMaxConfig ps5_rightFollowerConfig = new SparkMaxConfig();

    

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


    ps5_globalConfig
        .smartCurrentLimit(50)
        .idleMode(IdleMode.kBrake);

    // Apply the global config and invert since it is on the opposite side
    ps5_rightLeaderConfig
        .apply(ps5_globalConfig)
        .inverted(true);

    // Apply the global config and set the leader SPARK for follower mode
    ps5_leftFollowerConfig
        .apply(ps5_globalConfig)
        .follow(ps5_leftLeader);

    // Apply the global config and set the leader SPARK for follower mode
    ps5_rightFollowerConfig
        .apply(ps5_globalConfig)
        .follow(ps5_rightLeader);

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

    ps5_leftLeader.configure(globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    ps5_leftFollower.configure(leftFollowerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    ps5_rightLeader.configure(rightLeaderConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    ps5_rightFollower.configure(rightFollowerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    // Initialize joystick
    joystick = new XboxController(0);
    joyPs5Controller = new PS5Controller(0);
  }

  @Override
  public void robotPeriodic() {
    // Display the applied output of the left and right side onto the dashboard
    SmartDashboard.putNumber("Left Out", leftLeader.getAppliedOutput());
    SmartDashboard.putNumber("Right Out", rightLeader.getAppliedOutput());

    SmartDashboard.putNumber("Left Out", ps5_leftLeader.getAppliedOutput());
    SmartDashboard.putNumber("Right Out", ps5_rightLeader.getAppliedOutput());
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
    double maxSpeed = 0.5;
    double maxRotation = 0.2;
    // ARCADE
    //double forward = joystick.getLeftY()*maxSpeed;
    //double rotation = -joystick.getRightX()*maxRotation;

    // DIFFERENTIAL
    double left = joystick.getLeftY()*maxSpeed;
    double right = joystick.getRightY()*maxSpeed;
    double ps5_left = joyPs5Controller.getLeftY()*maxSpeed;
    double ps5_right = joyPs5Controller.getRightY()*maxSpeed;


    /*
     * Apply values to left and right side. We will only need to set the leaders
     * since the other motors are in follower mode.
     */
    // ARCADE
    //leftLeader.set(forward + rotation);
    //rightLeader.set(forward - rotation);

    // DIFFERENTIAL
    leftLeader.set(left);
    rightLeader.set(right);

    ps5_leftLeader.set(ps5_left);
    ps5_rightLeader.set(ps5_right);
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
