package Service;

import Models.Products.Hookah;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class HookahService {

    private ArrayList<Hookah> hookahsList;

    public static void main(String[] args) {
        HookahService hookahService = new HookahService();
        System.out.println(hookahService.hookahsList());
    }

    public HookahService() {
        Date date = new Date();
        parseAllHookahs();
        Date date2 = new Date();
        long dateRes = date2.getTime() - date.getTime();
        System.out.println("Парсинг кальянов занял " + dateRes / 1000 + " с " + dateRes % 1000 + " мс");
    }

    public ArrayList<Hookah> hookahsList() {
        return hookahsList;
    }

    public ArrayList<String> getAllBrandsList() {
        Document document = null;
        try {
            document = Jsoup.connect("https://hookahinrussia.ru/product-category/%d0%ba%d0%b0%d0%bb%d1%8c%d1%8f%d0%bd%d1%8b/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> brands = new ArrayList<>();
        assert document != null;
        Elements elements = document.getElementsByClass("children");
        for (Element element : elements.select("a")) {
            brands.add(element.text().replace("Кальян ", "").replace("-", " ").trim().toUpperCase());
        }
        return brands;
    }

    public Hookah getHookahById(long id) {
        for (Hookah h : hookahsList) {
            if (h.getId() == id)
                return h;
        }
        return null;
    }

    public ArrayList<Hookah> getHookahsByBrand(String brandName) {
        ArrayList<Hookah> resHookahs = new ArrayList<>();
        for (Hookah h : hookahsList) {
            if (h.getBrand().equals(brandName))
                resHookahs.add(h);
        }
        return resHookahs;
    }
    public ArrayList<Hookah> getAvailableHookahsByBrand(String brandName) {
        ArrayList<Hookah> resHookahs = new ArrayList<>();
        for (Hookah h : hookahsList) {
            if (h.isAvailable()) {
                if (h.getBrand().equals(brandName))
                    resHookahs.add(h);
            }
        }
        return resHookahs;
    }

    public ArrayList<String> getAvailableBrandsList() {
        HashSet<String> resBrands = new HashSet<>();
        for (Hookah h : hookahsList) {
            if (h.isAvailable())
                resBrands.add(h.getBrand());
        }
        return new ArrayList<>(resBrands);
    }

    public void parseAllHookahs() {
        hookahsList = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            Document document;
            try {
                document = Jsoup.connect("https://hookahinrussia.ru/product-category/%d0%ba%d0%b0%d0%bb%d1%8c%d1%8f%d0%bd%d1%8b/page/" + i + "/").get();
            } catch (IOException e) {
                System.err.println("Указанной страницы '" + i + "' не существует!");
                break;
            }
            ArrayList<Hookah> tempHookahs = new ArrayList<>();
            ArrayList<String> brandList = getAllBrandsList();
            Elements elements = document.getElementsByClass("products columns-4").select("li");
            for (Element e : elements) {
                Hookah hookah = new Hookah();
                String productUrl = e.child(0).attr("href");
                try {
                    document = Jsoup.connect(productUrl).get();
                } catch (IOException ioException) {
                    System.err.println("Такой странички с кальяном не существует");
                }
                Elements info = document.getElementsByClass("summary entry-summary");
                Element image = document.getElementsByClass("attachment-shop_thumbnail woocommerce-product-gallery__image").first();
                String name = info.first().child(0).text().replace("Кальян", "");
                for (String brand : brandList) {
                    if (name.toLowerCase().contains(brand.toLowerCase().replace("hookah","").trim())) {
                        hookah.setBrand(brand.toUpperCase());
                        hookah.setName(name.toUpperCase().replace(brand.toUpperCase(),"").replace("X ","")
                                .replace("DSH", "").trim());
                        break;
                    }
                }
                hookah.setImg(image.child(0).attr("href"));
                String price = info.first().child(1).text().replaceAll(".00 руб.", "");
                if (price.length() > 6) {
                    String[] priceArr = price.split(" ");
                    hookah.setPrice(Long.parseLong(priceArr[1]));
                }
                else {
                    hookah.setPrice(Long.parseLong(price));
                }
                hookah.setAvailable(!info.first().child(4).text().contains("Нет в наличии"));
                Element description = document.getElementsByClass("woocommerce-Tabs-panel woocommerce-Tabs-panel--description panel entry-content wc-tab").first();
                if (description.child(4).text().contains("ПОДРОБНЕЕ")) {
                    hookah.setDescription(description.child(5).text());
                }
                else {
                    if (description.child(5).text().contains("ПОДРОБНЕЕ")) {
                        hookah.setDescription(description.child(6).text());
                    }
                    else if (description.child(6).text().contains("ПОДРОБНЕЕ")) {
                        hookah.setDescription(description.child(7).text());
                    }
                    else {
                        try {
                            hookah.setDescription(description.text().split("ПОДРОБНЕЕ О КАЛЬЯНЕ")[1].split("Купить кальян")[0]);
                        }
                        catch (Exception e1) {
                            hookah.setDescription(description.child(3).text());
                        }
                    }
                }
                tempHookahs.add(hookah);
            }
            hookahsList.addAll(tempHookahs);
            for (int id = 0; id < hookahsList.size(); id++)
                hookahsList.get(id).setId(id);
        }
    }
}
