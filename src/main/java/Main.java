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



    //����� ������ ��� ��������� ������ ���� �����
    private void intType(Map<String, FileConcatinator> inputFileMap, String outputStr, Boolean ascSort) throws IOException {
        //��������� Writer ��� ������ � �������� ����
        FileWriter writer = new FileWriter(outputStr);

        //��������� ������ ������� �� ���� ������� ������
        for (Map.Entry<String, FileConcatinator> entry : inputFileMap.entrySet()) {
            entry.getValue().fileReaderName();
        }

        //������ ��������� ����������
        //������ ������, ������� ���� ��������� ���������
        List<String> outFiles = new ArrayList<>();
        //����, � ������� �������� ������ - ����� ���� ��������, ����� ������ ���� � ���������� ������
        HashMap<String,Integer> tempArr = new HashMap();
        //�������, ����� ���������� ������ ��� ���� ��������� ���������
        int count = 0;
        // ���������� ������, ������� �� ������ ���������
        int size = inputFileMap.size();

        //�������, ���� ��������� �� ��������� ��� �����, ����� ����������
        while (count <= size - 1){

            //���� ������� ���� ���������, �� ��� ����� ��� �����������
            //������� ������ ������������ �� ��� ���������� � �������������� ����
            if (count == size - 1){
                for(Map.Entry<String, FileConcatinator> entry : inputFileMap.entrySet()) {
                    //���������, ������������ �� ��� ����
                    if(!tempArr.containsKey(entry.getKey())){
                        //���� ������������ - ���������� ������� �� ��� ���, ���� �� ��������� ���������� ������
                        while (!tempArr.containsKey(entry.getKey())) {
                            //��������, ���� �� � ������ ������� �������
                            //���� ���, �� ���� �, � ���������� � ���� ����������� ����� � � �������������� ����
                            String firstLine = entry.getValue().getLine();
                            if (!spaceInLine(firstLine)) {
                                //�������� �� ��, ��� ������ ����� �� �������� ��������� ��� ������
                                try {
                                    tempArr.put(entry.getKey(), Integer.parseInt(firstLine));
                                    writer.write(tempArr.get(entry.getKey()).toString());
                                    //���� ���� ��� �� �������� ����������� - ������ ������� �� ����� ������ � �������������� �����
                                    if (entry.getValue().hasNextLine()) {writer.write("\n");}
                                    //���� ��������� ��� �������� - ����� �������� �� ����
                                } catch (NumberFormatException e) {
                                    System.err.println("File " + entry.getKey() + " has string data at <" + firstLine + "> line");
                                }
                            } else {
                                //���� � ������ ���� ������ - ����� ��������� �� ������
                                System.err.println("File " + entry.getKey() + "has a space symbol at <" + tempArr.get(entry.getKey()) + "> line");
                            }
                        }
                    //���� ���� �� ������������
                    } else {
                        //�� �������� �������, ������� ��������� �� �����
                        //��������, ���� �� � ������� �������
                        if (!spaceInLine(tempArr.get(entry.getKey()).toString())) {
                            //���� ��� ������� - ���������� ������
                            writer.write(tempArr.get(entry.getKey()).toString());
                            //���� ���� ��� �� �������� ����������� - ������ ������� �� ����� ������ � �������������� �����
                            if (entry.getValue().hasNextLine()) {writer.write("\n");}
                        } else {
                            //���� � ������ ���� ������ - ����� ��������� �� ������
                            System.err.println("File " + entry.getKey() + "has a space symbol at <" + tempArr.get(entry.getKey()) + "> line");
                        }
                    }

                    //���� ���� �� ���������� - ������������ ������� ������ � �������������� ����
                    while (entry.getValue().hasNextLine()) {
                        String temp = entry.getValue().getLine();
                        //��������, ���� �� � ������� �������
                        if (!spaceInLine(temp)){
                            //�������� �� ��, ��� ������ ����� �� �������� ��������� ��� ������
                            try {
                                //�������� �� ���������� ���������� �����, ������ ������� ����������� ������ � ������� ����������� ������
                                if (!isBBiggerA(tempArr.get(entry.getKey()).toString(), temp, ascSort, "numbers")){
                                    //���� ������������ ��������� - ���������� ������ � �������������� ����
                                    writer.write(temp);
                                    if(entry.getValue().hasNextLine()) {writer.write("\n");}
                                    //� ���� ������ ���������, ��� �� �������� �������� ������������ �����
                                    tempArr.put(entry.getKey(),Integer.parseInt(temp));
                                //���� ������������ ����������� - ����� ��������� �� ������ � ������������� ����������
                                } else {
                                    System.err.println("File " + entry.getKey()+ " is not sorted correctly");
                                    break;
                                }
                            //���� ��������� ��� �������� - ����� �������� �� ����
                            } catch (NumberFormatException e){
                                System.err.println("File " + entry.getKey()+ " has string data at <" + temp + "> line");
                            }
                        } else {
                            //���� ���� ������� - ����� �� ���� ���������
                            System.err.println("File " + entry.getKey()+ " has a space symbol at <" + temp + "> line" );
                        }
                    }
                }
                //������� �� �����, �.�. ������� ��������� ����
                break;

                //���� ���������� ��������� ������ ��� ������
            } else {
                for(Map.Entry<String, FileConcatinator> entry : inputFileMap.entrySet()) {
                    //���������, ����� �� ������ ��������� ������� ����� � ���� �� ��������� �������
                    //���� ��� ������� ����������� - �������� ��������� ��������� �������, ���� ��� �� ����� ������
                    while(entry.getValue().isNeedToRead() & entry.getValue().hasNextLine()){
                        String temp = entry.getValue().getLine();
                        //�������� �� ������ � ������
                        if (!spaceInLine(temp)){
                            //�������� �� ��������� ��� ������
                            try {
                                //���� ��� ������ �� ������� �� ����������� ����� � ����, �� ���������
                                //��������, ��� ���� ������ �� �����
                                if(!tempArr.containsKey(entry.getKey())){
                                    tempArr.put(entry.getKey(), Integer.valueOf(temp));
                                    entry.getValue().needToRead(false);
                                } else {
                                    //���� ��� ���� �������� ������
                                    //��������� ���������� �� ���������� ������ �����
                                    //���� ���������� ���������� - ���������� ������ � ����. ��������, ��� ���� ������ �� �����
                                    if (!isBBiggerA(tempArr.get(entry.getKey()).toString(), temp, ascSort, "numbers")){
                                        tempArr.put(entry.getKey(), Integer.valueOf(temp));
                                        entry.getValue().needToRead(false);
                                    //���� �������� - �� ������� ������
                                    //������� ��������� �� ������
                                    //�������������� ���� �� �������� �� ���������
                                    //������� ������ �� ����, ����� �� ������������� ������������ ������
                                    } else {
                                        entry.getValue()._hasNextLine = false;
                                        tempArr.remove(entry.getKey());
                                        System.err.println("File " + entry.getKey()+ " is not sorted correctly");
                                        break;
                                    }
                                }
                            //���� ��������� ��� �������� - ����� �������� �� ����
                            } catch (NumberFormatException e){
                                System.err.println("File " + entry.getKey()+ " has string data at <" + temp + "> line");
                            }
                        //���� ���� ������� - ����� �� ���� ���������
                        } else{
                            System.err.println("File " + entry.getKey()+ " has a space symbol at <"+temp+"> line" );
                        }
                    }
                    //���� ��������� ������� � ����� ��� (���� ����������) � ����� ��� � ������ �� ��������
                    //������� ���� ���� � ������ �� ��������
                    if (!entry.getValue().hasNextLine()){
                        if (!outFiles.contains(entry.getKey()))
                            outFiles.add(entry.getKey());
                    }

                }

                //��������, ��� ����� �������� ������ �� ���� �� ������� 1 ����
                if (!(tempArr.size() == 1)) {
                    //������ 2 ����������, ������� �������� ������� ����������� �������
                    //��� ��������
                    Integer minValue = null;
                    //�� ������ �� �����
                    String minKey = null;

                    //���������� ��� ����� � ����, ������� �� ���������� �����
                    for (Map.Entry<String, Integer> entry : tempArr.entrySet()) {
                        if (minValue == null) {
                            minValue = entry.getValue();
                            minKey = entry.getKey();
                        } else {
                            //���� ���������� �� �����������, �� ���� ����������� �����
                            if (ascSort) {
                                if (minValue > entry.getValue()) {
                                    minValue = entry.getValue();
                                    minKey = entry.getKey();
                                }
                            } else {
                                //���� ���������� �� ���������, �� ���� ������������ �����
                                if (minValue < entry.getValue()) {
                                    minValue = entry.getValue();
                                    minKey = entry.getKey();
                                }
                            }
                        }
                    }
                    //���� � ���������� ��������� ��������� ��� ������ � ��������� ��������
                    inputFileMap.get(minKey).needToRead(true);
                    //���������� ��������� � �������������� ����
                    try{
                        assert minValue != null;
                        writer.write(minValue.toString());
                        writer.write("\n");
                    } catch (IOException e){
                        e.printStackTrace();
                    }


                }

                //���������, ���� �� �����, �� ������� �� �� �������
                //���� � ������ ����������� ��� ������ ������ ���� ����� ����, ������� ����� �� �� ������� ����� �����
                if (outFiles.size() > 0){
                    for (String outFile : outFiles) {
                        //���� ��� �� ������ ���� �����
                        if (inputFileMap.containsKey(outFile)) {
                            //���� ������� �� ������, � � ��������� �������� �� ���� ��������� ������� ������������� ������
                            if (inputFileMap.get(outFile).isNeedToRead()) {
                                //���� ��������� �� ������ �� ������ ������� ������
                                inputFileMap.remove(outFile);
                                //���� ��������� �� ���� � �������
                                tempArr.remove(outFile);
                                //����������� ������� ����������� ������ �� 1
                                count++;
                            }
                        }
                    }
                }

            }
        }

        //��������� ��� ������
        for(Map.Entry<String, FileConcatinator> entry : inputFileMap.entrySet()) {
            entry.getValue().close();
        }
        writer.close();

    }

    //����� ������ ��� ��������� ������ ���� ������
    private void strType(Map<String, FileConcatinator> inputFileMap, String outputStr, Boolean ascSort) throws IOException {
        //��������� Writer ��� ������ � �������� ����
        FileWriter writer = new FileWriter(outputStr);

        //��������� ������ ������� �� ���� ������� ������
        for (Map.Entry<String, FileConcatinator> entry : inputFileMap.entrySet()) {
            entry.getValue().fileReaderName();
        }

        //������ ��������� ����������
        //������ ������, ������� ���� ��������� ���������
        List<String> outFiles = new ArrayList<>();
        //����, � ������� �������� ������ - ����� ���� ��������, ����� ������ ���� � ���������� ������
        HashMap<String,String> tempArr = new HashMap();
        //�������, ����� ���������� ������ ��� ���� ��������� ���������
        int count = 0;
        // ���������� ������, ������� �� ������ ���������
        int size = inputFileMap.size();

        //�������, ���� ��������� �� ��������� ��� �����, ����� ����������
        while (count <= size - 1){

            //���� ������� ���� ���������, �� ��� ����� ��� �����������
            //������� ������ ������������ �� ��� ���������� � �������������� ����
            if (count == size - 1){
                for(Map.Entry<String, FileConcatinator> entry : inputFileMap.entrySet()) {
                    //���������, ������������ �� ��� ����
                    if(!tempArr.containsKey(entry.getKey())){
                        //���� ������������ - ���������� ������� �� ��� ���, ���� �� ��������� ���������� ������
                        while (!tempArr.containsKey(entry.getKey())) {
                            //��������, ���� �� � ������ ������� �������
                            //���� ���, �� ���� �, � ���������� � ���� ����������� ����� � � �������������� ����
                            String firstLine = entry.getValue().getLine();
                            if (!spaceInLine(firstLine)) {
                                //�������� �� ��, ��� ������ ����� �� �������� ��������� ��� ������
                                try {
                                    tempArr.put(entry.getKey(), firstLine);
                                    writer.write(tempArr.get(entry.getKey()));
                                    //���� ���� ��� �� �������� ����������� - ������ ������� �� ����� ������ � �������������� �����
                                    if (entry.getValue().hasNextLine()) {writer.write("\n");}
                                    //���� ��������� ��� �������� - ����� �������� �� ����
                                } catch (NumberFormatException e) {
                                    System.err.println("File " + entry.getKey() + " has string data at <" + firstLine + "> line");
                                }
                            } else {
                                //���� � ������ ���� ������ - ����� ��������� �� ������
                                System.err.println("File " + entry.getKey() + "has a space symbol at <" + tempArr.get(entry.getKey()) + "> line");
                            }
                        }
                        //���� ���� �� ������������
                    } else {
                        //�� �������� �������, ������� ��������� �� �����
                        //��������, ���� �� � ������� �������
                        if (!spaceInLine(tempArr.get(entry.getKey()))) {
                            //���� ��� ������� - ���������� ������
                            writer.write(tempArr.get(entry.getKey()));
                            //���� ���� ��� �� �������� ����������� - ������ ������� �� ����� ������ � �������������� �����
                            if (entry.getValue().hasNextLine()) {writer.write("\n");}
                        } else {
                            //���� � ������ ���� ������ - ����� ��������� �� ������
                            System.err.println("File " + entry.getKey() + "has a space symbol at <" + tempArr.get(entry.getKey()) + "> line");
                        }
                    }

                    //���� ���� �� ���������� - ������������ ������� ������ � �������������� ����
                    while (entry.getValue().hasNextLine()) {
                        String temp = entry.getValue().getLine();
                        //��������, ���� �� � ������� �������
                        if (!spaceInLine(temp)){
                            //�������� �� ��, ��� ������ ����� �� �������� ��������� ��� ������
                            try {
                                //�������� �� ���������� ���������� �����, ������ ������� ����������� ������ � ������� ����������� ������
                                if (!isBBiggerA(tempArr.get(entry.getKey()), temp, ascSort, "str")){
                                    //���� ������������ ��������� - ���������� ������ � �������������� ����
                                    writer.write(temp);
                                    if(entry.getValue().hasNextLine()) {writer.write("\n");}
                                    //� ���� ������ ���������, ��� �� �������� �������� ������������ �����
                                    tempArr.put(entry.getKey(),temp);
                                    //���� ������������ ����������� - ����� ��������� �� ������ � ������������� ����������
                                } else {
                                    System.err.println("File " + entry.getKey()+ " is not sorted correctly");
                                    break;
                                }
                                //���� ��������� ��� �������� - ����� �������� �� ����
                            } catch (NumberFormatException e){
                                System.err.println("File " + entry.getKey()+ " has string data at <" + temp + "> line");
                            }
                        } else {
                            //���� ���� ������� - ����� �� ���� ���������
                            System.err.println("File " + entry.getKey()+ " has a space symbol at <" + temp + "> line" );
                        }
                    }
                }
                //������� �� �����, �.�. ������� ��������� ����
                break;

                //���� ���������� ��������� ������ ��� ������
            } else {
                for(Map.Entry<String, FileConcatinator> entry : inputFileMap.entrySet()) {
                    //���������, ����� �� ������ ��������� ������� ����� � ���� �� ��������� �������
                    //���� ��� ������� ����������� - �������� ��������� ��������� �������, ���� ��� �� ����� ������
                    while(entry.getValue().isNeedToRead() & entry.getValue().hasNextLine()){
                        String temp = entry.getValue().getLine();
                        //�������� �� ������ � ������
                        if (!spaceInLine(temp)){
                            //�������� �� ��������� ��� ������
                            try {
                                //���� ��� ������ �� ������� �� ����������� ����� � ����, �� ���������
                                //��������, ��� ���� ������ �� �����
                                if(!tempArr.containsKey(entry.getKey())){
                                    tempArr.put(entry.getKey(), temp);
                                    entry.getValue().needToRead(false);
                                } else {
                                    //���� ��� ���� �������� ������
                                    //��������� ���������� �� ���������� ������ �����
                                    //���� ���������� ���������� - ���������� ������ � ����. ��������, ��� ���� ������ �� �����
                                    if (!isBBiggerA(tempArr.get(entry.getKey()), temp, ascSort, "str")){
                                        tempArr.put(entry.getKey(), temp);
                                        entry.getValue().needToRead(false);
                                        //���� �������� - �� ������� ������
                                        //������� ��������� �� ������
                                        //�������������� ���� �� �������� �� ���������
                                        //������� ������ �� ����, ����� �� ������������� ������������ ������
                                    } else {
                                        entry.getValue()._hasNextLine = false;
                                        tempArr.remove(entry.getKey());
                                        System.err.println("File " + entry.getKey()+ " is not sorted correctly");
                                        break;
                                    }
                                }
                                //���� ��������� ��� �������� - ����� �������� �� ����
                            } catch (NumberFormatException e){
                                System.err.println("File " + entry.getKey()+ " has string data at <" + temp + "> line");
                            }
                            //���� ���� ������� - ����� �� ���� ���������
                        } else{
                            System.err.println("File " + entry.getKey()+ " has a space symbol at <"+temp+"> line" );
                        }
                    }
                    //���� ��������� ������� � ����� ��� (���� ����������) � ����� ��� � ������ �� ��������
                    //������� ���� ���� � ������ �� ��������
                    if (!entry.getValue().hasNextLine()){
                        if (!outFiles.contains(entry.getKey()))
                            outFiles.add(entry.getKey());
                    }

                }

                //��������, ��� ����� �������� ������ �� ���� �� ������� 1 ����
                if (!(tempArr.size() == 1)) {
                    //������ 2 ����������, ������� �������� ������� ����������� �������
                    //��� ��������
                    String minValue = null;
                    //�� ������ �� �����
                    String minKey = null;

                    //���������� ��� ����� � ����, ������� �� ���������� �����
                    for (Map.Entry<String, String> entry : tempArr.entrySet()){
                        if (minValue == null) {
                            minValue = entry.getValue();
                            minKey = entry.getKey();
                        } else {
                            //���� ���������� �� �����������, �� ���� ����������� ������
                            if (ascSort){
                                if (minValue.compareTo(entry.getValue()) > 0){
                                    minValue = entry.getValue();
                                    minKey = entry.getKey();
                                }
                                //���� ���������� �� ��������, �� ���� ������������ ������
                            } else {
                                if (minValue.compareTo(entry.getValue()) < 0){
                                    minValue = entry.getValue();
                                    minKey = entry.getKey();
                                }
                            }
                        }
                    }

                    //���� � ���������� ��������� ��������� ��� ������ � ��������� ��������
                    inputFileMap.get(minKey).needToRead(true);
                    //���������� ��������� � �������������� ����
                    try{
                        assert minValue != null;
                        writer.write(minValue);
                        writer.write("\n");
                    } catch (IOException e){
                        e.printStackTrace();
                    }


                }

                //���������, ���� �� �����, �� ������� �� �� �������
                //���� � ������ ����������� ��� ������ ������ ���� ����� ����, ������� ����� �� �� ������� ����� �����
                if (outFiles.size() > 0){
                    for (String outFile : outFiles) {
                        //���� ��� �� ������ ���� �����
                        if (inputFileMap.containsKey(outFile)) {
                            //���� ������� �� ������, � � ��������� �������� �� ���� ��������� ������� ������������� ������
                            if (inputFileMap.get(outFile).isNeedToRead()) {
                                //���� ��������� �� ������ �� ������ ������� ������
                                inputFileMap.remove(outFile);
                                //���� ��������� �� ���� � �������
                                tempArr.remove(outFile);
                                //����������� ������� ����������� ������ �� 1
                                count++;
                            }
                        }
                    }
                }

            }
        }

        //��������� ��� ������
        for(Map.Entry<String, FileConcatinator> entry : inputFileMap.entrySet()) {
            entry.getValue().close();
        }
        writer.close();

    }


    //����� ��� ����������� ���� �� ������ ������ ��� ���.
    private Boolean spaceInLine (String line){
        Boolean spaceTrue = false;
        int index = line.indexOf(" ");
        if (index > -1) {spaceTrue = true;}

        return spaceTrue;
    }

    //����� ��� �����������, ��� ������ � ��� �. ���������� false, ���� ���������� �� ��������.
    private Boolean isBBiggerA (String a, String b, Boolean ascSort, String type){

        Boolean bigger = true;
        //���������� �����������
        if (ascSort) {
            //������
            if (Objects.equals(type, "str")) {
                bigger = a.compareTo(b) >= 0;
            }
            //�����
            if (Objects.equals(type, "numbers")) {
                bigger = Integer.parseInt(a) > Integer.parseInt(b);
            }
        }
        //���������� ��������
        if (!ascSort){
            //������
            if (Objects.equals(type, "str")) {
                bigger = a.compareTo(b) <= 0;
            }
            //�����
            if (Objects.equals(type, "numbers")){
                bigger = Integer.parseInt(a) < Integer.parseInt(b);
            }
        }


        return bigger;
    }



}
