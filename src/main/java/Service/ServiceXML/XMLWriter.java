package Service.ServiceXML;

import Models.Products.Accessory;
import Models.Products.Hookah;
import Models.Products.Tobacco;
import Service.AccessoriesService;
import Service.HookahService;
import Service.TobaccoService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;

public class XMLWriter {
    private static final HookahService HOOKAH_SERVICE = new HookahService();
    private static final TobaccoService TOBACCO_SERVICE = new TobaccoService();
    private static final AccessoriesService ACCESSORIES_SERVICE = new AccessoriesService();

    public static void main(String[] args) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            // создаем корневой элемент
            Element rootElement =
                    doc.createElement("Products");
            Element hookahNode = doc.createElement("Hookahs");
            for (Hookah h: HOOKAH_SERVICE.getAllHookahs()) {
                hookahNode.appendChild(addHookah(doc,h.getId(),h.getBrand(),h.getName(),
                        h.getPrice(),h.getImg(),h.isAvailable(),h.getDescription()));
            }
            Element tobaccoNode = doc.createElement("Tobaccos");
            for (Tobacco t: TOBACCO_SERVICE.getAllTobacco()) {
                tobaccoNode.appendChild(addTobacco(doc,t.getId(),t.getName(),t.getPrice(),t.getImg(),t.isAvailable(),
                        t.getTaste(),t.getRadonejskayaTastes(),t.getKarlaMarksaTastes(),t.getDescription(),t.getFortress()));
            }
            Element accessoryNode = doc.createElement("Accessories");
            for (Accessory a: ACCESSORIES_SERVICE.getAllAccessories()) {
                accessoryNode.appendChild(addAccessory(doc,a.getId(),a.getBrand(),a.getName(),a.getPrice(),a.getImg(),
                        a.isAvailable(),a.getType(),a.getDescription()));
            }
            // добавляем корневой элемент в объект Document
            doc.appendChild(rootElement);
            // добавляем дочерние элементы в корневой элемент
            rootElement.appendChild(hookahNode);
            rootElement.appendChild(tobaccoNode);
            rootElement.appendChild(accessoryNode);
            //создаем объект TransformerFactory для печати в консоль
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // для красивого вывода в консоль
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult file = new StreamResult(new File("src/main/java/Service/ServiceXML/products.xml"));
            //записываем данные
            transformer.transform(source, file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Node addHookah(Document doc, long id, String brand, String name, long price,
                                  String img, boolean isAvailable, String description) {
        Element newHookah = doc.createElement("Hookah");
        newHookah.setAttribute("id", String.valueOf(id));
        newHookah.setAttribute("brand", brand);
        newHookah.setAttribute("name", name);
        newHookah.setAttribute("price", String.valueOf(price));
        newHookah.setAttribute("img", img);
        newHookah.setAttribute("isAvailable", String.valueOf(isAvailable));
        newHookah.setAttribute("description", description);
        return newHookah;
    }

    private static Node addTobacco(Document doc, long id, String name, long price, String img,
                                   boolean isAvailable, String taste, ArrayList<String> radonejskayaTastes,
                                   ArrayList<String> karlaMarksaTastes, String description, String fortress) {
        Element newTobacco = doc.createElement("Tobacco");
        newTobacco.setAttribute("id", String.valueOf(id));
        newTobacco.setAttribute("name", name);
        newTobacco.setAttribute("price", String.valueOf(price));
        newTobacco.setAttribute("img", img);
        newTobacco.setAttribute("isAvailable", String.valueOf(isAvailable));
        newTobacco.setAttribute("taste", taste);
        newTobacco.setAttribute("radTastes", String.valueOf(radonejskayaTastes));
        newTobacco.setAttribute("karTastes", String.valueOf(karlaMarksaTastes));
        newTobacco.setAttribute("description", description);
        newTobacco.setAttribute("fortress", fortress);
        return newTobacco;
    }

    private static Node addAccessory(Document doc, long id, String brand, String name, long price, String img,
                                   boolean isAvailable, String type, String description) {
        Element newAccessory = doc.createElement("Accessory");
        newAccessory.setAttribute("id", String.valueOf(id));
        newAccessory.setAttribute("brand", brand);
        newAccessory.setAttribute("name", name);
        newAccessory.setAttribute("price", String.valueOf(price));
        newAccessory.setAttribute("img", img);
        newAccessory.setAttribute("isAvailable", String.valueOf(isAvailable));
        newAccessory.setAttribute("type", type);
        newAccessory.setAttribute("description", description);
        return newAccessory;
    }
}