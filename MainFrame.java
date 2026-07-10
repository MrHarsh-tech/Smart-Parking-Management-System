package gui;

import javax.swing.*;
import java.awt.*;
import service.ParkingManager;
import model.Vehicle;
import model.ParkingInfo;
import model.Reservation;


public class MainFrame {

    private JFrame frame;
    private ParkingManager manager;
    private JButton[] slotButtons;
    private JLabel statsLabel;

    public MainFrame()
    {

        manager = new ParkingManager(20);

        frame = new JFrame(
                "Smart Parking Management System"
        );

        ImageIcon logo =
                new ImageIcon("logo.jpeg");

        frame.setIconImage(
                logo.getImage()
        );


        frame.setSize(700, 600);

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        frame.setLocationRelativeTo(null);

        frame.setLayout(
                new BorderLayout()
        );
        JPanel leftPanel =
                new JPanel();

        JPanel rightPanel =
                new JPanel();
        leftPanel.setLayout(
                new GridLayout(
                        6,
                        1,
                        10,
                        10
                )
        );
        rightPanel.setLayout(
                new BorderLayout()
        );

        statsLabel =
                new JLabel();

        statsLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        statsLabel.setText(
                "Total: 20 | Occupied: 0 | Available: 20"
        );

        statsLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );
        JPanel slotPanel =
                new JPanel(
                        new GridLayout(
                                4,
                                5,
                                5,
                                5
                        )
                );

        slotButtons =
                new JButton[20];
        for(int i = 0; i < 20; i++)
        {
            final int slotNumber = i + 1;

            slotButtons[i] =
                    new JButton(
                            "P" + slotNumber
                    );

            slotButtons[i].addActionListener(e -> {

                slotClicked(slotNumber);

            });

            slotPanel.add(
                    slotButtons[i]
            );
        }

        rightPanel.add(
                statsLabel,
                BorderLayout.NORTH
        );

        rightPanel.add(
                slotPanel,
                BorderLayout.CENTER
        );



