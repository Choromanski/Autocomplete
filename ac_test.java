import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ac_test{

    static DLB dictionary = new DLB();
    static DLB user_history = new DLB();
    static char input;
    static int numRuns = 0;
    static double startTime, endTime, totalTime;
    static String word, print = "";
    static ArrayList<String> allPredictions, historicPredictions = new ArrayList<String>(), previousPredictions = new ArrayList<String>(), predictions = new ArrayList<String>();

    public static void main(String[] args){
        System.out.println("\nGenerating Dictionary Trie...\n");
        try{
            double time = System.nanoTime();
            Scanner dictionaryScanner = new Scanner(new File("dictionary.txt"));
            while(dictionaryScanner.hasNextLine()){
                dictionary.add(dictionaryScanner.nextLine());
            }
            time = System.nanoTime() - time;
            System.out.print("Generated Dictionary Trie In ");
            System.out.format("%f", (time) / 1000000000);
            System.out.println(" Seconds\n");
        }catch(IOException e){
            System.out.println("dictionary.txt not found\n");
        }
        System.out.println("Generating User History Trie...\n");
        try{
            double time = System.nanoTime();
            Scanner historyScanner = new Scanner(new File("user_history.txt"));
            while(historyScanner.hasNextLine()){
                user_history.add(historyScanner.nextLine());
            }
            time = System.nanoTime() - time;
            System.out.print("Generated User History Trie In ");
            System.out.format("%f", (time) / 1000000000);
            System.out.println(" Seconds\n\n");
        }catch(FileNotFoundException e){
            System.out.println("ERROR: user_history.txt not found\nCreating file now...\n");
            try{
                FileWriter fileWriter = new FileWriter(new File("user_history.txt"));
                fileWriter.close();
            }catch(IOException e1){
                System.out.println("ERROR creating user_history.txt\n");
            }
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your first character: ");
        do{
            word = "";
            if(numRuns > 0){
                System.out.print("Enter first Character of the next word: ");
            }
            input = scanner.nextLine().charAt(0);
            previousPredictions.clear();
            dictionary.resetSuggestions();
            user_history.resetSuggestions();
            while(input >= 'A'){
                predictions.clear();
                word = word + input;
                int j = 0;
                print = "";
                startTime = System.nanoTime();
                allPredictions = dictionary.getSuggestions(input);
                historicPredictions = user_history.getSuggestions(input);
                for(int i = 0; i < historicPredictions.size(); i++){
                    predictions.add(historicPredictions.get(i));
                    print = print + "(" + (i + 1) + ")" + historicPredictions.get(i) + "    ";
                }
                for(int i = historicPredictions.size(); i < 5; i++){
                    while(allPredictions.size() > j && (previousPredictions.contains(allPredictions.get(j)) || historicPredictions.contains(allPredictions.get(j)))){
                        j++;
                    }
                    if(allPredictions.size() > j){
                        previousPredictions.add(allPredictions.get(j));
                        predictions.add(allPredictions.get(j));
                        print = print + "(" + (i + 1) + ") " + allPredictions.get(j) + "    ";
                        j++;
                    }
                }
                endTime = System.nanoTime();
                if(print.equals("")){
                    print = print + "No predictions found when you're done typing your word enter '$' to save it to your history";
                }
                print = print + '\n';
                System.out.print("\n(");
                System.out.format("%f", (endTime - startTime) / 1000000000);
                System.out.println(" s)\nPredictions: ");
                System.out.println(print);
                numRuns++;
                totalTime += (endTime - startTime) / 1000000000;
                System.out.print("Enter the next character: ");
                input = scanner.nextLine().charAt(0);
            }
            if(input > '0' && input < '6'){
                System.out.println("\n\nWORD COMPLETED:  " + predictions.get(input - '1') + "\n");
                if(!historicPredictions.contains(predictions.get(input - '1'))){
                    user_history.add(predictions.get(input - '1'));
                    try{
                        FileWriter fileWriter = new FileWriter(new File("user_history.txt"), true);
                        fileWriter.write(predictions.get(input - '1') + "\n");
                        fileWriter.close();
                    }catch(IOException e){
                        System.out.println("ERROR opening/editing/closing user_history.txt");
                    }
                }
            }else if(input == '$'){
                System.out.println("\n\nWORD COMPLETED:  " + word + "\n");
                if(!historicPredictions.contains(word)){
                    user_history.add(word);
                    try{
                        FileWriter fileWriter = new FileWriter(new File("user_history.txt"), true);
                        fileWriter.write(word + "\n");
                        fileWriter.close();
                    }catch(IOException e){
                        System.out.println("ERROR opening/editing/closing user_history.txt");
                    }
                }
            }

        }while(input != '!');
        System.out.print("\n\nAverage time:  ");
        System.out.format("%f", totalTime / numRuns);
        System.out.println(" s\nBye!");
    }
}
