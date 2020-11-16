package Models;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Order {
    private long chat_id;
    private String customerName;
    private String customerSurname;
    private String customerPhone = "";
    private String deliveryMethod;
    private String address;
    String customerCart;

    public Order() {
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

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getCustomerCart() {
        return customerCart;
    }

    public void setCustomerCart(String customerCart) {
        this.customerCart = customerCart;
    }

    @Override
    public String toString() {
        String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(Calendar.getInstance().getTime());
        return "*Заказ* (номер чата " + chat_id + "):" +
                "\nДата заказа: " + timeStamp + "\n-\n" +
                "*Покупатель*" +
                "\nИмя: " + customerName +
                "\nФамилия: " + customerSurname +
                "\nНомер телефона: " + customerPhone + "\n-\n" +
                "*Получение*" +
                "\nСпособ получения: " + deliveryMethod +
                "\nАдрес получения: " + address + "\n-\n" +
                "*Содержимое корзины*\n" + customerCart;
    }
}
