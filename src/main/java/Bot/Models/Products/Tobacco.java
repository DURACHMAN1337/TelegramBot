package Bot.Models.Products;

import java.util.ArrayList;

public class Tobacco extends Product implements Cloneable {
    private long id;
    private String name;
    private long price;
    private String img;
    private boolean isAvailable;
    private String taste;
    private ArrayList<String> radonejskayaTastes;
    private ArrayList<String> karlaMarksaTastes;
    private String description;
    private String fortress;
    private int count = 1;

    public Tobacco() {
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
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

    public boolean isAvailable() {
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

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Tobacco{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", img='" + img + '\'' +
                ", isAvailable=" + isAvailable +
                ", taste='" + taste + '\'' +
                ", radonejskayaTastes=" + radonejskayaTastes +
                ", karlaMarksaTastes=" + karlaMarksaTastes +
                ", description='" + description + '\'' +
                ", fortress='" + fortress + '\'' +
                '}';
    }
}
