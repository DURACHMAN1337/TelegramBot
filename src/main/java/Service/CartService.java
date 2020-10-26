package Service;

import Models.Cart;
import java.util.HashMap;

public class CartService {

    private HashMap<Long, Cart> carts;

    public CartService() {
        carts = new HashMap<>();
    }

/*    public getUserCart(long chat_id) {
        for ()
    }*/

    public HashMap<Long, Cart> getCarts() {
        return carts;
    }
}
