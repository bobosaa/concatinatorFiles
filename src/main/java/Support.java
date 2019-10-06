import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heymn on 03.10.2019.
 */
public class Support {

    private Boolean ascSort = true;
    private Boolean descSort = false;
    private Boolean numbers = false;
    private Boolean str = false;
    private String outputFile;
    private List<String> inputFile = new ArrayList<>();
    private String[] args;
    private int indexOutFile;

    public Support(String[] args){
        this.args = args;
        sortTypeFinder();
        outputFileFinder();
        inputFileFinder();
    }


    private void sortTypeFinder (){

        for (int i = 0; i < 2; i++){
            switch (args[i]){
                case ("-a"):
                    ascSort = true;
                    descSort = false;
                    break;
                case ("-d"):
                    ascSort = false;
                    descSort = true;
                    break;
                case ("-s"):
                    indexOutFile = i + 1;
                    numbers = false;
                    str = true;
                    break;
                case ("-i"):
                    indexOutFile = i + 1;
                    numbers = true;
                    str = false;
                    break;
                default:
                    System.err.println("Unknown argument " + args[i]);
                    break;
            }
        }
    }

    private void outputFileFinder (){
        File file = new File(args[indexOutFile]);
        if (file.exists()) {
            outputFile = args[indexOutFile];
        } else {
            System.err.println("Output file "+ args[indexOutFile] +" not found. Program exits");
            System.exit(0);
        }

    }

    private void inputFileFinder (){
        int count = 0;
        for (int i = indexOutFile + 1; i < args.length; i++){
            File file = new File(args[i]);
            if (file.exists()) {
                inputFile.add(args[i]);
            } else {
                count++;
                System.err.println("Input file "+ args[i] +" not found");
            }
        }

        if (count == args.length - indexOutFile - 1){
            System.err.println("No input files found. Program exits");
            System.exit(0);
        }


    }

    public Boolean getSort(){
        Boolean sort = true;
        if (ascSort == true)
            sort = true;
        if (descSort == true)
            sort = false;
       return sort;
    }

    public String getType(){
        String type;
        if (numbers){
            type = "numbers";
        } else
            if (str){
                type = "str";
            } else {
                type = "error";
            }

        if ((numbers&str)|(!numbers&!str)){
            System.err.println("Incorrect data type. Please, choose one of types");
            System.exit(0);
        }

        return type;
    }

    public String getOutputFile() {

        return outputFile;
    }

    public List<String> getInputFile(){
        return inputFile;
    }
}
