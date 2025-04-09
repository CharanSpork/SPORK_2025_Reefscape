package frc.robot.common.subsystems.drive;

import static edu.wpi.first.units.Units.RadiansPerSecond;

import org.ironmaple.simulation.drivesims.GyroSimulation;

import frc.robot.common.util.OdometryTimeStampsSim;

public class GyroIOSim implements GyroIO {
    private final GyroSimulation gyroSimulation;

    public GyroIOSim(GyroSimulation gyroSimulation) {
        this.gyroSimulation = gyroSimulation;
    }

    @Override
    public void updateInputs(GyroIOInputs inputs) {
        inputs.connected = true;
        inputs.odometryYawPositions = gyroSimulation.getCachedGyroReadings();
        inputs.odometryYawTimestamps = OdometryTimeStampsSim.getTimeStamps();
        inputs.yawPosition = gyroSimulation.getGyroReading();
        inputs.yawVelocityRadPerSec =
                gyroSimulation.getMeasuredAngularVelocity().in(RadiansPerSecond);
    }
}
