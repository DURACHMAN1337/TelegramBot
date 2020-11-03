package Service;

import Models.Cart;
import Models.Products.Hookah;
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
        }
        else {
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
        ArrayList<Product> cart = carts.get(chat_id).getCart();
        cart.removeIf(p -> p.getName().equals(product.getName()));
    }
}
