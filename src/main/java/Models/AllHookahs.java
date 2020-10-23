package Models;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AllHookahs {

    private Document document;
    private ArrayList<Hookah> hookahs;

    public AllHookahs() {
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
        ArrayList<String> hookahBrands = new ArrayList<>();
        Elements elements = document.getElementsByClass("children");
        for (Element element : elements.select("a")) {
            hookahBrands.add(element.text());
        }
        return hookahBrands;
    }

    public ArrayList<Hookah> getHookahsByBrand(String brandName) {
        ArrayList<Hookah> hookahs = getAllHookahs();
        ArrayList<Hookah> resHookahs = new ArrayList<>();
        for (Hookah h : hookahs) {
            if (h.getName().contains(brandName))
                resHookahs.add(h);
        }
        return resHookahs;
    }

    public ArrayList<Hookah> parseAllHookahs() {
        ArrayList<Hookah> hookahs = new ArrayList<>();
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
                hookah.setImg(element.attr("src"));
                tempHookahs.add(hookah);
            }
            for (Element element : namesElem) {
                tempHookahs.get(namesElem.indexOf(element)).setName(element.text());
            }
            for (Element element : priceElem) {
                tempHookahs.get(priceElem.indexOf(element)).setPrice(Long.parseLong(element.text().replaceAll(".00 руб.+", "")));
            }
            hookahs.addAll(tempHookahs);
        }
        return hookahs;
    }
}
