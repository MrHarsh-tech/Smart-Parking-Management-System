package dsa;

import model.ParkingRecord;

public class ParkingHistory {

    private ParkingRecord head;

    public ParkingHistory()
    {
        head = null;
    }

    public int countRecords()
    {
        int count = 0;

        ParkingRecord current = head;

        while(current != null)
        {
            count++;

            current = current.getNext();
        }

        return count;
    }

    public String getHistoryText()
    {
        StringBuilder historyText =
                new StringBuilder();

        ParkingRecord current = head;

        while(current != null)
        {
            historyText.append(
                    current.getVehicleNumber()
            );

            historyText.append(" | ");

            historyText.append(
                    current.getOwnerName()
            );

            historyText.append(" | Slot ");

            historyText.append(
                    current.getSlotNumber()
            );

            historyText.append("\n");

            current = current.getNext();
        }

        if(historyText.length() == 0)
        {
            return "No History Available";
        }

        return historyText.toString();
    }

    public void addRecord(
            ParkingRecord record)
    {
        if(head == null)
        {
            head = record;
            return;
        }

        ParkingRecord current = head;

        while(current.getNext() != null)
        {
            current = current.getNext();
        }

        current.setNext(record);
    }

    public void displayHistory()
    {
        ParkingRecord current = head;

        while(current != null)
        {
            System.out.println(
                    current.getVehicleNumber()
                            + " | "
                            + current.getOwnerName()
                            + " | Slot "
                            + current.getSlotNumber()
            );

            current = current.getNext();
        }
    }
}