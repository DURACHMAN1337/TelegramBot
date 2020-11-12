package Service;

import Models.Cart;
import Models.Products.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class CartService {

    private final HashMap<Long, Cart> carts;

    public CartService() {
        carts = new HashMap<>();
    }

    public Cart getUserCart(long chat_id) {
        if (carts.containsKey(chat_id)) {
            return carts.get(chat_id);
        } else {
            Cart cart = new Cart(chat_id);
            carts.put(chat_id, cart);
            return cart;
        }
    }

    public void clearUserCart(long chat_id) {
        carts.get(chat_id).getCart().clear();
    }

    public long cartsSize() {
        return carts.size();
    }

    public void deleteFromCart(Product product, long chat_id) {
        ArrayList<Product> cart = getUserCart(chat_id).getCart();
        cart.removeIf(p -> p.getName().equals(product.getName()));
    }

    public void addToCart(Product product, long chat_id) {
        ArrayList<Product> cart = getUserCart(chat_id).getCart();
        boolean isContains = false;
        for (Product p : cart) {
            if (cartIsContains(product, chat_id)) {
                int count = p.getCount() + product.getCount();
                p.setCount(count);
                isContains = true;
            }
        }
        if (!isContains)
            cart.add(product);
    }

    public boolean cartIsContains(Product product, long chat_id) {
        ArrayList<Product> cart = carts.get(chat_id).getCart();
        for (Product p : cart) {
            if (p.getName().equals(product.getName())) {
                if (p.getPrice() == product.getPrice()) {
                    return true;
                }
            }
        }
        return false;
    }
}
