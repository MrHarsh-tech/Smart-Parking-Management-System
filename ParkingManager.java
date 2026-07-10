package service;

import model.ParkingSlot;
import model.Vehicle;
import java.util.HashMap;
import model.ParkingInfo;
import java.util.Queue;
import java.util.LinkedList;
import dsa.ParkingHistory;
import model.ParkingRecord;
import model.Reservation;

public class ParkingManager {

    private ParkingSlot[] slots;
    private HashMap<String, ParkingInfo> vehicleMap;
    private Queue<Vehicle> waitingQueue;
    private ParkingHistory history;
    private HashMap<Integer, Reservation> reservations;
    private int reservationCounter = 1000;

    public ParkingManager(int totalSlots)
    {
        slots = new ParkingSlot[totalSlots];

        for(int i = 0; i < totalSlots; i++)
        {
            slots[i] = new ParkingSlot(i + 1);
        }

        vehicleMap = new HashMap<>();
        waitingQueue = new LinkedList<>();
        history = new ParkingHistory();
        reservations = new HashMap<>();
    }
    public int getOccupiedSlotCount()
    {
        int count = 0;

        for(ParkingSlot slot : slots)
        {
            if(slot.isOccupied())
            {
                count++;
            }
        }

        return count;
    }

    public int getAvailableSlotCount()
    {
        return slots.length
                - getOccupiedSlotCount()
                - getReservedSlotCount();
    }

    public int getTotalVehiclesServed()
    {
        return history.countRecords();
    }

    public int getWaitingQueueSize()
    {
        return waitingQueue.size();
    }

    public boolean isSlotReserved(int slotNumber)
    {
        return reservations.containsKey(
                slotNumber
        );
    }

    public int getReservedSlotCount()
    {
        return reservations.size();
    }

    public ParkingSlot[] getSlots()
    {
        return slots;
    }

    public ParkingSlot findAvailableSlot()
    {
        for(ParkingSlot slot : slots)
        {
            if(!slot.isOccupied()
                    && !isSlotReserved(
                    slot.getSlotNumber()))
            {
                return slot;
            }
        }

        return null;
    }

    private boolean parkNormally(Vehicle vehicle)
    {
        ParkingSlot slot = findAvailableSlot();

        if(slot == null)
        {
            waitingQueue.offer(vehicle);
            return false;
        }

        slot.parkVehicle(vehicle);

        vehicleMap.put(
                vehicle.getVehicleNumber(),
                new ParkingInfo(
                        vehicle,
                        slot.getSlotNumber()
                )
        );

        return true;
    }

    private boolean parkReserved(
            Vehicle vehicle,
            Reservation reservation)
    {
        int slotNumber =
                reservation.getSlotNumber();

        ParkingSlot slot =
                slots[slotNumber - 1];

        slot.parkVehicle(vehicle);

        vehicleMap.put(
                vehicle.getVehicleNumber(),
                new ParkingInfo(
                        vehicle,
                        slotNumber
                )
        );

        reservations.remove(
                slotNumber
        );

        return true;
    }

    public boolean parkVehicle(Vehicle vehicle)
    {
        if(vehicleMap.containsKey(
                vehicle.getVehicleNumber()))
        {
            return false;
        }

        Reservation reservation =
                findReservation(
                        vehicle.getVehicleNumber()
                );

        if(reservation != null)
        {
            return parkReserved(
                    vehicle,
                    reservation
            );
        }

        return parkNormally(vehicle);
    }

    public ParkingHistory getHistory()
    {
        return history;
    }

    public boolean reserveSlot(
            String vehicleNumber,
            int slotNumber)
    {
        if(slotNumber < 1 ||
                slotNumber > slots.length)
        {
            return false;
        }

        if(slots[slotNumber - 1]
                .isOccupied())
        {
            return false;
        }

        if(reservations.containsKey(
                slotNumber))
        {
            return false;
        }

        if(vehicleExists(vehicleNumber))
        {
            return false;
        }
        for(Reservation reservation :
                reservations.values())
        {
            if(reservation.getVehicleNumber()
                    .equals(vehicleNumber))
            {
                return false;
            }
        }

        reservations.put(
                slotNumber,
                new Reservation(
                        "R" + reservationCounter++,
                        vehicleNumber,
                        slotNumber
                )
        );

        return true;
    }

    public Reservation getReservation(
            int slotNumber)
    {
        return reservations.get(
                slotNumber
        );
    }

    public Reservation findReservation(
            String vehicleNumber)
    {
        for(Reservation reservation :
                reservations.values())
        {
            if(reservation.getVehicleNumber()
                    .equals(vehicleNumber))
            {
                return reservation;
            }
        }

        return null;
    }

