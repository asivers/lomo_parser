import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
    public static void main(String[] args) {
        try {
//            sensibilityAdj("1", new double[1]);
            String calibrationPath = "./src/files/calibration.mdrs";
            double[] calibrationData = getData(calibrationPath);
//            for (int i = 0; i < 326; i++) {
//                System.out.println(calibrationData[i]);
//            }
            File data_only = new File("./src/data_only");
            data_only.mkdir();
            File calibrated = new File("./src/calibrated");
            calibrated.mkdir();

            File spectrometer = new File("./src/files/spectrometer");
            List<File> spectrometerFoldersList = Arrays.asList(spectrometer.listFiles());
            List<File> spectrometerFilesList;
            for (File folder : spectrometerFoldersList) {
                spectrometerFilesList = Arrays.asList(folder.listFiles());
                for (File file : spectrometerFilesList) {
                    System.out.println(file);
                }
            }
        }
        catch (Exception e) { }
    }

    public static double[] getData(String path) throws FileNotFoundException, IOException {
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

    public static double[] sensibilityAdj(String path, double[] data) {
        Matcher m = Pattern.compile("\\d+mv").matcher(path);
        String stringSens;
        int sens = 0;
        while (m.find()) {
            stringSens = m.group();
            sens = Integer.parseInt(stringSens.substring(0, stringSens.length() - 2));
        }
        for (int i = 0; i < data.length; i++) {
            data[i] = data[i] * sens;
        }
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
