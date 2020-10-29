package Models.Products;

public class Accessory extends Product {
    private String brand;
    private String name;
    private long price;
    private String img;
    private String type;
    private Boolean isAvailable;
    private String description;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @Override
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Accessory{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", img='" + img + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                ", id='" + id + '\'' +

                '}' + "\n";
    }
}
