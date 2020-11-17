package Bot.Service;

import Bot.Models.Products.Hookah;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class HookahService {
    private ArrayList<Hookah> hookahsList = new ArrayList<>();

    public static void main(String[] args) {
        HookahService hookahService = new HookahService();
    }

    public HookahService() {
        Date date = new Date();
        parseAllHookahs("xml");
        Date date2 = new Date();
        long dateRes1 = date2.getTime() - date.getTime();
        System.out.println("Парсинг кальянов занял " + dateRes1 / 1000 + " с " + dateRes1 % 1000 + " мс");
    }

    public ArrayList<Hookah> getAllHookahs() {
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

    public void parseHookahsFromXML() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        org.w3c.dom.Document document = null;
        try {
            assert builder != null;
            document = builder.parse(new File("src/main/java/Bot/Service/ServiceXML/products.xml"));
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        assert document != null;
        NodeList hookahNodes = document.getDocumentElement().getElementsByTagName("Hookah");
        for (int i = 0; i < hookahNodes.getLength(); i++) {
            Node hookahNode = hookahNodes.item(i);
            Hookah hookah = new Hookah();
            hookah.setId(Long.parseLong(hookahNode.getAttributes().getNamedItem("id").getNodeValue()));
            hookah.setBrand(hookahNode.getAttributes().getNamedItem("brand").getNodeValue());
            hookah.setName(hookahNode.getAttributes().getNamedItem("name").getNodeValue());
            hookah.setPrice(Long.parseLong(hookahNode.getAttributes().getNamedItem("price").getNodeValue()));
            hookah.setImg(hookahNode.getAttributes().getNamedItem("img").getNodeValue());
            hookah.setAvailable(Boolean.valueOf(hookahNode.getAttributes().getNamedItem("isAvailable").getNodeValue()));
            hookah.setDescription(hookahNode.getAttributes().getNamedItem("description").getNodeValue());
            hookahsList.add(hookah);
        }
    }

    public void parseAllHookahs(String type) {
        if (type.equals("xml")) {
            parseHookahsFromXML();
        } else {
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
                        if (name.toLowerCase().contains(brand.toLowerCase().replace("hookah", "").trim())) {
                            hookah.setBrand(brand.toUpperCase());
                            hookah.setName(name.toUpperCase().replace(brand.toUpperCase(), "").replace("X ", "")
                                    .replace("DSH", "").trim());
                            break;
                        }
                    }
                    hookah.setImg(image.child(0).attr("href"));
                    String price = info.first().child(1).text().replaceAll(".00 руб.", "");
                    if (price.length() > 6) {
                        String[] priceArr = price.split(" ");
                        hookah.setPrice(Long.parseLong(priceArr[1]));
                    } else {
                        hookah.setPrice(Long.parseLong(price));
                    }
                    hookah.setAvailable(!info.first().child(4).text().contains("Нет в наличии"));
                    Element description = document.getElementsByClass("woocommerce-Tabs-panel woocommerce-Tabs-panel--description panel entry-content wc-tab").first();
                    if (description.text().split("ПОДРОБНЕЕ О КАЛЬЯНЕ").length > 1) {
                        String desc = description.text().split("ПОДРОБНЕЕ О КАЛЬЯНЕ")[1].trim();
                        hookah.setDescription(desc.split("Купить кальян")[0].trim());
                    }
                    else if (description.text().split("ПОДРОБНЕЕ О КАЛЬЯНЕ.").length > 1) {
                        String desc = description.text().split("ПОДРОБНЕЕ О КАЛЬЯНЕ.")[1].trim();
                        hookah.setDescription(desc.split("Купить кальян")[0].trim());
                    }
                    else
                        hookah.setDescription("Неправильный description");
                    tempHookahs.add(hookah);
                }
                hookahsList.addAll(tempHookahs);
                for (int id = 0; id < hookahsList.size(); id++)
                    hookahsList.get(id).setId(id);
            }
        }
    }
}
