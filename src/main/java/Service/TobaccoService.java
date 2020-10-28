package Service;

import Models.Products.Tobacco;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class TobaccoService {
    private ArrayList<Tobacco> tobaccoList;

    public static void main(String[] args) throws IOException {
        TobaccoService tobaccoService = new TobaccoService();
        ArrayList<Tobacco> tobaccos = tobaccoService.getAllTobacco();
        for (Tobacco t : tobaccos) {
            System.out.println(t.getName() + " | " + t.getFortress());
        }
        System.out.println(tobaccoService.getAllFortresses());
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
        for (Tobacco t : getAllTobacco()) {
            names.add(t.getName());
        }
        return names;
    }

    public ArrayList<String> getAllFortresses() {
        List<String> fortresses = Arrays.asList("Легкая", "Средняя", "Выше средней", "Высокая", "Очень высокая");
        return new ArrayList<>(fortresses);
    }

    public ArrayList<Tobacco> getTobaccoByFortress(String fortress) {
        ArrayList<Tobacco> tobacco = getAllTobacco();
        ArrayList<Tobacco> resTobacco = new ArrayList<>();
        for (Tobacco t : tobacco) {
            if (t.getFortress().contains(fortress))
                resTobacco.add(t);
        }
        return resTobacco;
    }

    public Tobacco getTobaccoById(long id) {
        ArrayList<Tobacco> tobacco = getAllTobacco();
        Tobacco tob = new Tobacco();
        for (Tobacco t : tobacco) {
            if (t.getId() == id) {
                tob.setId(t.getId());
                tob.setName(t.getName());
                tob.setPrice(t.getPrice());
                tob.setImg(t.getImg());
                tob.setTaste(t.getTaste());
                tob.setAvailable(t.getAvailable());
                tob.setDescription(t.getDescription());
                tob.setKarlaMarksaTastes(t.getKarlaMarksaTastes());
                tob.setRadonejskayaTastes(t.getRadonejskayaTastes());
                tob.setFortress(t.getFortress());
                return tob;
            }
        }
        return null;
    }

    public Tobacco getTobaccoByName(String name) {
        ArrayList<Tobacco> tobacco = getAllTobacco();
        Tobacco tob = new Tobacco();
        for (Tobacco t : tobacco) {
            if (t.getName().equals(name)) {
                tob.setName(t.getName());
                tob.setPrice(t.getPrice());
                tob.setImg(t.getImg());
                tob.setTaste(t.getTaste());
                tob.setAvailable(t.getAvailable());
                tob.setDescription(t.getDescription());
                tob.setKarlaMarksaTastes(t.getKarlaMarksaTastes());
                tob.setRadonejskayaTastes(t.getRadonejskayaTastes());
                tob.setFortress(t.getFortress());
                return tob;
            }
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
            Elements elements = document.getElementsByClass("products columns-4").select("li");
            for (Element e : elements) {
                Tobacco tobacco = new Tobacco();
                String productUrl = e.child(0).attr("href");
                try {
                    document = Jsoup.connect(productUrl).get();
                } catch (IOException ioException) {
                    System.err.println("Такой странички с табаком не существует");
                }
                Elements info = document.getElementsByClass("summary entry-summary");
                Element image = document.getElementsByClass("attachment-shop_thumbnail woocommerce-product-gallery__image").first();
                String name = info.first().child(0).text();
                tobacco.setName(name);
                tobacco.setImg(image.child(0).child(0).attr("src"));
                String price = info.first().child(1).text().replaceAll(".00 руб.", "");
                if (price.length() > 5) {
                    String[] priceArr = price.split(" ");
                    tobacco.setPrice(Long.parseLong(priceArr[1]));
                }
                else {
                    tobacco.setPrice(Long.parseLong(price));
                }
                Element description = document.getElementsByClass("woocommerce-Tabs-panel woocommerce-Tabs-panel--description panel entry-content wc-tab").first();
                if (description.child(1).text().contains("Крепость:")) {
                    tobacco.setFortress(description.child(1).textNodes().get(0).text().replace("Крепость: ", "").trim());
                    tobacco.setDescription(description.child(3).text());
                }
                else {
                    if (description.child(2).text().contains("Крепость:")) {
                        tobacco.setFortress(description.child(2).textNodes().get(0).text().replace("Крепость: ", "").trim());
                        tobacco.setDescription(description.child(4).text());
                    }
                    else if (description.child(3).text().contains("Крепость:")) {
                        tobacco.setFortress(description.child(3).textNodes().get(0).text().replace("Крепость: ", "").trim());
                        tobacco.setDescription(description.child(5).text());
                    }
                    else {
                        tobacco.setDescription(description.child(1).text());
                    }
                }
                ArrayList<String> radonejskaya = new ArrayList<>();
                ArrayList<String> karlaMarksa = new ArrayList<>();
                Elements table = document.getElementsByClass("woocommerce-product-details__short-description").select("tr");
                if (!table.isEmpty()) {
                    Element tbody = table.get(1);
                    Elements addresses = tbody.children();
                    for (TextNode taste : addresses.get(0).textNodes()) {
                        if (!taste.text().isEmpty() && !taste.text().equals("–"))
                            radonejskaya.add(taste.text().toLowerCase().trim());
                    }
                    for (TextNode taste : addresses.get(1).textNodes()) {
                        if (!taste.text().isEmpty() && !taste.text().equals("–"))
                            karlaMarksa.add(taste.text().toLowerCase().trim());
                    }
                    if (!radonejskaya.isEmpty()) {
                        tobacco.setAvailable(true);
                        tobacco.setRadonejskayaTastes(radonejskaya);
                    }
                    if (!karlaMarksa.isEmpty()) {
                        tobacco.setAvailable(true);
                        tobacco.setKarlaMarksaTastes(karlaMarksa);
                    }
                }
                else {
                    tobacco.setAvailable(false);
                }
                tempTobacco.add(tobacco);
            }
            tobaccoList.addAll(tempTobacco);
            for (int id = 0; id < tobaccoList.size(); id++)
                tobaccoList.get(id).setId(id);
        }
    }
}
