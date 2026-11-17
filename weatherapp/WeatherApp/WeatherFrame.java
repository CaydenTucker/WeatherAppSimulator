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
        setDefaultCloseOperation(JFRAME.EXIT_ON_CLOSE);
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
        
        String[] locations = {"Seattle, WA", "Miami, FL", "Denver, CO"};
        cmbLocations = new JComboBox<>(locations);
        cmbLocations.addActionListeners(e -> {
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
        btnReport.addActionListener(e -> showStatisticsDialog());
        
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
        
        lblLocationTime = new JLabel("Condition", JLabel.CENTER);
        lblLocationTime.setFont(new Font("Arial", Font.BOLD, 16));
        lblLocationTime.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lblTempMain = new JLabel("--°C", JLabel.CENTER);
        lblTempMain.setFont(new Font("Arial", Font.BOLD, 48));
        lblTempMain.setForeground(Color.DARK_GRAY);
        lblTempMain.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
