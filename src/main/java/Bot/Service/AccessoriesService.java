package Bot.Service;

import Bot.Models.Products.Accessory;
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
import java.util.*;

public class AccessoriesService {

    private ArrayList<Accessory> accessoriesList = new ArrayList<>();

    public AccessoriesService() {
        Date date = new Date();
        parseAllAccessories("xm");
        Date date2 = new Date();
        long dateRes = date2.getTime() - date.getTime();
        System.out.println("Парсинг акссесуаров занял " + dateRes / 1000 + " с " + dateRes % 1000 + " мс");
    }

    public ArrayList<Accessory> getAllAccessories() {
        return accessoriesList;
    }

    public ArrayList<Accessory> getAvailableAccessories() {
        ArrayList<Accessory> list = getAllAccessories();
        ArrayList<Accessory> result = new ArrayList<>();
        for (Accessory accessory : list) {
            if (accessory.isAvailable()){
                result.add(accessory);
            }
        }
        return result;
    }

    public ArrayList<String> getAllTypes() {
        List<String> list =  Arrays.asList("Чаша", "Колба", "Мундштук", "Щипцы", "Прочее");
        return new ArrayList<>(list);
    }

    public ArrayList<Accessory> getAccessoriesByType(String type) {
        ArrayList<Accessory> accessories = getAllAccessories();
        ArrayList<Accessory> resAccessories = new ArrayList<>();
        for (Accessory accessory : accessories) {
            if (accessory.getType().contains(type)) {
                resAccessories.add(accessory);
            }
        }
        return resAccessories;
    }

    public ArrayList<Accessory> getAccessoriesBowlsByBrand(String brand) {
        ArrayList<Accessory> resAccessories = new ArrayList<>();
        for (Accessory accessory : getAccessoriesByType("Чаша")) {
            if (accessory.getBrand() != null) {
                if (accessory.getBrand().contains(brand)) {
                    resAccessories.add(accessory);
                }
            }
        }
        return resAccessories;
    }

    public ArrayList<Accessory> getAvailableAccessoriesBowlsByBrand(String brand) {
        ArrayList<Accessory> resAccessories = new ArrayList<>();
        for (Accessory accessory : getAvailableAccessoriesByType("Чаша")) {
            if (accessory.getBrand().contains(brand)) {
                resAccessories.add(accessory);
            }
        }
        return resAccessories;
    }

    public ArrayList<Accessory> getAvailableAccessoriesByType(String type){
        ArrayList<Accessory> result = new ArrayList<>();
        ArrayList<Accessory> list = getAvailableAccessories();
        for (Accessory a : list) {
            if (a.getType().equals(type)){
                result.add(a);
            }
        }
        return result;
    }

    public Accessory getAccessoryById(long id) {
        ArrayList<Accessory> accessories = getAllAccessories();
        for (Accessory a : accessories) {
            if (a.getId() == id) {
                return a;
            }
        }
        return null;
    }

    public ArrayList<String> getBowlsBrandList() {
        HashSet<String> brands = new HashSet<>();
        for (Accessory a : getAccessoriesByType("Чаша"))
            brands.add(a.getBrand());
        return new ArrayList<>(brands);
    }

    public ArrayList<String> getAvailableBowlsBrandList() {
        HashSet<String> brands = new HashSet<>();
        for (Accessory a : getAvailableAccessoriesByType("Чаша"))
            brands.add(a.getBrand());
        return new ArrayList<>(brands);
    }

