package Models.Products;

public class Product implements Cloneable {
    private String name;
    private long price;
    private String img;
    private String taste;
    private String brand;

    public Product() {
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public String getImg() {
        return img;
    }

    public String getTaste() {
        return taste;
    }

    public String getBrand() {
        return brand;
    }
}
