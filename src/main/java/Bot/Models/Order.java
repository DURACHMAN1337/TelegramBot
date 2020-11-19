package Bot.Models;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
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
    String customerCartHTML;

    public Order() {
    }

    public void setChat_id(long chat_id) {
        this.chat_id = chat_id;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
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

    public String getCustomerCartHTML() {
        return customerCartHTML;
    }

    public void setCustomerCartHTML(String customerCartHTML) {
        this.customerCartHTML = customerCartHTML;
    }

    @Override
    public String toString() {
        String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(Calendar.getInstance().getTime());
        return "Заказ (номер чата " + chat_id + "):" +
                "\nДата заказа: " + timeStamp + "\n--------------------------------\n" +
                "\uD83D\uDC64 Покупатель:" +
                "\nИмя: " + customerName +
                "\nФамилия: " + customerSurname +
                "\nНомер телефона: " + customerPhone + "\n--------------------------------\n" +
                "\uD83D\uDCE6 Получение:" +
                "\nСпособ получения: " + deliveryMethod +
                "\nАдрес получения: " + address + "\n--------------------------------\n" +
                "\uD83D\uDED2 Содержимое корзины:\n" + customerCart;
    }

    public MimeMultipart toStringHTML() {
        MimeMultipart multipart = new MimeMultipart();
        MimeBodyPart bodyPart = new MimeBodyPart();
        try {
            bodyPart.addHeader("Content-Type", "text/html; charset=UTF-8");
            String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(Calendar.getInstance().getTime());
            bodyPart.setDataHandler(
                    new DataHandler(
    "<html>" +
            "<body>" +
            "<div style=\"display: flex; gap: 10px; justify-content: space-evenly; align-items: center; justify-content: center\">" +
                "<div style=\"padding-right: 15px; border-right: 3px solid #535353 \">" +
                    "<h3 style=\"margin-top: 0px;\">\uD83D\uDCCB Заказ</h3>" +
                    "<b>Номер чата:</b> " + chat_id + "</br>" +
                    "<b>Дата заказа:</b> " + timeStamp +
                    "<hr size= 2 px; color=\"#535353\"/>" +
                    "<h3>\uD83D\uDC64 Покупатель</h3>" +
                    "<b>Имя:</b> " + customerName + "</br>" +
                    "<b>Фамилия:</b> " + customerSurname + "</br>" +
                    "<b>Телефон:</b> " + customerPhone +
                     "<hr size= 2 px; color=\"#535353\"/>" +
                    "<h3>\uD83D\uDCE6 Получение</h3>" +
                    "<b>Способ получения:</b> " + deliveryMethod + "</br>" +
                    "<b>Адрес получения:</b> " + address +
                "</div>" +
                "<div>" +
                    "<h3>\uD83D\uDED2 Содержимое корзины</h3>" +
                    customerCartHTML +
                "</div>" +
            "</div>" +
            "</body>" +
            "</html>",
    "text/html; charset=\"utf-8\""
                    )
            );
            multipart.addBodyPart(bodyPart);
            return multipart;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
