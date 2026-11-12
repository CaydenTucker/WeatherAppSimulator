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
    
    public WeatherModel generateNextState(LocalDateTime currentTime, WeatherModel previousState, String location){
        int hour = currentTime.getHour();
        int month = currentTime.getMonthValue();
        
        double seasonalOffset = Math.sin((month - 3) * (2.0 * Math.PI / 12.0)) * temperatureVar;
        
        double diurnalOffset = 5.0 * Math.cos((hour - 15.0) * (2.0 * Math.PI / 24.0));
        
        currentTrend += (random.nextDouble() - 0.5) * 0.4;
        currentTrend = Math.max(-3.0, Math.min(3.0, currentTrend));
        
        double targetTemp = baseTemperature + seasonalOffset + diurnalOffset + currentTrend;
        
        // --- provides transition from previouse state if available --- \\\
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
        
        double windDrift = (random.nextDouble() - 0.5) * 4.0 - (finalTemp - targetTemp) * 0.3;
        if (random.nextDouble() < 0.08){
            windDrift += (random.nextDouble() * 15.0);
        }
        double finalWind = clamp(prevWind + windDrift, 0.0, 90.0);
        
        double pressureDrift = (random.nextDouble() - 0.5) * 2.5;
        double finalPressure = clamp(prevPressure + pressureDrift, 950.0, 1055.0);
        
        // --- Sets the final values as the new condition --- \\\
        WeatherModel.Condition condition = evaluateCondition(finalTemp, finalHumidity, finalWind, finalPressure);
        
        if (condition == WeatherModel.Condition.RAINY){
            accumulatedRain = 0.5 + random.nextDouble() * 4.5;
        }
        else if (condition == WeatherModel.Condition.THUNDERSTORM){
             accumulatedRain = 3.0 + random.nextDouble() * 12.0;
        }
        else if (condition == WeatherModel.Condition.SNOWY){
            accumulatedRain = 0.2 + random.nextDouble() * 2.0;
        }
        
        return new WeatherModel(currentTime, finalTemp, finalHumidity, finalWind, finalPressure);
    }
    
    private WeatherModel.Condition evaluateCondition(double temp, double humidity, double pressure, double wind){
        if (pressure <  1000.0 && humidity > 82.0 && wind > 35.0){
            return WeatherModel.Condition.THUNDERSTORM;
        }
        if (humidity > 85.0 && temp <= 1.0) {
            return WeatherModel.Condition.SNOWY;
        }
        if (humidity > 78.0){
            return random.nextDouble() < 0.3 ? WeatherModel.Condition.FOGGY : WeatherModel.Condition.RAINY;
        }
        if (humidity > 60.0){
            return WeatherModel.Condition.PARTLY_CLOUDY;
        }
        if (humidity > 40.0){
            return WeatherModel.Condition.PARTLY_CLOUDY;
        }
        
        return WeatherModel.Condition.SUNNY;
    }
}