    public void parseAccessoriesFromXML() {
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
        NodeList accessoryNodes = document.getDocumentElement().getElementsByTagName("Accessory");
        for (int i = 0; i < accessoryNodes.getLength(); i++) {
            Node accessoryNode = accessoryNodes.item(i);
            Accessory accessory = new Accessory();
            accessory.setId(Long.parseLong(accessoryNode.getAttributes().getNamedItem("id").getNodeValue()));
            accessory.setBrand(accessoryNode.getAttributes().getNamedItem("brand").getNodeValue());
            accessory.setName(accessoryNode.getAttributes().getNamedItem("name").getNodeValue());
            accessory.setPrice(Long.parseLong(accessoryNode.getAttributes().getNamedItem("price").getNodeValue()));
            accessory.setImg(accessoryNode.getAttributes().getNamedItem("img").getNodeValue());
            accessory.setType(accessoryNode.getAttributes().getNamedItem("type").getNodeValue());
            accessory.setAvailable(Boolean.valueOf(accessoryNode.getAttributes().getNamedItem("isAvailable").getNodeValue()));
            accessory.setDescription(accessoryNode.getAttributes().getNamedItem("description").getNodeValue());
            accessoriesList.add(accessory);
        }
    }

    public void parseAllAccessories(String type) {
        if (type.equals("xml")) {
            parseAccessoriesFromXML();
        }
        else {
            accessoriesList = new ArrayList<>();
            for (int i = 1; i < 12; i++) {
                Document document;
                try {
                    document = Jsoup.connect("https://hookahinrussia.ru/product-category/%D0%B0%D0%BA%D1%81%D0%B5%D1%81%D1%81%D1%83%D0%B0%D1%80%D1%8B/page/" + i + "/").get();
                } catch (IOException e) {
                    System.err.println("Указанной страницы '" + i + "' не существует!");
                    break;
                }
                ArrayList<Accessory> tempAccessories = new ArrayList<>();
                Elements elements = document.getElementsByClass("products columns-4").select("li");
                Accessory accessory;
                for (Element e : elements) {
                    accessory = new Accessory();
                    accessory.setAvailable(e.child(1).text().contains("В корзину"));
                    String productUrl = e.child(0).attr("href");
                    try {
                        document = Jsoup.connect(productUrl).get();
                    } catch (IOException ioException) {
                        System.err.println("Такой странички с акссесуаром не существует " + productUrl);
                    }
                    Elements info = document.getElementsByClass("summary entry-summary");
                    Element image = document.getElementsByClass("attachment-shop_thumbnail woocommerce-product-gallery__image").first();
                    String name = info.first().child(0).text();
                    accessory.setName(name);
                    if (name.contains("Чаша")) {
                        accessory.setType("Чаша");
                        accessory.setName(accessory.getName().replace("Чаша", "").trim());
                        accessory.setBrand(accessory.getName().split(" \\| ")[0].trim());
                        accessory.setName(accessory.getName().split("\\|")[1].trim());
                    } else if (name.contains("Колба")) {
                        accessory.setType("Колба");
                        accessory.setName(accessory.getName().replace("Колба для кальяна", "").trim());
                    } else if (name.contains("Щипцы")) {
                        accessory.setType("Щипцы");
                        accessory.setName(accessory.getName().replace("Щипцы для кальяна", "").trim());
                    } else if (name.contains("Мундштук")) {
                        accessory.setType("Мундштук");
                        accessory.setName(accessory.getName().replace("Мундштук для кальяна", "").trim());
                    } else {
                        accessory.setType("Прочее");
                    }
                    accessory.setImg(image.child(0).child(0).attr("src"));
                    String price = info.first().child(1).text().replaceAll(".00 руб.", "");
                    if (price.length() > 5) {
                        String[] priceArr = price.split(" ");
                        accessory.setPrice(Long.parseLong(priceArr[1]));
                    } else {
                        accessory.setPrice(Long.parseLong(price));
                    }
                    accessory.setPrice(Long.parseLong(price));
                    Element description = document.getElementsByClass("woocommerce-Tabs-panel woocommerce-Tabs-panel--description panel entry-content wc-tab").first();
                    if (description != null) {
                        accessory.setDescription(description.child(1).text());
                    } else {
                        accessory.setDescription("Описание данного товара не доступно.");
                    }
                    tempAccessories.add(accessory);
                }
                accessoriesList.addAll(tempAccessories);
            }
            for (Accessory a :accessoriesList)
                a.setId(accessoriesList.indexOf(a));
        }
    }
}

