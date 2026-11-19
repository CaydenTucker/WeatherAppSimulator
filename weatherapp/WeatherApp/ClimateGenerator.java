/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package WeatherApp;


import java.time.LocalDateTime;
import java.util.Random;

public class ClimateGenerator {
    private final Random random; // psuedo random generator for weather
    private double baseTemperature; // baseline average temperature in Cels
    private double temperatureVar; // stores temp variation caused by changes
    private double currentTrend; // stores the gradual temperature trend that changes over time
    
    // --- consturctor to initialize the ClimateGenerator object --- //
    public ClimateGenerator(double baseTemperature, double temperatureVar) {
        this.random = new Random(); //creates new random object
        this.baseTemperature = baseTemperature; // assigns the provided base temp to the class var
        this.temperatureVar = temperatureVar; // assigns provided temp to the class var
        this.currentTrend = 0.0; // initializes the current temp trend to zero
    }
    
    // --- Generates the next weather state using the current time
    // --- previous weather conditions and the specified location
    public WeatherModel generateNextState(LocalDateTime currentTime, WeatherModel previousState, String location){
        int hour = currentTime.getHour(); //grabs current hour from LocalDateTime 
        int month = currentTime.getMonthValue(); // grabs current month as a number from 1 to 12
        
        // Calculates the seasonal temp var
        // uses month to determine the position in the yearly season cycle
        // temperatureVar controlls how strong the seasonal temp 
        double seasonalOffset = Math.sin((month - 3) * (2.0 * Math.PI / 12.0)) * temperatureVar;
        
        // Calculates the daily temp var using cosine
        // the month determins the position in the yearly seasonal cycle
        // the value 5.0 controlss the strength of the daily temp var
        double diurnalOffset = 5.0 * Math.cos((hour - 15.0) * (2.0 * Math.PI / 24.0));
        
        currentTrend += (random.nextDouble() - 0.5) * 0.4; // updates the temp trend using small random change
        currentTrend = Math.max(-3.0, Math.min(3.0, currentTrend)); // restricts the temp trend between -3.0 to 3.0
        
        // Calculates the target temp by combining the given variables 
        double targetTemp = baseTemperature + seasonalOffset + diurnalOffset + currentTrend;
        
        // --- provides transition from previouse state if available --- \\\
        double finalTemp = targetTemp;
        
        // sets given variable a certain value
        double prevHumidity = 65.0; 
        double prevWind = 12.0;
        double prevPressure = 1013.25;
        double accumulatedRain = 0.0;
        
        // Checks whether a prev weather state has been provided
        if (previousState != null){
            finalTemp = (previousState.getTemperatureCels() * 0.7) + (targetTemp * 0.3);
            prevHumidity = previousState.getHumidityPercentage();
            prevWind = previousState.getWindSpeed();
            prevPressure = previousState.getAirPressure();
        }
        
        // adjust humidity with slight inverse response
        double humidityDrift = (random.nextDouble() - 0.5) * 4.0 - (finalTemp - targetTemp) * 0.5;
        double finalHumidity = clamp(prevHumidity + humidityDrift, 15.0, 98.0);
        
        // simulates wind speed with a given 8% chance of wind gust 
        double windDrift = (random.nextDouble() - 0.5) * 4.0 - (finalTemp - targetTemp) * 0.3;
        if (random.nextDouble() < 0.08){
            windDrift += (random.nextDouble() * 15.0);
        }
        double finalWind = clamp(prevWind + windDrift, 0.0, 90.0);
        
        // drifts pressure around typical sea-level norms
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
        
        return new WeatherModel(currentTime, finalTemp, finalHumidity, finalWind, finalPressure, accumulatedRain, condition, location);
    }
    
    /* Determines the weather condition based on temperature, humidity,
    atmospheric pressure and wind speed*/
    private WeatherModel.Condition evaluateCondition(double temp, double humidity, double pressure, double wind){
        
        // if statements to check values to set condition
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
    
    // sets/clamps generated values within physical limits
    private double clamp(double val, double min, double max){
        return Math.max(min, Math.min(max, val));
    }
    
    // --- update base temperature used in future calculations 
    public void setBaseTemperature(double baseTemperature){
        this.baseTemperature = baseTemperature;
    }
    
    // --- returns the base temperature 
    public double getBaseTemperature(){
        return baseTemperature;
    }
}
