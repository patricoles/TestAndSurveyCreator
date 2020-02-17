import java.io.*;

public interface Question extends Serializable
{
    public void display(OutputStream out);
    public String getAnswer(OutputStream out, InputStream in) throws IOException;
    public void createQuestion(OutputStream out, InputStream in) throws IOException;
    public void modify(OutputStream out, InputStream in) throws IOException;
    public String type();
    public boolean isGradable();
}
