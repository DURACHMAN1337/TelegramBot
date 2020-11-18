package Bot.Service;

import Bot.Models.Products.Tobacco;
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

public class TobaccoService {
    private ArrayList<Tobacco> tobaccoList = new ArrayList<>();

    public TobaccoService() {
        Date date = new Date();
        parseAllTobacco("xm");
        Date date2 = new Date();
        long dateRes = date2.getTime() - date.getTime();
        System.out.println("Парсинг табаков занял " + dateRes / 1000 + " с " + dateRes % 1000 + " мс");
    }

    public ArrayList<Tobacco> getAllTobacco() {
        return tobaccoList;
    }

    public ArrayList<Tobacco> getAllAvailableTobacco() {
        ArrayList<Tobacco> availableTobaccos = new ArrayList<>();
        for(Tobacco t : tobaccoList) {
            if (t.isAvailable())
                availableTobaccos.add(t);
        }
        return availableTobaccos;
    }

    public ArrayList<String> getAllFortresses() {
        List<String> fortresses = Arrays.asList("Легкая", "Средняя", "Выше средней", "Высокая", "Очень высокая");
        return new ArrayList<>(fortresses);
    }

    public ArrayList<Tobacco> getTobaccoByFortress(String fortress, ArrayList<Tobacco> tobaccos) {
        ArrayList<Tobacco> resTobacco = new ArrayList<>();
        for (Tobacco t : tobaccos) {
            if (t.getFortress().contains(fortress))
                resTobacco.add(t);
        }
        return resTobacco;
    }

    public ArrayList<String> getAllAvailableFortresses() {
        HashSet<String> resFortresses = new HashSet<>();
        for (Tobacco t : tobaccoList) {
            if (t.isAvailable()) {
                resFortresses.add(t.getFortress());
            }
        }
        return new ArrayList<>(resFortresses);
    }

    public Tobacco getTobaccoById(long id) {
        ArrayList<Tobacco> tobacco = getAllTobacco();
        for (Tobacco t : tobacco) {
            if (t.getId() == id) {
                try {
                    return (Tobacco) t.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void parseTobaccosFromXML() {
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
        NodeList tobaccoNodes = document.getDocumentElement().getElementsByTagName("Tobacco");
        for (int i = 0; i < tobaccoNodes.getLength(); i++) {
            Node tobaccoNode = tobaccoNodes.item(i);
            Tobacco tobacco = new Tobacco();
            tobacco.setId(Long.parseLong(tobaccoNode.getAttributes().getNamedItem("id").getNodeValue()));
            tobacco.setName(tobaccoNode.getAttributes().getNamedItem("name").getNodeValue());
            tobacco.setPrice(Long.parseLong(tobaccoNode.getAttributes().getNamedItem("price").getNodeValue()));
            tobacco.setImg(tobaccoNode.getAttributes().getNamedItem("img").getNodeValue());
            tobacco.setAvailable(Boolean.valueOf(tobaccoNode.getAttributes().getNamedItem("isAvailable").getNodeValue()));
            String rt = tobaccoNode.getAttributes().getNamedItem("radTastes").getNodeValue()
                    .replace("[","").replace("]","");
            String kt = tobaccoNode.getAttributes().getNamedItem("karTastes").getNodeValue()
                    .replace("[","").replace("]","");
            ArrayList<String> radTastes = new ArrayList<>();
            ArrayList<String> karTastes = new ArrayList<>();
            for (String s : rt.split(","))
                radTastes.add(s.trim());
            for (String s : kt.split(","))
                karTastes.add(s.trim());
            tobacco.setRadonejskayaTastes(radTastes);
            tobacco.setKarlaMarksaTastes(karTastes);
            tobacco.setDescription(tobaccoNode.getAttributes().getNamedItem("description").getNodeValue());
            tobacco.setFortress(tobaccoNode.getAttributes().getNamedItem("fortress").getNodeValue());
            tobaccoList.add(tobacco);
        }
    }

    public void parseAllTobacco(String type) {
        if (type.equals("xml")) {
            parseTobaccosFromXML();
        }
        else {
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
                    } else {
                        tobacco.setPrice(Long.parseLong(price));
                    }
                    Element description = document.getElementsByClass("woocommerce-Tabs-panel woocommerce-Tabs-panel--description panel entry-content wc-tab").first();
                    if (description.text().split("ПОДРОБНЕЕ").length > 1) {
                        String desc = description.text().replace("Описание","").split("ПОДРОБНЕЕ")[1].trim();
                        tobacco.setDescription(desc.split("Купить")[0].trim());
                        String fortress = description.text().split("Крепость: ")[1].split(" ")[0];
                        if (fortress.equals("Очень"))
                            tobacco.setFortress("Очень высокая");
                        else if (fortress.equals("Выше"))
                            tobacco.setFortress("Выше средней");
                        else
                            tobacco.setFortress(fortress);
                    }
                    else {
                        tobacco.setDescription("Неправильный description");
                    }
                    ArrayList<String> radonejskaya = new ArrayList<>();
                    ArrayList<String> karlaMarksa = new ArrayList<>();
                    Elements table = document.getElementsByClass("woocommerce-product-details__short-description").select("tr");
                    if (!table.isEmpty()) {
                        Element tbody = table.get(1);
                        Elements addresses = tbody.children();
                        List<org.jsoup.nodes.Node> rTastes = addresses.get(0).childNodes();
                        List<org.jsoup.nodes.Node> kTastes = addresses.get(1).childNodes();
                        for (org.jsoup.nodes.Node tasteNode : rTastes) {
                            String taste = tasteNode.toString().replace("<p>","").replace("</p>","")
                                    .replaceAll("<br>","");
                            if (!taste.isEmpty() && !taste.equals("–") && !taste.equals("-") && !taste.equals(" "))
                                radonejskaya.add(taste.toLowerCase().trim());
                        }
                        for (org.jsoup.nodes.Node tasteNode : kTastes) {
                            String taste = tasteNode.toString().replace("<p>","").replace("</p>","")
                                    .replaceAll("<br>","");
                            if (!taste.isEmpty() && !taste.equals("–") && !taste.equals("-") && !taste.equals(" "))
                                karlaMarksa.add(taste.toLowerCase().trim());
                        }
                        if (!radonejskaya.isEmpty()) {
                            tobacco.setAvailable(true);
                            tobacco.setRadonejskayaTastes(radonejskaya);
                        }
                        if (!karlaMarksa.isEmpty()) {
                            tobacco.setAvailable(true);
                            tobacco.setKarlaMarksaTastes(karlaMarksa);
                        }
                    } else {
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
}
