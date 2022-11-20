
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Scanner;

public class Main {

    private static boolean basketLoadEnable = false;
    private static String basketLoadFileName = "";
    private static FileFormat basketLoadFormat = FileFormat.JSON;

    private static boolean basketSaveEnable = false;
    private static String basketSaveFileName = "";
    private static FileFormat basketSaveFormat = FileFormat.JSON;

    private static boolean logSaveEnable = false;
    private static String logFileName = "";

    public static void main(String[] args) throws Exception {

        String[] listOfProducts = {"Молоко", "Соль", "Помидоры", "Оливки"};
        int[] prices = {70, 25, 100, 90};

        Basket basket = new Basket(listOfProducts, prices);

        int productNumber = 0;
        int productCount = 0;


        ClientLog clientLog = new ClientLog();

        //Загружаем настройки
        loadSettings();
        System.out.println(" ");

        File basketFileForLoad = new File(basketLoadFileName);
        File basketFileForSave = new File(basketSaveFileName);
        File logFile = new File(logFileName);

        Scanner scan = new Scanner(System.in);
        if (basketFileForLoad.exists() && basketLoadEnable) {
            //Если формат загружаемого файла JSON, то выгружаем корзину из него
            if (basketLoadFormat == FileFormat.JSON) {
                basket = Basket.loadFromJSON(basketFileForLoad);
            }
            //Если формат загружаемого файла TXT, то выгружаем корзину из него
            if (basketLoadFormat == FileFormat.TXT) {
                basket = Basket.loadFromBinFile(basketFileForLoad);
            }
        } else {
            //Иначе создаем новую корзину по конструктору
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
                String a = split[0];//До пробела, чтобы получить номер продукта
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


            String b = split[1]; //После пробела, чтобы получить количество
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

            //Добавляем номер продукта и количество в соответствующие файлы
            basket.addToCart(productNumber, productCount);
            if (basketSaveEnable) {
                if (basketSaveFormat == FileFormat.JSON) {
                    basket.saveToJSON(basketFileForSave);
                }
                if (basketSaveFormat == FileFormat.TXT) {
                    basket.saveBin(basketFileForSave);
                }
            }

            //Записываем в файл log
            clientLog.log(productNumber, productCount);


            if (logSaveEnable) {
                clientLog.exportAsCSV(logFile);
            }
            basket.printCart();

        }
    }

    static void loadSettings() throws Exception {
        // Строится структура документаа
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        // Создается дерево DOM документа из файла
        Document document = documentBuilder.parse("shop.xml");
        Node root = document.getDocumentElement();

        String sectionName;
        String parameterName;
        String parameterValue;
        // Просматриваем все подэлементы
        NodeList config = root.getChildNodes();
        for (int i = 0; i < config.getLength(); i++) {
            Node section = config.item(i);
            // Если нода не текст, то заходим внутрь
            if (section.getNodeType() != Node.TEXT_NODE) {
                sectionName = section.getNodeName();
                NodeList options = section.getChildNodes();
                for (int k = 0; k < options.getLength(); k++) {
                    Node parameter = options.item(k);
                    if (parameter.getNodeType() != Node.TEXT_NODE) {
                        parameterName = parameter.getNodeName();
                        parameterValue = parameter.getFirstChild().getTextContent();
                        setOption(sectionName, parameterName, parameterValue);
                    }
                }
            }
        }
    }

    private static void setOption(String sectionName, String parameterName, String parameterValue) {
        if (sectionName.equals("load")) {
            if (parameterName.equals("enabled")) {
                basketLoadEnable = parameterValue.equals("true");
            }
            if (parameterName.equals("fileName")) {
                basketLoadFileName = parameterValue;
            }
            if (parameterName.equals("format")) {
                if (parameterValue.equals("json")) {
                    basketLoadFormat = FileFormat.JSON;
                } else {
                    basketLoadFormat = FileFormat.TXT;
                }
            }
        }
        if (sectionName.equals("save")) {
            if (parameterName.equals("enabled")) {
                basketSaveEnable = parameterValue.equals("true");
            }
            if (parameterName.equals("fileName")) {
                basketSaveFileName = parameterValue;
            }
            if (parameterName.equals("format")) {
                if (parameterValue.equals("json")) {
                    basketSaveFormat = FileFormat.JSON;
                } else {
                    basketSaveFormat = FileFormat.TXT;
                }
            }

        }
        if (sectionName.equals("log")) {
            if (parameterName.equals("enabled")) {
                logSaveEnable = parameterValue.equals("true");
            }
            if (parameterName.equals("fileName")) {
                logFileName = parameterValue;
            }
        }
    }
}
