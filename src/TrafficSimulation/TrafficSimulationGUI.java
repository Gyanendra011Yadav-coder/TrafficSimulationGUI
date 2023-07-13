package TrafficSimulation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TrafficSimulationGUI extends JFrame {
    private JPanel timeStampPanel, trafficLightPanel, carPositionPanel;
    private JLabel timeStampLabel, intersection1Label, intersection2Label, intersection3Label;
    private ArrayList<JLabel> carLabels = new ArrayList<>();

    // Simulation variables
    private boolean running = false;
    private int simulationSpeed = 1000; // 1 second interval
    private double distanceBetweenLights = 1000; // meters
    private String distanceUnit = "meters";
    private String speedUnit = "km/hour";
    private ArrayList<Double> carSpeeds = new ArrayList<>();

    public TrafficSimulationGUI() {

        // Create the GUI components
        timeStampPanel = new JPanel();
        trafficLightPanel = new JPanel();
        carPositionPanel = new JPanel();
        timeStampLabel = new JLabel();
        intersection1Label = new JLabel("Intersection 1: ");
        intersection2Label = new JLabel("Intersection 2: ");
        intersection3Label = new JLabel("Intersection 3: ");

        // Load car images
        ImageIcon carIcon1 = new ImageIcon("car1.png");
//        ImageIcon carIcon2 = new ImageIcon("car2.png");
//        ImageIcon carIcon3 = new ImageIcon("car3.png");

        // Create car labels and add them to car position panel
        JLabel carLabel1 = new JLabel(carIcon1);
        carLabel1.setBounds(10, carLabels.size() * 50, carIcon1.getIconWidth(), carIcon1.getIconHeight());

//        JLabel carLabel2 = new JLabel(carIcon2);
//        carLabel2.setBounds(20, 20, carIcon2.getIconWidth(), carIcon2.getIconHeight());
//        carPositionPanel.add(carLabel2);
//
//        JLabel carLabel3 = new JLabel(carIcon3);
//        carLabel3.setBounds(30, 30, carIcon3.getIconWidth(), carIcon3.getIconHeight());
//        carPositionPanel.add(carLabel3);

        // Load traffic light images
        ImageIcon trafficLightIcon1 = new ImageIcon("traffic_light1.png");
//        ImageIcon trafficLightIcon2 = new ImageIcon("traffic_light2.png");

        // Create traffic light labels and add them to traffic light panel
        JLabel trafficLightLabel1 = new JLabel(trafficLightIcon1);
        trafficLightLabel1.setBounds(10, 10, trafficLightIcon1.getIconWidth(), trafficLightIcon1.getIconHeight());
        trafficLightPanel.add(trafficLightLabel1);

//        JLabel trafficLightLabel2 = new JLabel(trafficLightIcon2);
//        trafficLightLabel2.setBounds(10, 30, trafficLightIcon2.getIconWidth(), trafficLightIcon2.getIconHeight());
//        trafficLightPanel.add(trafficLightLabel2);

        // Add the GUI components to the frame
        add(timeStampPanel, BorderLayout.NORTH);
        add(trafficLightPanel, BorderLayout.CENTER);
        add(carPositionPanel, BorderLayout.SOUTH);

        // Set the properties of the GUI components
        timeStampPanel.add(timeStampLabel);
        trafficLightPanel.setLayout(new GridLayout(1, 3));
        trafficLightPanel.add(intersection1Label);
        trafficLightPanel.add(intersection2Label);
        trafficLightPanel.add(intersection3Label);
        carPositionPanel.setLayout(new GridLayout(3, 1));

        // Add labels for the initial set of cars
        addCarLabel();
        addCarLabel();
        addCarLabel();

        // Set the properties of the frame
        setTitle("Traffic Simulation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create buttons to start, pause, stop, and continue the simulation
        JButton startButton = new JButton("Start");
        JButton pauseButton = new JButton("Pause");
        JButton stopButton = new JButton("Stop");
        JButton continueButton = new JButton("Continue");

        // Create a text field to get input for the number of cars
        JTextField numCarsField = new JTextField(10);
        numCarsField.setText("3");

        // Create a text field to get input for the number of intersections
        JTextField numIntersectionsField = new JTextField(10);
        numIntersectionsField.setText("3");

        // Create a button to add more cars to the simulation
        JButton addCarButton = new JButton("Add Car");
        addCarButton.addActionListener(e -> {
            addCarLabel();
        });

        // Create a button to add more intersections to the simulation
        JButton addIntersectionButton = new JButton("Add Intersection");
        addIntersectionButton.addActionListener(e -> {
            addIntersectionLabel();
        });

        // Add button listeners
        startButton.addActionListener(e -> {
            running = true;
            startSimulation();
        });

        pauseButton.addActionListener(e -> {
            running = false;
        });

        stopButton.addActionListener(e -> {
            running = false;
            resetSimulation();
        });

        continueButton.addActionListener(e -> {
            running = true;
        });

        // Add buttons and text fields to the frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(continueButton);
        buttonPanel.add(numCarsField);
        buttonPanel.add(addCarButton);
        buttonPanel.add(numIntersectionsField);
        buttonPanel.add(addIntersectionButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Add a label for a new car
    private void addCarLabel() {
        JLabel carLabel = new JLabel("Car " + (carLabels.size()+1) + ": ");
        carLabels.add(carLabel);
        carPositionPanel.add(carLabel);
        carSpeeds.add(60.0); // default speed is 60 km/hour
    }

    // Add a label for a new intersection
    private void addIntersectionLabel() {
        JLabel intersectionLabel = new JLabel("Intersection " + (trafficLightPanel.getComponentCount()+1) + ": ");
        trafficLightPanel.add(intersectionLabel);
    }

    // Start the simulation in a new thread
    private void startSimulation() {
        new Thread(() -> {
            while (running) {
                // Update the time stamp label
                SwingUtilities.invokeLater(() -> {
                    timeStampLabel.setText("Current Time: " + System.currentTimeMillis());
                });

                // Update the traffic light states
                // ...

                // Update the car positions
                for (int i = 0; i < carLabels.size(); i++) {
                    double distanceTraveled = carSpeeds.get(i) * (double)
                            simulationSpeed / 1000.0; // convert to seconds
                    JLabel carLabel = carLabels.get(i);
                    double currentPosition = carLabel.getX();
                    double newPosition = currentPosition + (distanceTraveled / distanceBetweenLights) * carPositionPanel.getWidth();
                    carLabel.setLocation((int) newPosition, carLabel.getY());
                }

                // Sleep for the specified simulation speed
                try {
                    Thread.sleep(simulationSpeed);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    // Reset the simulation to its initial state
    private void resetSimulation() {
        // Reset the car positions
        for (JLabel carLabel : carLabels) {
            carLabel.setLocation(0, carLabel.getY());
        }

        // Reset the time stamp label
        timeStampLabel.setText("Current Time: ");

        // Reset the traffic light states
        // ...
    }

    public static void main(String[] args) {
        TrafficSimulationGUI gui = new TrafficSimulationGUI();
        gui.setVisible(true);
    }
}