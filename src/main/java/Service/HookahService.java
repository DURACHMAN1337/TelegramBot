package Service;

import Models.Products.Hookah;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class HookahService {

    private Document document;
    private ArrayList<Hookah> hookahs;

    public static void main(String[] args) {
        HookahService hookahService = new HookahService();
        System.out.println(hookahService.getAllBrandsList());
    }

    public HookahService() {
        Date date = new Date();
        parseAllHookahs();
        Date date2 = new Date();
        long dateRes = date2.getTime() - date.getTime();
        System.out.println("Парсинг кальянов занял " + dateRes / 1000 + " с " + dateRes % 1000 + " мс");
    }

    public ArrayList<Hookah> getAllHookahs() {
        return hookahs;
    }

    public ArrayList<String> getAllBrandsList() {
        ArrayList<String> brands = new ArrayList<>();
        Elements elements = document.getElementsByClass("children");
        for (Element element : elements.select("a")) {
                brands.add(element.text().toUpperCase().replace("КАЛЬЯН ", "").replace("-", " "));
        }
        return brands;
    }

    public ArrayList<String> getAllNamesList() {
        ArrayList<String> names = new ArrayList<>();
        for (Hookah h : getAllHookahs()) {
            names.add(h.getName());
        }
        return names;
    }

    public ArrayList<Hookah> getHookahsByBrand(String brandName) {
        ArrayList<Hookah> hookahs = getAllHookahs();
        ArrayList<Hookah> resHookahs = new ArrayList<>();
        for (Hookah h : hookahs) {
            if (h.getName().contains(brandName.replace("HOOKAH", "").trim()))
                resHookahs.add(h);
        }
        return resHookahs;
    }

    public Hookah getHookahByName(String name) {
        ArrayList<Hookah> hookahs = getAllHookahs();
        for (Hookah h : hookahs) {
            if (h.getName().equals(name))
                return h;
        }
        return null;
    }

    public void parseAllHookahs() {
        hookahs = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            try {
                document = Jsoup.connect("https://hookahinrussia.ru/product-category/%d0%ba%d0%b0%d0%bb%d1%8c%d1%8f%d0%bd%d1%8b/page/" + i + "/").get();
            } catch (IOException e) {
                System.err.println("Указанной страницы '" + i + "' не существует!");
                break;
            }
            ArrayList<Hookah> tempHookahs = new ArrayList<>();
            Elements elements = document.getElementsByClass("products columns-4");
            Elements namesElem = elements.select("H2");
            Elements priceElem = document.getElementsByClass("price");
            Hookah hookah;
            for (Element element : elements.select("img")) {
                hookah = new Hookah();
                hookah.setImg(element.attr("src").replace("300x300", "1024x1024"));
                tempHookahs.add(hookah);
            }
            for (Element element : namesElem) {
                tempHookahs.get(namesElem.indexOf(element)).setName(element.text().toUpperCase().replaceAll("КАЛЬЯН ", ""));

            }
            for (Element element : priceElem) {
                String tempPrice = element.text().replaceAll(".00 руб.", "");
                if (tempPrice.length() > 5) {
                    String[] price = tempPrice.split(" ");
                    tempHookahs.get(priceElem.indexOf(element)).setPrice(Long.parseLong(price[1]));
                }
                else
                    tempHookahs.get(priceElem.indexOf(element)).setPrice(Long.parseLong(tempPrice));
            }
            hookahs.addAll(tempHookahs);
        }
    }
}