    public Reservation findReservation(
            String reservationId,
            String vehicleNumber)
    {
        Reservation reservation =
                findReservation(
                        vehicleNumber
                );

        if(reservation == null)
        {
            return null;
        }

        if(!reservation.getReservationId()
                .equals(reservationId))
        {
            return null;
        }

        return reservation;
    }

    public String getReservationDetails(
            String vehicleNumber)
    {
        Reservation reservation =
                findReservation(vehicleNumber);

        if(reservation == null)
        {
            return "No Reservation Found";
        }

        return "Vehicle Number : "
                + reservation.getVehicleNumber()

                + "\nReserved Slot : "
                + reservation.getSlotNumber();
    }

    public String getReservationReceipt(
            String vehicleNumber)
    {
        Reservation reservation =
                findReservation(vehicleNumber);

        if(reservation == null)
        {
            return "Reservation Failed";
        }

        return "Reservation Successful"

                + "\n\nReservation ID : "
                + reservation.getReservationId()

                + "\nVehicle Number : "
                + reservation.getVehicleNumber()

                + "\nSlot Number : "
                + reservation.getSlotNumber()

                + "\n\nPlease keep your Reservation ID safe.";
    }

    public boolean cancelReservation(int slotNumber)
    {
        if(!reservations.containsKey(slotNumber))
        {
            return false;
        }

        reservations.remove(slotNumber);

        return true;
    }

    public boolean cancelReservation(
            String vehicleNumber)
    {
        Reservation reservation =
                findReservation(
                        vehicleNumber
                );

        if(reservation == null)
        {
            return false;
        }

        reservations.remove(
                reservation.getSlotNumber()
        );

        return true;
    }

    public boolean cancelReservation(
            String reservationId,
            String vehicleNumber)
    {
        Reservation reservation =
                findReservation(
                        vehicleNumber
                );

        if(reservation == null)
        {
            return false;
        }

        if(!reservation.getReservationId()
                .equals(reservationId))
        {
            return false;
        }

        reservations.remove(
                reservation.getSlotNumber()
        );

        return true;
    }

    public String getReservationReport()
    {
        if(reservations.isEmpty())
        {
            return "No Reservations Found";
        }

        StringBuilder report =
                new StringBuilder();

        report.append(
                "===== RESERVED SLOTS =====\n\n"
        );

        for(Reservation reservation :
                reservations.values())
        {
            report.append("Slot ");

            report.append(
                    reservation.getSlotNumber()
            );

            report.append(" : ");

            report.append(
                    reservation.getVehicleNumber()
            );

            report.append("\n");
        }

        return report.toString();
    }

    public void displayParkingStatus()
    {
        for(ParkingSlot slot : slots)
        {
            if(slot.isOccupied())
            {
                System.out.println(
                        "Slot "
                                + slot.getSlotNumber()
                                + " : Occupied"
                );
            }
            else
            {
                System.out.println(
                        "Slot "
                                + slot.getSlotNumber()
                                + " : Available"
                );
            }
        }
    }

    public ParkingInfo searchVehicle(String vehicleNumber)
    {
        return vehicleMap.get(vehicleNumber);
    }

    public ParkingInfo getParkingInfoBySlot(int slotNumber)
    {
        for(ParkingInfo info : vehicleMap.values())
        {
            if(info.getSlotNumber() == slotNumber)
            {
                return info;
            }
        }

        return null;
    }

    public boolean vehicleExists(String vehicleNumber)
    {
        return vehicleMap.containsKey(
                vehicleNumber
        );
    }

    public boolean removeVehicle(String vehicleNumber)
    {
        ParkingInfo info =
                vehicleMap.get(vehicleNumber);

        if(info == null)
        {
            return false;
        }

        int slotNumber =
                info.getSlotNumber();

        slots[slotNumber - 1]
                .removeVehicle();

        ParkingRecord record =
                new ParkingRecord(
                        info.getVehicle()
                                .getVehicleNumber(),

                        info.getVehicle()
                                .getOwnerName(),

                        slotNumber
                );

        history.addRecord(record);

        vehicleMap.remove(vehicleNumber);

        if(!waitingQueue.isEmpty())
        {
            Vehicle nextVehicle =
                    waitingQueue.poll();

            slots[slotNumber - 1]
                    .parkVehicle(nextVehicle);

            vehicleMap.put(
                    nextVehicle.getVehicleNumber(),
                    new ParkingInfo(
                            nextVehicle,
                            slotNumber
                    )
            );
        }

        return true;
    }
}
