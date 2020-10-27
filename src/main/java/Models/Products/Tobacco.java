package Models.Products;

import java.util.ArrayList;

public class Tobacco extends Product {
   private String brand;
   private String name;
   private long price;
   private String img;
   private ArrayList<String> radonejskayaTastes;
   private ArrayList<String> karlaMarkasaTastes;
   private Boolean isAvailable;


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

    @Override
    public String toString() {
        return "Tobacco{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", img='" + img + '\'' +
                '}' + "\n";
    }
}
