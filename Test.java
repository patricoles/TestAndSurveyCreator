import java.io.*;
import java.util.*;
import java.lang.*;

public class Test extends Survey implements Serializable
{
    private ArrayList<String> answers;

    public Test()
    {
        super();
        answers = new ArrayList<String>();
    }

    public String grade(ArrayList<String> answerSheet)
    {

        int correct = 0;
        int total = 0;
        for(int i=0; i<questions.size(); ++i)
        {
            if(questions.get(i).isGradable())
            {
                total++;
                if(answerSheet.get(i).equals(answers.get(i)))
                    correct++;
            } 
        }

        return Integer.toString(correct) + "0/" + Integer.toString(total) + "0";
    }

    public void create(OutputStream out, InputStream in) throws IOException
    {
        Menu menu = new Menu(true);
        for(int i=0; i<questionTypes.size(); ++i)
        {
            final Question question = questionTypes.get(i);
            final int j = i;
            menu.addMenuItem("Add a new " + questionTypes.get(i).type() + " question", () -> 
            { 
                try
                {
                    question.createQuestion(out, in);
                    questions.add(question);
                    if(question.isGradable())
                        answers.add(question.getAnswer(out,in));
                    else
                        answers.add(null);
                    questionTypes.set(j, question.getClass().newInstance());
                }catch(IOException | InstantiationException | IllegalAccessException e)
                {

                }
            });
        }
        menu.run(out, in);
    }

    public void modify(OutputStream out, InputStream in) throws IOException
    {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        pw.println("What question do you wish to modify?");
        pw.flush();
        int response = -1;

        while(true)
        {
            String line = br.readLine();
            if(line.isEmpty())
                continue;

            StringTokenizer tk = new StringTokenizer(line);
            try{
                response = Integer.parseInt(tk.nextToken());
            }catch(NumberFormatException e)
            {
                continue;
            }

            if(response >= 1 && response <= questions.size())
                break;
        }

        questions.get(response-1).modify(out, in);

        pw.println("Do you wish to modify the correct answer?");
        pw.flush();

        String line  = br.readLine().toLowerCase();
        while( !(line.equals("yes") || line.equals("no")))
        {
            line = br.readLine().toLowerCase();
        }

        if(line.equals("yes"))
            answers.set(response-1,questions.get(response-1).getAnswer(out, in));
    }

    public void display(OutputStream out) throws IOException
    {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
        for(int i=0; i<questions.size(); ++i)
        {
            questions.get(i).display(out);
            pw.println(answers.get(i));
            pw.flush();
        }
    }

    public static Test load(FileInputStream f) throws IOException,ClassNotFoundException
    {
        Test temp;
        ObjectInputStream in = new ObjectInputStream(f);
        temp = (Test) in.readObject();
        in.close();
        return temp;
    }
}
