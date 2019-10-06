import java.io.*;

/**
 * Created by heymn on 03.10.2019.
 */
public class FileConcatinator {

    private String fileName;
    private BufferedReader reader;
    private String nextLine;
    Boolean _hasNextLine = true;
    private Boolean _needToRead = true;


    public FileConcatinator(String fileName){
        this.fileName = fileName;
    }

    public void fileReaderName() throws IOException {
        reader = new BufferedReader(new FileReader(fileName));
        fileLineReader();
    }


    private void fileLineReader() throws IOException {
        nextLine = reader.readLine();
        _hasNextLine = nextLine != null;
    }

    public String getLine() throws IOException {
        String temp = nextLine;
        if (_hasNextLine){
            fileLineReader();
        }

        return temp;
    }


    public Boolean hasNextLine(){
        return _hasNextLine;
    }

    public void needToRead(Boolean _needToRead){
        this._needToRead = _needToRead;
    }

    public Boolean isNeedToRead(){
        return _needToRead;
    }


    public void close() throws IOException {

        reader.close();
    }




}
