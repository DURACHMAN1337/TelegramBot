package Bot.Service;

import Bot.Models.Products.Vaporizer;
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

public class VaporizerService {
    private ArrayList<Vaporizer> vaporizersList = new ArrayList<>();

    public VaporizerService() {
        Date date = new Date();
        parseAllVaporizers("xm");
        Date date2 = new Date();
        long dateRes = date2.getTime() - date.getTime();
        System.out.println("Парсинг испарителей занял " + dateRes / 1000 + " с " + dateRes % 1000 + " мс");
    }

    public ArrayList<Vaporizer> getAllVaporizers(){
        return  vaporizersList;
    }

    public ArrayList<Vaporizer> getAvailableVaporizers(){
        ArrayList<Vaporizer> result = new ArrayList<>();
        for (Vaporizer vaporizer : vaporizersList) {
            if (vaporizer.isAvailable()){
                result.add(vaporizer);
            }
        }
        return result;
    }

    public ArrayList<String> getAvailableVaporizersNamesList(){
        ArrayList<Vaporizer> list = getAvailableVaporizers();
        ArrayList<String> result = new ArrayList<>();

        for (Vaporizer vaporizer : list) {
            result.add(vaporizer.getName());
        }
        return result;
    }

    public ArrayList<String> getAllNamesList() {
        ArrayList<String> names = new ArrayList<>();
        for (Vaporizer vaporizer : getAllVaporizers()) {
            names.add(vaporizer.getName());
        }
        return names;
    }

    public Vaporizer getVaporizerById(long id) {
        for (Vaporizer vaporizer : vaporizersList) {
            if (vaporizer.getId() == id)
                return vaporizer;
        }
        return null;
    }

    public void parseVaporizersFromXML() {
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
        NodeList vaporizersNode = document.getDocumentElement().getElementsByTagName("Vaporizer");
        for (int i = 0; i < vaporizersNode.getLength(); i++) {
            Node charcoalNode = vaporizersNode.item(i);
            Vaporizer vaporizer = new Vaporizer();
            vaporizer.setId(Long.parseLong(charcoalNode.getAttributes().getNamedItem("id").getNodeValue()));
            vaporizer.setName(charcoalNode.getAttributes().getNamedItem("name").getNodeValue());
            vaporizer.setPrice(Long.parseLong(charcoalNode.getAttributes().getNamedItem("price").getNodeValue()));
            vaporizer.setImg(charcoalNode.getAttributes().getNamedItem("img").getNodeValue());
            vaporizer.setAvailable(Boolean.parseBoolean(charcoalNode.getAttributes().getNamedItem("isAvailable").getNodeValue()));
            vaporizer.setDescription(charcoalNode.getAttributes().getNamedItem("description").getNodeValue());
            vaporizersList.add(vaporizer);
        }
    }

    public void parseAllVaporizers(String type) {
        if (type.equals("xml")) {
            parseVaporizersFromXML();
        } else {
            Document document;
            try {
                document = Jsoup.connect("https://hookahinrussia.ru/product-category/elecrtonic_vaporizers/").get();
                ArrayList<Vaporizer> tempVaporizers = new ArrayList<>();
                Elements elements = document.getElementsByClass("products columns-4").select("li");

                for (Element e : elements) {
                    Vaporizer vaporizer = new Vaporizer();
                    vaporizer.setAvailable(e.child(1).text().contains("В корзину"));
                    String productUrl = e.child(0).attr("href");

                    try {
                        document = Jsoup.connect(productUrl).get();
                    } catch (IOException ioException) {
                        System.err.println("Такой странички с флешкой не существует");
                    }
                    Elements info = document.getElementsByClass("summary entry-summary");
                    Element image = document.getElementsByClass("attachment-shop_thumbnail woocommerce-product-gallery__image").first();
                    String name = info.first().child(0).text();
                    vaporizer.setName(name.replace(" Электронный одноразовый парогенератор", ""));

                    vaporizer.setImg(image.child(0).child(0).attr("src"));

                    String price = info.first().child(1).text().replaceAll(".00 руб.", "");
                    if (price.length() > 5) {
                        String[] priceArr = price.split(" ");
                        vaporizer.setPrice(Long.parseLong(priceArr[1]));
                    } else {
                        vaporizer.setPrice(Long.parseLong(price));
                    }
                    Element description = document.getElementsByClass("woocommerce-Tabs-panel woocommerce-Tabs-panel--description panel entry-content wc-tab").first();
                    if (description != null) {
                        vaporizer.setDescription(description.child(2).text() + ". " + description.child(1).text());
                    } else {
                        vaporizer.setDescription("Описание данного товара не доступно.");
                    }
                    tempVaporizers.add(vaporizer);
                }
                vaporizersList.addAll(tempVaporizers);
                for (int i = 0; i < vaporizersList.size(); i++) {
                    vaporizersList.get(i).setId(i);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
