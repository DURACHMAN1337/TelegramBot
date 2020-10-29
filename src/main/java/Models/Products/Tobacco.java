package Models.Products;

import java.util.ArrayList;

public class Tobacco extends Product {
    private long id;
    private String name;
    private long price;
    private String img;
    private String taste;
    private boolean sale;
    private ArrayList<String> radonejskayaTastes;
    private ArrayList<String> karlaMarksaTastes;
    private boolean isAvailable;
    private String description;
    private String fortress;

    public Tobacco() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public ArrayList<String> getRadonejskayaTastes() {
        return radonejskayaTastes;
    }

    public void setRadonejskayaTastes(ArrayList<String> radonejskayaTastes) {
        this.radonejskayaTastes = radonejskayaTastes;
    }

    public ArrayList<String> getKarlaMarksaTastes() {
        return karlaMarksaTastes;
    }

    public void setKarlaMarksaTastes(ArrayList<String> karlaMarksaTastes) {
        this.karlaMarksaTastes = karlaMarksaTastes;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFortress() {
        return fortress;
    }

    public void setFortress(String fortress) {
        this.fortress = fortress;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Tobacco{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", img='" + img + '\'' +
                ", taste='" + taste + '\'' +
                ", radonejskayaTastes=" + radonejskayaTastes +
                ", karlaMarkasaTastes=" + karlaMarksaTastes +
                ", isAvailable=" + isAvailable +
                ", description='" + description + '\'' +
                ", fortress='" + fortress + '\'' +
                '}';
    }
}
