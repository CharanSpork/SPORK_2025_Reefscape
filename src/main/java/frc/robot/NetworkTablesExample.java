package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class NetworkTablesExample {
    public static void main(String[] args) {
        var instance = NetworkTableInstance.getDefault();
        var table = instance.getTable("SmartDashboard");
        NetworkTableEntry entry = table.getEntry("Auto Mode");

        String selectedAuto = entry.getString("Default Auto");
        System.out.println("Selected Autonomous: " + selectedAuto);
    }
}
