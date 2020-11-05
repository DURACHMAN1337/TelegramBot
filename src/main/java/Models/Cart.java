package Models;

import Models.Products.Accessory;
import Models.Products.Hookah;
import Models.Products.Product;
import Models.Products.Tobacco;

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
        sb.append("*КОРЗИНА*\n\n");
        for (Product product : cart) {
            if (product instanceof Tobacco) {
                sumPrice += product.getPrice();
                sb.append(cart.indexOf(product) + 1)
                        .append(".  *").append(product.getName()).append("*\n").append("Вкус: ").append(product.getTaste())
                        .append("\n").append(product.getPrice()).append(" _руб._\n\n---\n\n");
            }
            else if (product instanceof Hookah) {
                sumPrice += product.getPrice();
                sb.append(cart.indexOf(product) + 1)
                        .append(".  *").append(product.getBrand()).append("* ").append(product.getName()).append("\n")
                        .append(product.getPrice()).append(" _руб._\n\n---\n\n");
            }
            else if (product instanceof Accessory){
                sumPrice += product.getPrice();
                sb.append(cart.indexOf(product) +1)
                        .append(".  *").append(((Accessory) product).getType()).append("* ").append(product.getName()).append("\n")
                        .append(product.getPrice()).append(" _руб._\n\n---\n\n");
            }

        }
        sb.append("*ИТОГО:*  ").append(sumPrice).append(" _руб._");
        if (sumPrice == 0)
            return "*КОРЗИНА*\n\nВаша корзина пуста!\n\n---\n\n";
        else
            return new String(sb);
    }
}
