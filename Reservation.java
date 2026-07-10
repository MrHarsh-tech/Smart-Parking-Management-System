package model;

public class Reservation {

    private String vehicleNumber;
    private int slotNumber;
    private String reservationId;

    public Reservation(
            String reservationId,
            String vehicleNumber,
            int slotNumber)
    {
        this.reservationId = reservationId;
        this.vehicleNumber = vehicleNumber;
        this.slotNumber = slotNumber;
    }

    public String getVehicleNumber()
    {
        return vehicleNumber;
    }

    public int getSlotNumber()
    {
        return slotNumber;
    }

    public String getReservationId()
    {
        return reservationId;
    }
}
