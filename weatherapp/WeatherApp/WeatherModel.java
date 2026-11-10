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
        
        
    }
}
