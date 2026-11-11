/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package WeatherApp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
/**
 *
 * @author catuc
 */
public class WeatherModel implements Serializable, Comparable<WeatherModel>{
    
    private static final long serialVersionUID = 1L;
    
    public enum Condition{
        SUNNY("Sunny", "Clear skies with high solar radiation"),
        PARTLY_CLOUDY("Partly Cloudy", "Scattered with a chance of rain"),
        CLOUDY("Overcast", "Dense cloud cover blocking the sun"),
        RAINY("Rainy", "Continous steady precipitation"),
        THUNDERSTORM("Thunderstorm", "Severe electrical storm with heavy rainfall"),
        SNOWY("Snowy", "Sub-zero temperatures with crystalline precipatation"),
        FOGGY("Foggy", "Low-lying stratus clouds reducing visibility");
        
        private final String displayName;
        private final String description;
        
        Condition(String displayName, String description){
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() {return displayName;}
        public String getDescription() {return description;}
    }
    
    private LocalDateTime timestamp;
    private double temperatureCels;
    private double humidityPercentage;
    private double windSpeed;
    private double airPressure;
    private double precipitation;
    private Condition condition;
    private String location;
    
    public WeatherModel(LocalDateTime timestamp, double temperatureCels, double humidityPercentage,
            double windSpeed, double airPressure, double precipitation, 
            Condition condition, String locationName){
        this.timestamp = timestamp;
        this.temperatureCels = temperatureCels;
        this.humidityPercentage = clamp(humidityPercentage, 0.0, 100.0);
        this.windSpeed = Math.max(0.0, windSpeed);
        this.airPressure = clamp(airPressure, 870.0, 1085.0);
        this.precipitation = Math.max(0.0, precipitation);
        this.condition = condition;
        this.location = location;
        
    }
    
    // --- Temperature Getters & Setters (Celsius & Fahrenheit) ---
    
    public double getTemperatureCels(){
        return temperatureCels;
    }
    
    public void setTemperatureCels(double temperatureCels){
        this.temperatureCels = temperatureCels;
    }
    
    public double getTemperatureFahr(){
        return (temperatureCels * 9.0 / 5.0) + 32.0;
    }
    
    public void setTempetatureFahr(double fahrenheit){
        this.temperatureCels = (fahrenheit - 32.0) * 5.0 / 9.0;
    }
    
    // --- Magnus formula for calculating Dew points in Celsius ---
    public double getDewPoint(){
        double a = 17.27;
        double b = 237.7; 
        double alpha = ((a * temperatureCels) / (b + temperatureCels)) + Math.log(humidityPercentage / 100.0);
        return (b * alpha) / (a - alpha);
    }
    
    // --- takes Celsius result and converts to Fahrenheit --- //
    public double getdewPointFahrenheit(){
        return (getDewPoint() * 9.0 / 5.0) + 32.0;
    }
    
    // --- The National Weather Sevice heat index formula --- //
    public double getHeatIndexCelsius(){
        if (temperatureCels < 27.0 || humidityPercentage < 40.0){
            return temperatureCels;
        }
        
        // --- T = temperature | RH = rising humidity | hif = heat index into Fahrenheit --- //
        double T = getTemperatureFahr();
        double RH = humidityPercentage;
        double hiF = -42.379 + 2.04901523 * T + 10.14333127 * RH - 0.22475541 * T * RH
                - 0.00683783 * T * T - 0.05481717 * RH * RH + 0.00122874 * T * T * RH
                + 0.00085282 * T * RH * RH - 0.00000199 * T * T * RH * RH;
        return (hiF - 32.0) * 5.0 / 9.0;
    }
    
    private double clamp( double value, double min, double max){
        return Math.max(min, Math.min(max, value));
    }
    
    // --- Getters and Setters
    public LocalDateTime getTimestamp(){ return timestamp; }
    public void setTimestamp(LocalDateTime timestamp){ this.timestamp = timestamp; }
    
    public double getHumidityPercentage(){ return humidityPercentage; }
    public void setHumidityPercentage(double humidityPercentage){ this.humidityPercentage = humidityPercentage; }
    
    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }
    
    public double getAirPressure(){ return humidityPercentage; }
    public void setAirPressure(double airPressure){ this.airPressure = airPressure; }
    
    public double getPrecipitation() { return precipitation; }
    public void setPrecipitation(double precipitation) { this.precipitation = precipitation; }
    
    public Condition getCondition() { return condition; }
    public void setCondition(Condition condition) { this.condition = condition; }
    
    public String getLocationName() { return location; }
    public void setLocationName(String location) { this.location = location; }
}
