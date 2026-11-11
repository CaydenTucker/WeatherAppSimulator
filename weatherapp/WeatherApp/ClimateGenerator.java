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
    
    public WeathermModel generateNextState(LocalDateTime currentTime, WeatherModel previousState, String location){
        int hour = currentTime.getHour();
        int month = currentTime.getMonthValue();
        
        double seasonalOffset = Math.sin((month - 3) * (2.0 * Math.PI / 12.0)) * temperatureVar;
        
        double diurnalOffset = 5.0 * Math.cos((hour - 15.0) * (2.0 * Math.PI / 24.0));
        
        currentTrend += (random.nextDouble() - 0.5) * 0.4;
        currentTrend = Math.max(-3.0, Math.min(3.0, currentTrend));
        
        double targetTemp = baseTemperature + seasonalOffset + diurnalOffset + currentTrend;
        
        // --- provides transition from previouse state if available ---
        double finalTemp = targetTemp;
        double prevHumidity = 65.0;
        double prevWind = 12.0;
        double prevPressure = 1013.25;
        double accumulatedRain = 0.0;
        
        if (previousState != null){
            finalTemp = (previousState.getTemperatureCels() * 0.7) + (targetTemp * 0.3);
            prevHumidity = previousState.getHumidityPercentage();
            prevWind = previousState.getWindSpeed();
            prevPressure = previousState.getAirPressure();
        }
        
        double humidityDrift = (random.nextDouble() - 0.5) * 4.0 - (finalTemp - targetTemp) * 0.5;
        double finalHumidity = clamp(prevHumidity + humidityDrift, 15.0, 98.0);
        
        
    }
}
