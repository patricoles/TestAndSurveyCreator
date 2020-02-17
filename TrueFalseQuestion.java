import java.io.*;

public class TrueFalseQuestion implements Question,Serializable
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
        pw.println("Enter new prompt:");
        pw.flush();
        prompt = br.readLine();
    }

    public String getAnswer(OutputStream out, InputStream in) throws IOException
    {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        pw.println("Enter answer (t or f):");
        pw.flush();

        String line;
        while(true)
        {
            line = br.readLine();
            if(line.isEmpty())
                continue;
            line = line.substring(0,1).toLowerCase();
            if(line.equals("t") || line.equals("f"))
                break;
        }

        return line;
    }

    public String type()
    {
        return "T/F";
    }

    public boolean isGradable()
    {
        return true;
    }
}
