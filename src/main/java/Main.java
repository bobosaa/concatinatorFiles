import java.io.*;
import java.util.*;

/**
 * Created by heymn on 03.10.2019.
 */
public class Main {

    public static void main(String[] args) throws IOException {

        HashMap<String, FileConcatinator> inputFileMap = new HashMap();
        Support argsFilter = new Support(args);
        Main main = new Main();

        for (int i = 0; i < argsFilter.getInputFile().size(); i++) {
            FileConcatinator fileConcatinator = new FileConcatinator(argsFilter.getInputFile().get(i));
            inputFileMap.put(argsFilter.getInputFile().get(i), fileConcatinator);
        }

        Boolean ascSort;
        if (argsFilter.getSort()){
            ascSort = true;
        }else{
            ascSort = false;
        }

        switch (argsFilter.getType()) {
            case ("str"):
                main.strType(inputFileMap, argsFilter.getOutputFile(), ascSort);
                break;
            case ("numbers"):
                main.intType(inputFileMap, argsFilter.getOutputFile(), ascSort);
                break;
            case ("error"):
                System.err.println("Error in type file");
        }


    }



    //Метод чтения для обработки файлов типа Число
    private void intType(Map<String, FileConcatinator> inputFileMap, String outputStr, Boolean ascSort) throws IOException {
        //Запускаем Writer для записи в выходной файл
        FileWriter writer = new FileWriter(outputStr);


        //Запускаем чтение потоком из всех входных файлов
        for (Map.Entry<String, FileConcatinator> entry : inputFileMap.entrySet()) {
            entry.getValue().fileReaderName();
        }

        //Создаём временные переменные
        //Список файлов, которые были прочитаны полностью
        List<String> outFiles = new ArrayList<>();
        //Мапу, в которой содержим данные - какой файл прочитан, какие данные были в прочитаной строке
        HashMap<String,Integer> tempArr = new HashMap();
        //Счётчик, какое количество файлов уже были прочитаны полностью
        int count = 0;
        // Количество файлов, которые мы должны проверить
        int size = inputFileMap.size();

        //Смотрим, пока программа не прочитает все файлы, кроме последнего
        while (count <= size - 1){

            //Если входной файл последний, то нет нужды его сортировать
            //Поэтому просто переписываем всё его содержимое в результирующий файл
            if (count == size - 1){
                for(Map.Entry<String, FileConcatinator> entry : inputFileMap.entrySet()) {
                    //Не забываем строчку, которую прочитали до этого
                    if (!spaceInLine(tempArr.get(entry.getKey()).toString())){
                        writer.write(tempArr.get(entry.getKey()).toString());
                        if(entry.getValue().hasNextLine()) {writer.write("\n");}
                    } else {
                        System.err.println("File " + entry.getKey()+ "has a space symbol at <"+tempArr.get(entry.getKey())+"> line" );
                    }

                    //Пока файл не закончится - переписываем текущую строку в результирующий файл
                    while (entry.getValue().hasNextLine()) {
                        String temp = entry.getValue().getLine();
                        if (!spaceInLine(temp)){
                            if (!isBBiggerA(tempArr.get(entry.getKey()).toString(), temp, ascSort, "numbers")){
                                writer.write(temp);
                                if(entry.getValue().hasNextLine()) {writer.write("\n");}
                                tempArr.put(entry.getKey(),Integer.parseInt(temp));
                            } else {
                                System.err.println("File " + entry.getKey()+ " is not sorted correctly");
                                break;
                            }

                        } else {
                            System.err.println("File " + entry.getKey()+ "has a space symbol at <"+temp+"> line" );
                        }
                    }
                }
                //выходим из цикла, т.е. считали последний файл
                break;

                //Если существует несколько файлов для чтения
            } else {
                for(Map.Entry<String, FileConcatinator> entry : inputFileMap.entrySet()) {
                    //Проверяем, нужно ли читать следующую строчку файла и есть ли следующая строчка
                    //Если оба условия выполняются - считываем следующую строчку
                    //Помечаем, что файл читать не нужно
                    while(entry.getValue().isNeedToRead() & entry.getValue().hasNextLine()){
                        String temp = entry.getValue().getLine();
                        if (!spaceInLine(temp)){
                            if (!isBBiggerA(tempArr.get(entry.getKey()).toString(), temp, ascSort, "numbers")){
                                tempArr.put(entry.getKey(), Integer.valueOf(temp));
                                entry.getValue().needToRead(false);
                            } else {
                                entry.getValue()._hasNextLine = false;
                                System.err.println("File " + entry.getKey()+ " is not sorted correctly");
                                break;
                            }
                        } else{
                            System.err.println("File " + entry.getKey()+ "has a space symbol at <"+temp+"> line" );
                        }
                    }
                    //Если следующей строчки в файле нет (файл закончится) и файла нет в списке на удаление
                    //Заносим этот файл в список на удаление
                    if (!entry.getValue().hasNextLine()){
                        if (!outFiles.contains(entry.getKey()))
                            outFiles.add(entry.getKey());
                    }

                }

                //Создаём 2 переменные, которые отмечают текущий минимальный элемент
                    //Его значение
                Integer minValue = null;
                    //Из какого он файла
                String minKey = null;

                //Перебираем ряд чисел в мапе, смотрим на подходящее число
                for (Map.Entry<String, Integer> entry : tempArr.entrySet()) {
                    if (minValue == null) {
                        minValue = entry.getValue();
                        minKey = entry.getKey();
                    } else {
                        //Если сортировка по возрастанию, то ищем минимальное число
                        if (ascSort) {
                            if (minValue > entry.getValue()) {
                                minValue = entry.getValue();
                                minKey = entry.getKey();
                            }
                        } else {
                            //Если сортировка по убиыванию, то ищем максимальное число
                            if (minValue < entry.getValue()) {
                                minValue = entry.getValue();
                                minKey = entry.getKey();
                            }
                        }
                    }
                }


                //Файл с наименьшим значением разрешаем для чтения в следующей итерации
                inputFileMap.get(minKey).needToRead(true);
                //Записываем результат в результирующий файл
                writer.write(minValue.toString());
                writer.write("\n");


                //Проверяем, есть ли файлы, из которых мы всё считали
                //Если в списке законченных для чтения файлов есть любой файл, смотрим можем ли мы удалить такие файлы
                if (outFiles.size() > 0){
                    for (int i = 0; i < outFiles.size();i++){
                        //Файл ещё не должен быть удалён
                        if(inputFileMap.containsKey(outFiles.get(i)) == true){
                            //Файл отмечен на чтение, и в следующей итерации из него попробуют считать несущствующую строку
                            if (inputFileMap.get(outFiles.get(i)).isNeedToRead() == true){
                                //Файл удаляется из списка на списка входных файлов
                                inputFileMap.remove(outFiles.get(i));
                                //Файл удаляется из мапы с данными
                                tempArr.remove(outFiles.get(i));
                                //Увеличиваем счётчик прочитанных файлов на 1
                                count++;
                            }
                        }
                    }
                }

            }
        }

        //Закрываем все потоки
        for(Map.Entry<String, FileConcatinator> entry : inputFileMap.entrySet()) {
            entry.getValue().close();
        }
        writer.close();

    }




