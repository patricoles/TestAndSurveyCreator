import java.io.*;
import java.util.*;
import java.lang.*;

public class Survey implements Serializable
{
    protected ArrayList<Question> questionTypes;
    protected ArrayList<Question> questions;

    public Survey()
    {
        questionTypes = new ArrayList<Question>();
        questionTypes.add(new MultipleChoiceQuestion());
        questionTypes.add(new TrueFalseQuestion());
        questionTypes.add(new EssayQuestion());
        questionTypes.add(new ShortAnswerQuestion());
        questionTypes.add(new RankingQuestion());
        questionTypes.add(new MatchingQuestion());

        questions = new ArrayList<Question>();
    }

    public void tabulate(ArrayList<ArrayList<String> > answerSheets, OutputStream out)
    {
        int numTakers = answerSheets.size();
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
        for(int i=0; i< questions.size(); ++i)
        {
            pw.println("Question " + Integer.toString(i));

            Map<String, Integer> answers = new HashMap<String, Integer>();
            for(int j=0; j<answerSheets.size(); ++j)
            {
                String answer = answerSheets.get(j).get(i);
                if(!answers.containsKey(answer))
                    answers.put(answer, 1);
                else
                    answers.put(answer,answers.get(answer)+1);
            }

            for (Map.Entry<String, Integer> entry : answers.entrySet())
            {
                pw.println(entry.getKey() + ": " + Integer.toString(entry.getValue()));
            }
        }
    
        pw.flush();
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
    }

    public ArrayList<String> take(OutputStream out, InputStream in) throws IOException
    {
        ArrayList<String> answers = new ArrayList<String>();
        for(int i=0; i<questions.size(); ++i)
        {
            questions.get(i).display(out);
            answers.add(questions.get(i).getAnswer(out, in));
        }

        return answers;
    }

    public void display(OutputStream out) throws IOException
    {
        for(int i=0; i<questions.size(); ++i)
        {
            questions.get(i).display(out);
        }
    }

    public void save(FileOutputStream f) throws IOException
    {
        ObjectOutputStream out = new ObjectOutputStream(f);
        out.writeObject(this);
        out.close();
    }

    public static Survey load(FileInputStream f) throws IOException,ClassNotFoundException
    {
        Survey temp;
        ObjectInputStream in = new ObjectInputStream(f);
        temp = (Survey) in.readObject();
        in.close();
        return temp;
    }
}
