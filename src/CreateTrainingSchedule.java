
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: joann
 * Date: 6/11/12
 * Time: 7:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateTrainingSchedule {

    public static void main(String[] argv) throws IOException {
        System.out.println("Welcome! This tool will help you plan a 25 week training program to get yourself");
        System.out.println("ready for a 5k, 10k or half marathon race.");
        System.out.println("Your program will be broken down into 5 key phases,");
        System.out.println("Base, Strength, Anaerobic Threshold, V02 Max and Peak");
        System.out.println("Good luck!");
        System.out.println();
        System.out.println("Hi, what is your name? ");
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in),1);
        String runnerName = stdin.readLine();
        System.out.println("What level of intensity do you want to train at? (Enter 'High', 'Medium' or 'Low')");
        String runnerIntensity = stdin.readLine();
        System.out.println("What is the maximum number of miles per week you want to run? (Recommend 30 or more for Low intensity, 40 or more for Medium and 50 or more for High)");
        String runnerMaxMiles = stdin.readLine();


        Runner aRunner = new Runner();
        aRunner.setRunnerName(runnerName);
        aRunner.setIntensityLevel(runnerIntensity);   // Choose Low, Medium or High
        aRunner.setMaxWeeklyDistance(Integer.parseInt(runnerMaxMiles));

        System.out.println(aRunner.getRunnerName() + " is training at a "
                + aRunner.getIntensityLevel() + " intensity level with a maximum of " + aRunner.getMaxWeeklyDistance()
                + " miles per week" );

        aRunner.createWorkout();
        aRunner.addWorkout("Base",aRunner.getMaxWeeklyDistance(), aRunner.getIntensityLevel());
        aRunner.milesInWorkoutSet();
        aRunner.addWorkout("Strength and Hills",aRunner.getMaxWeeklyDistance(),aRunner.getIntensityLevel());
        aRunner.addWorkout("Anaerobic Threshold",aRunner.getMaxWeeklyDistance(),aRunner.getIntensityLevel());
        aRunner.addWorkout("V02Max",aRunner.getMaxWeeklyDistance(),aRunner.getIntensityLevel());
        aRunner.addWorkout("Peak",aRunner.getMaxWeeklyDistance(),aRunner.getIntensityLevel());

        }

}
