package model;

public class ParkingInfo {

    private Vehicle vehicle;
    private int slotNumber;

    public ParkingInfo(Vehicle vehicle, int slotNumber)
    {
        this.vehicle = vehicle;
        this.slotNumber = slotNumber;
    }

    public Vehicle getVehicle()
    {
        return vehicle;
    }

    public int getSlotNumber()
    {
        return slotNumber;
    }
}