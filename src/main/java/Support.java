import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heymn on 03.10.2019.
 */
//Класс, отвечающий за обработку агрументов приложения
public class Support {

    //Тип сортировки - по возрастанию. По условия задачи по умолчанию этот тип
    private Boolean ascSort = true;
    //Тип сортировки - по убыванию
    private Boolean descSort = false;
    //Тип данных - числа
    private Boolean numbers = false;
    //Тип данных - строки
    private Boolean str = false;
    //Название выходного файла
    private String outputFile;
    //Название всех входных файлов
    private List<String> inputFile = new ArrayList<>();
    //Сам массив аргументов
    private String[] args;
    //Место в аргументах, где находится название выходного файла
    private int indexOutFile;

    //Создаём новый экземпляр класса.
    public Support(String[] args){
        this.args = args;
        try {
            sortTypeFinder();
        }catch (ArrayIndexOutOfBoundsException e){
            System.err.println("Enter program data type arguments. Program exits");
            System.exit(0);
        }

        try {
            outputFileFinder();
        }catch (ArrayIndexOutOfBoundsException e){
            System.err.println("Enter the name of the output file. Program exits");
            System.exit(0);
        }

        inputFileFinder();
    }


    //Узнаём, какой тип данных и вид сортировки выбирает пользователь.
    //Узнаём место, в котором находится выходной файл
    //Проверяем на ошибку ввода аргументов
    private void sortTypeFinder () throws ArrayIndexOutOfBoundsException{
        int i = 0;
        Boolean type = false;
        while((i != 2) & (type != true)){
            switch (args[i]){
                case ("-a"):
                    ascSort = true;
                    descSort = false;
                    i++;
                    break;
                case ("-d"):
                    ascSort = false;
                    descSort = true;
                    i++;
                    break;
                case ("-s"):
                    indexOutFile = i + 1;
                    numbers = false;
                    str = true;
                    type = true;
                    i++;
                    break;
                case ("-i"):
                    indexOutFile = i + 1;
                    numbers = true;
                    str = false;
                    i++;
                    type = true;
                    break;
                default:
                    System.err.println("Unknown argument " + args[i]);
                    i++;
                    break;
            }
        }
    }

    //Узнаём имя выходного файла
    //Проверяем файл на существование, если такого файла нет - выдаём ошибку и заканчиваем работу
    private void outputFileFinder () throws ArrayIndexOutOfBoundsException {
        File file = new File(args[indexOutFile]);
        if (file.exists()) {
            outputFile = args[indexOutFile];
        } else{
            System.err.println("Output file "+ args[indexOutFile] +" not found. Program exits");
            System.exit(0);
        }

    }

    //Узнаём имена выходных файлов
    //Проверяем файл на существование, если такого файла нет - выдаём ошибку
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
        //Если нет ни одного из входных из файлов - выдаём ошибку и заканчиваем работу
        if (count == args.length - indexOutFile - 1){
            System.err.println("No input files found. Program exits");
            System.exit(0);
        }


    }

    //Получаем тип сортировки
    public Boolean getSort(){
        Boolean sort = true;
        if (ascSort == true)
            sort = true;
        if (descSort == true)
            sort = false;
       return sort;
    }

    //Получаем тип данных.
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
        //Если тип не указан, или указано 2 типа - выдаём ошибку
        if ((numbers&str)|(!numbers&!str)){
            System.err.println("Incorrect data type. Please, choose one of types");
            System.exit(0);
        }

        return type;
    }

    //Получаем имя выходного файла
    public String getOutputFile() {

        return outputFile;
    }

    //Получаем список входных файлов
    public List<String> getInputFile(){
        return inputFile;
    }
}
