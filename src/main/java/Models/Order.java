package Models;

import Service.CartService;

public class Order {
    private long chat_id;
    private String customerName;
    private String customerSurname;
    private String customerPhone;
    private String address;
    private Cart customerCart;

    public Order() {
        CartService CART_SERVICE = new CartService();
        customerCart = CART_SERVICE.getUserCart(chat_id);
        customerPhone = "";
    }

    public Order(int chat_id, String customerName, String customerSurname, String address, Cart customerCart) {
        this.chat_id = chat_id;
        this.customerName = customerName;
        this.customerSurname = customerSurname;
        this.address = address;
        this.customerCart = customerCart;
    }

    public void setChat_id(long chat_id) {
        this.chat_id = chat_id;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        if (this.customerPhone.length() < 12)
            this.customerPhone = customerPhone;
    }

    public long getChat_id() {
        return chat_id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSurname() {
        return customerSurname;
    }

    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Cart getCustomerCart() {
        return customerCart;
    }

    public void setCustomerCart(Cart customerCart) {
        this.customerCart = customerCart;
    }
}