        JLabel title =
                new JLabel(
                        "SMART PARKING MANAGEMENT SYSTEM",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        24
                )
        );

        JButton entryButton =
                new JButton("Vehicle Entry");
        entryButton.addActionListener(e -> {

            String vehicleNumber =
                    getInput(
                            "Enter Vehicle Number"
                    );

            if(vehicleNumber == null)
            {
                return;
            }

            if(manager.vehicleExists(
                    vehicleNumber))
            {
                JOptionPane.showMessageDialog(
                        frame,
                        "Vehicle Already Parked"
                );

                return;
            }

            String ownerName =
                    getInput(
                            "Enter Owner Name"
                    );

            if(ownerName == null)
            {
                return;
            }

            String vehicleType =
                    getInput(
                            "Enter Vehicle Type"
                    );

            if(vehicleType == null)
            {
                return;
            }

            Vehicle vehicle =
                    new Vehicle(
                            vehicleNumber,
                            ownerName,
                            vehicleType
                    );

            boolean parked =
                    manager.parkVehicle(vehicle);

            if(parked)
            {
                JOptionPane.showMessageDialog(
                        frame,
                        "Vehicle Parked Successfully"

                );
                refreshDashboard();
            }
            else
            {
                JOptionPane.showMessageDialog(
                        frame,
                        "Parking Full. Added To Waiting Queue."
                );
            }
        });


        JButton exitButton =
                new JButton("Vehicle Exit");
        exitButton.addActionListener(e -> {

            String vehicleNumber =
                    getInput(
                            "Enter Vehicle Number"
                    );

            if(vehicleNumber == null)
            {
                return;
            }

            boolean removed =
                    manager.removeVehicle(
                            vehicleNumber
                    );

            if(removed)
            {
                JOptionPane.showMessageDialog(
                        frame,
                        "Vehicle Removed Successfully"
                );
                refreshDashboard();
            }
            else
            {
                JOptionPane.showMessageDialog(
                        frame,
                        "Vehicle Not Found"
                );
            }
        });


        JButton searchButton =
                new JButton("Search Vehicle");
        searchButton.addActionListener(e -> {

            String vehicleNumber =
                    getInput(
                            "Enter Vehicle Number"
                    );

            if(vehicleNumber == null)
            {
                return;
            }

            ParkingInfo info =
                    manager.searchVehicle(
                            vehicleNumber
                    );

            if(info == null)
            {
                JOptionPane.showMessageDialog(
                        frame,
                        "Vehicle Not Found"
                );

                return;
            }

            String message =
                    "Vehicle Number : "
                            + info.getVehicle()
                            .getVehicleNumber()

                            + "\nOwner Name : "
                            + info.getVehicle()
                            .getOwnerName()

                            + "\nVehicle Type : "
                            + info.getVehicle()
                            .getVehicleType()

                            + "\nSlot Number : "
                            + info.getSlotNumber();

            JOptionPane.showMessageDialog(
                    frame,
                    message
            );
        });

        JButton reservationButton =
                new JButton("Reservations");
        reservationButton.addActionListener(e -> {

            String[] options = {
                    "Reserve Slot",
                    "Search My Reservation",
                    "Cancel My Reservation"
            };

            int choice =
                    JOptionPane.showOptionDialog(
                            frame,
                            "Choose an Option",
                            "Reservation Management",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            options,
                            options[0]
                    );

            if(choice == 0)
            {
                reserveSlot();
            }
            else if(choice == 1)
            {
                searchReservation();
            }
            else if(choice == 2)
            {
                cancelReservation();
            }

        });

        JButton statusButton =
                new JButton("Parking Status");
        statusButton.addActionListener(e -> {

            String message =
                    "Total Slots : "
                            + manager.getSlots().length

                            + "\nOccupied Slots : "
                            + manager.getOccupiedSlotCount()

                            + "\nAvailable Slots : "
                            + manager.getAvailableSlotCount()

                            + "\nWaiting Vehicles : "
                            + manager.getWaitingQueueSize();

            JOptionPane.showMessageDialog(
                    frame,
                    message
            );
        });

        JButton reportButton =
                new JButton("Reports");
        reportButton.addActionListener(e -> {

            String message =
                    "===== REPORT ====="

                            + "\n\nTotal Vehicles Served : "
                            + manager.getTotalVehiclesServed()

                            + "\nOccupied Slots : "
                            + manager.getOccupiedSlotCount()

                            + "\nReserved Slots : "
                            + manager.getReservedSlotCount()

                            + "\nAvailable Slots : "
                            + manager.getAvailableSlotCount()

                            + "\nWaiting Vehicles : "
                            + manager.getWaitingQueueSize();

            JOptionPane.showMessageDialog(
                    frame,
                    message
            );
        });

        JButton historyButton =
                new JButton("Parking History");
        historyButton.addActionListener(e -> {

            String historyText =
                    manager.getHistory()
                            .getHistoryText();

            JOptionPane.showMessageDialog(
                    frame,
                    historyText
            );
        });

        leftPanel.add(entryButton);

        leftPanel.add(exitButton);

        leftPanel.add(searchButton);

        leftPanel.add(reservationButton);

        leftPanel.add(historyButton);

        leftPanel.add(reportButton);
        frame.add(
                title,
                BorderLayout.NORTH
        );

        leftPanel.setPreferredSize(
                new Dimension(
                        180,
                        0
                )
        );

        frame.add(
                leftPanel,
                BorderLayout.WEST
        );

        frame.add(
                rightPanel,
                BorderLayout.CENTER
        );
        refreshDashboard();
        frame.setVisible(true);
    }

    private void slotClicked(int slotNumber)
    {
        if(manager.getSlots()[slotNumber - 1].isOccupied())
        {
            ParkingInfo info =
                    manager.getParkingInfoBySlot(
                            slotNumber
                    );

            String message =
                    "SLOT " + slotNumber

                            + "\n\nStatus : Occupied"

                            + "\n\nVehicle Number : "
                            + info.getVehicle().getVehicleNumber()

                            + "\nOwner Name : "
                            + info.getVehicle().getOwnerName()

                            + "\nVehicle Type : "
                            + info.getVehicle().getVehicleType();

            JOptionPane.showMessageDialog(
                    frame,
                    message
            );

            return;
        }

        if(manager.isSlotReserved(slotNumber))
        {
            Reservation reservation =
                    manager.getReservation(
                            slotNumber
                    );

            int choice =
                    JOptionPane.showConfirmDialog(
                            frame,
                            "SLOT " + slotNumber

                                    + "\n\nStatus : Reserved"

                                    + "\nVehicle Number : "
                                    + reservation.getVehicleNumber()

                                    + "\n\nCancel Reservation?",
                            "Reserved Slot",
                            JOptionPane.YES_NO_OPTION
                    );

            if(choice == JOptionPane.YES_OPTION)
            {
                String reservationId =
                        getInput(
                                "Enter Reservation ID to confirm cancellation"
                        );

                if(reservationId == null)
                {
                    return;
                }

                boolean cancelled =
                        manager.cancelReservation(
                                reservationId,
                                reservation.getVehicleNumber()
                        );

                if(cancelled)
                {
                    refreshDashboard();

                    JOptionPane.showMessageDialog(
                            frame,
                            "Reservation Cancelled Successfully"
                    );
                }
                else
                {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Invalid Reservation ID"
                    );
                }
            }

            return;
        }

        int choice =
                JOptionPane.showConfirmDialog(
                        frame,
                        "SLOT " + slotNumber

                                + "\n\nStatus : Available"

                                + "\n\nReserve this slot?",
                        "Available Slot",
                        JOptionPane.YES_NO_OPTION
                );

        if(choice != JOptionPane.YES_OPTION)
        {
            return;
        }

        String vehicleNumber =
                getInput(
                        "Enter Vehicle Number"
                );

        if(vehicleNumber == null)
        {
            return;
        }

        boolean reserved =
                manager.reserveSlot(
                        vehicleNumber,
                        slotNumber
                );

        if(reserved)
        {
            refreshDashboard();

            JOptionPane.showMessageDialog(
                    frame,
                    manager.getReservationReceipt(
                            vehicleNumber
                    )
            );
        }
        else
        {
            JOptionPane.showMessageDialog(
                    frame,
                    "Reservation Failed"
            );
        }
    }

    private void refreshDashboard()
    {
        statsLabel.setText(
                "Total: "
                        + manager.getSlots().length

                        + " | Occupied: "
                        + manager.getOccupiedSlotCount()

                        + " | Reserved: "
                        + manager.getReservedSlotCount()

                        + " | Available: "
                        + manager.getAvailableSlotCount()

                        + " | Waiting: "
                        + manager.getWaitingQueueSize()
        );

        for(int i = 0; i < manager.getSlots().length; i++)
        {
            int slotNumber = i + 1;

            if(manager.getSlots()[i].isOccupied())
            {
                slotButtons[i].setBackground(
                        Color.RED
                );
            }
            else if(manager.isSlotReserved(
                    slotNumber))
            {
                slotButtons[i].setBackground(
                        Color.ORANGE
                );
            }
            else
            {
                slotButtons[i].setBackground(
                        Color.GREEN
                );
            }

            slotButtons[i].setOpaque(true);

            slotButtons[i].setBorderPainted(false);
        }
    }

    private void reserveSlot()
    {
        String vehicleNumber =
                getInput(
                        "Enter Vehicle Number"
                );

        if(vehicleNumber == null)
        {
            return;
        }

        String slotText =
                getInput(
                        "Enter Slot Number"
                );

        if(slotText == null)
        {
            return;
        }

        int slotNumber;

        try
        {
            slotNumber =
                    Integer.parseInt(slotText);
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(
                    frame,
                    "Invalid Slot Number"
            );
            return;
        }

        boolean reserved =
                manager.reserveSlot(
                        vehicleNumber,
                        slotNumber
                );

        if(reserved)
        {
            JOptionPane.showMessageDialog(
                    frame,
                    manager.getReservationReceipt(
                            vehicleNumber
                    )
            );

            refreshDashboard();
        }
        else
        {
            JOptionPane.showMessageDialog(
                    frame,
                    "Reservation Failed"
            );
        }
    }

    private void searchReservation()
    {
        String vehicleNumber =
                getInput(
                        "Enter Vehicle Number"
                );

        if(vehicleNumber == null)
        {
            return;
        }

        JOptionPane.showMessageDialog(
                frame,
                manager.getReservationDetails(
                        vehicleNumber
                )
        );
    }

    private void cancelReservation()
    {

        String reservationId =
                getInput(
                        "Enter Reservation ID"
                );

        if(reservationId == null)
        {
            return;
        }

        String vehicleNumber =
                getInput(
                        "Enter Vehicle Number"
                );

        if(vehicleNumber == null)
        {
            return;
        }

        Reservation reservation =
                manager.findReservation(
                        reservationId,
                        vehicleNumber
                );

        if(reservation == null)
        {
            JOptionPane.showMessageDialog(
                    frame,
                    "Invalid Reservation ID or Vehicle Number"
            );

            return;
        }

        String details =
                "Reservation ID : "
                        + reservation.getReservationId()

                        + "\nVehicle Number : "
                        + reservation.getVehicleNumber()

                        + "\nReserved Slot : "
                        + reservation.getSlotNumber();

        int confirm =
                JOptionPane.showConfirmDialog(
                        frame,
                        details
                                + "\n\nCancel this reservation?",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION
                );

        if(confirm == JOptionPane.YES_OPTION)
        {
            boolean cancelled =
                    manager.cancelReservation(
                            reservationId,
                            vehicleNumber
                    );

            if(cancelled)
            {
                refreshDashboard();

                JOptionPane.showMessageDialog(
                        frame,
                        "Reservation Cancelled Successfully"
                );
            }
            else
            {
                JOptionPane.showMessageDialog(
                        frame,
                        "Invalid Reservation ID or Vehicle Number"
                );
            }
        }
    }

    private void viewReservations()
    {
        JOptionPane.showMessageDialog(
                frame,
                manager.getReservationReport()
        );
    }

    private String getInput(String message)
    {
        String input =
                JOptionPane.showInputDialog(
                        frame,
                        message
                );

        if(input == null ||
                input.trim().isEmpty())
        {
            return null;
        }

        return input.trim();
    }

}
