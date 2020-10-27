package Service;

import Models.Products.Accessory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AccessoriesService {
    private Document document;
    private ArrayList<Accessory> accessories;

    public static void main(String[] args) {
        AccessoriesService accessoriesService = new AccessoriesService();
    }

    public ArrayList<Accessory> getAllAccessories(){
        return accessories;
    }

    public AccessoriesService() {
        Date date = new Date();
        parseAllAccessories();
        Date date2 = new Date();
        long dateRes = date2.getTime() - date.getTime();
        System.out.println("Парсинг акссесуаров занял " + dateRes / 1000 + " с " + dateRes % 1000 + " мс");
    }

    public void parseAllAccessories() {
        accessories = new ArrayList<>();
        try {
            document = Jsoup.connect("https://hookahinrussia.ru/wp-admin/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Accessory> tempAccessories = new ArrayList<>();
        Elements elements = document.body().getElementsByClass("wp-list-table widefat fixed striped posts");
        System.out.println(elements);
        Elements namesElem = elements.select("td").select("post_title");
        Elements priceElem = document.getElementsByClass("woocommerce-Price-amount amount");
        for (Element element : elements) {
/*            Accessory accessory = new Accessory();
            accessory.setImg(element.attr("src").replace("100x100", "300x300"));
            tempAccessories.add(accessory);*/
        }
        for (Element element : namesElem) {
            tempAccessories.get(namesElem.indexOf(element)).setName(element.text().toUpperCase());

        }

        for (Element element : priceElem) {
            String tempPrice = element.text().replaceAll(".00 руб.", "");
            if (tempPrice.length() > 5) {
                String[] price = tempPrice.split(" ");
                tempAccessories.get(priceElem.indexOf(element)).setPrice(Long.parseLong(price[1]));
            }
            else
                tempAccessories.get(priceElem.indexOf(element)).setPrice(Long.parseLong(tempPrice));
        }
        accessories.addAll(tempAccessories);
    }

}
