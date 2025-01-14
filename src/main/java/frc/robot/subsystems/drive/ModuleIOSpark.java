// Copyright 2021-2024 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot.subsystems.drive;

import static frc.robot.subsystems.drive.DriveConstants.*;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.geometry.Rotation2d;

/**
 * Module IO implementation for Spark Max motor controllers and CAN-based encoders (CANCoder).
 */
public class ModuleIOSpark implements ModuleIO {
    private final Rotation2d zeroRotation;
    private final int module;

    // Hardware objects
    private final SparkMax driveSpark;
    private final SparkMax turnSpark;
    private final CANCoder moduleEncoder;

    // Closed-loop controllers
    private final SparkClosedLoopController driveController;
    private final SparkClosedLoopController turnController;

    // Connection debouncers
    private final Debouncer driveConnectedDebounce = new Debouncer(0.5);
    private final Debouncer turnConnectedDebounce = new Debouncer(0.5);

    public ModuleIOSpark(int module) {
        this.module = module;

        System.out.println("Initializing ModuleIOSpark " + module);

        zeroRotation = switch (module) {
            case 0 -> frontLeftZeroRotation;
            case 1 -> frontRightZeroRotation;
            case 2 -> backLeftZeroRotation;
            case 3 -> backRightZeroRotation;
            default -> new Rotation2d();
        };

        // Initialize drive and turn motor controllers
        System.out.println("Initializing drive motor for module " + module);
        driveSpark = new SparkMax(
                switch (module) {
                    case 0 -> frontLeftDriveCanId;
                    case 1 -> frontRightDriveCanId;
                    case 2 -> backLeftDriveCanId;
                    case 3 -> backRightDriveCanId;
                    default -> 0;
                },
                MotorType.kBrushless
        );

        System.out.println("DriveSpark CAN ID: " + driveSpark.getDeviceId());
        if (driveSpark.getFirmwareVersion() == 0) {
            System.out.println("DriveSpark for module " + module + " is not responding. Check CAN ID and wiring.");
    }

        System.out.println("Initializing turn motor for module " + module);
        turnSpark = new SparkMax(
                switch (module) {
                    case 0 -> frontLeftTurnCanId;
                    case 1 -> frontRightTurnCanId;
                    case 2 -> backLeftTurnCanId;
                    case 3 -> backRightTurnCanId;
                    default -> 0;
                },
                MotorType.kBrushless
        );

    System.out.println("TurnSpark CAN ID: " + turnSpark.getDeviceId());
    if (turnSpark.getFirmwareVersion() == 0) {
        System.out.println("TurnSpark for module " + module + " is not responding. Check CAN ID and wiring.");
    }

        // Initialize CAN-based encoder
        System.out.println("Initializing CANCoder for module " + module);
        moduleEncoder = new CANCoder(
                switch (module) {
                    case 0 -> frontLeftEncoderCanId;
                    case 1 -> frontRightEncoderCanId;
                    case 2 -> backLeftEncoderCanId;
                    case 3 -> backRightEncoderCanId;
                    default -> 0;
                }
        );

        System.out.println("CANCoder CAN ID: " + moduleEncoder.getDeviceID());
            if (moduleEncoder.hasResetOccurred()) {
                System.out.println("CANCoder for module " + module + " has reset. Check wiring or CAN bus health.");
        }
        if (moduleEncoder.getLastError() != ErrorCode.OK) {
            System.out.println("CANCoder error for module " + module + ": " + moduleEncoder.getLastError());
        }

        driveController = driveSpark.getClosedLoopController();
        turnController = turnSpark.getClosedLoopController();

        System.out.println("Configuring drive and turn motors for module " + module);

        // Configure drive motor
        var driveConfig = new SparkMaxConfig();
        driveConfig
                .idleMode(IdleMode.kBrake)
                .smartCurrentLimit(driveMotorCurrentLimit)
                .voltageCompensation(12.0);
        driveConfig.encoder
                .positionConversionFactor(driveEncoderPositionFactor)
                .velocityConversionFactor(driveEncoderVelocityFactor);
        driveConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(driveKp, 0.0, driveKd);
        driveSpark.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        // Configure turn motor
        var turnConfig = new SparkMaxConfig();
        turnConfig
                .inverted(turnInverted)
                .idleMode(IdleMode.kBrake)
                .smartCurrentLimit(turnMotorCurrentLimit)
                .voltageCompensation(12.0);
        turnConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(turnKp, 0.0, turnKd);
        turnSpark.configure(turnConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public void updateInputs(ModuleIOInputs inputs) {

        // Update drive inputs
        inputs.drivePositionRad = Math.toRadians(moduleEncoder.getPosition());
        inputs.driveVelocityRadPerSec = Math.toRadians(moduleEncoder.getVelocity());

        // Update turn inputs
        inputs.turnPosition = new Rotation2d(Math.toRadians(moduleEncoder.getAbsolutePosition())).minus(zeroRotation);
        inputs.turnVelocityRadPerSec = Math.toRadians(moduleEncoder.getVelocity());
    }

    @Override
    public void setDriveOpenLoop(double output) {
        driveSpark.setVoltage(output);
    }

    @Override
    public void setTurnOpenLoop(double output) {
        turnSpark.setVoltage(output);
    }

    @Override
public void setDriveVelocity(double velocityRadPerSec) {
    // Apply inversion to velocity based on the module
    double adjustedVelocity = driveInversions[module] ? -velocityRadPerSec : velocityRadPerSec;

    // Calculate feedforward voltage using adjusted velocity
    double ffVolts = driveKs * Math.signum(adjustedVelocity) + driveKv * adjustedVelocity;

    // Set the adjusted velocity and feedforward voltage
    driveController.setReference(
            adjustedVelocity,                           // Target velocity
            ControlType.kVelocity,                     // Control type
            ClosedLoopSlot.kSlot0,                     // PID slot
            ffVolts,                                   // Feedforward voltage
            SparkClosedLoopController.ArbFFUnits.kVoltage // Feedforward units
    );
}

    @Override
    public void setTurnPosition(Rotation2d rotation) {
        double setpoint = MathUtil.inputModulus(rotation.plus(zeroRotation).getRadians(), -Math.PI, Math.PI);
        turnController.setReference(setpoint, ControlType.kPosition);
    }
}
