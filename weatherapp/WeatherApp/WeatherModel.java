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
        this.windSpeed = windSpeed;
        this.airPressure = airPressure;
        this.precipitation = precipitation;
        this.condition = condition;
        this.location = location;
        
    }
}
