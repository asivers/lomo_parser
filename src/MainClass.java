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
