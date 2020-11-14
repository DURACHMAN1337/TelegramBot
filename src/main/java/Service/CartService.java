package Service;

import Models.Cart;
import Models.Products.Product;
import Models.Products.Tobacco;

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
        for (Product p : cart) {
            if (p.getName().equals(product.getName())) {
                if (p.getPrice() == product.getPrice()) {
                    if (product instanceof Tobacco) {
                        if (p.getTaste().equals(product.getTaste())) {
                            cart.remove(p);
                            break;
                        }
                    }
                    else {
                        cart.remove(p);
                        break;
                    }
                }
            }
        }
    }

    public void addToCart(Product product, long chat_id) {
        ArrayList<Product> cart = getUserCart(chat_id).getCart();
        if (cartIsContain(product, chat_id)) {
            setProductCount(product, chat_id);
        }
        else
            cart.add(product);
    }

    public boolean cartIsContain(Product product, long chat_id) {
        ArrayList<Product> cart = carts.get(chat_id).getCart();
        for (Product p : cart) {
            if (p.getName().equals(product.getName())) {
                if (p.getPrice() == product.getPrice()) {
                    if (product instanceof Tobacco) {
                        if (p.getTaste().equals(product.getTaste()))
                            return true;
                    }
                    else
                        return true;
                }
            }
        }
        return false;
    }

    public void setProductCount(Product product, long chat_id) {
        ArrayList<Product> cart = carts.get(chat_id).getCart();
        for (Product p : cart) {
            if (p.getName().equals(product.getName())) {
                if (p.getPrice() == product.getPrice()) {
                    if (product instanceof Tobacco) {
                        if (p.getTaste().equals(product.getTaste())) {
                            p.setCount(p.getCount() + product.getCount());
                        }
                    }
                    else
                        p.setCount(p.getCount() + product.getCount());
                }
            }
        }
    }
}
