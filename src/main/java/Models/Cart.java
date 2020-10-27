package Models;

import Models.Products.Product;

import java.util.ArrayList;

public class Cart {

    private long chat_id;
    private ArrayList<Product> cart;

    public Cart(long chat_id) {
        cart = new ArrayList<>();
        this.chat_id = chat_id;
    }

    public Cart() {
    }

    public long getChat_id() {
        return chat_id;
    }

    public void setChat_id(long chat_id) {
        this.chat_id = chat_id;
    }

    public ArrayList<Product> getCart() {
        return cart;
    }

    public void setCart(ArrayList<Product> cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        long sumPrice = 0;
        sb.append("———КОРЗИНА———\n\n");
        for (Product product : cart) {
            sumPrice+=product.getPrice();
            sb.append(cart.indexOf(product)+1)
                    .append(".   ").append(product.getName()).append(" | ")
                    .append(product.getPrice()).append(" руб.\n\n———————————————\n\n");
        }
        sb.append("ИТОГО: ").append(sumPrice).append(" руб.");

        if (sumPrice == 0)
            return "—————КОРЗИНА—————\n\nВаша корзина пуста!\n\n———————————————\n\nДавайте скорее добавим туда что-нибудь...";
        else
            return new String(sb);
    }
}
