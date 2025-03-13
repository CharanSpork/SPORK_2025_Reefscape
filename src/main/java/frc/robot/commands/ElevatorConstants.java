package frc.robot.commands;

public class ElevatorConstants{

    //Starting number for the coder should be 3 inches(string is already 3 inches high)
    public static final double coder_height = 5;
    public static final double L1 = 15.5 - coder_height;
    public static final double L2 = 31.5 - coder_height;
    public static final double L3 = 47.5 - coder_height;
    public static final double L4 = 71.5 - coder_height;
    public static final double elevator_speed = 0.2;
    public static final double error = 0.5;
    public static final int spark_channel = 9;

    public static final int coder_port = 0;
    public static final int coder_length = 80;
    public static final int coder_voltage = 5;
    public static final double voltage_to_distance_factor = coder_length / coder_voltage;


    public static final int shooter_spark_channel = 11;
}