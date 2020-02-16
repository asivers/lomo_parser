import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
    public static void main(String[] args) {
        try {
            String calibrationPath = "./src/files/calibration.mdrs";
            double[] calibrationData = removeNoise(getData(calibrationPath));

            new File("./src/not_adjusted").mkdir();
            new File("./src/sensitivity_adjusted").mkdir();
            new File("./src/spectrum_adjusted").mkdir();

            File spectrometer = new File("./src/files/spectrometer");
            List<File> spectrometerFoldersList = Arrays.asList(spectrometer.listFiles());
            List<File> spectrometerFilesList;
            String folderName;
            String fileName;
            for (File folder : spectrometerFoldersList) {
                folderName = folder.getName();
                new File("./src/not_adjusted/" + folderName).mkdir();
                new File("./src/sensitivity_adjusted/" + folderName).mkdir();
                new File("./src/spectrum_adjusted/" + folderName).mkdir();
                spectrometerFilesList = Arrays.asList(folder.listFiles());
                for (File file : spectrometerFilesList) {
                    fileName = file.getName();

                    double[] fileData = getData(file.getPath());
                    writeData("./src/not_adjusted/" + folderName + "/" + fileName, fileData);

                    double[] sensitivityAdjustedData = sensitivityAdj(fileName, removeNoise(fileData));
                    writeData("./src/sensitivity_adjusted/" + folderName + "/" + fileName, sensitivityAdjustedData);

                    double[] spectrumAdjustedData = spectrumAdj(sensitivityAdjustedData, calibrationData);
                    writeData("./src/spectrum_adjusted/" + folderName + "/" + fileName, spectrumAdjustedData);
                }
            }
        }
        catch (Exception e) { }
    }

    public static double[] getData(String path) throws IOException {
        File calibration = new File(path);
        BufferedReader reader = new BufferedReader(new FileReader(calibration));
        double[] data = new double[326];
        String line;
        for (int i = 0; i < 10; i++)
            line = reader.readLine();
        for (int i = 0; i < 326; i++) {
            line = reader.readLine();
            data[i] = Double.parseDouble(line.substring(4));
        }
        return data;
    }

    public static void writeData(String path, double[] data) throws IOException {
        File newFile = new File(path);
        newFile.createNewFile();
        FileWriter fStream = new FileWriter(path, true);
        BufferedWriter out = new BufferedWriter(fStream);
        for (int i = 0; i < data.length; i++)
            out.write("" + data[i] + "\n");
        out.flush();
    }

    public static double[] removeNoise(double[] data) {
        double min = 0;
        for (int i = 0; i < data.length; i++)
            if (data[i] < min)
                min = data[i];
        for (int i = 0; i < data.length; i++)
            data[i] -= min;
        return data;
    }

    public static double[] sensitivityAdj(String name, double[] data) {
        Matcher m = Pattern.compile("\\d+mv").matcher(name);
        String stringSens;
        int sens = 0;
        while (m.find()) {
            stringSens = m.group();
            sens = Integer.parseInt(stringSens.substring(0, stringSens.length() - 2));
        }
        for (int i = 0; i < data.length; i++) {
            data[i] *= sens;
        }
        return data;
    }

    public static double[] spectrumAdj(double[] data, double[] calibrationData) {
        for (int i = 0; i < data.length; i++)
            data[i] /= calibrationData[i];
        return data;
    }













//
//    public static boolean find(String pattern, String str, boolean write, int lim) {
//        List<String> allMatches = new ArrayList<>();
//
//        while (m.find()) {
//            allMatches.add(m.group());
//        }
//        if (write == true) {
//            for (int i = 0; i < allMatches.size(); i++) {
//                String s = "";
//                for (int j = 3; j < allMatches.get(i).length() - 4; j++) {
//                    s += allMatches.get(i).charAt(j);
//                }
//                allMatches.set(i, s);
//
//            }
//            if (allMatches.size() < lim)
//                lim = allMatches.size();
//            for (int i = 0; i < lim; i++) {
//                System.out.println(allMatches.get(i));
//            }
//        }
//        if (allMatches.size() > 0)
//            return true;
//        else
//            return false;
//    }
}



/*
import java.io.*;
        import java.util.Arrays;
        import java.util.List;

public class MainClass {
    public static void main(String[] args) {

        // stringLength - длина строки, короче которой строки не будут записываться

        // по умолчанию
        int stringLength = 49;
        // если задавать из командной строки
        if (args.length > 0) {
            stringLength = Integer.parseInt(args[0]);
        }

        // название папки-источника
        String from = "polina_files";
        // название папки куда кладем файлы
        String to = "polina_new_files";

        // при работе с файлами нужна эта конструкция чтобы ловить исключения
        try {
            // папка-источник
            // "./" значит что находится там же где мы вызываем программу из командной строки (относительный путь)
            File dirFrom = new File("./" + from + "/");
            // собираем массив всех файлов из папки источника
            File[] arrFiles = dirFrom.listFiles();
            // преобразуем массив в список для удобства работы
            List<File> lst = Arrays.asList(arrFiles);

            // создаем папку куда кладем файлы
            File dirTo = new File("./" + to + "/");
            dirTo.mkdir();

            // для каждого файла из папки-источника
            for (File f : lst) {
                // конструкция для чтения файла
                BufferedReader reader = new BufferedReader(new FileReader(f));
                // присваиваем адрес новому файлу (извлекаем его имя, и приплюсовываем к нему ./polina_new_files/)
                String newAddress = "./" + to + "/" + f.toString().substring(f.toString().lastIndexOf('\\') + 1);
                // собственно создаем новый файл
                File newFile = new File(newAddress);
                newFile.createNewFile();
                // конструкция для записи в файл
                FileWriter fstream = new FileWriter(newAddress, true);
                BufferedWriter out = new BufferedWriter(fstream);
                // объявляем переменную строки
                String line;
                // записываем в новый файл те строки из исходного, длина которых больше чем stringLength
                while ((line = reader.readLine()) != null)
                    if (line.length() > stringLength)
                        out.write(line + "\n");
            }
        }
        // при работе с файлами нужна эта конструкция чтобы ловить исключения
        catch (Exception e) { }
    }
}
*/
