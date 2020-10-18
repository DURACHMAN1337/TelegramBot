package Bot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Hookah {

    public static void main(String[] args) {
        Hookah hookah = new Hookah();
        System.out.println(hookah.getImg());

    }

    private Document document;

    public Hookah(){
        connect();
    }
    public void connect(){
       try {
           document = Jsoup.connect("https://hookahinrussia.ru/shop/%d0%ba%d0%b0%d0%bb%d1%8c%d1%8f%d0%bd%d1%8b/euphoria/%d0%ba%d0%b0%d0%bb%d1%8c%d1%8f%d0%bd-euphoria-6/").get();
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

}
