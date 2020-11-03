package Service;

import Models.Products.Tobacco;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
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

    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        TobaccoService tobaccoService = new TobaccoService();
    }

    public TobaccoService() {
        Date date = new Date();
        parseAllTobacco("xml");
        Date date2 = new Date();
        long dateRes = date2.getTime() - date.getTime();
        System.out.println("Парсинг табаков занял " + dateRes / 1000 + " с " + dateRes % 1000 + " мс");
    }

    public ArrayList<Tobacco> getAllTobacco() {
        return tobaccoList;
    }

    public ArrayList<String> getAllFortresses() {
        List<String> fortresses = Arrays.asList("Легкая", "Средняя", "Выше средней", "Высокая", "Очень высокая");
        return new ArrayList<>(fortresses);
    }

    public ArrayList<Tobacco> getTobaccoByFortress(String fortress) {
        ArrayList<Tobacco> resTobacco = new ArrayList<>();
        for (Tobacco t : tobaccoList) {
            if (t.getFortress().contains(fortress))
                resTobacco.add(t);
        }
        return resTobacco;
    }

    public ArrayList<Tobacco> getAvailableTobaccoByFortress(String fortress, String street) {
        ArrayList<Tobacco> resTobacco = getTobaccoByFortress(fortress);
        ArrayList<Tobacco> resTobaccoRad = new ArrayList<>();
        ArrayList<Tobacco> resTobaccoKar = new ArrayList<>();
        for (Tobacco t : resTobacco) {
            if (t.getRadonejskayaTastes() != null)
                resTobaccoRad.add(t);
            else if (t.getKarlaMarksaTastes() != null)
                resTobaccoKar.add(t);
        }
        if (street.equals("Rad"))
            return resTobaccoRad;
        else
            return resTobaccoKar;
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

    public ArrayList<String> getAvailableFortressesByStreet(String street) {
        HashSet<String> resFortressesRad = new HashSet<>();
        HashSet<String> resFortressesKar = new HashSet<>();
        for (Tobacco t : tobaccoList) {
            if (t.isAvailable()) {
                String fortress = t.getFortress();
                if (t.getRadonejskayaTastes() != null)
                    resFortressesRad.add(fortress);
                else if (t.getKarlaMarksaTastes() != null)
                    resFortressesKar.add(fortress);
            }
        }
        if (street.equals("Rad"))
            return new ArrayList<>(resFortressesRad);
        else
            return new ArrayList<>(resFortressesKar);
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
            document = builder.parse(new File("src/main/java/Service/ServiceXML/products.xml"));
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
            String[] temp1 = tobaccoNode.getAttributes().getNamedItem("radTastes").getNodeValue()
                    .replace("[","").replace("]","").replace(",", "").split(" ");
            ArrayList<String> radTastes = new ArrayList<>(Arrays.asList(temp1));
            tobacco.setRadonejskayaTastes(radTastes);
            String[] temp2 = tobaccoNode.getAttributes().getNamedItem("karTastes").getNodeValue()
                    .replace("[","").replace("]","").replace(",", "").split(" ");
            ArrayList<String> karTastes = new ArrayList<>(Arrays.asList(temp2));
            tobacco.setKarlaMarksaTastes(karTastes);
            tobacco.setDescription(tobaccoNode.getAttributes().getNamedItem("description").getNodeValue());
            tobacco.setFortress(tobaccoNode.getAttributes().getNamedItem("fortress").getNodeValue());
            tobaccoList.add(tobacco);
        }
    }

    public void parseAllTobacco(String type) {
        if (type.equals("xml")) {
            parseTobaccosFromXML();
        } else {
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
                        for (TextNode taste : addresses.get(0).textNodes()) {
                            if (taste.text() != null && !taste.text().equals("–"))
                                radonejskaya.add(taste.text().toLowerCase());
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
