import java.io.*;
import java.util.*;

public class MultipleChoiceQuestion implements Question,Serializable
{
    private String prompt = null;
    private ArrayList<String> answers = null;

    public void display(OutputStream out)
    {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
        pw.println(prompt);
        
        for(int i=0; i<answers.size(); i++)
            pw.println( Integer.toString(i+1) + ")" + answers.get(i));
        pw.flush();
    }

    public void createQuestion(OutputStream out, InputStream in) throws IOException
    {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        pw.println("Enter prompt:");
        pw.flush();
        prompt = br.readLine();

        pw.println("Enter answers seperated by line breaks, to finish enter empty line:");
        pw.flush();
        answers = new ArrayList<String>();
        String line;
        line = br.readLine();
        while( !line.isEmpty() )
        {
            answers.add(line); 
            line = br.readLine();
        };
    }

    public void modify(OutputStream out, InputStream in) throws IOException
    {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        pw.println(prompt);
        pw.println("Do you wish to modify the prompt?");
        pw.flush();
        String response = br.readLine().toLowerCase();
        while( !(response.equals("yes") || response.equals("no")))
        {
            response = br.readLine().toLowerCase();
        }

        if(response.equals("yes"))
        {
            pw.println("Enter new prompt:");
            pw.flush();
            prompt = br.readLine();
        }

        pw.println("Do you wish to modify the choices?");
        pw.flush();

        response = br.readLine().toLowerCase();

        while( !(response.equals("yes") || response.equals("no")))
        {
            response = br.readLine().toLowerCase();
        }

        if(response.equals("yes"))
        {
            for(int i=0; i<answers.size(); i++)
                pw.println( Integer.toString(i+1) + ")" + answers.get(i));
            pw.println("Which choice do you want to modify?");
            pw.flush();

            int choice = -1;
            while(true)
            {
                String line = br.readLine();
                if(line.isEmpty())
                    continue;

                StringTokenizer tk = new StringTokenizer(line);
                try{
                    choice = Integer.parseInt(tk.nextToken());
                }catch(NumberFormatException e)
                {
                    continue;
                }

                if(choice >= 1 && choice <= answers.size()) 
                    break;
            }

            pw.println("Enter new choice:");
            pw.flush();

            answers.set(choice-1, br.readLine());
        }
    }

    public String getAnswer(OutputStream out, InputStream in) throws IOException
    {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        pw.println("Enter number corresponding to answer:");
        pw.flush();
        String line;
        int response = -1;
        while(true)
        {
            line = br.readLine();
            if(line.isEmpty())
                continue;

            StringTokenizer tk = new StringTokenizer(line);
            try{
                response = Integer.parseInt(tk.nextToken());
            }catch(NumberFormatException e)
            {
                continue;
            }

            if(response >= 1 && response <= answers.size())
                break;
        }

        return Integer.toString(response);
    }

    public String type()
    {
        return "multiple choice";
    }

    public boolean isGradable()
    {
        return true;
    }
}
