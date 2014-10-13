import com.sun.corba.se.spi.orbutil.threadpool.Work;

import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: joann
 * Date: 5/26/12
 * Time: 9:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class Runner {
    private String runnerName;
    private String intensityLevel;
    private Integer maxWeeklyDistance;
    private HashSet<Workout> workoutSet;


    public void setIntensityLevel(String intensityLevel) {
        this.intensityLevel = intensityLevel;
    }

    public void setMaxWeeklyDistance(Integer maxWeeklyDistance) {
        this.maxWeeklyDistance = maxWeeklyDistance;
    }

    public void setRunnerName(String runnerName){
        this.runnerName = runnerName;
    }

    public String getIntensityLevel() {
        return intensityLevel;
    }

    public Integer getMaxWeeklyDistance() {
        return maxWeeklyDistance;
    }

    public String getRunnerName(){
        return runnerName;
    }

    public void createWorkout(){
        workoutSet = new HashSet<Workout>();
    }

    public void addWorkout(String workoutType, int workoutMiles, String intensityLevel){
        Workout aWorkout = new Workout(workoutType, workoutMiles, intensityLevel);
        aWorkout.CreateBaseWorkoutSet();
        workoutSet.add(aWorkout);
    }



    public int milesInWorkoutSet(){
        int totalMiles = 0;
        for(Workout aWorkout : workoutSet){
            totalMiles += aWorkout.getMiles();
        }
        return totalMiles;
    }
}
