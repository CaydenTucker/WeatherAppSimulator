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
    
    public String getDominantCondition(){
        if (history == null || history.isEmpty()) return "N/A";
        
        long sunnyCount = history.stream().filter(w -> w.getCondition() == WeatherModel.Condition.SUNNY).count();
        long rainyCount = history.stream().filter(w -> w.getCondition() == WeatherModel.Condition.RAINY).count();
        long cloudyCount = history.stream().filter(w -> w.getCondition() == WeatherModel.Condition.CLOUDY).count();
        long stormyCount = history.stream().filter(w -> w.getCondition() == WeatherModel.Condition.THUNDERSTORM).count();
        
        long max = Math.max(Math.max(sunnyCount, rainyCount), Math.max(cloudyCount, stormyCount));
        if (max == sunnyCount) return "Sunny";
        if (max == rainyCount) return "Rainy";
        if (max == cloudyCount) return "Cloudy";
        if (max == stormyCount) return "Stormy";
        return "Mixed";
    }
    
    public String generateFullReport(){
        if (history == null || history.isEmpty()){
            return "No weather history available for analysis";
        }
        
        double avgC = getAverageTemperatureCels(); //average Celsius
        double avgF = (avgC * 9.0 / 5.0) + 32.0; //average Fahrenheit
        double minC = getMinTemperatureCels(); //minimum Celsius 
        double minF = (minC * 9.0 / 5.0) + 32.0; //minimum Fahrenheit
        double maxC = getMaxTemperatureCels(); //maximum Celsius
        double maxF = (maxC * 9.0 / 5.0) + 32.0; //maximum Fahrenheit
        
        StringBuilder sb = new StringBuilder();
        sb.append("// ==== METROROLIGICAL SIMULATION REPORT ==== //\n");
        sb.append(String.format("Total Data Points Analyzed: %d\n", history.size()));
        sb.append(String.format("Temperature Range: %.1f°C (%.1f°F) to %.1f°C (%.1f°F)\n", 
                minC, minF, maxC, maxF));
        sb.append(String.format("Average Temperature: %.1f°C (%.1f°F)\n", avgC, avgF));
        sb.append(String.format("Average Humidity: %.1f%%\n", getAverageHumidity()));
        sb.append(String.format("Peak Wind Gust: %.1f km/h\n", getMaxWindSpeed()));
        sb.append(String.format("Total Cumulative Precipitation: %.2f mm\n", getTotalPrecipitation()));
        sb.append(String.format("Dominant Weather Condition: %s\n", getDominantCondition()));
        sb.append("==========================================");
        
        return sb.toString();
    }
}
