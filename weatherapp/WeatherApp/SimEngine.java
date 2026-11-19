/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package WeatherApp;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 *
 * @author catuc
 */
public class SimEngine {
    private LocalDateTime currentSimTime; // stores current simulated date and time
    private final ClimateGenerator climateGenerator; // creates ClimateGenerator responsable for generating weather conditions 
    private final List<WeatherModel> weatherHistory; // stores a collection of previously generatede weather states
    private WeatherModel currentState; // stores the most generated weather condition
    private String selectedLocation; // stores the name of the location currently being simulated
    private int simSpeed; // controls the simulation speed
    private boolean isRunning; // tracks whether the sim is currently running or paused 
    
    // --- defines an interface that allows other classes to receive simulation updates --- //
    public interface SimListener{
        void onTick(WeatherModel newState); // called whenever a new weather state is generated
        void onEventTriggered(String eventDescription); // called whenever a special weather event or warning is triggered 
    }
    
    // Called whenever a special weather event or warning is triggered
    private final List<SimListener> listeners;
    
    // --- Initializes the simulation using a location and base temperature
    public SimEngine(String initialLocation, double baseTemp){
        this.selectedLocation = initialLocation; // sets the initial location for the simulation
        this.currentSimTime = LocalDateTime.now().withMinute(0).withSecond(0); // gets current time and resets mins and secs to 0
        this.climateGenerator = new ClimateGenerator(baseTemp, 12.0); // uses given base temp and seasonal var
        this.weatherHistory = new ArrayList<>(); // creates an empty list to store weather history
        this.listeners = new ArrayList<>(); // creates an empty list to store simulation listeners
        this.simSpeed = 1000; // sets the initial simulation speed to 1000 milliseconds or one sec
        this.isRunning = false; // sets simulation staturs to false so it starts paused 
        
        // --- Initializing first stance --- \\
        this.currentState = climateGenerator.generateNextState(currentSimTime, null, initialLocation);
        weatherHistory.add(currentState); // adds the initial weather state to the weather history
    }
     // --- registers a listener to receive weather updates and even notifications --- \\
    public void addSimListener(SimListener listener){
        if (listener != null){
            listeners.add(listener);
        }
    }
    
    // --- removes a prev registered listener from the simulation --- \\
    public void removeSimListener(SimListener listener){
        listeners.remove(listener);
    }
    
    // --- advances the simulation by the specified number of hours --- \\
    public synchronized void advanceTime(int hours){
        
        // repeats the siimulation update once for every requested hour
        for (int i = 0; i < hours; i++){
            // advances the simulated date and time by one hour
            currentSimTime = currentSimTime.plusHours(1);
            
            // generates new weather condition using the current time and previous weather stat
            currentState = climateGenerator.generateNextState(currentSimTime, currentState, selectedLocation);
            
            // adds the newly generated weather state to the history collection
            weatherHistory.add(currentState);
            
            // checks whether the weather history contains 720 records
            if (weatherHistory.size() > 720){
                weatherHistory.remove(0);
            }
            
            notifyListenersTick(); // notifies listeners that a new weathe state is available
            checkForSpecialEvents(); // checks the current weathe condition triggered special event
        }   
    }
    
    // --- resets the simulation using a new location and base temperature
    public void resetSimulation(String newLocation, double baseTemp){
        this.selectedLocation = newLocation;
        this.currentSimTime = LocalDateTime.now().withMinute(0).withSecond(0);
        this.climateGenerator.setBaseTemperature(baseTemp);
        this.weatherHistory.clear();
        
        // generates a new initial weather state without using previous weather conditions
        this.currentState = climateGenerator.generateNextState(currentSimTime, null, selectedLocation);
        weatherHistory.add(currentState); // adds the newly generated state to the history collection
        notifyListenersTick(); // notfies all listeners about the new weather state
    }
    
    // --- sends the latest weather update to all registered sim listeners --- \\
    private void notifyListenersTick(){
        for (SimListener listener : listeners){
            listener.onTick(currentState);
        }
    }
    
    // --- checks the current weather conditions for special events and warnings --- \\ 
    private void checkForSpecialEvents(){
        if(currentState.getCondition() == WeatherModel.Condition.THUNDERSTORM){
            notifyEvent("Severe thunderstorm waening issued for " + selectedLocation + "!");
        }
        else if (currentState.getWindSpeed() > 70.0){
            notifyEvent("High wind advisory active! Wind speeds exceeding 70 km/h.");
        }
        else if (currentState.getTemperatureCels() > 38.0){
            notifyEvent("Extreme heat advisory! Temperatures reaching dangerous levels.");
        }
    }
    
    // --- sends a special weather event notification to all registered listeners  
    private void notifyEvent(String description){
        for (SimListener listener : listeners){
            listener.onEventTriggered(description);
        }
    }
    
    // --- provides access to the weather hisory without allowing external modifications 
    public List<WeatherModel> getWeatherHistory(){
        return Collections.unmodifiableList(weatherHistory);
    }
    
    // --- return statements --- \\
    public WeatherModel getCurrentState(){return currentState;}
    public LocalDateTime getCurrentSimTime() { return currentSimTime; }
    public String getSelectedLocation() { return selectedLocation; }
    
    // --- return and set statement for speed --- \\
    public int getSimulatedSpeed() { return simSpeed; }
    public void setSimSpeed(int simSpeed) { this.simSpeed = Math.max(100, simSpeed); }
    
    // --- checks if running | updates running status using provided boolean value
    public boolean isRunning() { return isRunning; }
    public void setRunning(boolean running) { isRunning = running; }
}
