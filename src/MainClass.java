import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
    public static void main(String[] args) {
        try {
            String prefix = "/src";
            if (args.length > 0)
                prefix = "";

            String calibrationPath = "." + prefix + "/calibration.mdrs";
            double[] calibrationData = removeNoise(getData(calibrationPath));

            new File("." + prefix + "/not_adjusted").mkdir();
            new File("." + prefix + "/sensitivity_adjusted").mkdir();
            new File("." + prefix + "/spectrum_adjusted").mkdir();

            File spectrometer = new File("." + prefix + "/files_spectrometer");
            List<File> spectrometerFoldersList = Arrays.asList(spectrometer.listFiles());
            List<File> spectrometerFilesList;
            String folderName;
            String fileName;
            for (File folder : spectrometerFoldersList) {
                folderName = folder.getName();
                new File("." + prefix + "/not_adjusted/" + folderName).mkdir();
                new File("." + prefix + "/sensitivity_adjusted/" + folderName).mkdir();
                new File("." + prefix + "/spectrum_adjusted/" + folderName).mkdir();
                spectrometerFilesList = Arrays.asList(folder.listFiles());
                for (File file : spectrometerFilesList) {
                    fileName = file.getName();

                    double[] fileData = getData(file.getPath());
                    writeData("." + prefix + "/not_adjusted/" + folderName + "/" + fileName, fileData);

                    double[] sensitivityAdjustedData = sensitivityAdj(fileName, removeNoise(fileData));
                    writeData("." + prefix + "/sensitivity_adjusted/" + folderName + "/" + fileName, sensitivityAdjustedData);

                    double[] spectrumAdjustedData = spectrumAdj(sensitivityAdjustedData, calibrationData);
                    writeData("." + prefix + "/spectrum_adjusted/" + folderName + "/" + fileName, spectrumAdjustedData);
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

}