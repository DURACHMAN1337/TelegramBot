package Service;

import Models.Cart;
import Models.Order;
import Models.Products.Product;
import Models.Products.Tobacco;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderService {

    private final HashMap<Long, Order> orders;

    public OrderService() {
        orders = new HashMap<>();
    }

    public Order getOrder(long chat_id) {
        if (orders.containsKey(chat_id)) {
            return orders.get(chat_id);
        } else {
            Order order = new Order();
            order.setChat_id(chat_id);
            orders.put(chat_id, order);
            return order;
        }
    }

    public long cartsSize() {
        return orders.size();
    }
}
