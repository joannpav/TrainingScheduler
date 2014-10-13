import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;
import org.xml.sax.*;

import java.io.*;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import javax.xml.xpath.*;

/**
 * Created by IntelliJ IDEA.
 * User: joann
 * Date: 5/26/12
 * Time: 9:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class Workout {
    private Integer maxMilesPerWeek;
    private Integer longRunLength;
    private String intensityLevel;
    private String workoutPhase;
    private static int trainingWeekNumber = 0;

    private ArrayList<TrainingPhase> workoutPlan = new ArrayList<TrainingPhase>();


    public Workout(String workoutPhase, int workoutMiles, String intensityLevel){
        this.maxMilesPerWeek = workoutMiles;
        this.workoutPhase = workoutPhase;
        this.intensityLevel = intensityLevel;
        this.longRunLength = (int)Math.round(this.maxMilesPerWeek * .25);
    }

    public int getMiles(){
        return this.maxMilesPerWeek;
    }

    public void CreateBaseWorkoutSet(){
        readWorkoutXML();

    }

    public void readWorkoutXML(){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);

        DocumentBuilder db = null;
        try{
            db = dbf.newDocumentBuilder();
            db.setErrorHandler(new ErrorHandler(){
                public void error(SAXParseException spe){
                    System.err.println(spe);
                }
                public void fatalError(SAXParseException spe) {
                    System.err.println(spe);
                }
                public void warning(SAXParseException spe){
                    System.out.println(spe);
                }
            });
        }catch (ParserConfigurationException pce){
            System.err.println(pce);
            System.exit(1);
        }
        Document doc = null;
        try{
            doc = db.parse(new File("/Users/joann/IdeaProjects/TrainingScheduler/src/workout.xml"));
        } catch (SAXException se) {
            System.err.println(se);
        } catch (IOException ioe){
            System.err.println(ioe);
        }
        //NodeList nodeList =  doc.getChildNodes();
        getBaseWorkoutSet(doc);

    }


    private void getBaseWorkoutSet(Document doc){
        TrainingPhase bp[] = new TrainingPhase[5];
        XPathFactory  factory=XPathFactory.newInstance();
        XPath xPath=factory.newXPath();

        try{
            XPathExpression phaseInfo = xPath.compile("//phase[@name='"+this.workoutPhase+"']/@info");
            XPathExpression longWeekPercent = xPath.compile("//phase[@name='"+this.workoutPhase+"']/long/week/@weekPercent");
            XPathExpression longWeekNum = xPath.compile("//phase[@name='"+this.workoutPhase+"']/long/week/@weekNum");
            XPathExpression workDesc1  = xPath.compile("//phase[@name='"+this.workoutPhase+"']/work1/week/@desc");
            XPathExpression workTotal1  = xPath.compile("//phase[@name='"+this.workoutPhase+"']/work1/week/@total");
            XPathExpression workDesc2  = xPath.compile("//phase[@name='"+this.workoutPhase+"']/work2/week/@desc");
            XPathExpression workTotal2  = xPath.compile("//phase[@name='"+this.workoutPhase+"']/work2/week/@total");
            XPathExpression workDesc3  = xPath.compile("//phase[@name='"+this.workoutPhase+"']/work3/week/@desc");
            XPathExpression workTotal3  = xPath.compile("//phase[@name='"+this.workoutPhase+"']/work3/week/@total");
            Object pi = phaseInfo.evaluate(doc, XPathConstants.NODESET);
            Object wp = longWeekPercent.evaluate(doc, XPathConstants.NODESET);
            Object wn = longWeekNum.evaluate(doc, XPathConstants.NODESET);
            Object wd1 = workDesc1.evaluate(doc, XPathConstants.NODESET);
            Object wt1 = workTotal1.evaluate(doc, XPathConstants.NODESET);
            Object wd2 = workDesc2.evaluate(doc, XPathConstants.NODESET);
            Object wt2 = workTotal2.evaluate(doc, XPathConstants.NODESET);
            Object wd3 = workDesc3.evaluate(doc, XPathConstants.NODESET);
            Object wt3 = workTotal3.evaluate(doc, XPathConstants.NODESET);

            NodeList piNodes = (NodeList) pi;
            NodeList wpNodes = (NodeList) wp;
            NodeList wnNodes = (NodeList) wn;
            NodeList wdNodes1 = (NodeList) wd1;
            NodeList wtNodes1 = (NodeList) wt1;
            NodeList wdNodes2 = (NodeList) wd2;
            NodeList wtNodes2 = (NodeList) wt2;
            NodeList wdNodes3 = (NodeList) wd3;
            NodeList wtNodes3 = (NodeList) wt3;

            for (int i = 0; i < wpNodes.getLength(); i++) {
                bp[i] = new TrainingPhase();
                if (piNodes.item(i) != null) { bp[i].setInformation(piNodes.item(i).getNodeValue()); }
                bp[i].setPhaseName(this.workoutPhase);
                bp[i].setWeekPercent(wpNodes.item(i).getNodeValue());
                bp[i].setWeekNumber(wnNodes.item(i).getNodeValue());
                bp[i].setWorkInfo1(wdNodes1.item(i).getNodeValue());
                bp[i].setThisWeekWorkoutDistance1(wtNodes1.item(i).getNodeValue());
                if (this.intensityLevel.startsWith("Medium")){
                    bp[i].setWorkInfo2(wdNodes2.item(i).getNodeValue());
                    bp[i].setThisWeekWorkoutDistance2(wtNodes2.item(i).getNodeValue());
                } else if (this.intensityLevel.startsWith("High")){
                    bp[i].setWorkInfo2(wdNodes2.item(i).getNodeValue());
                    bp[i].setThisWeekWorkoutDistance2(wtNodes3.item(i).getNodeValue());
                    bp[i].setWorkInfo3(wdNodes3.item(i).getNodeValue());
                    bp[i].setThisWeekWorkoutDistance3(wtNodes3.item(i).getNodeValue());
                }
                bp[i].setThisWeekLongRun(String.valueOf( Math.round(this.maxMilesPerWeek * Integer.parseInt(bp[i].getWeekPercent())/100 * .25) ));
                this.workoutPlan.add(bp[i]);
            }
        }catch (XPathExpressionException xpe){
            System.out.println(xpe);
        }


        Integer wkPercent;
        Integer easyMiles = 0;
        for (int i = 0; i<bp.length;i++){
            trainingWeekNumber++;
            wkPercent = Integer.parseInt(bp[i].getWeekPercent());
            System.out.println("------------------------------------------------------------------------");
            System.out.println("Week " + trainingWeekNumber + " - " + bp[i].getPhaseName() + " Phase");
            if (bp[i].getInformation() != null){System.out.println(bp[i].getInformation());}
            System.out.println("------------------------------------------------------------------------");
            System.out.println("Mileage this week: " + bp[i].getWeekPercent() + " percent of max. ");
            System.out.println("Miles for week: " + (this.maxMilesPerWeek* wkPercent/100 ) + " miles");
            System.out.println("Long run: " + bp[i].getThisWeekLongRun());

            if (this.intensityLevel.startsWith("Low")){
                easyMiles = (this.maxMilesPerWeek*wkPercent/100)- Integer.parseInt(bp[i].getThisWeekLongRun()) - Integer.parseInt(bp[i].getThisWeekWorkoutDistance1());
                System.out.println("Workout this week: " + bp[i].getWorkInfo1() + ", for a total of " + bp[i].getThisWeekWorkoutDistance1() + " miles");
                System.out.println("Easy runs: 4 days of easy runs for a total of " + easyMiles + " miles");
            } else if (this.intensityLevel.startsWith("Medium")){
                easyMiles = (this.maxMilesPerWeek*wkPercent/100)- Integer.parseInt(bp[i].getThisWeekLongRun()) - Integer.parseInt(bp[i].getThisWeekWorkoutDistance1())-Integer.parseInt(bp[i].getThisWeekWorkoutDistance1());
                System.out.println("Workout 1 this week: " + bp[i].getWorkInfo1() + ", for a total of " + bp[i].getThisWeekWorkoutDistance1() + " miles");
                System.out.println("Workout 2 this week: " + bp[i].getWorkInfo2() + ", for a total of " + bp[i].getThisWeekWorkoutDistance2() + " miles");
                System.out.println("Easy runs: 3 days of easy runs for a total of " + easyMiles + " miles");
            } else if (this.intensityLevel.startsWith("High")){
                easyMiles = (this.maxMilesPerWeek*wkPercent/100)- Integer.parseInt(bp[i].getThisWeekLongRun()) - Integer.parseInt(bp[i].getThisWeekWorkoutDistance1()) - Integer.parseInt(bp[i].getThisWeekWorkoutDistance2())- Integer.parseInt(bp[i].getThisWeekWorkoutDistance3());
                System.out.println("Workout 1 this week: " + bp[i].getWorkInfo1() + ", for a total of " + bp[i].getThisWeekWorkoutDistance1() + " miles");
                System.out.println("Workout 2 this week: " + bp[i].getWorkInfo2() + ", for a total of " + bp[i].getThisWeekWorkoutDistance2() + " miles");
                System.out.println("Workout 3 this week: " + bp[i].getWorkInfo3() + ", for a total of " + bp[i].getThisWeekWorkoutDistance3() + " miles");
                System.out.println("Easy runs: 3 days of easy running for a total of " + easyMiles + " miles");
            }
        }
    }
}