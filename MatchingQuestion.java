
import java.io.*;
import java.util.*;

public class MatchingQuestion implements Question,Serializable
{
    private String prompt = null;
    private ArrayList<String> elements;
    private ArrayList<String> right_elements;
 
    public void display(OutputStream out)
    {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
        pw.println(prompt);
        
        for(int i=0; i<elements.size(); i++)
            pw.println(elements.get(i) + " " + Integer.toString(i+1)+")" + right_elements.get(i));
        pw.flush();
    }

    public void createQuestion(OutputStream out, InputStream in) throws IOException
    {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        pw.println("Enter prompt:");
        pw.flush();
        prompt = br.readLine();

        pw.println("Enter left hand elements seperated by line breaks, to finish enter empty line:");
        pw.flush();

        elements = new ArrayList<String>();
        String line;
        line = br.readLine();
        while(!line.isEmpty())
        {
            elements.add(line); 
            line = br.readLine();
        }

        pw.println("Enter right hand elements seperated by line breaks");
        pw.flush();

        right_elements = new ArrayList<String>();
        for(int i=0; i<elements.size(); ++i)
        {
            line = br.readLine();
            right_elements.add(line); 
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

        pw.println("Do you wish to modify the left elements?");
        pw.flush();

        response = br.readLine().toLowerCase();

        while( !(response.equals("yes") || response.equals("no")))
        {
            response = br.readLine().toLowerCase();
        }

        if(response.equals("yes"))
        {
            for(int i=0; i<elements.size(); i++)
                pw.println( Integer.toString(i+1) + ")" + elements.get(i));
            pw.println("Which left element do you want to modify?");
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

                if(choice >= 1 && choice <= elements.size()) 
                    break;
            }

            pw.println("Enter new element:");
            pw.flush();

            elements.set(choice-1, br.readLine());
        }

        pw.println("Do you wish to modify the right elements?");
        pw.flush();

        response = br.readLine().toLowerCase();

        while( !(response.equals("yes") || response.equals("no")))
        {
            response = br.readLine().toLowerCase();
        }

        if(response.equals("yes"))
        {
            for(int i=0; i<right_elements.size(); i++)
                pw.println( Integer.toString(i+1) + ")" + right_elements.get(i));
            pw.println("Which right element do you want to modify?");
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

                if(choice >= 1 && choice <= elements.size()) 
                    break;
            }

            pw.println("Enter new element:");
            pw.flush();

            right_elements.set(choice-1, br.readLine());
        }
    }

    public String getAnswer(OutputStream out, InputStream in) throws IOException
    {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        pw.println("Enter numbers corresponding to matching right hand element, seperated by line breaks:");
        pw.flush();
        ArrayList<Integer> answer = new ArrayList<Integer>();
        for(int i=0; i<elements.size(); ++i)
        {
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

                if(response >= 1 && response <= elements.size() && elements.indexOf(response) == -1 )
                    break;
            }
            answer.add(response);
        }

        return answer.toString();
    }

    public String type()
    {
        return "matching";
    }

    public boolean isGradable()
    {
        return true;
    }

}
