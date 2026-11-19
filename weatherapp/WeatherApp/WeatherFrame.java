/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package WeatherApp;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author catuc
 */
public class WeatherFrame extends JFrame implements SimEngine.SimListener{
    private final SimEngine simEngine;
    
    private JLabel lblLocationTime;
    private JLabel lblConditionTitle;
    private JLabel lblTempMain;
    private JLabel lblDetailsHumidity;
    private JLabel lblDetailsWind;
    private JLabel lblDetailsPressure;
    private JLabel lblDetailsRain;
    private JTextArea txtEventLog;
    private JComboBox<String> cmbLocations;
    private Timer autoPlayTimer;
    
    public WeatherFrame(){
        this.simEngine = new SimEngine("Seattle, WA", 12.0);
        this.simEngine.addSimListener(this);
        
        setTitle("Simulated Weather Station");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        updateDisplay(simEngine.getCurrentState());
    }
    
    private void initComponents(){
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(new Color(45, 52, 54));
        
        JLabel lblSelectLoc = new JLabel("Location:");
        lblSelectLoc.setForeground(Color.WHITE);
        
        String[] locations = {"Seattle, WA", "Miami, FL", "Denver, CO", "Southside, AL\", \"Baton Rouge, LA"};
        cmbLocations = new JComboBox<>(locations);
        cmbLocations.addActionListener(e -> {
           String loc = (String) cmbLocations.getSelectedItem();
           double baseTemp = getBaseTempForLocation(loc);
           simEngine.resetSimulation(loc, baseTemp);
        });
        
        JButton btnStep1H = createStyledButton("Advance 1h");
        btnStep1H.addActionListener(e -> simEngine.advanceTime(1));
        
        JButton btnStep24H = createStyledButton("Advance 24h");
        btnStep1H.addActionListener(e -> simEngine.advanceTime(24));
        
        JButton btnPlayPause = createStyledButton("Play / Pause");
        btnPlayPause.addActionListener(e -> toggleAutoPlay());

        JButton btnReport = createStyledButton("View Stats");
        btnReport.addActionListener(e -> showStatDialog());
        
        topPanel.add(lblSelectLoc);
        topPanel.add(cmbLocations);
        topPanel.add(btnStep1H);
        topPanel.add(btnStep24H);
        topPanel.add(btnPlayPause);
        topPanel.add(btnReport);
        
        add(topPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        centerPanel.setBackground(new Color(223, 230, 233));
        
        lblLocationTime = new JLabel("Location & Time", JLabel.CENTER);
        lblLocationTime.setFont(new Font("Arial", Font.BOLD, 18));
        lblLocationTime.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lblConditionTitle = new JLabel("Condition", JLabel.CENTER);
        lblConditionTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblConditionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lblTempMain = new JLabel("--°C", JLabel.CENTER);
        lblTempMain.setFont(new Font("Arial", Font.BOLD, 48));
        lblTempMain.setForeground(Color.DARK_GRAY);
        lblTempMain.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel metricsGrid = new JPanel(new GridLayout(2, 2, 15, 15));
        metricsGrid.setBorder(new EmptyBorder(20, 50, 20, 50));
        metricsGrid.setBackground(new Color(223, 230, 233));
        
        lblDetailsHumidity = createMetricLabel("Humidity: --%");
        lblDetailsWind = createMetricLabel("Wind Speed: -- km/h");
        lblDetailsPressure = createMetricLabel("Pressure: -- hPa");
        lblDetailsRain = createMetricLabel("Precipitation: -- mm");
        
        metricsGrid.add(lblDetailsHumidity);
        metricsGrid.add(lblDetailsWind);
        metricsGrid.add(lblDetailsPressure);
        metricsGrid.add(lblDetailsRain);
        
        centerPanel.add(lblDetailsHumidity);
        centerPanel.add(Box.createVerticalStrut(10));
        lblConditionTitle.setForeground(new Color(99, 110, 114));
        centerPanel.add(lblConditionTitle);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(lblTempMain);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(metricsGrid);
        
        add (centerPanel, BorderLayout.CENTER);
        
        txtEventLog = new JTextArea(6, 40);
        txtEventLog.setEditable(false);
        txtEventLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(txtEventLog);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Simulation Event Log"));
        
        add(scrollPane, BorderLayout.SOUTH);
        
        autoPlayTimer = new Timer(1000, e -> simEngine.advanceTime(1));
    }
    
    private JButton createStyledButton(String text){
        JButton btn = new JButton(text);
        btn.setBackground(new Color(9, 132, 227));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        return btn;
    }
    
    private JLabel createMetricLabel(String text){
        JLabel lbl = new JLabel(text, JLabel.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setOpaque(true);
        lbl.setBackground(Color.WHITE);
        lbl.setBorder(BorderFactory.createLineBorder(new Color(178, 190, 195)));
        return lbl;
    }
    
    private double getBaseTempForLocation(String Location){
        switch (Location){
            case "Miami, FL": return 26.0;
            case "Denver, CO": return 10.0;
            case "Southside, AL": return 2.0;
            default: return 12.0;
        }
    }
    
    private void toggleAutoPlay(){
        if (autoPlayTimer.isRunning()) {
            autoPlayTimer.stop();
            txtEventLog.append("[System] Auto-play simulation paused.\n");
        } else {
            autoPlayTimer.start();
            txtEventLog.append("[System] Auto-play simulation started.\n");
        }
    }
    
    private void updateDisplay(WeatherModel model){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy - HH:mm");
        lblLocationTime.setText(model.getLocationName() + " | " + model.getTimestamp().format(formatter));
        lblConditionTitle.setText(model.getCondition().getDisplayName() + " - " + model.getCondition().getDescription());
        lblTempMain.setText(String.format("%.1f°C (%.1f°F)", model.getTemperatureCels(), model.getTemperatureFahr()));
        
        lblDetailsHumidity.setText(String.format("Humidity: %.1f%%", model.getHumidityPercentage()));
        lblDetailsWind.setText(String.format("Wind Speed: %.1f km/h", model.getWindSpeed()));
        lblDetailsPressure.setText(String.format("Pressure: %.1f hPa", model.getAirPressure()));
        lblDetailsRain.setText(String.format("Precipitation: %.2f mm", model.getPrecipitation()));
    }
    
    private void showStatDialog(){
        StatAnalyzer analyzer = new StatAnalyzer(simEngine.getWeatherHistory());
        JOptionPane.showMessageDialog(this, analyzer.generateFullReport(), "Simulation Analytics", JOptionPane.INFORMATION_MESSAGE);
    }
    @Override
    public void onTick(WeatherModel newState){
        updateDisplay(newState);
    }
    
    @Override 
    public void onEventTriggered(String eventDescription){
        txtEventLog.append("[" + simEngine.getCurrentSimTime().toLocalTime() + "]" + eventDescription + "\n");
        txtEventLog.setCaretPosition(txtEventLog.getDocument().getLength());
    }
}
