package Service;

import Models.Products.Tobacco;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class TobaccoService {
    private ArrayList<Tobacco> tobaccoList;

    public static void main(String[] args) throws IOException {
        TobaccoService tobaccoService = new TobaccoService();
        ArrayList<Tobacco> list = tobaccoService.tobaccoList;
    }

    public TobaccoService() {
        Date date = new Date();
        parseAllTobacco();
        Date date2 = new Date();
        long dateRes = date2.getTime() - date.getTime();
        System.out.println("Парсинг табаков занял " + dateRes / 1000 + " с " + dateRes % 1000 + " мс");
    }

    public ArrayList<Tobacco> getAllTobacco() {
        return tobaccoList;
    }

    public ArrayList<String> getAllNamesList() {
        ArrayList<String> names = new ArrayList<>();
        for (Tobacco h : getAllTobacco()) {
            names.add(h.getName());
        }
        return names;
    }

    public ArrayList<Tobacco> getTobaccoByBrand(String brandName) {
        ArrayList<Tobacco> tobacco = getAllTobacco();
        ArrayList<Tobacco> resTobacco = new ArrayList<>();
        for (Tobacco h : tobacco) {
            if (h.getName().contains(brandName.trim()))
                resTobacco.add(h);
        }
        return resTobacco;
    }

    public Tobacco getTobaccoByName(String name) {
        ArrayList<Tobacco> tobacco = getAllTobacco();
        for (Tobacco h : tobacco) {
            if (h.getName().equals(name))
                return h;
        }
        return null;
    }

    public void parseAllTobacco() {
        tobaccoList = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            Document document;
            try {
                document = Jsoup.connect("https://hookahinrussia.ru/product-category/%D0%B1%D1%80%D0%B5%D0%BD%D0%B4%D1%8B/page/" + i + "/").get();
            } catch (IOException e) {
                System.err.println("Указанной страницы '" + i + "' не существует!");
                break;
            }
            ArrayList<Tobacco> tempTobacco = new ArrayList<>();
            Elements elements = document.getElementsByClass("products columns-4");
            Elements namesElem = elements.select("H2");
            Elements priceElem = document.getElementsByClass("price");
            Tobacco tobacco;
            for (Element element : elements.select("img")) {
                tobacco = new Tobacco();
                tobacco.setImg(element.attr("src"));
                tempTobacco.add(tobacco);
            }
            for (Element element : namesElem) {
                Tobacco t = tempTobacco.get(namesElem.indexOf(element));
                t.setName(element.text().toUpperCase());
                try {
                    parseTobaccoTastes(t);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (Element element : priceElem) {
                tempTobacco.get(priceElem.indexOf(element)).setPrice(Long.parseLong(element.text().replaceAll(".00 руб.+", "")));
            }
            tobaccoList.addAll(tempTobacco);
        }
    }

    public void parseTobaccoTastes(Tobacco currTobacco) throws IOException {
/*        ArrayList<String> radonejskaya = new ArrayList<>();
        ArrayList<String> karlaMarksa = new ArrayList<>();
        Document document = Jsoup.connect("https://hookahinrussia.ru/shop/%D0%B1%D1%80%D0%B5%D0%BD%D0%B4%D1%8B/%D1%82%D0%B0%D0%B1%D0%B0%D0%BA-%D0%B4%D0%BB%D1%8F-%D0%BA%D0%B0%D0%BB%D1%8C%D1%8F%D0%BD%D0%B0-" +
                currTobacco.getName().replaceAll(" ", "-").toLowerCase() + "/").get();
        Element tbody = document.getElementsByClass("woocommerce-product-details__short-description").select("tr").get(1);
        Elements addresses = tbody.children();
        for (TextNode taste : addresses.get(0).textNodes()) {
            radonejskaya.add(taste.text().trim());
        }
        for (TextNode taste : addresses.get(1).textNodes()) {
            karlaMarksa.add(taste.text().trim());
        }
        currTobacco.setRadonejskayaTastes(radonejskaya);
        currTobacco.setKarlaMarkasaTastes(karlaMarksa);*/
    }
}
