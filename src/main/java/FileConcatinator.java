import java.io.*;

/**
 * Created by heymn on 03.10.2019.
 */
//Класс, отвечающий за потоковое считывание файла
public class FileConcatinator {

    //Имя файла
    private String fileName;
    //Ридер
    private BufferedReader reader;
    //Строка во входном файле
    private String nextLine;
    //Есть ли следующая строка в файле
    Boolean _hasNextLine = true;
    //Нужно ли считывать файл или нет
    private Boolean _needToRead = true;


    //Конструктор, где мы получаем имя входного файла
    public FileConcatinator(String fileName){
        this.fileName = fileName;
    }

    //Инициализируем ридер и получаем первую строчку файла
    public void fileReaderName() throws IOException {
        reader = new BufferedReader(new FileReader(fileName));
        fileLineReader();
    }


    //Берём строчку файла и проверяем, конец это файла или нет
    private void fileLineReader() throws IOException {
        nextLine = reader.readLine();
        _hasNextLine = nextLine != null;
    }

    //Возвращаем строчку файла, если это не конец файла
    public String getLine() throws IOException {
        String temp = nextLine;
        if (_hasNextLine){
            fileLineReader();
        }

        return temp;
    }


    //Возвращаем, конец ли это файла
    public Boolean hasNextLine(){
        return _hasNextLine;
    }

    //Указываем, нужно ли читать строчку
    public void needToRead(Boolean _needToRead){
        this._needToRead = _needToRead;
    }

    //Вовзращаем можно ли читать строчку или нет
    public Boolean isNeedToRead(){
        return _needToRead;
    }


    //Закрытие потока на чтение
    public void close() throws IOException {

        reader.close();
    }




}
