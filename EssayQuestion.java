import java.io.*;

public class EssayQuestion implements Question,Serializable
{
    private String prompt = null;

    public void display(OutputStream out)
    {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
        pw.println(prompt);
        pw.flush();
    }
    
    public void createQuestion(OutputStream out, InputStream in) throws IOException
    {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        pw.println("Enter prompt:");
        pw.flush();
        prompt = br.readLine();
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
    }

    public String getAnswer(OutputStream out, InputStream in) throws IOException
    {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        pw.println("Enter answer");
        pw.flush(); 
        return br.readLine();
    }

    public String type()
    {
        return "essay";
    }

    public boolean isGradable()
    {
        return false;
    }
}
