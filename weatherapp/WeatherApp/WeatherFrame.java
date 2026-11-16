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
    
    private JLabel lblLocataionTime;
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
}
