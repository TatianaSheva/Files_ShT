import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClientLog {

    String log = "productNum,amount\n";


    //Покупатель добавил покупку - это действие должно быть там сохранено.
    public void log(int productNum, int amount) {
        log += String.format("%d,%d\n", productNum, amount);
    }

    // Для сохранения всего журнала действия в файл в формате csv.
    public void exportAsCSV(File txtFile) throws IOException {
        FileWriter writer = new FileWriter(txtFile);
        writer.write(log);
        writer.close();
    }
}




