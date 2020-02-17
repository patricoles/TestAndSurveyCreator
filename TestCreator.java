import java.io.*;
import java.lang.*;
import java.util.*;

public class TestCreator
{
    public static void main(String[] args) 
    {
        final ArrayList<Survey> survey = new ArrayList<Survey>();
        survey.add(null);
        final ArrayList<Test> test = new ArrayList<Test>();
        test.add(null);

        OutputStream out = new VoiceOutputStream();
        InputStream in = System.in;

        try{
            Menu mainMenu = new Menu(true);
            mainMenu.addMenuItem("Create a new Survey", () -> 
                { 
                    try{
                        survey.set(0,new Survey()); 
                        survey.get(0).create(out, in);
                    }catch(IOException e){}
                });
            mainMenu.addMenuItem("Create a new Test", () -> 
                { 
                    try{
                        test.set(0, new Test());
                        test.get(0).create(out, in);
                    }catch(IOException e){}
                });
            mainMenu.addMenuItem("Display a survey", () -> 
                { 
                    try{
                        if(survey.get(0) == null)
                        {
                            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
                            pw.println("No loaded survey");
                            pw.flush();
                        }
                        else
                            survey.get(0).display(out);
                    }catch(IOException e){}
                });
            mainMenu.addMenuItem("Display a test", () -> 
                { 
                    try{
                        if(test.get(0) == null)
                        {
                            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
                            pw.println("No loaded test");
                            pw.flush();
                        }
                        else
                            test.get(0).display(out);
                    }catch(IOException e){}
                });
            mainMenu.addMenuItem("Load a Survey", () -> 
                { 
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    
                    try{
                        pw.println("Enter survey name:");
                        pw.flush();
                        String filename = "./surveys/" + br.readLine();    
                        FileInputStream fos = new FileInputStream(filename);
                        survey.set(0, Survey.load(fos));
                        fos.close();
                    }catch(IOException | ClassNotFoundException e){
                        pw.println("Failed to load survey");
                        pw.flush();
                    }
                });
            mainMenu.addMenuItem("Load a Test", () ->
                {
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    
                    try{
                        pw.println("Enter test name:");
                        pw.flush();
                        String filename = "./tests/" + br.readLine();    
                        FileInputStream fos = new FileInputStream(filename);
                        test.set(0,Test.load(fos));
                        fos.close();
                    }catch(IOException | ClassNotFoundException e){
                        pw.println("Failed to load test");
                        pw.flush();
                    }
                });
            mainMenu.addMenuItem("Save a Survey", () ->
                {
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    if(survey.get(0) == null)
                    {
                        pw.println("No survey to save");
                        pw.flush();
                        return;
                    }
                    try{
                        pw.println("Enter survey name");
                        pw.flush();
                        String filename = "./surveys/" + br.readLine();
                        FileOutputStream fos = new FileOutputStream(filename);
                        survey.get(0).save(fos);
                        fos.close();
                    }catch(IOException e){
                        pw.println("Failed to save survey");
                        pw.flush();
                    }
                });
            mainMenu.addMenuItem("Save a Test", () ->
                {
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    if(test.get(0) == null)
                    {
                        pw.println("No test to save");
                        pw.flush();
                        return;
                    }
                    try{
                        pw.println("Enter test name:");
                        pw.flush();
                        String filename = "./tests/" + br.readLine();
                        FileOutputStream fos = new FileOutputStream(filename);
                        test.get(0).save(fos);
                        fos.close();
                    }catch(IOException e){
                        pw.println("Failed to save test");
                        pw.flush();
                    }
                });
            mainMenu.addMenuItem("Modify an existing Survey", () ->
                {
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));

                    try{
                        pw.println("Enter survey name:");
                        pw.flush();
                        String filename = "./surveys/" + br.readLine();    
                        FileInputStream fos = new FileInputStream(filename);
                        survey.set(0, Survey.load(fos));
                        fos.close();

                        survey.get(0).modify(out, in);
                    }catch(IOException | ClassNotFoundException e){
                        pw.println("Failed to load survey");
                        pw.flush();
                    }
                });
            mainMenu.addMenuItem("Modify an existing Test", () ->
                {
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));

