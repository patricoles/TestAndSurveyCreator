import java.util.*;
import java.io.*;

public class Menu
{
    private boolean persistent;
    private ArrayList<String> menuTexts;
    private ArrayList<Runnable> menuActions;

    public Menu(boolean persistent)
    {
        this.persistent = persistent;
        this.menuTexts = new ArrayList<String>();
        this.menuActions = new ArrayList<Runnable>();
    }

    public void addMenuItem(String text, Runnable action)
    {
        menuTexts.add(text);
        menuActions.add(action);
    } 

    public void run(OutputStream out, InputStream in) throws IOException
    {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        int quit_response = menuTexts.size()+1;
        int response = -1;
        do
        { 
            for(int i=0; i<menuTexts.size(); i++)
                pw.println( Integer.toString(i+1) + ")" + menuTexts.get(i));
            
            if(persistent)
                pw.println( Integer.toString(quit_response) + ")Quit");

            pw.flush();
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

                if(response >= 1 && (response < quit_response || (persistent == true && response == quit_response)))
                    break;
            }

            if(response != quit_response)
                menuActions.get(response-1).run();

        }while(persistent && response != quit_response);
    }
}