    //Метод чтения для обработки файлов типа Строка
    private void strType(Map<String, FileConcatinator> inputFileMap, String outputStr, Boolean ascSort) throws IOException {

        //Запускаем Writer для записи в выходной файл
        FileWriter writer = new FileWriter(outputStr);
        //Запускаем чтение потоком из всех входных файлов
        for (Map.Entry<String, FileConcatinator> entry : inputFileMap.entrySet()) {
            entry.getValue().fileReaderName();
        }

        //Создаём временные переменные
            //Список файлов, которые были прочитаны полностью
        List<String> outFiles = new ArrayList<>();
            //Мапу, в которой содержим данные - какой файл прочитан, какие данные были в прочитаной строке
        HashMap<String,String> tempArr = new HashMap();
            //Счётчик, какое количество файлов уже были прочитаны полностью
        int count = 0;
            // Количество файлов, которые мы должны проверить
        int size = inputFileMap.size();

        //Смотрим, пока программа не прочитает все файлы, кроме последнего
        while (count <= size - 1){

            //Если входной файл последний, то нет нужды его сортировать
            //Поэтому просто переписываем всё его содержимое в результирующий файл
            if (count == size - 1){
                for(Map.Entry<String, FileConcatinator> entry : inputFileMap.entrySet()) {
                    //Не забываем строчку, которую прочитали до этого
                    if (!spaceInLine(tempArr.get(entry.getKey()))){
                        writer.write(tempArr.get(entry.getKey()));
                        if(entry.getValue().hasNextLine()) {writer.write("\n");}
                    } else {
                        System.err.println("File " + entry.getKey()+ "has a space symbol at <"+tempArr.get(entry.getKey())+"> line" );
                    }

                    //Пока файл не закончится - переписываем текущую строку в результирующий файл
                    while (entry.getValue().hasNextLine()) {
                        String temp = entry.getValue().getLine();
                        if (!spaceInLine(temp)){
                            writer.write(temp);
                            if(entry.getValue().hasNextLine()) {writer.write("\n");}
                        } else {
                            System.err.println("File " + entry.getKey()+ "has a space symbol at <"+temp+"> line" );
                        }
                    }
                }
                //выходим из цикла, т.е. считали последний файл
                break;

            //Если существует несколько файлов для чтения
            } else {
                for(Map.Entry<String, FileConcatinator> entry : inputFileMap.entrySet()) {
                    //Проверяем, нужно ли читать следующую строчку файла и есть ли следующая строчка
                    //Если оба условия выполняются - считываем следующую строчку
                    //Помечаем, что файл читать не нужно
                    while(entry.getValue().isNeedToRead() & entry.getValue().hasNextLine()){
                        String temp = entry.getValue().getLine();
                        if (!spaceInLine(temp)){
                            tempArr.put(entry.getKey(), temp);
                            entry.getValue().needToRead(false);
                        } else{
                            System.err.println("File " + entry.getKey()+ "has a space symbol at <"+temp+"> line" );
                        }
                    }
                    //Если следующей строчки в файле нет (файл закончится) и файла нет в списке на удаление
                    //Заносим этот файл в список на удаление
                    if (!entry.getValue().hasNextLine()){
                        if (!outFiles.contains(entry.getKey()))
                            outFiles.add(entry.getKey());
                    }
                }

                //Создаём 2 переменные, которые отмечают текущий минимальный элемент
                    //Его значение
                String minValue = null;
                    //Из какого он файла
                String minKey = null;

                //Перебираем все строки в мапе, смотрим подходящую
                for (Map.Entry<String, String> entry : tempArr.entrySet()){
                    if (minValue == null) {
                        minValue = entry.getValue();
                        minKey = entry.getKey();
                    } else {
                        //Если сортировка по возростанию, то ищем минимальную строку
                        if (ascSort){
                            if (minValue.compareTo(entry.getValue()) > 0){
                                minValue = entry.getValue();
                                minKey = entry.getKey();
                            }
                        //Если сортировка по убиванию, то ищем максимальную строку
                        } else {
                            if (minValue.compareTo(entry.getValue()) < 0){
                                minValue = entry.getValue();
                                minKey = entry.getKey();
                            }
                        }
                    }
                }


                //Файл с наименьшим значением разрешаем для чтения в следующей итерации
                inputFileMap.get(minKey).needToRead(true);
                //Записываем результат в результирующий файл
                writer.write(minValue);
                writer.write("\n");


                //Проверяем, есть ли файлы, из которых мы всё считали
                //Если в списке законченных для чтения файлов есть любой файл, смотрим можем ли мы удалить такие файлы
                if (outFiles.size() > 0){
                    for (int i = 0; i < outFiles.size();i++){
                        //Файл ещё не должен быть удалён
                        if(inputFileMap.containsKey(outFiles.get(i)) == true){
                            //Файл отмечен на чтение, и в следующей итерации из него попробуют считать несущствующую строку
                            if (inputFileMap.get(outFiles.get(i)).isNeedToRead() == true){
                                //Файл удаляется из списка на списка входных файлов
                                inputFileMap.remove(outFiles.get(i));
                                //Файл удаляется из мапы с данными
                                tempArr.remove(outFiles.get(i));
                                //Увеличиваем счётчик прочитанных файлов на 1
                                count++;
                            }
                        }
                    }
                }

            }
        }

        //Закрываем все потоки
        for(Map.Entry<String, FileConcatinator> entry : inputFileMap.entrySet()) {
            entry.getValue().close();
        }
        writer.close();

    }

    private Boolean spaceInLine (String line){
        Boolean spaceTrue = false;
        int index = line.indexOf(" ");
        if (index > -1) {spaceTrue = true;}

        return spaceTrue;
    }

    private Boolean isBBiggerA (String a, String b, Boolean ascSort, String type){

        Boolean bigger = true;

        if (ascSort) {
            if (type == "str") {
                if (a.compareTo(b) > 0) {
                    bigger = false;
                } else {
                    bigger = true;
                }
            }
            if (type == "numbers") {
                if (Integer.parseInt(a) > Integer.parseInt(b)) {
                    bigger = false;
                } else {
                    bigger = true;
                }
            }
        }
        if (!ascSort){
            if (type == "str") {
                if (a.compareTo(b) < 0) {
                    bigger = false;
                } else {
                    bigger = true;
                }
            }
            if (type == "numbers"){
                if (Integer.parseInt(a) < Integer.parseInt(b)){
                    bigger = false;
                } else {
                    bigger = true;
                }
            }
        }


        return bigger;
    }



}
