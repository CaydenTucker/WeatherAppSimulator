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
    private LocalDateTime currentSimTime;
    private final ClimateGenerator climateGenerator;
    private final List<WeatherModel> weatherHistory;
    private WeatherModel currentState;
    private String selectedLocation;
    private int simSpeed;
    private boolean isRunning;
    
    public interface SimListener{
        void onTick(WeatherModel newState);
        void onEventTriggered(String eventDescription);
    }
    
    private final List<SimListener> listeners;
    
    public SimEngine(String initialLocation, double baseTemp){
        this.selectedLocation = initialLocation;
        this.currentSimTime = LocalDateTime.now().withMinute(0).withSecond(0);
        this.climateGenerator = new ClimateGenerator(baseTemp, 12.0);
        this.weatherHistory = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.simSpeed = 1000;
        this.isRunning = false;
        
        // --- Initializing first stance --- \\\
        this.currentState = climateGenerator.generateNextState(currentSimTime, null, initialLocation);
        weatherHistory.add(currentState);
    }
    
    public void addSimListener(SimListener listener){
        if (listener != null){
            listeners.add(listener);
        }
    }
    
    public void removeSimListener(SimListener listener){
        listeners.remove(listener);
    }
    
    public synchronized void advanceTime(int hours){
        for (int i = 0; i < hours; i++){
            currentSimTime = currentSimTime.plusHours(1);
            currentState = climateGenerator.generateNextState(currentSimTime, currentState, selectedLocation);
            weatherHistory.add(currentState);
            
            if (weatherHistory.size > 720){
                weatherHistory.remove(0);
            }
            
            notifyListenersTick();
            checkForSpecialEvents();
        }   
    }
}
