/**
 * Created by IntelliJ IDEA.
 * User: joann
 * Date: 5/27/12
 * Time: 9:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class TrainingSchedulerTester {
    public static void main(String argv[]) {
        // Create a runner with a training set with 5 phases of 5 weeks each phase

        Runner joann = new Runner();
        joann.setRunnerName("Joann");
        joann.setIntensityLevel("High");   // Choose Low, Medium or High
        joann.setMaxWeeklyDistance(60);


        System.out.println(joann.getRunnerName() + " is training at a "
                + joann.getIntensityLevel() + " intensity level with a maximum of " + joann.getMaxWeeklyDistance()
                + " miles per week" );

        joann.createWorkout();
        joann.addWorkout("Base",joann.getMaxWeeklyDistance(), joann.getIntensityLevel());
        joann.milesInWorkoutSet();
        joann.addWorkout("Strength and Hills",joann.getMaxWeeklyDistance(),joann.getIntensityLevel());
        joann.addWorkout("Anaerobic Threshold",joann.getMaxWeeklyDistance(),joann.getIntensityLevel());
        joann.addWorkout("V02Max",joann.getMaxWeeklyDistance(),joann.getIntensityLevel());
        joann.addWorkout("Peak",joann.getMaxWeeklyDistance(),joann.getIntensityLevel());

    }
}
