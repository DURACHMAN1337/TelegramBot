package Service;

import Models.Cart;
import java.util.HashMap;

public class CartService {

    private HashMap<Long, Cart> carts;

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

    public HashMap<Long, Cart> getCarts() {
        return carts;
    }
}
