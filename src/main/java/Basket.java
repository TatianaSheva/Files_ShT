import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.text.ParseException;

public class Basket implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String[] productName;
    protected int[] price;
    protected int[] prodAmount;

    protected int productNum;

    public Basket(String[] productName, int[] price) {
        this.productName = productName;
        this.price = price;
        this.prodAmount = new int[productName.length];
    }

    public Basket(String[] productName, int[] price, int[] prodAmount) {
        this.productName = productName;
        this.price = price;
        this.prodAmount = prodAmount;
    }


    //Метод добавления в корзину продукта: количество и изменение цены в зависимости от добавленного количества
    public void addToCart(int productNum, int amount) {
        prodAmount[productNum] += amount;
        //sum[productNum] = price[productNum] * amount;
    }

    // Метод вывода на экран покупательской корзины.
    public void printCart() {
        System.out.println(" ");
        System.out.println("Ваша корзина:");
        int sumProducts = 0;
        for (int i = 0; i < productName.length; i++) {
            if (prodAmount[i] != 0) {
                System.out.println(productName[i] + " " + prodAmount[i] + " шт., " + price[i] + " руб., " + (prodAmount[i] * price[i]) + " рублей в сумме.");
                sumProducts += prodAmount[i] * price[i];
            }
        }
        System.out.println("Итого в корзине: " + sumProducts + " рублей.");
    }


    public void saveBin(File file) {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static Basket loadFromBinFile(File file) throws IOException, ClassNotFoundException {
        //Откроем входной поток для чтения файла
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        return (Basket) ois.readObject();
    }



    public void saveToJSON(File textFile) throws IOException {
        FileWriter writer = new FileWriter(textFile);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        writer.write(gson.toJson(this, Basket.class));
        writer.close();
    }

    public static Basket loadFromJSON(File textFile) throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(textFile);
        return gson.fromJson(reader, Basket.class);
    }
}