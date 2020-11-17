package Bot.Models;

import Bot.Models.Products.*;

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
        sb.append("*🛒 КОРЗИНА*\n\n");
        for (Product product : cart) {
            if (product instanceof Tobacco) {
                sumPrice += product.getPrice() * product.getCount();
                sb.append(cart.indexOf(product) + 1)
                        .append(".  *").append(product.getName()).append("*\n     (`").append(product.getTaste()).append("`)\n")
                        .append("     ").append(product.getCount()).append(" _шт._ `X` ").append(product.getPrice())
                        .append(" _руб._\n\n");
            }
            else if (product instanceof Hookah) {
                sumPrice += product.getPrice() * product.getCount();
                sb.append(cart.indexOf(product) + 1)
                        .append(".  *").append(product.getBrand()).append(" ").append(product.getName()).append("*\n")
                        .append("     ").append(product.getCount()).append(" _шт._ `X` ").append(product.getPrice())
                        .append(" _руб._\n\n");
            }
            else if (product instanceof Accessory) {
                sumPrice += product.getPrice() * product.getCount();
                sb.append(cart.indexOf(product) + 1)
                        .append(".  *").append(((Accessory) product).getType()).append("* ").append(product.getName())
                        .append("\n").append("     ").append(product.getCount()).append(" _шт._ `X` ").append(product.getPrice())
                        .append(" _руб._\n\n");
            }
            else if (product instanceof Charcoal) {
                sumPrice += product.getPrice() * product.getCount();
                sb.append(cart.indexOf(product) + 1)
                        .append(".  *").append(product.getName()).append("* ").append("\n")
                        .append("     ").append(product.getCount()).append(" _шт._ `X` ").append(product.getPrice())
                        .append(" _руб._\n\n");
            }
            else if (product instanceof Vaporizer) {
                sumPrice += product.getPrice() * product.getCount();
                sb.append(cart.indexOf(product) + 1)
                        .append(".  *").append(product.getName()).append("* ").append("\n")
                        .append("     ").append(product.getCount()).append(" _шт._ `X` ").append(product.getPrice())
                        .append(" _руб._\n\n");
            }
        }
        sb.append("*ИТОГО:*  ").append(sumPrice).append(" _руб._").append("\n");
        if (sumPrice == 0)
            return "*🛒 КОРЗИНА*\n\nВаша корзина пуста!\nВоспользуйтесь каталогом, чтобы найти всё что нужно\n";
        else
            return new String(sb);
    }

    public String toStringEdit(int position) {
        String pos = String.valueOf(position);
        return toString().replace(pos + ".","\uD83D\uDCCD");
    }

    public String toStringOrder() {
        StringBuilder sb = new StringBuilder();
        long sumPrice = 0;
        for (Product product : cart) {
            if (product instanceof Tobacco) {
                sumPrice += product.getPrice() * product.getCount();
                sb.append("Табак ").append(product.getName()).append("\n(").append(product.getTaste()).append(")\n")
                        .append(product.getCount()).append(" шт. X ").append(product.getPrice())
                        .append(" руб.\n\n");
            } else if (product instanceof Hookah) {
                sumPrice += product.getPrice() * product.getCount();
                sb.append("Кальян ").append(product.getBrand()).append(" ").append(product.getName()).append("\n")
                        .append(product.getCount()).append(" шт. X ").append(product.getPrice())
                        .append(" руб.\n\n");
            } else if (product instanceof Accessory) {
                sumPrice += product.getPrice() * product.getCount();
                sb.append("Аксессуар ").append(product.getName())
                        .append("\n").append(product.getCount()).append(" шт. X ").append(product.getPrice())
                        .append(" руб.\n\n");
            } else if (product instanceof Charcoal) {
                sumPrice += product.getPrice() * product.getCount();
                sb.append("Уголь ").append(product.getName()).append("\n")
                        .append(product.getCount()).append(" шт. X ").append(product.getPrice())
                        .append(" руб.\n\n");
            }
            else if (product instanceof Vaporizer) {
                sumPrice += product.getPrice() * product.getCount();
                sb.append("Эл. Испаритель ").append(product.getName()).append("\n")
                        .append(product.getCount()).append(" шт. X ").append(product.getPrice())
                        .append(" руб.\n\n");
            }
        }
        sb.append("ИТОГО:  ").append(sumPrice).append(" руб.").append("\n");
        return new String(sb);
    }

    public String toStringOrderHTML() {
        StringBuilder sb = new StringBuilder();
        long sumPrice = 0;
        sb.append("<ul>");
        for (Product product : cart) {
            if (product instanceof Tobacco) {
                sumPrice += product.getPrice() * product.getCount();
                sb.append("<li>Табак <a href=\"").append(product.getImg()).append("\"><b>").append(product.getName())
                        .append("</b></a><br>(").append(product.getTaste()).append(")<br>")
                        .append(product.getCount()).append(" шт. X ").append(product.getPrice())
                        .append(" руб.</li>");
            } else if (product instanceof Hookah) {
                sumPrice += product.getPrice() * product.getCount();
                sb.append("<li>Кальян <a href=\"").append(product.getImg()).append("\"><b>").append(product.getBrand())
                        .append(" ").append(product.getName()).append("</b></a><br>")
                        .append(product.getCount()).append(" шт. X ").append(product.getPrice())
                        .append(" руб.</li>");
            } else if (product instanceof Accessory) {
                sumPrice += product.getPrice() * product.getCount();
                sb.append("<li>Аксессуар <a href=\"").append(product.getImg()).append("\"><b>").append(product.getName())
                        .append("</b></a><br>").append(product.getCount()).append(" шт. X ").append(product.getPrice())
                        .append(" руб.</li>\n\n");
            } else if (product instanceof Charcoal) {
                sumPrice += product.getPrice() * product.getCount();
                sb.append("<li>Уголь <a href=\"").append(product.getImg()).append("\"><b>").append(product.getName())
                        .append("</b></a><br>")
                        .append(product.getCount()).append(" шт. X ").append(product.getPrice())
                        .append(" руб.</li>");
            } else if (product instanceof Vaporizer) {
                sumPrice += product.getPrice() * product.getCount();
                sb.append("<li>Эл. Испаритель <a href=\"").append(product.getImg()).append("\"><b>").append(product.getName())
                        .append("</b></a><br>")
                        .append(product.getCount()).append(" шт. X ").append(product.getPrice())
                        .append(" руб.</li>");
            }
        }
        sb.append("<br><b>ИТОГО:  ").append(sumPrice).append(" руб.").append("</b></ul>");
        return new String(sb);
    }
}
