import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.runtime.ObjectMethods;
import java.sql.SQLOutput;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Main {
    public  static final void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to the Number Guessing Game! \n" +
                " I'm thinking of a number between 1 and 100.  \n" +
                " You have 5 chances to guess the correct number.");
        ObjectMapper objectMapper = new ObjectMapper();
        File file  = new File("maxCnt.txt");
        Boolean  fileExist=false;
        Map<Integer,Integer>mp = new HashMap<>();

        fileExist = getFileExist(file, mp, objectMapper, fileExist);

        System.out.println("Do you wish to check the maximum score...type yes or no ");
        String checkScore  = input.nextLine();

        checkExistingFile(fileExist, checkScore, objectMapper);

        Boolean flag = true;
        double noTakenByComputer = Math.random();
        int noTaken = (int)(noTakenByComputer *(double) 100);

        while(flag){
            startComment();
            int totalAttemptTaken=0;
            int difficulty = input.nextInt();
            Instant timeStart =Instant.now();

            input.nextLine();
            
            int noOfChances =10;
            if(difficulty==2)noOfChances=5;
            else if(difficulty==3)noOfChances=3;
            else if (difficulty==1 ){
               noOfChances=1;
            }else {
                System.out.println("Input no. is incorrect. provide correct no.");
                continue;
            }
            System.out.println("Great you have entered game \n" +
                    "you  have "+ noOfChances +" chance remaining to guess ");
           
            while(noOfChances>0){
                totalAttemptTaken++;
                System.out.println("Enter your choice : ");
                int outPut = input.nextInt();
                input.nextLine();
                if(outPut==noTaken){
                    guessNumber(noTaken, totalAttemptTaken, timeStart, objectMapper, difficulty);
                    return;
                }
                getHint(input, outPut, noTaken);
                noOfChances--;
            }
            System.out.println("You have exhausted all your chances , do you which to play more. Confirm  with yes or no ");
            String moreChance = input.nextLine();
            if(moreChance.compareTo("no")==0) {
                flag = false;
                System.out.println("Well Played. Correct ans was "+ noTaken);
            }

        }
    }

    private static void checkExistingFile(Boolean fileExist, String checkScore, ObjectMapper objectMapper) throws IOException {
        if(fileExist){
            if(checkScore.compareTo("yes")==0 ){
                Map<Integer,Integer>map= objectMapper.readValue(new File("maxCnt.txt"),new TypeReference<Map<Integer,Integer>>() {});
                map.forEach((key,value)->{
                    String dificulty;
                    if(key==1)dificulty="Easy";
                    else if(key==2)dificulty="Medium";
                    else if(key==3)dificulty="Hard";
                    System.out.println("for difficulty "+key+ " Minimum attempt taken are "+ value);
                });
            }
        }else System.out.println("No previous record present ");
    }

    private static Boolean getFileExist(File file, Map<Integer, Integer> mp, ObjectMapper objectMapper, Boolean fileExist) throws IOException {
        if(!file.exists()){
            mp.put(1,1000);
            mp.put(2,1000);
            mp.put(3,1000);
            objectMapper.writerWithDefaultPrettyPrinter().
                    writeValue(new File("maxCnt.txt"), mp);

        }
        else fileExist =true;
        return fileExist;
    }

    private static void getHint(Scanner input, int outPut, int noTaken) {
        System.out.println("Its a wrong guess....Do you want to take hint.");
        String hint = input.nextLine();

        if(hint.compareTo("yes")==0&& outPut < noTaken){
            System.out.println("Number is higher than "+ outPut);
        }
        else if (hint.compareTo("yes")==0&& outPut > noTaken) {
            System.out.println("Number is lower than "+ outPut);
        }
    }

    private static void guessNumber(int noTaken, int totalAttemptTaken, Instant timeStart, ObjectMapper objectMapper, int difficulty) throws IOException {
        Map<Integer, Integer> mp;
        System.out.println("you have guessed it correctly , i was thinking of number "+ noTaken +"\n" +
                "you have taken "+ totalAttemptTaken +" attempt to guess");
        Duration timeTaken = Duration.between(timeStart,Instant.now());
        System.out.println("time taken " + timeTaken);
        mp = objectMapper.readValue(new File("maxCnt.txt"), new TypeReference<Map<Integer, Integer>>() {});
        Integer valFile = mp.get(difficulty);
        if(valFile > totalAttemptTaken) {
            valFile = totalAttemptTaken;
            mp.put(difficulty,valFile);
            System.out.println(mp);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("maxCnt.txt"),mp);
        }

        return;
    }

    private static void startComment() {
        System.out.println("Please select the difficulty level: \n" +
                "1) Eazy (10 chances) \n" +
                "2) Medium (5 chances) \n" +
                "3) Hard (3 chances) \n");
        System.out.println("Enter your choice: ");
    }
}
