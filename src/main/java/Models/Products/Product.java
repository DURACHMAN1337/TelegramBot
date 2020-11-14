package Models.Products;

public class Product implements Cloneable {
    private String name;
    private long price;
    private String img;
    private String taste;
    private String brand;
    private long id;
    private int count = 1;

    public Product() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
