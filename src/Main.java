
import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        String[] listOfProducts = {"Молоко", "Соль", "Помидоры", "Оливки"};
        int[] prices = {70, 25, 100, 90};

        Basket basket = new Basket(listOfProducts, prices);

        int productNumber = 0;
        int productCount = 0;


        File basketFile = new File("basket.bin");
        Scanner scan = new Scanner(System.in);
        if (basketFile.exists()) {
            System.out.println("Загрузить корзину ENTER?");
            if (scan.nextLine().equals("")) {
                basket = Basket.loadFromBinFile(basketFile);
                basket.printCart();
                System.out.println(" ");
                System.out.println("_______________________________________ ");

            } else {
                basket = new Basket(listOfProducts, prices);
            }
        } else {
            basket = new Basket(listOfProducts, prices);
        }


        System.out.println("Список возможных товаров для покупки: ");
        for (int i = 0; i < listOfProducts.length; i++) {
            System.out.println((i + 1) + ". " + listOfProducts[i] + " " + prices[i] + " руб./шт.");
        }

        while (true) {
            System.out.println("Выберите товар и количество или введите `end`");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if ("end".equals(input)) {
                break;
            }


            String[] split = input.split(" ");
            if (split.length != 2) {
                System.out.println("Ошибка ввода: Вы ввели 1 число или более 2 чисел =(");
                continue;
            }

            // Исключение для ввода слов NumberFormatException
            try {
                String a = split[0];//до пробела, чтобы получить номер продукта
                productNumber = Integer.parseInt(a) - 1;
                // throw new NumberFormatException("Ошибка ввода: Вы ввели не число. Для корректной работы программы введите число!");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка ввода: Вы ввели не число. Для корректной работы программы введите число!");
                continue;
            }
            if (productNumber + 1 > listOfProducts.length || productNumber < 0) {
                System.out.println("Ошибка ввода: Вы ввели слишком большое или неположительное число!");
                continue;
            }


            String b = split[1]; //после пробела, чтобы получить количество
            try {

                productCount = Integer.parseInt(b);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка ввода: Вы ввели не число. Для корректной работы программы введите число!");
                continue;
            }

            if (productCount < 0) {
                System.out.println("Ошибка ввода: Вы ввели слишком большое или неположительное число!");
                continue;
            }

            basket.addToCart(productNumber, productCount);
            basket.saveBin(basketFile);
            basket.printCart();

        }
    }
}
