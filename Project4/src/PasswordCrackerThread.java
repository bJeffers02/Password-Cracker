import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.lingala.zip4j.core.*;
import net.lingala.zip4j.exception.*;

public class PasswordCrackerThread extends Thread{
    ZipFile zipFile;
    int lastIndex, firstIndex;
    int id;

    //constructor
    public PasswordCrackerThread(ZipFile zipFile, int firstIndex, int lastIndex, int id){
        this.zipFile = zipFile;
        this.lastIndex = lastIndex;
        this.firstIndex = firstIndex;
        this.id = id;
    }

    @Override
    public void run(){
        int count = 1;
        for(int i = firstIndex; i < lastIndex; i++){
            
            //finishes thread if finished boolean is set to true
            if(ThreadHandler.isFinished()){
                break;
            }

            //attempts password
            try{
                zipFile.setPassword(Main.passwords.get(i));
                zipFile.extractAll("contents/" + id);
                System.out.println("Successfully cracked: " + new String(Main.passwords.get(i)) + "\nTried in this thread: " + count);    
                ThreadHandler.finishThreads();
                break;
            }catch(ZipException ze){
                
            }catch(Exception e){
                e.printStackTrace();
            }
            count++;
        }

        //clean up contents folder
        try {
            Files.delete(Path.of("contents/"+id+"/message.txt"));
        } catch (IOException e) {
            System.out.println("Failed1");
        }
        try {
            Files.delete(Path.of("contents/"+id));
        } catch (Exception e) {
            System.out.println("Failed1");
        }
    }
}
