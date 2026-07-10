package model;

public class ParkingRecord {

    private String vehicleNumber;
    private String ownerName;
    private int slotNumber;

    private ParkingRecord next;

    public ParkingRecord(
            String vehicleNumber,
            String ownerName,
            int slotNumber)
    {
        this.vehicleNumber = vehicleNumber;
        this.ownerName = ownerName;
        this.slotNumber = slotNumber;

        this.next = null;
    }

    public String getVehicleNumber()
    {
        return vehicleNumber;
    }

    public String getOwnerName()
    {
        return ownerName;
    }

    public int getSlotNumber()
    {
        return slotNumber;
    }

    public ParkingRecord getNext()
    {
        return next;
    }

    public void setNext(ParkingRecord next)
    {
        this.next = next;
    }
}