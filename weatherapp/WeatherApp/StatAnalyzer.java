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
    private final List<WeatherModel> history; // stores the lisst of weather records that will be analyzed 
    
    // constructor that receives the weather history be analyzed
    public StatAnalyzer(List<WeatherModel> history){
        this.history = history;
    }
    
    // --- calculates nd returns the average temperature in Celsius --- \\
    public double getAverageTemperatureCels(){
        // checks whether the history list is null or containes no weather records
        if (history == null || history.isEmpty()) return 0.0;
        // uses a stream to collect variables and return 0.0 if no value exists
        return history.stream().mapToDouble(WeatherModel::getTemperatureCels).average().orElse(0.0);
    }
    
    // --- finds and returns the highest recorded temperature in Celsius --- \\
    public double getMaxTemperatureCels(){
        // checks whether the history list is null or empty before performing calculations.
        if (history == null || history.isEmpty()) return 0.0;
        // extracts all temperatures, finds the maximum value, and returns 0.0 if no value exists.
        return history.stream().mapToDouble(WeatherModel::getTemperatureCels).max().orElse(0.0);
    }
    
    // --- finds and returns the lowest recorded temperature in Celsius --- \\
    public double getMinTemperatureCels(){
        // checks whether the history list is null or empty before performing calculations.
        if (history == null || history.isEmpty()) return 0.0;
        // extracts all temperatures, finds the minimum value, and returns 0.0 if no value exists.
        return history.stream().mapToDouble(WeatherModel::getTemperatureCels).min().orElse(0.0);
    }
    
    // --- calculates the total amount of precipitation recorded in the weather history --- \\
    public double getTotalPrecipitation() {
        // checks whether the history list is null or empty befor calculating precipitation
        if (history == null || history.isEmpty()) return 0.0;
        // extracts all precipitation values and adds them together to calculate the total.
        return history.stream().mapToDouble(WeatherModel::getPrecipitation).sum();
    }
    
    // --- Calculates and returns the average humidity percentage across all weather records --- \\
    public double getAverageHumidity() {
        // Checks whether the history list is null or empty before calculating average humidity
        if (history == null || history.isEmpty()) return 0.0;
        // Extracts all humidity values, calculates their average, and returns 0.0 if no value exists
        return history.stream().mapToDouble(WeatherModel::getHumidityPercentage).average().orElse(0.0);
    }
    
    // --- Finds and returns the highest wind speed recorded in the weather history --- \\
    public double getMaxWindSpeed() {
        // checks whether the history is null or empty before finding maximum wind speed
        if (history == null || history.isEmpty()) return 0.0;
        // extracts all wind speeds, finds the maximum value, and returns 0.0 if no value exists
        return history.stream().mapToDouble(WeatherModel::getWindSpeed).max().orElse(0.0);
    }
    
    // --- Determines and returns the weather condition that appears most frequently in the history --- \\
    public String getDominantCondition(){
        // returns N/A when there are no weather records available for analysis
        if (history == null || history.isEmpty()) return "N/A";
        
        // counts how many weather records have (x) condition
        long sunnyCount = history.stream().filter(w -> w.getCondition() == WeatherModel.Condition.SUNNY).count();
        long rainyCount = history.stream().filter(w -> w.getCondition() == WeatherModel.Condition.RAINY).count();
        long cloudyCount = history.stream().filter(w -> w.getCondition() == WeatherModel.Condition.CLOUDY).count();
        long stormyCount = history.stream().filter(w -> w.getCondition() == WeatherModel.Condition.THUNDERSTORM).count();
        
        // finds the highest occurance count among conditions
        long max = Math.max(Math.max(sunnyCount, rainyCount), Math.max(cloudyCount, stormyCount));
        
        // returns (x) condition if (x) condition has highest occurance count 
        if (max == sunnyCount) return "Sunny";
        if (max == rainyCount) return "Rainy";
        if (max == cloudyCount) return "Cloudy";
        if (max == stormyCount) return "Stormy";
        return "Mixed";
    }
    
    // --- Generates and returns a complete statistical report based on the weather history
    public String generateFullReport(){
        
        // checks whether the weather history is null or empty before generating the report 
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
        
        // adds the title and heading of the meteorological simulation report 
        sb.append("// ==== METROROLIGICAL SIMULATION REPORT ==== //\n");
        // adds the total number of weather records included in the analysis
        sb.append(String.format("Total Data Points Analyzed: %d\n", history.size()));
        // adds the minimum and maximum temperatures in both Celsius and Fahrenheit
        sb.append(String.format("Temperature Range: %.1f°C (%.1f°F) to %.1f°C (%.1f°F)\n", 
                minC, minF, maxC, maxF));
        // adds the average temperature in Celsius and Fahrenheit with one decimal place
        sb.append(String.format("Average Temperature: %.1f°C (%.1f°F)\n", avgC, avgF));
        // adds the average humidity percentage with once decimal place 
        sb.append(String.format("Average Humidity: %.1f%%\n", getAverageHumidity()));
        // adds the highesr recorded wind speed in kilometers per hour
        sb.append(String.format("Peak Wind Gust: %.1f km/h\n", getMaxWindSpeed()));
        // adds the total accumulated precipitation in millimeters with two decimal places 
        sb.append(String.format("Total Cumulative Precipitation: %.2f mm\n", getTotalPrecipitation()));
        // adds the most frequently occuring weather condition 
        sb.append(String.format("Dominant Weather Condition: %s\n", getDominantCondition()));
        // adds the closing separator at the bottom of the report 
        sb.append("==========================================");
        
        return sb.toString();
    }
}
