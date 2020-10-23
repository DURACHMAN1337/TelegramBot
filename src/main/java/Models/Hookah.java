package Models;

public class Hookah {

    private String brand;
    private String name;
    private long price;
    private String img;

    public Hookah() {
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    @Override
    public String toString() {
        return "Hookah{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", img='" + img + '\'' +
                '}';
    }
}
