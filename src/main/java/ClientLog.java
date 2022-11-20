import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClientLog {

    StringBuilder log;

    public ClientLog() {
        log = new StringBuilder("productNum,amount\n");
    }

    public void log(int productNum, int amount) {
        log.append(String.format("%d,%d\n", productNum, amount));
    }

    // Для сохранения всего журнала действия в файл в формате csv.
    public void exportAsCSV(File txtFile) throws IOException {
        FileWriter writer = new FileWriter(txtFile);
        writer.write(String.valueOf(log));
        writer.close();
    }
}




