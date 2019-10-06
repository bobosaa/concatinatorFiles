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
        ascSort = argsFilter.getSort();

        switch (argsFilter.getType()) {
            case ("str"):
                main.strType(inputFileMap, argsFilter.getOutputFile(), ascSort);
                break;
            case ("numbers"):
                main.intType(inputFileMap, argsFilter.getOutputFile(), ascSort);
                break;
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
                    //Проверяем, единственный ли это файл
                    if(!tempArr.containsKey(entry.getKey())){
                        //Если единственный - перебираем строчки до тех пор, пока не обнаружим подходящие данные
                        while (!tempArr.containsKey(entry.getKey())) {
                            //Проверка, если ли в первой строчке пробелы
                            //Если нет, то берём её, и записываем в мапу прочитанных файов и в результирующий файл
                            String firstLine = entry.getValue().getLine();
                            if (!spaceInLine(firstLine)) {
                                //Проверка на то, что внутри файла не появится строковой тип данных
                                try {
                                    tempArr.put(entry.getKey(), Integer.parseInt(firstLine));
                                    writer.write(tempArr.get(entry.getKey()).toString());
                                    //Если файл еще не закончил считываться - делаем перенос на новую строку в результирующем файле
                                    if (entry.getValue().hasNextLine()) {writer.write("\n");}
                                    //Если строковый тип считался - пишем собщение об этом
                                } catch (NumberFormatException e) {
                                    System.err.println("File " + entry.getKey() + " has string data at <" + firstLine + "> line");
                                }
                            } else {
                                //Если в строке есть пробел - выдаём сообщение об ошибке
                                System.err.println("File " + entry.getKey() + "has a space symbol at <" + tempArr.get(entry.getKey()) + "> line");
                            }
                        }
                    //если файл не единственный
                    } else {
                        //Не забываем строчку, которую прочитали до этого
                        //Проверка, если ли в строчке пробелы
                        if (!spaceInLine(tempArr.get(entry.getKey()).toString())) {
                            //Если нет пробела - записываем данные
                            writer.write(tempArr.get(entry.getKey()).toString());
                            //Если файл еще не закончил считываться - делаем перенос на новую строку в результирующем файле
                            if (entry.getValue().hasNextLine()) {writer.write("\n");}
                        } else {
                            //Если в строке есть пробел - выдаём сообщение об ошибке
                            System.err.println("File " + entry.getKey() + "has a space symbol at <" + tempArr.get(entry.getKey()) + "> line");
                        }
                    }

                    //Пока файл не закончится - переписываем текущую строку в результирующий файл
                    while (entry.getValue().hasNextLine()) {
                        String temp = entry.getValue().getLine();
                        //Проверка, если ли в строчке пробелы
                        if (!spaceInLine(temp)){
                            //Проверка на то, что внутри файла не появится строковой тип данных
                            try {
                                //Проверка на правильную сортировку файла, сверяя прошлую прочитанную строку и текущую прочитанную строку
                                if (!isBBiggerA(tempArr.get(entry.getKey()).toString(), temp, ascSort, "numbers")){
                                    //если отсортирован правильно - записываем данные в результирующий файл
                                    writer.write(temp);
                                    if(entry.getValue().hasNextLine()) {writer.write("\n");}
                                    //В мапе делаем указываем, что мы изменили значение прочинанного файла
                                    tempArr.put(entry.getKey(),Integer.parseInt(temp));
                                //Если отсортирован неправильно - выдаём сообщение об ошибке и останавливаем проложение
                                } else {
                                    System.err.println("File " + entry.getKey()+ " is not sorted correctly");
                                    break;
                                }
                            //Если строковый тип считался - пишем собщение об этом
                            } catch (NumberFormatException e){
                                System.err.println("File " + entry.getKey()+ " has string data at <" + temp + "> line");
                            }
                        } else {
                            //Если есть пробелы - пишем об этом сообщение
                            System.err.println("File " + entry.getKey()+ " has a space symbol at <" + temp + "> line" );
                        }
                    }
                }
                //выходим из цикла, т.е. считали последний файл
                break;

                //Если существует несколько файлов для чтения
            } else {
                for(Map.Entry<String, FileConcatinator> entry : inputFileMap.entrySet()) {
                    //Проверяем, нужно ли читать следующую строчку файла и есть ли следующая строчка
                    //Если оба условия выполняются - пытаемся считываем следующую строчку, если она не выдаёт ошибку
                    while(entry.getValue().isNeedToRead() & entry.getValue().hasNextLine()){
                        String temp = entry.getValue().getLine();
                        //Проверка на пробел в строке
                        if (!spaceInLine(temp)){
                            //Проверка на строковый тип данных
                            try {
                                //Если ещё ничего не считали из конкретного файла в мапу, то считываем
                                //Помечаем, что файл читать не нужно
                                if(!tempArr.containsKey(entry.getKey())){
                                    tempArr.put(entry.getKey(), Integer.valueOf(temp));
                                    entry.getValue().needToRead(false);
                                } else {
                                    //Если уже были записаны данные
                                    //Проверяем правильная ли сортировка внутри файла
                                    //Если правильная сортировка - записываем данные в мапу. Помечаем, что файл читать не нужно
                                    if (!isBBiggerA(tempArr.get(entry.getKey()).toString(), temp, ascSort, "numbers")){
                                        tempArr.put(entry.getKey(), Integer.valueOf(temp));
                                        entry.getValue().needToRead(false);
                                    //Если неверная - не читаеем данные
                                    //Выволим сообщение об ошибке
                                    //Подготавливаем файл на удаление из алгоритма
                                    //Удаляем данные из мапы, чтобы не просачивались неактуальные данные
                                    } else {
                                        entry.getValue()._hasNextLine = false;
                                        tempArr.remove(entry.getKey());
                                        System.err.println("File " + entry.getKey()+ " is not sorted correctly");
                                        break;
                                    }
                                }
                            //Если строковый тип считался - пишем собщение об этом
                            } catch (NumberFormatException e){
                                System.err.println("File " + entry.getKey()+ " has string data at <" + temp + "> line");
                            }
                        //Если есть пробелы - пишем об этом сообщение
                        } else{
                            System.err.println("File " + entry.getKey()+ " has a space symbol at <"+temp+"> line" );
                        }
                    }
                    //Если следующей строчки в файле нет (файл закончится) и файла нет в списке на удаление
                    //Заносим этот файл в список на удаление
                    if (!entry.getValue().hasNextLine()){
                        if (!outFiles.contains(entry.getKey()))
                            outFiles.add(entry.getKey());
                    }

                }

                //Проверка, что после удаления данных из мапы не остался 1 файл
                if (!(tempArr.size() == 1)) {
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
                    try{
                        assert minValue != null;
                        writer.write(minValue.toString());
                        writer.write("\n");
                    } catch (IOException e){
                        e.printStackTrace();
                    }


                }

                //Проверяем, есть ли файлы, из которых мы всё считали
                //Если в списке законченных для чтения файлов есть любой файл, смотрим можем ли мы удалить такие файлы
                if (outFiles.size() > 0){
                    for (String outFile : outFiles) {
                        //Файл ещё не должен быть удалён
                        if (inputFileMap.containsKey(outFile)) {
                            //Файл отмечен на чтение, и в следующей итерации из него попробуют считать несущствующую строку
                            if (inputFileMap.get(outFile).isNeedToRead()) {
                                //Файл удаляется из списка на списка входных файлов
                                inputFileMap.remove(outFile);
                                //Файл удаляется из мапы с данными
                                tempArr.remove(outFile);
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
                    //Проверяем, единственный ли это файл
                    if(!tempArr.containsKey(entry.getKey())){
                        //Если единственный - перебираем строчки до тех пор, пока не обнаружим подходящие данные
                        while (!tempArr.containsKey(entry.getKey())) {
                            //Проверка, если ли в первой строчке пробелы
                            //Если нет, то берём её, и записываем в мапу прочитанных файов и в результирующий файл
                            String firstLine = entry.getValue().getLine();
                            if (!spaceInLine(firstLine)) {
                                //Проверка на то, что внутри файла не появится строковой тип данных
                                try {
                                    tempArr.put(entry.getKey(), firstLine);
                                    writer.write(tempArr.get(entry.getKey()));
                                    //Если файл еще не закончил считываться - делаем перенос на новую строку в результирующем файле
                                    if (entry.getValue().hasNextLine()) {writer.write("\n");}
                                    //Если строковый тип считался - пишем собщение об этом
                                } catch (NumberFormatException e) {
                                    System.err.println("File " + entry.getKey() + " has string data at <" + firstLine + "> line");
                                }
                            } else {
                                //Если в строке есть пробел - выдаём сообщение об ошибке
                                System.err.println("File " + entry.getKey() + "has a space symbol at <" + tempArr.get(entry.getKey()) + "> line");
                            }
                        }
                        //если файл не единственный
                    } else {
                        //Не забываем строчку, которую прочитали до этого
                        //Проверка, если ли в строчке пробелы
                        if (!spaceInLine(tempArr.get(entry.getKey()))) {
                            //Если нет пробела - записываем данные
                            writer.write(tempArr.get(entry.getKey()));
                            //Если файл еще не закончил считываться - делаем перенос на новую строку в результирующем файле
                            if (entry.getValue().hasNextLine()) {writer.write("\n");}
                        } else {
                            //Если в строке есть пробел - выдаём сообщение об ошибке
                            System.err.println("File " + entry.getKey() + "has a space symbol at <" + tempArr.get(entry.getKey()) + "> line");
                        }
                    }

                    //Пока файл не закончится - переписываем текущую строку в результирующий файл
                    while (entry.getValue().hasNextLine()) {
                        String temp = entry.getValue().getLine();
                        //Проверка, если ли в строчке пробелы
                        if (!spaceInLine(temp)){
                            //Проверка на то, что внутри файла не появится строковой тип данных
                            try {
                                //Проверка на правильную сортировку файла, сверяя прошлую прочитанную строку и текущую прочитанную строку
                                if (!isBBiggerA(tempArr.get(entry.getKey()), temp, ascSort, "str")){
                                    //если отсортирован правильно - записываем данные в результирующий файл
                                    writer.write(temp);
                                    if(entry.getValue().hasNextLine()) {writer.write("\n");}
                                    //В мапе делаем указываем, что мы изменили значение прочинанного файла
                                    tempArr.put(entry.getKey(),temp);
                                    //Если отсортирован неправильно - выдаём сообщение об ошибке и останавливаем проложение
                                } else {
                                    System.err.println("File " + entry.getKey()+ " is not sorted correctly");
                                    break;
                                }
                                //Если строковый тип считался - пишем собщение об этом
                            } catch (NumberFormatException e){
                                System.err.println("File " + entry.getKey()+ " has string data at <" + temp + "> line");
                            }
                        } else {
                            //Если есть пробелы - пишем об этом сообщение
                            System.err.println("File " + entry.getKey()+ " has a space symbol at <" + temp + "> line" );
                        }
                    }
                }
                //выходим из цикла, т.е. считали последний файл
                break;

                //Если существует несколько файлов для чтения
            } else {
                for(Map.Entry<String, FileConcatinator> entry : inputFileMap.entrySet()) {
                    //Проверяем, нужно ли читать следующую строчку файла и есть ли следующая строчка
                    //Если оба условия выполняются - пытаемся считываем следующую строчку, если она не выдаёт ошибку
                    while(entry.getValue().isNeedToRead() & entry.getValue().hasNextLine()){
                        String temp = entry.getValue().getLine();
                        //Проверка на пробел в строке
                        if (!spaceInLine(temp)){
                            //Проверка на строковый тип данных
                            try {
                                //Если ещё ничего не считали из конкретного файла в мапу, то считываем
                                //Помечаем, что файл читать не нужно
                                if(!tempArr.containsKey(entry.getKey())){
                                    tempArr.put(entry.getKey(), temp);
                                    entry.getValue().needToRead(false);
                                } else {
                                    //Если уже были записаны данные
                                    //Проверяем правильная ли сортировка внутри файла
                                    //Если правильная сортировка - записываем данные в мапу. Помечаем, что файл читать не нужно
                                    if (!isBBiggerA(tempArr.get(entry.getKey()), temp, ascSort, "str")){
                                        tempArr.put(entry.getKey(), temp);
                                        entry.getValue().needToRead(false);
                                        //Если неверная - не читаеем данные
                                        //Выволим сообщение об ошибке
                                        //Подготавливаем файл на удаление из алгоритма
                                        //Удаляем данные из мапы, чтобы не просачивались неактуальные данные
                                    } else {
                                        entry.getValue()._hasNextLine = false;
                                        tempArr.remove(entry.getKey());
                                        System.err.println("File " + entry.getKey()+ " is not sorted correctly");
                                        break;
                                    }
                                }
                                //Если строковый тип считался - пишем собщение об этом
                            } catch (NumberFormatException e){
                                System.err.println("File " + entry.getKey()+ " has string data at <" + temp + "> line");
                            }
                            //Если есть пробелы - пишем об этом сообщение
                        } else{
                            System.err.println("File " + entry.getKey()+ " has a space symbol at <"+temp+"> line" );
                        }
                    }
                    //Если следующей строчки в файле нет (файл закончится) и файла нет в списке на удаление
                    //Заносим этот файл в список на удаление
                    if (!entry.getValue().hasNextLine()){
                        if (!outFiles.contains(entry.getKey()))
                            outFiles.add(entry.getKey());
                    }

                }

                //Проверка, что после удаления данных из мапы не остался 1 файл
                if (!(tempArr.size() == 1)) {
                    //Создаём 2 переменные, которые отмечают текущий минимальный элемент
                    //Его значение
                    String minValue = null;
                    //Из какого он файла
                    String minKey = null;

                    //Перебираем ряд строк в мапе, смотрим на подходящее число
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
                    try{
                        assert minValue != null;
                        writer.write(minValue);
                        writer.write("\n");
                    } catch (IOException e){
                        e.printStackTrace();
                    }


                }

                //Проверяем, есть ли файлы, из которых мы всё считали
                //Если в списке законченных для чтения файлов есть любой файл, смотрим можем ли мы удалить такие файлы
                if (outFiles.size() > 0){
                    for (String outFile : outFiles) {
                        //Файл ещё не должен быть удалён
                        if (inputFileMap.containsKey(outFile)) {
                            //Файл отмечен на чтение, и в следующей итерации из него попробуют считать несущствующую строку
                            if (inputFileMap.get(outFile).isNeedToRead()) {
                                //Файл удаляется из списка на списка входных файлов
                                inputFileMap.remove(outFile);
                                //Файл удаляется из мапы с данными
                                tempArr.remove(outFile);
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


    //Метод для определения есть ли внутри пробел или нет.
    private Boolean spaceInLine (String line){
        Boolean spaceTrue = false;
        int index = line.indexOf(" ");
        if (index > -1) {spaceTrue = true;}

        return spaceTrue;
    }

    //Метод для определения, что больше А или Б. Возвращает false, если сортировка не нарушена.
    private Boolean isBBiggerA (String a, String b, Boolean ascSort, String type){

        Boolean bigger = true;
        //Сортировка возрастания
        if (ascSort) {
            //Строки
            if (Objects.equals(type, "str")) {
                bigger = a.compareTo(b) >= 0;
            }
            //Числа
            if (Objects.equals(type, "numbers")) {
                bigger = Integer.parseInt(a) > Integer.parseInt(b);
            }
        }
        //сортировка убывания
        if (!ascSort){
            //Строки
            if (Objects.equals(type, "str")) {
                bigger = a.compareTo(b) <= 0;
            }
            //Числа
            if (Objects.equals(type, "numbers")){
                bigger = Integer.parseInt(a) < Integer.parseInt(b);
            }
        }


        return bigger;
    }



}
