package Service;

import Models.Products.Charcoal;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class CharcoalService {
    private ArrayList<Charcoal> charcoalList = new ArrayList<>();

    public static void main(String[] args) {
        CharcoalService charcoalService = new CharcoalService();
        System.out.println(charcoalService.getAllNamesList());
        System.out.println(charcoalService.getAvailableCharcoal());
        System.out.println(charcoalService.getCharcoalById(1));
        System.out.println(charcoalService.getAvailableCharcoalNamesList());

    }

    public CharcoalService() {
        Date date = new Date();
        //parseAllCharcoal();
        Date date2 = new Date();
        long dateRes = date2.getTime() - date.getTime();
        System.out.println("Парсинг углей занял " + dateRes / 1000 + " с " + dateRes % 1000 + " мс");
    }

    public ArrayList<Charcoal> getAllCharcoal() {
        return charcoalList;
    }

    public ArrayList<Charcoal> getAvailableCharcoal() {
        ArrayList<Charcoal> result = new ArrayList<>();
        for (Charcoal charcoal : charcoalList) {
            if (charcoal.isAvailable()){
                result.add(charcoal);
            }
        }
        return result;
    }

    public ArrayList<String> getAvailableCharcoalNamesList(){
        ArrayList<Charcoal> list = getAvailableCharcoal();
        ArrayList<String> result = new ArrayList<>();

        for (Charcoal charcoal : list) {
            result.add(charcoal.getName());
        }
        return result;
    }

    public ArrayList<String> getAllNamesList() {
        ArrayList<String> names = new ArrayList<>();
        for (Charcoal charcoal : getAllCharcoal()) {
            names.add(charcoal.getName());
        }
        return names;
    }

    public Charcoal getCharcoalById(long id) {
        ArrayList<Charcoal> charcoals = getAllCharcoal();
        Charcoal charcoal = new Charcoal();
        for (Charcoal a : charcoals) {
            if (a.getId() == id) {
                charcoal.setId(a.getId());
                charcoal.setName(a.getName());
                charcoal.setPrice(a.getPrice());
                charcoal.setImg(a.getImg());
                charcoal.setAvailable(a.isAvailable());
                charcoal.setDescription(a.getDescription());

                return charcoal;
            }
        }
        return null;
    }

    public Charcoal getCharcoalByName(String name) {
        ArrayList<Charcoal> charcoals = getAllCharcoal();
        Charcoal charcoal = new Charcoal();
        for (Charcoal c : charcoals) {
            if (c.getName().equals(name)) {
                charcoal.setId(c.getId());
                charcoal.setName(c.getName());
                charcoal.setPrice(c.getPrice());
                charcoal.setImg(c.getImg());
                charcoal.setAvailable(c.isAvailable());
                charcoal.setDescription(c.getDescription());

                return charcoal;
            }
        }
        return null;
    }

    public void parseAllCharcoal() {
        Document document;
        try {
            document = Jsoup.connect("https://hookahinrussia.ru/product-category/%d1%83%d0%b3%d0%be%d0%bb%d1%8c/").get();
            ArrayList<Charcoal> tempCharcoal = new ArrayList<>();
            Elements elements = document.getElementsByClass("products columns-4").select("li");
            for (Element e : elements) {
                Charcoal charcoal = new Charcoal();
                if (e.child(1).text().contains("В корзину")) {
                    charcoal.setAvailable(true);
                } else {
                    charcoal.setAvailable(false);
                }
                String productUrl = e.child(0).attr("href");
                try {
                    document = Jsoup.connect(productUrl).get();
                } catch (IOException ioException) {
                    System.err.println("Такой странички с кальяном не существует");
                }
                Elements info = document.getElementsByClass("summary entry-summary");
                Element image = document.getElementsByClass("attachment-shop_thumbnail woocommerce-product-gallery__image").first();

                String name = info.first().child(0).text().replace("Уголь для кальяна ", "");
                charcoal.setName(name);

                charcoal.setImg(image.child(0).child(0).attr("src"));

                String price = info.first().child(1).text().replaceAll(".00 руб.", "");
                if (price.length() > 5) {
                    String[] priceArr = price.split(" ");
                    charcoal.setPrice(Long.parseLong(priceArr[1]));
                } else {
                    charcoal.setPrice(Long.parseLong(price));
                }

                Element description = document.getElementsByClass("woocommerce-Tabs-panel woocommerce-Tabs-panel--description panel entry-content wc-tab").first();

                if (description != null) {
                    charcoal.setDescription(description.child(2).text());
                } else {
                    charcoal.setDescription("Описание данного товара не доступно.");
                }
                tempCharcoal.add(charcoal);
            }
            charcoalList.addAll(tempCharcoal);

            for (int i = 0; i < charcoalList.size(); i++) {
                charcoalList.get(i).setId(i);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
