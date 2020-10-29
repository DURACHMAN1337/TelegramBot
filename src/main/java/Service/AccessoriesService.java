package Service;

import Models.Products.Accessory;
import Models.Products.Tobacco;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AccessoriesService {
    private Document document;
    private ArrayList<Accessory> accessoriesList;

    public static void main(String[] args) {
        AccessoriesService accessoriesService = new AccessoriesService();

    }

    public AccessoriesService() {
        Date date = new Date();
        parseAllAccessories();
        Date date2 = new Date();
        long dateRes = date2.getTime() - date.getTime();
        System.out.println("Парсинг акссесуаров занял " + dateRes / 1000 + " с " + dateRes % 1000 + " мс");
    }

    public ArrayList<Accessory> getAllAccessories() {
        return accessoriesList;
    }

    public List<String> getAllTypes() {
        List<String> types = Arrays.asList("Чаша", "Колба", "Мундштук", "Щипцы", "Прочее");
        return types;
    }

    public ArrayList<Accessory> getAccessoriesByType(String type) {
        ArrayList<Accessory> accessories = getAllAccessories();
        ArrayList<Accessory> resAccessories = new ArrayList<>();
        for (Accessory accessory : accessories) {
            if (accessory.getType().contains(type)) {
                resAccessories.add(accessory);
            }
        }
        return resAccessories;
    }

    public ArrayList<String> getAllNamesList() {
        ArrayList<String> names = new ArrayList<>();
        for (Accessory a : getAllAccessories()) {
            names.add(a.getName());
        }
        return names;
    }

    public Accessory getAccessoryById(long id) {
        ArrayList<Accessory> accessories = getAllAccessories();
        Accessory accessory = new Accessory();
        for (Accessory a : accessories) {
            if (a.getId() == id) {
                accessory.setId(a.getId());
                accessory.setName(a.getName());
                accessory.setPrice(a.getPrice());
                accessory.setImg(a.getImg());
                accessory.setAvailable(a.getAvailable());
                accessory.setDescription(a.getDescription());
                accessory.setType(a.getType());
                return accessory;
            }
        }
        return null;
    }

    public Accessory getAccessoryByIName(String name) {
        ArrayList<Accessory> accessories = getAllAccessories();
        Accessory accessory = new Accessory();
        for (Accessory a : accessories) {
            if (a.getName().equals(name)) {
                accessory.setId(a.getId());
                accessory.setName(a.getName());
                accessory.setPrice(a.getPrice());
                accessory.setImg(a.getImg());
                accessory.setAvailable(a.getAvailable());
                accessory.setDescription(a.getDescription());
                accessory.setType(a.getType());
                return accessory;
            }
        }
        return null;
    }


    public void parseAllAccessories() {
        accessoriesList = new ArrayList<>();

        for (int i = 1; i < 12; i++) {

            try {
                document = Jsoup.connect("https://hookahinrussia.ru/product-category/%D0%B0%D0%BA%D1%81%D0%B5%D1%81%D1%81%D1%83%D0%B0%D1%80%D1%8B/page/" + i + "/").get();
            } catch (IOException e) {
                System.err.println("Указанной страницы '" + i + "' не существует!");
                break;
            }


            ArrayList<Accessory> tempAccessories = new ArrayList<>();
            Elements elements = document.getElementsByClass("products columns-4").select("li");
            Accessory accessory;

            for (Element e : elements) {
                accessory = new Accessory();
                String productUrl = e.child(0).attr("href");
                try {
                    document = Jsoup.connect(productUrl).get();
                } catch (IOException ioException) {
                    System.err.println("Такой странички с акссесуаром не существует");
                }
                Elements info = document.getElementsByClass("summary entry-summary");
                Element image = document.getElementsByClass("attachment-shop_thumbnail woocommerce-product-gallery__image").first();
                String name = info.first().child(0).text();
                accessory.setName(name);
                accessory.setImg(image.child(0).child(0).attr("src"));
                String price = info.first().child(1).text().replaceAll(".00 руб.", "");


                if (price.length() > 5) {
                    String[] priceArr = price.split(" ");
                    accessory.setPrice(Long.parseLong(priceArr[1]));
                } else {
                    accessory.setPrice(Long.parseLong(price));
                }
                accessory.setPrice(Long.parseLong(price));
                Element description = document.getElementsByClass("woocommerce-Tabs-panel woocommerce-Tabs-panel--description panel entry-content wc-tab").first();
                accessory.setDescription(description.child(1).text());



                tempAccessories.add(accessory);
            }

            accessoriesList.addAll(tempAccessories);
            System.out.println(accessoriesList);
        }
        for (Accessory accessory : accessoriesList) {
            if (accessory.getName().contains("Чаша")) {
                accessory.setType("Чаша");
            } else if (accessory.getName().contains("Колба")) {
                accessory.setType("Колба");
            } else if (accessory.getName().contains("Щипцы")) {
                accessory.setType("Щипцы");
            } else if (accessory.getName().contains("Мундштук")) {
                accessory.setType("Мундштук");
            } else {
                accessory.setType("Прочее");
            }
        }
        for (int i = 0; i < accessoriesList.size(); i++) {
            accessoriesList.get(i).setId(i);
        }
    }


}

