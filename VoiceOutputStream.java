import java.io.*;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class VoiceOutputStream extends OutputStream {

    public Voice voice;
    public byte[] buffer = new byte[1024];
    public int index = 0;

    public VoiceOutputStream()
    {
        String voiceName = "kevin16";
        
        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice newVoice = voiceManager.getVoice(voiceName);
        newVoice.allocate();
        this.voice = newVoice;
    }

    public void write(int b) throws IOException
    {
        byte lowByte = (byte)(b & 0xFF);
        if(lowByte == '\n')
        {
            buffer[index] = '\0';
            voice.speak(new String(buffer, "UTF-8"));
            index = 0;
        }
        else
        {
            buffer[index] = lowByte;
            index++;
        }
    }
}
