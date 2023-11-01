//3 threads: 5135 ms
//4 threads: 8711 ms        
//it takes time to start a new thread and also depends on the starting point of each thread

import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.lingala.zip4j.core.*;
import net.lingala.zip4j.exception.*;

public class Main {

    static int length = 3;
    static ArrayList<char[]> passwords = new ArrayList<>();
    static char[] password = new char[length];
    
    //recursive method, increments the letter to the left
    static public void nextLetter(int length){
        if(length >= 1){
            password[length-1]++;
            if(password[length-1] > 'z'){
                password[length-1] = 'a';
                nextLetter(length-1);
            }
        }
    }
    
    //fill password array
    public static void initializeArray(){
        for(int i = 0; i < length; i++){
            password[i] = 'a';
        }

        for(int i = 1; i < Math.pow(26.0, length); i++){
            password[password.length-1]++;
            if(password[password.length-1] > 'z'){
                password[password.length-1] = 'a';
                nextLetter(password.length-1);
            }
            passwords.add(Arrays.copyOf(password, length));
        }
    }

    public static void main(String[] args) throws ZipException, InterruptedException {
        //number of worker threads
        double numThreads = 4.0;
        
        //holds threads
        ArrayList<PasswordCrackerThread> threads = new ArrayList<>();

        //holds zip files
        ArrayList<ZipFile> zipFiles = new ArrayList<>();
        
        long timeStart, timeEnd;

        //clean up zip files
        for(int i = 0; i < numThreads; i++){
            try {
                Files.delete(Path.of("src/protected" + length + i + ".zip"));
            } catch (IOException e) {
                System.out.println("no file");
            }
        }
        
        //record the time of start
        timeStart = System.currentTimeMillis();

        //generate all passwords
        initializeArray();
        
        //create copies of zip file for threads to work on
        for(int i = 0; i < numThreads; i++){
            try {
                Files.copy(Path.of("src/protected" + length + ".zip"), Path.of("src/protected" + length + i + ".zip"));
            } catch (IOException e) {
                System.out.println("failed");
            }
        }

        //create ZipFile objects
        for(int i = 0; i < numThreads; i++){
            zipFiles.add(new ZipFile("src/protected" + length + i + ".zip"));
        }
        
        //calculate work divisions for threads
        int indexPerThread = (int) (passwords.size() / numThreads);
        int firstIndex = 0;
        int lastIndex = firstIndex + indexPerThread;

        //initialize threads
        for(int i = 0; i < numThreads; i++){
            System.out.println(firstIndex + " " + lastIndex);
            threads.add(new PasswordCrackerThread(zipFiles.get(i), firstIndex, lastIndex, i)); 
            threads.get(i).start();  
            
            firstIndex = lastIndex + 1;
            lastIndex = firstIndex + indexPerThread;
            if(lastIndex > passwords.size()){
                lastIndex = passwords.size();
            }
            
        }

        //wait on threads
        for(int i = 0; i < numThreads; i++){
            threads.get(i).join();
        }

        //record end time
        timeEnd = System.currentTimeMillis();
        
        //display total time to standard output
        System.out.println(timeEnd - timeStart);

        //delete contents folder
        try {
            Files.delete(Path.of("contents"));
        } catch (Exception e) {
            System.out.println("Failed3");
        }
        
        //delete copies of zip files
        for(int i = 0; i < numThreads; i++){
            try {
                Files.delete(Path.of("src/protected" + length + i + ".zip"));
            } catch (IOException e) {
                System.out.println("no files");
            }
        }
        
    }
}
