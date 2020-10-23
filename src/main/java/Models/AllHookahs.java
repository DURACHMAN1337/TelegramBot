package Models;

import org.checkerframework.checker.units.qual.A;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class AllHookahs {

    public static void main(String[] args) {
        AllHookahs hookahs = new AllHookahs();
        hookahs.parseAllHookahs();
    }

    private ArrayList<Hookah> hookahs;
    Document document;

    public AllHookahs() {
        connect();
    }

    public void connect() {
        try {
            document = Jsoup.connect("https://hookahinrussia.ru/product-category/%d0%ba%d0%b0%d0%bb%d1%8c%d1%8f%d0%bd%d1%8b/page/2/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    // Не доделано
    public void parseAllHookahs() {
        ArrayList<String> allImg = new ArrayList<>();
        ArrayList<String> allNames = new ArrayList<>();
        ArrayList<String> allPrices = new ArrayList<>();
        Elements elements = document.getElementsByClass("products columns-4");
/*        for (Element element : elements.select("img")) {
            allImg.add(element.attr("src"));
            System.out.println(element.attr("src"));
        }
        for (Element element : elements.select("H2")) {
            allNames.add(element.text());
            System.out.println(element.text());
        }*/
        for (Element element : elements.select("price")) {
            allPrices.add(element.text());
            System.out.println(element.text());
        }
    }
}
