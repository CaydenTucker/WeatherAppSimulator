/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package WeatherApp;


import java.time.LocalDateTime;
import java.util.Random;

public class ClimateGenerator {
    private final Random random;
    private double baseTemperature;
    private double temperatureVar;
    private double currentTrend;
    
    public ClimateGenerator(double baseTemperature, double temperatureVar) {
        this.random = new Random();
        this.baseTemperature = baseTemperature;
        this.temperatureVar = temperatureVar;
        this.currentTrend = 0.0;
    }
}
