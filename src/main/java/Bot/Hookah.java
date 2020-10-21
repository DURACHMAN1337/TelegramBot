package Bot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hookah {

    public static void main(String[] args) {
        Hookah hookah = new Hookah();
        System.out.println(hookah.getBrandList().toString());


    }

    private Document document;

    public Hookah(){
        connect();
    }
    public void connect(){
       try {
           document = Jsoup.connect("https://hookahinrussia.ru/product-category/%d0%b1%d1%80%d0%b5%d0%bd%d0%b4%d1%8b/").get();
       } catch (IOException e) {
           e.printStackTrace();
       }
    }
    public String getPrice(){
        Elements elements = document.getElementsByClass("woocommerce-Price-amount amount");
        return elements.text();

    }

    public String getDescription (){
        Elements elements = document.getElementsByClass("woocommerce-Tabs-panel woocommerce-Tabs-panel--description panel entry-content wc-tab");
        return elements.text();
    }

    public String getImg(){
        Element elements = document.select("img").get(2);
        String url = elements.attr("src");
        return url;

    }
    public List<String> getBrandList(){
        Elements elements = document.getElementsByClass("woocommerce-loop-product__title");
        String s = elements.text();
        List<String> list = new ArrayList<String>(Arrays.asList(s.split(" ")));

        return list;
    }

}
