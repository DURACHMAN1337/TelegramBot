package Models.Products;

import java.util.ArrayList;

public class Tobacco extends Product {
   private String brand;
   private String name;
   private long price;
   private String img;
   private String taste;
   private ArrayList<String> radonejskayaTastes;
   private ArrayList<String> karlaMarkasaTastes;
   private Boolean isAvailable;
   private String description;
   private String fortress;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public ArrayList<String> getKarlaMarkasaTastes() {
        return karlaMarkasaTastes;
    }

    public void setKarlaMarkasaTastes(ArrayList<String> karlaMarkasaTastes) {
        this.karlaMarkasaTastes = karlaMarkasaTastes;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public Boolean getAvailable() {
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

    @Override
    public String toString() {
        return "Tobacco{" +
                "brand='" + brand + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", img='" + img + '\'' +
                ", taste='" + taste + '\'' +
                ", radonejskayaTastes=" + radonejskayaTastes +
                ", karlaMarkasaTastes=" + karlaMarkasaTastes +
                ", isAvailable=" + isAvailable +
                ", description='" + description + '\'' +
                ", strength='" + fortress + '\'' +
                '}' + "\n";
    }
}
