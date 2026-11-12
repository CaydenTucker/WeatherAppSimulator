/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package WeatherApp;

import java.util.List;
import java.util.DoubleSummaryStatistics;

/**
 *
 * @author catuc
 */
public class StatAnalyzer {
    private final List<WeatherModel> history;
    
    public StatAnalyzer(List<WeatherModel> history){
        this.history = history;
    }
    
    public double getAverageTemperatureCels(){
        if (history == null || history.isEmpty()) return 0.0;
        return history.stream().mapToDouble(WeatherModel::getTemperatureCels).average().orElse(0.0);
    }
    
    public double getMaxTemperatureCels(){
        if (history == null || history.isEmpty()) return 0.0;
        return history.stream().mapToDouble(WeatherModel::getTemperatureCels).max().orElse(0.0);
    }
    
    public double getMinTemperatureCels(){
        if (history == null || history.isEmpty()) return 0.0;
        return history.stream().mapToDouble(WeatherModel::getTemperatureCels).min().orElse(0.0);
    }
    
    public double getTotalPrecipitation() {
        if (history == null || history.isEmpty()) return 0.0;
        return history.stream().mapToDouble(WeatherModel::getPrecipitation).sum();
    }
    
    public double getAverageHumidity() {
        if (history == null || history.isEmpty()) return 0.0;
        return history.stream().mapToDouble(WeatherModel::getHumidityPercentage).average().orElse(0.0);
    }
    
    public double getMaxWindSpeed() {
        if (history == null || history.isEmpty()) return 0.0;
        return history.stream().mapToDouble(WeatherModel::getWindSpeed).max().orElse(0.0);
    }
    
    
}