                    try{
                        pw.println("Enter test name:");
                        pw.flush();
                        String filename = "./tests/" + br.readLine();    
                        FileInputStream fos = new FileInputStream(filename);
                        test.set(0, Test.load(fos));
                        fos.close();

                        test.get(0).modify(out, in);
                    }catch(IOException | ClassNotFoundException e){
                        pw.println("Failed to load test");
                        pw.flush();
                    }
                });
            mainMenu.addMenuItem("Take a survey", () ->
                {
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));

                    try{
                        pw.println("Enter survey name:");
                        pw.flush();
                        String surveyName = br.readLine();    
                        String filename = "./surveys/" +surveyName;
                        FileInputStream fos = new FileInputStream(filename);
                        survey.set(0, Survey.load(fos));
                        fos.close();

                        pw.println("Enter your name:");
                        pw.flush();
                        String taker = br.readLine();

                        ArrayList<String> answers = survey.get(0).take(out, in);

                        String resultsDirectory = "./surveys/."+surveyName + "-results";
                        File resultsDir = new File(resultsDirectory);
                        if(!resultsDir.exists())
                            resultsDir.mkdir();

                        String resultsFilename =  resultsDirectory + "/" + taker;
                        FileOutputStream resultsFile = new FileOutputStream(resultsFilename);
                        ObjectOutputStream oout = new ObjectOutputStream(resultsFile);
                        oout.writeObject(answers);
                        oout.close();
                        resultsFile.close();
                    }catch(IOException | ClassNotFoundException e){
                        pw.println("Failed to load survey");
                        pw.flush();
                    }
                });
            mainMenu.addMenuItem("Take a test", () ->
                {
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));

                    try{
                        pw.println("Enter test name:");
                        pw.flush();
                        String testName = br.readLine();    
                        String filename = "./tests/" +testName;
                        FileInputStream fos = new FileInputStream(filename);
                        test.set(0, Test.load(fos));
                        fos.close();

                        pw.println("Enter your name:");
                        pw.flush();
                        String taker = br.readLine();

                        ArrayList<String> answers = test.get(0).take(out, in);

                        String resultsDirectory = "./tests/."+testName + "-results";
                        File resultsDir = new File(resultsDirectory);
                        if(!resultsDir.exists())
                            resultsDir.mkdir();

                        String resultsFilename =  resultsDirectory + "/" + taker;
                        FileOutputStream resultsFile = new FileOutputStream(resultsFilename);
                        ObjectOutputStream oout = new ObjectOutputStream(resultsFile);
                        oout.writeObject(answers);
                        oout.close();
                        resultsFile.close();
                    }catch(IOException | ClassNotFoundException e){
                        pw.println("Failed to load test");
                        pw.flush();
                    }
                });
            mainMenu.addMenuItem("Grade a test", () ->
                {
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));

                    try{
                        pw.println("Enter test name:");
                        pw.flush();
                        String testName = br.readLine();    
                        String filename = "./tests/" +testName;
                        FileInputStream fos = new FileInputStream(filename);
                        test.set(0, Test.load(fos));
                        fos.close();

                        pw.println("Enter taker name:");
                        pw.flush();
                        String taker = br.readLine();

                        String resultsFilename = "./tests/."+testName + "-results/" + taker;
                        FileInputStream resultsFile = new FileInputStream(resultsFilename);
                        ObjectInputStream oin = new ObjectInputStream(resultsFile);
                        ArrayList<String> answers = (ArrayList<String>) oin.readObject();

                        String grade = test.get(0).grade(answers);
                        pw.println(grade);
                        pw.flush();
                    }catch(IOException | ClassNotFoundException e){
                        pw.println("Failed to load test");
                        pw.flush();
                    }
                });
            mainMenu.addMenuItem("Tabulate a survey", () ->
                {
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));

                    try{
                        pw.println("Enter survey name:");
                        pw.flush();
                        String surveyName = br.readLine();    
                        String filename = "./surveys/" +surveyName;
                        FileInputStream fos = new FileInputStream(filename);
                        survey.set(0, Survey.load(fos));
                        fos.close();

                        ArrayList<ArrayList<String> > answerSheets = new ArrayList<ArrayList<String> >();
                        String resultsDirectory = "./surveys/."+surveyName + "-results/";
                        File dir = new File(resultsDirectory);
                        File[] directoryListing = dir.listFiles();
                        for (File child : directoryListing) {
                            FileInputStream resultsFile = new FileInputStream(child);
                            ObjectInputStream oin = new ObjectInputStream(resultsFile);
                            ArrayList<String> answers = (ArrayList<String>) oin.readObject();
                            answerSheets.add(answers);
                        }

                        survey.get(0).tabulate(answerSheets, out);
                    }catch(IOException | ClassNotFoundException e){
                        pw.println("Failed to load survey");
                        pw.flush();
                    }
                });
            mainMenu.addMenuItem("Tabulate a test", () ->
                {
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));

                    try{
                        pw.println("Enter test name:");
                        pw.flush();
                        String testName = br.readLine();    
                        String filename = "./tests/" +testName;
                        FileInputStream fos = new FileInputStream(filename);
                        test.set(0, Test.load(fos));
                        fos.close();

                        ArrayList<ArrayList<String> > answerSheets = new ArrayList<ArrayList<String> >();
                        String resultsDirectory = "./tests/."+testName + "-results/";
                        File dir = new File(resultsDirectory);
                        File[] directoryListing = dir.listFiles();
                        for (File child : directoryListing) {
                            FileInputStream resultsFile = new FileInputStream(child);
                            ObjectInputStream oin = new ObjectInputStream(resultsFile);
                            ArrayList<String> answers = (ArrayList<String>) oin.readObject();
                            answerSheets.add(answers);
                        }

                        test.get(0).tabulate(answerSheets, out);
                    }catch(IOException | ClassNotFoundException e){
                        pw.println("Failed to load test");
                        pw.flush();
                    }
                });
            mainMenu.run(out, in);
        }catch(IOException e)
        {}
    }
}
