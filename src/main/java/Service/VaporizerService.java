package Service;

import Models.Products.Vaporizer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class VaporizerService {
    private ArrayList<Vaporizer> vaporizersList = new ArrayList<>();

    public static void main(String[] args) {
        VaporizerService vaporizerService = new VaporizerService();
        System.out.println(vaporizerService.getAllNamesList());
        System.out.println(vaporizerService.getVaporizerById(5));
        System.out.println(vaporizerService.getAvailableVaporizers());

    }

    public VaporizerService() {
        Date date = new Date();
        //parseAllVaporizers();
        Date date2 = new Date();
        long dateRes = date2.getTime() - date.getTime();
        System.out.println("Парсинг испарителей занял " + dateRes / 1000 + " с " + dateRes % 1000 + " мс");
    }

    public ArrayList<Vaporizer> getAllVaporizers(){
        return  vaporizersList;
    }

    public ArrayList<Vaporizer> getAvailableVaporizers(){
        ArrayList<Vaporizer> result = new ArrayList<>();
        for (Vaporizer vaporizer : vaporizersList) {
            if (vaporizer.isAvailable()){
                result.add(vaporizer);
            }
        }
        return result;
    }

    public ArrayList<String> getAvailableVaporizersNamesList(){
        ArrayList<Vaporizer> list = getAvailableVaporizers();
        ArrayList<String> result = new ArrayList<>();

        for (Vaporizer vaporizer : list) {
            result.add(vaporizer.getName());
        }
        return result;
    }

    public ArrayList<String> getAllNamesList() {
        ArrayList<String> names = new ArrayList<>();
        for (Vaporizer vaporizer : getAllVaporizers()) {
            names.add(vaporizer.getName());
        }
        return names;
    }

    public Vaporizer getVaporizerById(long id) {
        ArrayList<Vaporizer> vaporizers = getAllVaporizers();
        Vaporizer vaporizer = new Vaporizer();
        for (Vaporizer a : vaporizers) {
            if (a.getId() == id) {
                vaporizer.setId(a.getId());
                vaporizer.setName(a.getName());
                vaporizer.setPrice(a.getPrice());
                vaporizer.setImg(a.getImg());
                vaporizer.setAvailable(a.isAvailable());
                vaporizer.setDescription(a.getDescription());

                return vaporizer;
            }
        }
        return null;
    }

    public Vaporizer getVaporizerByName(String name) {
        ArrayList<Vaporizer> vaporizers = getAllVaporizers();
        Vaporizer vaporizer = new Vaporizer();
        for (Vaporizer c : vaporizers) {
            if (c.getName().equals(name)) {
                vaporizer.setId(c.getId());
                vaporizer.setName(c.getName());
                vaporizer.setPrice(c.getPrice());
                vaporizer.setImg(c.getImg());
                vaporizer.setAvailable(c.isAvailable());
                vaporizer.setDescription(c.getDescription());

                return vaporizer;
            }
        }
        return null;
    }

    public void parseAllVaporizers() {
        Document document;
        try {
            document = Jsoup.connect("https://hookahinrussia.ru/product-category/elecrtonic_vaporizers/").get();
            ArrayList<Vaporizer> tempVaporizers = new ArrayList<>();
            Elements elements = document.getElementsByClass("products columns-4").select("li");

            for (Element e : elements) {
                Vaporizer vaporizer = new Vaporizer();
                if (e.child(1).text().contains("В корзину")) {
                    vaporizer.setAvailable(true);
                } else {
                    vaporizer.setAvailable(false);
                }
                String productUrl = e.child(0).attr("href");

                try {
                    document = Jsoup.connect(productUrl).get();
                } catch (IOException ioException) {
                    System.err.println("Такой странички с кальяном не существует");
                }
                Elements info = document.getElementsByClass("summary entry-summary");
                Element image = document.getElementsByClass("attachment-shop_thumbnail woocommerce-product-gallery__image").first();
                String name = info.first().child(0).text();
                vaporizer.setName(name);

                vaporizer.setImg(image.child(0).child(0).attr("src"));

                String price = info.first().child(1).text().replaceAll(".00 руб.", "");
                if (price.length() > 5) {
                    String[] priceArr = price.split(" ");
                    vaporizer.setPrice(Long.parseLong(priceArr[1]));
                } else {
                    vaporizer.setPrice(Long.parseLong(price));
                }
                Element description = document.getElementsByClass("woocommerce-Tabs-panel woocommerce-Tabs-panel--description panel entry-content wc-tab").first();
                if (description != null) {
                    vaporizer.setDescription(description.child(2).text() + ". " + description.child(1).text());
                } else {
                    vaporizer.setDescription("Описание данного товара не доступно.");
                }
                tempVaporizers.add(vaporizer);
            }
            vaporizersList.addAll(tempVaporizers);
            for (int i = 0; i < vaporizersList.size(); i++) {
                vaporizersList.get(i).setId(i);
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
