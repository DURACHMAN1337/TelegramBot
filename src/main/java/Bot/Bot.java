package Bot;

import Bot.Keyboards.InlineKeyboardMarkupBuilder;
import Bot.Keyboards.ReplyKeyboardMarkupBuilder;
import Models.Products.*;
import Service.*;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.Date;

public class Bot extends TelegramLongPollingBot {

    private static final HookahService HOOKAH_SERVICE = new HookahService();
    private static final TobaccoService TOBACCO_SERVICE = new TobaccoService();
    private static final CartService CART_SERVICE = new CartService();
    private static final AccessoriesService ACCESSORIES_SERVICE = new AccessoriesService();
    private static final CharcoalService CHARCOAL_SERVICE = new CharcoalService();
    private static final VaporizerService VAPORIZER_SERVICE = new VaporizerService();
    private static final ArrayList<String> allCharcoal = CHARCOAL_SERVICE.getAllNamesList();
    private static final ArrayList<String> availableCharcoal = CHARCOAL_SERVICE.getAvailableCharcoalNamesList();
    private static final ArrayList<String> allAccessories = ACCESSORIES_SERVICE.getAllNamesList();
    private static final ArrayList<String> availableAccessories = ACCESSORIES_SERVICE.getAvailableAccessoriesNamesList();
    private static final ArrayList<String> allAccessoryTypes = ACCESSORIES_SERVICE.getAllTypes();
    private static final ArrayList<String> allHookahBrands = HOOKAH_SERVICE.getAllBrandsList();
    private static final ArrayList<String> availableBrands = HOOKAH_SERVICE.getAvailableBrandsList();
    private static final ArrayList<Tobacco> allTobaccos = TOBACCO_SERVICE.getAllTobacco();
    private static final ArrayList<Tobacco> allAvailableTobaccos = TOBACCO_SERVICE.getAllAvailableTobacco();
    private static final ArrayList<String> allTobaccoFortresses = TOBACCO_SERVICE.getAllFortresses();
    private static final ArrayList<String> allAvailableTobaccoFortresses = TOBACCO_SERVICE.getAllAvailableFortresses();

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }


    public void onUpdateReceived(Update update) {
        try {
            updateHandle(update);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public EditMessageText messageHandle(String text, long chat_id, long mes_id) {
        text = text.substring(1);
        EditMessageText editMessageText;
        switch (text) {
            case "Наши Контакты":
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id).rebuild(mes_id);
                editMessageText.setText("г. Самара, ул. Радонежская, 1\n" +
                        "Часы работы: ПН – ВС, с 12.00 до 24.00\n" +
                        "\n" +
                        "Телефон: 8 (927) 002-75-57" +
                        "\n" +
                        "\nг. Самара, пр. Карла Маркса, 196 (ЖК Центральный)\n" +
                        "Часы работы: ПН – ВС, с 12.00 до 24.00\n" +
                        "\n" +
                        "Телефон: 8 (927) 760-11-17");
                return editMessageText;
            case "Каталог":
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Табаки", "mТабаки")
                        .button("Кальяны", "mКальяны")
                        .endRow()
                        .row()
                        .button("Акссесуары", "mАкссесуары")
                        .button("Уголь", "mУголь")
                        .endRow()
                        .row()
                        .button("Электронные испарители", "mИспарители")
                        .endRow()
                        .row()
                        .button("📌 Помощь", "mПомощьКаталог")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Каталог GRIZZLY SHOP*\nВыберите категорию интересующего товара:");
                return editMessageText;
            case "ПомощьКаталог":
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Табаки", "mТабаки")
                        .button("Кальяны", "mКальяны")
                        .endRow()
                        .row()
                        .button("Акссесуары", "mАкссесуары")
                        .button("Уголь", "mУголь")
                        .endRow()
                        .row()
                        .button("Электронные испарители", "mИспарители")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Каталог GRIZZLY SHOP*\n\nДля совершения заказа необходимо:\n*-* Выбрать интересующий товар из каталога" +
                        "\n*-* Добавить его в корзину\n*-* Оформить заказ, сформировав необходимые данные по доставке");
                return editMessageText;
            case "Наличие":
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Табаки", "tA/Табаки")
                        .button("Кальяны", "hA/Кальяны")
                        .endRow()
                        .row()
                        .button("Акссесуары", "mАкссесуары")
                        .button("Уголь", "mУголь")
                        .endRow()
                        .row()
                        .button("Электронные испарители", "mИспарители")
                        .endRow()
                        .row()
                        .button("📌 Помощь", "mПомощьНаличие")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Наличие GRIZZLY SHOP*\nВыберите категорию интересующего товара:");
                return editMessageText;
            case "ПомощьНаличие":
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Табаки", "tA/Табаки")
                        .button("Кальяны", "mКальяны")
                        .endRow()
                        .row()
                        .button("Акссесуары", "mАкссесуары")
                        .button("Уголь", "mУголь")
                        .endRow()
                        .row()
                        .button("Электронные испарители", "mИспарители")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Наличие GRIZZLY SHOP*\n\nДля совершения заказа необходимо:\n*-* Выбрать интересующий товар из каталога" +
                        "\n*-* Добавить его в корзину\n*-* Оформить заказ, сформировав необходимые данные по доставке");
                return editMessageText;
            case "Кальяны":
                return hookahHandle(text, chat_id, mes_id);
            case "Табаки":
                return tobaccoHandle(text, chat_id, mes_id);
            case "🛒 Корзина":
                return cartHandle(text, chat_id, mes_id);
            case "Акссесуары":
                return accessoryHandle(text, chat_id, mes_id);
            case "Уголь":
                return charcoalHandle(text, chat_id, mes_id);
        }
        return InlineKeyboardMarkupBuilder.create(chat_id)
                .rebuild(mes_id).setText("*Данный функционал находится в разработке*");
    }

    public SendMessage messageStarter(String text, SendMessage sendMessage) {
        long chat_id = Long.parseLong(sendMessage.getChatId());
        switch (text) {
            case "/start":
                sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                        .setText(" \uD83D\uDD1E *Данный магазин предназначен для лиц " +
                                "старше 18 лет.*\n\nНажимая данную кнопку, " +
                                "Вы подтверждаете что Вам исполнилось 18 лет")
                        .row()
                        .button("Продолжить")
                        .endRow()
                        .build();
                break;
            case "Назад":
            case "Продолжить":
            case "Главное меню":
                sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                        .setText("*Добро пожаловать в GRIZZLY SHOP!*" +
                                "\nУ нас вы найдёте большой ассортимент кальянной " +
                                "продукции, включая всевозможные аксесуары и огромный выбор табака!" +
//                                "\n[Наш сайт](https://hookahinrussia.ru/)" +
                                "\n\nИспользуйте кнопки на клавиатуре снизу для навигации")
                        .row()
                        .button("Каталог")
                        .button("Наличие")
                        .endRow()
                        .row()
                        .button("🛒 Корзина")
                        .button("Наши Контакты")
                        .endRow()
                        .build();
                break;
            case "Каталог":
                sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .setText("*Каталог GRIZZLY SHOP*\nВыберите категорию интересующего товара:")
                        .row()
                        .button("Табаки", "mТабаки")
                        .button("Кальяны", "mКальяны")
                        .endRow()
                        .row()
                        .button("Акссесуары", "mАкссесуары")
                        .button("Уголь", "mУголь")
                        .endRow()
                        .row()
                        .button("Электронные испарители", "mЭлектронные испарители")
                        .endRow()
                        .row()
                        .button("📌 Помощь", "mПомощьКаталог")
                        .endRow()
                        .build();
                break;
            case "Наличие":
                sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .setText("*Наличие GRIZZLY SHOP*\nВыберите категорию интересующего товара:")
                        .row()
                        .button("Табаки", "tA/Табаки")
                        .button("Кальяны", "hA/Кальяны")
                        .endRow()
                        .row()
                        .button("Акссесуары", "aA/Аксесуары")
                        .button("Уголь", "uA/Уголь")
                        .endRow()
                        .row()
                        .button("Электронные испарители", "eA/Испарители")
                        .endRow()
                        .row()
                        .button("📌 Помощь", "mПомощьНаличие")
                        .endRow()
                        .build();
                break;
            case "🛒 Корзина":
                if (CART_SERVICE.getUserCart(chat_id).getCart().size() == 0) {
                    sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                            .setText(CART_SERVICE.getUserCart(chat_id).toString())
                            .build();
                } else {
                    sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                            .setText(CART_SERVICE.getUserCart(chat_id).toString())
                            .row()
                            .button("\uD83D\uDCE6 Оформить заказ", "cmakeOrder")
                            .endRow()
                            .row()
                            .button("Изменить корзину", "c&edit")
                            .endRow()
                            .build();
                }
                break;
            case "Наши Контакты":
                sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .setText("г. Самара, ул. Радонежская, 1\n" +
                                "Часы работы: ПН – ВС, с 12.00 до 24.00\n" +
                                "\n" +
                                "Телефон: 8 (927) 002-75-57" +
                                "\n" +
                                "\nг. Самара, пр. Карла Маркса, 196 (ЖК Центральный)\n" +
                                "Часы работы: ПН – ВС, с 12.00 до 24.00\n" +
                                "\n" +
                                "Телефон: 8 (927) 760-11-17")
                        .build();
                break;
        }
        return sendMessage;
    }

    public EditMessageText availableHookahHandle(String text, long chat_id, long mes_id) {
        text = text.substring(3);
        EditMessageText editMessage;
        if (availableBrands.contains(text)) {
            ArrayList<Hookah> hookahs = HOOKAH_SERVICE.getAvailableHookahsByBrand(text);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .hookahButtons(hookahs, "hA/")
                    .row()
                    .button("🔙 Назад", "hA/Кальяны")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("Товары бренда " + text + ": ");
        } else if (text.contains("&")) {
            Hookah cartHookah;
            long id = Long.parseLong(text.split("&")[1].split("\\?")[0]);
            int count;
            cartHookah = HOOKAH_SERVICE.getHookahById(id);
            if (text.contains("up")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("hA/&" + id + "?" + ++count)
                        .endRow()
                        .row()
                        .button("🔙 Назад", "hA/id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + cartHookah.getBrand() + "* | " + cartHookah.getName() +
                        "\n💵 " + count + " _шт._ `X` " + (cartHookah.getPrice()) + " _руб._" +
                        " = " + (cartHookah.getPrice() * count) + " _руб._" +
                        "\n`(цена указана за полный комлект)`" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("down")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("hA/&" + id + "?" + --count)
                        .endRow()
                        .row()
                        .button("🔙 Назад", "hA/id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + cartHookah.getBrand() + "* | " + cartHookah.getName() +
                        "\n💵 " + count + " _шт._ `X` " + (cartHookah.getPrice()) + " _руб._" +
                        " = " + (cartHookah.getPrice() * count) + " _руб._" +
                        "\n`(цена указана за полный комлект)`" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("crt")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                cartHookah.setCount(count);
                CART_SERVICE.addToCart(cartHookah, chat_id);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Убрать из корзины", "hA/" + id + "del")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "hA/Кальяны")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + cartHookah.getBrand() + "* | " + cartHookah.getName() +
                        "\n💵 *" + cartHookah.getPrice() + " руб. *\n`(цена указана за полный комлект)`" +
                        "\n\n" + cartHookah.getDescription().trim() +
                        "\n[_](" + cartHookah.getImg() + ")");
            } else {
                count = 1;
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("hA/&" + id + "?" + "1")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "hA/id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + cartHookah.getBrand() + "* | " + cartHookah.getName() +
                        "\n💵 " + count + " _шт._ `X` " + (cartHookah.getPrice()) + " _руб._" +
                        " = " + (cartHookah.getPrice() * count) + " _руб._" +
                        "\n`(цена указана за полный комлект)`" +
                        "\n\nВыберите необходимое количество:");
            }
        } else if (text.contains("id")) {
            Hookah idHookah = HOOKAH_SERVICE.getHookahById(Long.parseLong(text.replace("id", "")));
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 В корзину", "hA/&" + idHookah.getId() + "?")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "hA/Кальяны")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("🏷 *" + idHookah.getBrand() + "* | " + idHookah.getName() +
                    "\n💵 *" + idHookah.getPrice() + " руб. *\n`(цена указана за полный комлект)`" +
                    "\n\n" + idHookah.getDescription().trim() +
                    "\n[_](" + idHookah.getImg() + ")");
        } else if (text.contains("del")) {
            Hookah delHookah = HOOKAH_SERVICE.getHookahById(Long.parseLong(text.replace("del", "")));
            CART_SERVICE.deleteFromCart(delHookah, chat_id);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 В корзину", "hA/&" + delHookah.getId() + "?")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "hA/Кальяны")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("🏷 *" + delHookah.getName() + "*" +
                    "\n💵 *" + delHookah.getPrice() + " руб.*" +
                    "\n\n" + delHookah.getDescription().trim() +
                    "\n[_](" + delHookah.getImg() + ")");
        } else {
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(availableBrands, "hA/")
                    .row()
                    .button("🔙 Назад", "mНаличие")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Наличие / Кальяны*\nВыберите бренд кальяна:");
        }
        return editMessage.setParseMode("Markdown");
    }

    public EditMessageText hookahHandle(String text, long chat_id, long mes_id) {
        text = text.substring(1);
        EditMessageText editMessage;
        Hookah currHookah;
        if (allHookahBrands.contains(text)) {
            ArrayList<Hookah> hookahs = HOOKAH_SERVICE.getHookahsByBrand(text);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .hookahButtons(hookahs, "h")
                    .row()
                    .button("🔙 Назад", "hКальяны")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("Товары бренда " + text + ": ");
        } else if (text.contains("&")) {
            Hookah cartHookah;
            long id = Long.parseLong(text.split("&")[1].split("\\?")[0]);
            int count;
            cartHookah = HOOKAH_SERVICE.getHookahById(id);
            if (text.contains("up")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("h&" + id + "?" + ++count)
                        .endRow()
                        .row()
                        .button("🔙 Назад", "hid" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + cartHookah.getBrand() + "* | " + cartHookah.getName() +
                        "\n💵 " + count + " _шт._ `X` " + (cartHookah.getPrice()) + " _руб._" +
                        " = " + (cartHookah.getPrice() * count) + " _руб._" +
                        "\n`(цена указана за полный комлект)`" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("down")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("h&" + id + "?" + --count)
                        .endRow()
                        .row()
                        .button("🔙 Назад", "hid" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + cartHookah.getBrand() + "* | " + cartHookah.getName() +
                        "\n💵 " + count + " _шт._ `X` " + (cartHookah.getPrice()) + " _руб._" +
                        " = " + (cartHookah.getPrice() * count) + " _руб._" +
                        "\n`(цена указана за полный комлект)`" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("crt")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                cartHookah.setCount(count);
                CART_SERVICE.addToCart(cartHookah, chat_id);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Убрать из корзины", "h" + id + "del")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "hКальяны")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + cartHookah.getBrand() + "* | " + cartHookah.getName() +
                        "\n💵 *" + cartHookah.getPrice() + " руб. *\n`(цена указана за полный комлект)`" +
                        "\n\n" + cartHookah.getDescription().trim() +
                        "\n[_](" + cartHookah.getImg() + ")");
            } else {
                count = 1;
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("h&" + id + "?" + "1")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "hid" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + cartHookah.getBrand() + "* | " + cartHookah.getName() +
                        "\n💵 " + count + " _шт._ `X` " + (cartHookah.getPrice()) + " _руб._" +
                        " = " + (cartHookah.getPrice() * count) + " _руб._" +
                        "\n`(цена указана за полный комлект)`" +
                        "\n\nВыберите необходимое количество:");
            }
        } else if (text.contains("id")) {
            currHookah = HOOKAH_SERVICE.getHookahById(Long.parseLong(text.replace("id", "")));
            if (currHookah.isAvailable()) {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("🛒 В корзину", "h&" + currHookah.getId() + "?")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "hКальяны")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + currHookah.getBrand() + "* | " + currHookah.getName() +
                        "\n💵 *" + currHookah.getPrice() + " руб. *\n`(цена указана за полный комлект)`" +
                        "\n\n" + currHookah.getDescription().trim() +
                        "\n[_](" + currHookah.getImg() + ")");
            } else {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("🔙 Назад", "hКальяны")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + currHookah.getBrand() + "* | " + currHookah.getName() + " \n_(нет в наличии)_" +
                        "\n💵 *" + currHookah.getPrice() + " руб. *\n`(цена указана за полный комлект)`" +
                        "\n\n" + currHookah.getDescription().trim() +
                        "\n[_](" + currHookah.getImg() + ")");
            }
        } else if (text.contains("del")) {
            Hookah delHookah = HOOKAH_SERVICE.getHookahById(Long.parseLong(text.replace("del", "")));
            CART_SERVICE.deleteFromCart(delHookah, chat_id);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 В корзину", "h&" + delHookah.getId() + "?")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "hКальяны")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("🏷 *" + delHookah.getName() + "*" +
                    "\n💵 *" + delHookah.getPrice() + " руб.*" +
                    "\n\n" + delHookah.getDescription().trim() +
                    "\n[_](" + delHookah.getImg() + ")");
        } else {
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(allHookahBrands, "h")
                    .row()
                    .button("🔙 Назад", "mКаталог")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Каталог / Кальяны*\nВыберите бренд кальяна:");
        }
        return editMessage.setParseMode("Markdown");
    }

    public EditMessageText availableCharcoalHandle(String text, long chat_id, long mes_id) {
        text = text.substring(3);
        EditMessageText editMessageText;
        if (text.contains("&")) {
            Charcoal cartCharcoal;
            long id = Long.parseLong(text.split("&")[1].split("\\?")[0]);
            int count;
            cartCharcoal = CHARCOAL_SERVICE.getCharcoalById(id);
            if (text.contains("up")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("uA/&" + id + "?" + ++count)
                        .endRow()
                        .row()
                        .button("🔙 Назад", "uA/id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartCharcoal.getName() + "* | " +
                        "\n💵 " + count + " _шт._ `X` " + (cartCharcoal.getPrice()) + " _руб._" +
                        " = " + (cartCharcoal.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("down")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("uA/&" + id + "?" + --count)
                        .endRow()
                        .row()
                        .button("🔙 Назад", "uA/id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartCharcoal.getName() + "* | " +
                        "\n💵 " + count + " _шт._ `X` " + (cartCharcoal.getPrice()) + " _руб._" +
                        " = " + (cartCharcoal.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("crt")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                cartCharcoal.setCount(count);
                CART_SERVICE.addToCart(cartCharcoal, chat_id);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Убрать из корзины", "uA/" + id + "del")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "uA/Уголь")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartCharcoal.getName() + "* | " +
                        "\n💵 *" + cartCharcoal.getPrice() + " руб. *" +
                        "\n\n" + cartCharcoal.getDescription().trim() +
                        "\n[_](" + cartCharcoal.getImg() + ")");

            } else {
                count = 1;
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("uA/&" + id + "?" + "1")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "uA/id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartCharcoal.getName() + "* | " +
                        "\n💵 " + count + " _шт._ `X` " + (cartCharcoal.getPrice()) + " _руб._" +
                        " = " + (cartCharcoal.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            }

        } else if (text.contains("id")) {
            Charcoal idCharcoal = CHARCOAL_SERVICE.getCharcoalById(Long.parseLong(text.replace("id", "")));
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 В корзину", "uA/&" + idCharcoal.getId() + "?")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "uA/Уголь")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("🏷 *" + idCharcoal.getName() + "* | " +
                    "\n💵 *" + idCharcoal.getPrice() + " руб. *" +
                    "\n\n" + idCharcoal.getDescription().trim() +
                    "\n[_](" + idCharcoal.getImg() + ")");

        } else if (text.contains("del")) {
            Charcoal delCharcoal = CHARCOAL_SERVICE.getCharcoalById(Long.parseLong(text.replace("del", "")));
            CART_SERVICE.deleteFromCart(delCharcoal, chat_id);
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 В корзину", "uA/&" + delCharcoal.getId() + "?")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "uA/Уголь")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("🏷 *" + delCharcoal.getName() + "*" +
                    "\n💵 *" + delCharcoal.getPrice() + " руб.*" +
                    "\n\n" + delCharcoal.getDescription().trim() +
                    "\n[_](" + delCharcoal.getImg() + ")");
        } else {
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .charcoalButtons(CHARCOAL_SERVICE.getAvailableCharcoal())
                    .row()
                    .button("🔙 Назад", "mНаличие")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("*Каталог / Уголь*\nВыберите Уголь:");
        }

        return editMessageText.setParseMode("Markdown");

    }

    public EditMessageText charcoalHandle(String text, long chat_id, long mes_id) {
        text = text.substring(1);
        EditMessageText editMessageText;
        Charcoal currCharcoal;
        if (text.contains("&")) {
            Charcoal cartCharcoal;
            long id = Long.parseLong(text.split("&")[1].split("\\?")[0]);
            int count;
            cartCharcoal = CHARCOAL_SERVICE.getCharcoalById(id);
            if (text.contains("up")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("u&" + id + "?" + ++count)
                        .endRow()
                        .row()
                        .button("🔙 Назад", "uid" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartCharcoal.getName() + "* | " +
                        "\n💵 " + count + " _шт._ `X` " + (cartCharcoal.getPrice()) + " _руб._" +
                        " = " + (cartCharcoal.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");

            } else if (text.contains("down")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("u&" + id + "?" + --count)
                        .endRow()
                        .row()
                        .button("🔙 Назад", "uid" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartCharcoal.getName() + "* | " +
                        "\n💵 " + count + " _шт._ `X` " + (cartCharcoal.getPrice()) + " _руб._" +
                        " = " + (cartCharcoal.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("crt")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                cartCharcoal.setCount(count);
                CART_SERVICE.addToCart(cartCharcoal, chat_id);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Убрать из корзины", "u" + id + "del")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "uУголь")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartCharcoal.getName() + "* | " +
                        "\n💵 *" + cartCharcoal.getPrice() + " руб. *" +
                        "\n\n" + cartCharcoal.getDescription().trim() +
                        "\n[_](" + cartCharcoal.getImg() + ")");
            } else {
                count = 1;
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("u&" + id + "?" + "1")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "uid" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartCharcoal.getName() + "* | " +
                        "\n💵 " + count + " _шт._ `X` " + (cartCharcoal.getPrice()) + " _руб._" +
                        " = " + (cartCharcoal.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            }

        } else if (text.contains("id")) {
            currCharcoal = CHARCOAL_SERVICE.getCharcoalById(Long.parseLong(text.replace("id", "")));
            if (currCharcoal.isAvailable()) {
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("🛒 В корзину", "u&" + currCharcoal.getId() + "?")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "uУголь")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + currCharcoal.getName() + "* | " +
                        "\n💵 *" + currCharcoal.getPrice() + " руб.*" +
                        "\n\n" + currCharcoal.getDescription().trim() +
                        "\n[_](" + currCharcoal.getImg() + ")");
            } else {
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("🔙 Назад", "uУголь")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + currCharcoal.getName() + "* | " + " \n_(нет в наличии)_" +
                        "\n💵 *" + currCharcoal.getPrice() + " руб.*" +
                        "\n\n" + currCharcoal.getDescription().trim() +
                        "\n[_](" + currCharcoal.getImg() + ")");
            }
        } else if (text.contains("del")) {
            Charcoal delCharcoal = CHARCOAL_SERVICE.getCharcoalById(Long.parseLong(text.replace("del", "")));
            CART_SERVICE.deleteFromCart(delCharcoal, chat_id);
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 В корзину", "u&" + delCharcoal.getId() + "?")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "uУголь")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("🏷 *" + delCharcoal.getName() + "*" +
                    "\n💵 *" + delCharcoal.getPrice() + " руб.*" +
                    "\n\n" + delCharcoal.getDescription().trim() +
                    "\n[_](" + delCharcoal.getImg() + ")");
        } else {
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .charcoalButtons(CHARCOAL_SERVICE.getAllCharcoal())
                    .row()
                    .button("🔙 Назад", "mКаталог")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("*Каталог / Уголь*\nВыберите Уголь:");
        }
        return editMessageText.setParseMode("Markdown");
    }

    public EditMessageText availableAccessoryHandle(String text, long chat_id, long mes_id) {
        text = text.substring(3);
        EditMessageText editMessageText;
        if (allAccessoryTypes.contains(text)) {
            ArrayList<Accessory> accessories = ACCESSORIES_SERVICE.getAvailableAccessoriesByType(text);
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .accessoryButtons(accessories, "aA/")
                    .row()
                    .button("🔙 Назад", "aA/Акссесуары")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("Акссесуары категории: " + text);
        } else if (text.contains("&")) {
            Accessory cartAccessory;
            long id = Long.parseLong(text.split("&")[1].split("\\?")[0]);
            int count;
            cartAccessory = ACCESSORIES_SERVICE.getAccessoryById(id);
            if (text.contains("up")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("aA/&" + id + "?" + ++count)
                        .endRow()
                        .row()
                        .button("🔙 Назад", "aA/id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartAccessory.getName() + "* | " +
                        "\n💵 " + count + " _шт._ `X` " + (cartAccessory.getPrice()) + " _руб._" +
                        " = " + (cartAccessory.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("down")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("aA/&" + id + "?" + --count)
                        .endRow()
                        .row()
                        .button("🔙 Назад", "aA/id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartAccessory.getName() + "* | " +
                        "\n💵 " + count + " _шт._ `X` " + (cartAccessory.getPrice()) + " _руб._" +
                        " = " + (cartAccessory.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");

            } else if (text.contains("crt")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                cartAccessory.setCount(count);
                CART_SERVICE.addToCart(cartAccessory, chat_id);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Убрать из корзины", "aA/" + id + "del")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "aA/Акссесуары")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartAccessory.getName() + "* | " +
                        "\n💵 *" + cartAccessory.getPrice() + " руб. *" +
                        "\n\n" + cartAccessory.getDescription().trim() +
                        "\n[_](" + cartAccessory.getImg() + ")");
            } else {
                count = 1;
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("aA/&" + id + "?" + "1")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "aA/id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartAccessory.getName() + "* | " +
                        "\n💵 " + count + " _шт._ `X` " + (cartAccessory.getPrice()) + " _руб._" +
                        " = " + (cartAccessory.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            }
        } else if (text.contains("id")) {
            Accessory idAccessory = ACCESSORIES_SERVICE.getAccessoryById(Long.parseLong(text.replace("id", "")));
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 В корзину", "aA/&" + idAccessory.getId() + "?")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "aA/Акссесуары")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("🏷 *" + idAccessory.getName() + "* | " +
                    "\n💵 *" + idAccessory.getPrice() + " руб. *" +
                    "\n\n" + idAccessory.getDescription().trim() +
                    "\n[_](" + idAccessory.getImg() + ")");

        }
        else if (text.contains("del")){
            Accessory delAccessory = ACCESSORIES_SERVICE.getAccessoryById(Long.parseLong(text.replace("del", "")));
            CART_SERVICE.deleteFromCart(delAccessory, chat_id);
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 В корзину", "aA/&" + delAccessory.getId() + "?")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "aA/Акссесуары")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("🏷 *" + delAccessory.getName() + "*" +
                    "\n💵 *" + delAccessory.getPrice() + " руб.*" +
                    "\n\n" + delAccessory.getDescription().trim() +
                    "\n[_](" + delAccessory.getImg() + ")");

        }
        else {
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(allAccessoryTypes, "aA/")
                    .row()
                    .button("🔙 Назад", "mНаличие")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("*Наличие / Акссесуары*\nВыберите категорию:");
        }
        return editMessageText.setParseMode("Markdown");
    }

    public EditMessageText accessoryHandle(String text, long chat_id, long mes_id) {
        text = text.substring(1);
        EditMessageText editMessageText;
        Accessory currAccessory;
        if (allAccessoryTypes.contains(text)) {
            ArrayList<Accessory> accessories = ACCESSORIES_SERVICE.getAccessoriesByType(text);
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .accessoryButtons(accessories, "a")
                    .row()
                    .button("🔙 Назад", "aАкссесуары")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("*Каталог / Акссесуары*\nКатегории Акссесуаров: " + text);
        } else if (text.contains("&")) {
            Accessory cartAccessory;
            long id = Long.parseLong(text.split("&")[1].split("\\?")[0]);
            int count;
            cartAccessory = ACCESSORIES_SERVICE.getAccessoryById(id);
            if (text.contains("up")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("a&" + id + "?" + ++count)
                        .endRow()
                        .row()
                        .button("🔙 Назад", "aid" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartAccessory.getName() + "* | " +
                        "\n💵 " + count + " _шт._ `X` " + (cartAccessory.getPrice()) + " _руб._" +
                        " = " + (cartAccessory.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("down")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("a&" + id + "?" + --count)
                        .endRow()
                        .row()
                        .button("🔙 Назад", "aid" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartAccessory.getName() + "* | " +
                        "\n💵 " + count + " _шт._ `X` " + (cartAccessory.getPrice()) + " _руб._" +
                        " = " + (cartAccessory.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("crt")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                cartAccessory.setCount(count);
                CART_SERVICE.addToCart(cartAccessory, chat_id);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Убрать из корзины", "a" + id + "del")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "aАкссесуары")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartAccessory.getName() + "* | " +
                        "\n💵 *" + cartAccessory.getPrice() + " руб. *" +
                        "\n\n" + cartAccessory.getDescription().trim() +
                        "\n[_](" + cartAccessory.getImg() + ")");

            } else {
                count = 1;
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("a&" + id + "?" + "1")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "aid" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartAccessory.getName() + "* | " +
                        "\n💵 " + count + " _шт._ `X` " + (cartAccessory.getPrice()) + " _руб._" +
                        " = " + (cartAccessory.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            }
        } else if (text.contains("id")) {
            currAccessory = ACCESSORIES_SERVICE.getAccessoryById(Long.parseLong(text.replace("id", "")));
            if (currAccessory.isAvailable()) {
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("🛒 В корзину", "a&" + currAccessory.getId() + "?")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "aАкссесуары")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + currAccessory.getName() + "* | " +
                        "\n💵 *" + currAccessory.getPrice() + " руб. *" +
                        "\n\n" + currAccessory.getDescription().trim() +
                        "\n[_](" + currAccessory.getImg() + ")");
            } else {
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("🔙 Назад", "aАкссесуары")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + currAccessory.getName() + "* | " + " \n_(нет в наличии)_" +
                        "\n💵 *" + currAccessory.getPrice() + " руб. *" +
                        "\n\n" + currAccessory.getDescription().trim() +
                        "\n[_](" + currAccessory.getImg() + ")");
            }
        } else if (text.contains("del")) {
            Accessory delAccessory = ACCESSORIES_SERVICE.getAccessoryById(Long.parseLong(text.replace("del", "")));
            CART_SERVICE.deleteFromCart(delAccessory, chat_id);
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 В корзину", "a&" + delAccessory.getId() + "?")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "aАкссесуары")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("🏷 *" + delAccessory.getName() + "*" +
                    "\n💵 *" + delAccessory.getPrice() + " руб.*" +
                    "\n\n" + delAccessory.getDescription().trim() +
                    "\n[_](" + delAccessory.getImg() + ")");

        } else {
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(allAccessoryTypes, "a")
                    .row()
                    .button("🔙 Назад", "mКаталог")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("*Каталог / Акссесуары*\nВыберите категорию:");

        }
        return editMessageText.setParseMode("Markdown");
    }

    public EditMessageText availableTobaccoHandle(String text, long chat_id, long mes_id) {
        text = text.substring(3);
        EditMessageText editMessage;
        if (allAvailableTobaccoFortresses.contains(text)) {
            ArrayList<Tobacco> tobaccos = TOBACCO_SERVICE.getTobaccoByFortress(text, allAvailableTobaccos);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .tobaccoButtons(tobaccos, "tA/", "")
                    .row()
                    .button("🔙 Назад", "tA/Табаки")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Наличие / Табаки*\nТабаки крепости: " + text);
        } else if (text.contains("cart")) {
            Tobacco crtTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("cart", "")));
            if (crtTobacco.getRadonejskayaTastes() != null && !crtTobacco.getRadonejskayaTastes().isEmpty()
                    && !crtTobacco.getRadonejskayaTastes().get(0).equals("null") && crtTobacco.getKarlaMarksaTastes() != null
                    && !crtTobacco.getKarlaMarksaTastes().isEmpty() && !crtTobacco.getKarlaMarksaTastes().get(0).equals("null")) {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Ул. Радонежская 1", "tA/" + crtTobacco.getId() + "RadTastes")
                        .endRow()
                        .row()
                        .button("Ул. Проспект Карла Маркса 196", "tA/" + crtTobacco.getId() + "KarTastes")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "tA/id" + crtTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("Для данного табака в наличии есть следующие вкусы:\n\n"
                        + "*ул. Радонежская 1* | `" + crtTobacco.getRadonejskayaTastes().toString().replace("{", "")
                        .replace("}", "") + "`\n\n*Проспект Карла Маркса 196* | `"
                        + crtTobacco.getKarlaMarksaTastes().toString().replace("{", "")
                        .replace("}", "") + "`\n\nВыберите магазин:");
            } else if (crtTobacco.getKarlaMarksaTastes() == null || crtTobacco.getKarlaMarksaTastes().isEmpty()
                    || crtTobacco.getKarlaMarksaTastes().get(0).equals("null")) {
                ArrayList<String> allTastes = crtTobacco.getRadonejskayaTastes();
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(allTastes, "tA/&" + crtTobacco.getId() + "?")
                        .row()
                        .button("🔙 Назад", "tA/id" + crtTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("Данный табак доступен по адресу *Ул. Радонежская 1*.\n\n" +
                        "Выберите интересующий вкус:");
            } else {
                ArrayList<String> allTastes = crtTobacco.getKarlaMarksaTastes();
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(allTastes, "tA/&" + crtTobacco.getId() + "?")
                        .row()
                        .button("🔙 Назад", "tA/id" + crtTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("Данный табак доступен по адресу *Проспект Карла Маркса 196*.\n\n" +
                        "Выберите интересующий вкус:");
            }
        } else if (text.contains("&")) {
            Tobacco tasteTobacco;
            long id = Long.parseLong(text.split("&")[1].split("\\?")[0]);
            int count;
            String taste = text.split("&")[1].split("\\?")[1];
            tasteTobacco = TOBACCO_SERVICE.getTobaccoById(id);
            tasteTobacco.setTaste(taste);
            if (text.contains("up")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[2]);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("tA/&" + id + "?" + taste + "?" + ++count)
                        .endRow()
                        .row()
                        .button("🔙 Назад", "tid" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + tasteTobacco.getName() + "*  (`" + taste + "`)" +
                        "\n💵 " + count + " _шт._ `X` " + (tasteTobacco.getPrice()) + " _руб._" +
                        " = " + (tasteTobacco.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("down")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[2]);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("tA/&" + id + "?" + taste + "?" + --count)
                        .endRow()
                        .row()
                        .button("🔙 Назад", "tid" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + tasteTobacco.getName() + "*  (`" + taste + "`)" +
                        "\n💵 " + count + " _шт._ `X` " + (tasteTobacco.getPrice()) + " _руб._" +
                        " = " + (tasteTobacco.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("crt")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[2]);
                tasteTobacco.setCount(count);
                CART_SERVICE.addToCart(tasteTobacco, chat_id);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Убрать из корзины", "t" + id + "del")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "tA/Табаки")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + tasteTobacco.getName() + "*" +
                        "\n💵 *" + tasteTobacco.getPrice() + " руб.*" +
                        "\n\n" + tasteTobacco.getDescription().trim() +
                        "\n[_](" + tasteTobacco.getImg() + ")");
            } else {
                count = 1;
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("tA/&" + id + "?" + taste + "?" + count)
                        .endRow()
                        .row()
                        .button("🔙 Назад", "tid" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + tasteTobacco.getName() + "*  (`" + taste + "`)" +
                        "\n💵 " + count + " _шт._ `X` " + (tasteTobacco.getPrice()) + " _руб._" +
                        " = " + (tasteTobacco.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            }
        } else if (text.contains("del")) {
            Tobacco delTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("del", "")));
            CART_SERVICE.deleteFromCart(delTobacco, chat_id);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 В корзину", "tA/" + delTobacco.getId() + "crt")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "tA/Табаки")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("🏷 *" + delTobacco.getName() + "*" +
                    "\n💵 *" + delTobacco.getPrice() + " руб.*" +
                    "\n\n" + delTobacco.getDescription().trim() +
                    "\n[_](" + delTobacco.getImg() + ")");
        } else if (text.contains("id")) {
            Tobacco idTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("id", "")));
            if (idTobacco.isAvailable()) {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("🛒 В корзину", "tA/" + idTobacco.getId() + "cart")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "tA/Табаки")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + idTobacco.getName() + "*" +
                        "\n💵 *" + idTobacco.getPrice() + " руб.*" +
                        "\n\n" + idTobacco.getDescription().trim() +
                        "\n[_](" + idTobacco.getImg() + ")");
            } else {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("🔙 Назад", "tA/Табаки")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + idTobacco.getName() + "* \n_(нет в наличии)_" +
                        "\n💵 *" + idTobacco.getPrice() + " руб.*" +
                        "\n\n" + idTobacco.getDescription().trim() +
                        "\n[_](" + idTobacco.getImg() + ")");
            }
        } else if (text.contains("Tastes")) {
            Tobacco addressTobacco;
            if (text.contains("Rad")) {
                addressTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("RadTastes", "")));
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(addressTobacco.getRadonejskayaTastes(), "tA/&" + addressTobacco.getId() + "?")
                        .row()
                        .button("🔙 Назад", "tA/id" + addressTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*Наличие вкусов по адресу: Радонежская 1*\nВыберите вкус табака:");
            } else {
                addressTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("KarTastes", "")));
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(addressTobacco.getKarlaMarksaTastes(), "tA/&" + addressTobacco.getId() + "?")
                        .row()
                        .button("🔙 Назад", "tA/id" + addressTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*Наличие вкусов по адресу: Проспект Карла Маркса 196*\nВыберите вкус табака:");
            }
        } else {
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(allAvailableTobaccoFortresses, "tA/")
                    .row()
                    .button("🔙 Назад", "mНаличие")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Наличие / Табаки*\nВыберите крепость табака:");
        }
        return editMessage;
    }

    public EditMessageText tobaccoHandle(String text, long chat_id, long mes_id) {
        text = text.substring(1);
        EditMessageText editMessage;
        if (allTobaccoFortresses.contains(text)) {
            ArrayList<Tobacco> tobaccos = TOBACCO_SERVICE.getTobaccoByFortress(text, allTobaccos);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .tobaccoButtons(tobaccos, "t", "")
                    .row()
                    .button("🔙 Назад", "tТабаки")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Каталог / Табаки*\nТабаки крепости: " + text);
        } else if (text.contains("cart")) {
            Tobacco crtTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("cart", "")));
            if (crtTobacco.getRadonejskayaTastes() != null && !crtTobacco.getRadonejskayaTastes().isEmpty()
                    && !crtTobacco.getRadonejskayaTastes().get(0).equals("null") && crtTobacco.getKarlaMarksaTastes() != null
                    && !crtTobacco.getKarlaMarksaTastes().isEmpty() && !crtTobacco.getKarlaMarksaTastes().get(0).equals("null")) {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Ул. Радонежская 1", "t" + crtTobacco.getId() + "RadTastes")
                        .endRow()
                        .row()
                        .button("Ул. Проспект Карла Маркса 196", "t" + crtTobacco.getId() + "KarTastes")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "tid" + crtTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("Для данного табака в наличии есть следующие вкусы:\n\n"
                        + "*ул. Радонежская 1* | `" + crtTobacco.getRadonejskayaTastes().toString().replace("{", "")
                        .replace("}", "") + "`\n\n*Проспект Карла Маркса 196* | `"
                        + crtTobacco.getKarlaMarksaTastes().toString().replace("{", "")
                        .replace("}", "") + "`\n\nВыберите магазин:");
            } else if (crtTobacco.getKarlaMarksaTastes() == null || crtTobacco.getKarlaMarksaTastes().isEmpty()
                    || crtTobacco.getKarlaMarksaTastes().get(0).equals("null")) {
                ArrayList<String> allTastes = crtTobacco.getRadonejskayaTastes();
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(allTastes, "t&" + crtTobacco.getId() + "?")
                        .row()
                        .button("🔙 Назад", "tid" + crtTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("Данный табак доступен по адресу *Ул. Радонежская 1*.\n\n" +
                        "Выберите интересующий вкус:");
            } else {
                ArrayList<String> allTastes = crtTobacco.getKarlaMarksaTastes();
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(allTastes, "t&" + crtTobacco.getId() + "?")
                        .row()
                        .button("🔙 Назад", "tid" + crtTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("Данный табак доступен по адресу *Проспект Карла Маркса 196*.\n\n" +
                        "Выберите интересующий вкус:");
            }
        } else if (text.contains("&")) {
            Tobacco tasteTobacco;
            long id = Long.parseLong(text.split("&")[1].split("\\?")[0]);
            int count;
            String taste = text.split("&")[1].split("\\?")[1];
            tasteTobacco = TOBACCO_SERVICE.getTobaccoById(id);
            tasteTobacco.setTaste(taste);
            if (text.contains("up")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[2]);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("t&" + id + "?" + taste + "?" + ++count)
                        .endRow()
                        .row()
                        .button("🔙 Назад", "tid" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + tasteTobacco.getName() + "*  (`" + taste + "`)" +
                        "\n💵 " + count + " _шт._ `X` " + (tasteTobacco.getPrice()) + " _руб._" +
                        " = " + (tasteTobacco.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("down")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[2]);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("t&" + id + "?" + taste + "?" + --count)
                        .endRow()
                        .row()
                        .button("🔙 Назад", "tid" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + tasteTobacco.getName() + "*  (`" + taste + "`)" +
                        "\n💵 " + count + " _шт._ `X` " + (tasteTobacco.getPrice()) + " _руб._" +
                        " = " + (tasteTobacco.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("crt")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[2]);
                tasteTobacco.setCount(count);
                CART_SERVICE.addToCart(tasteTobacco, chat_id);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Убрать из корзины", "t" + id + "del")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "tТабаки")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + tasteTobacco.getName() + "*" +
                        "\n💵 *" + tasteTobacco.getPrice() + " руб.*" +
                        "\n\n" + tasteTobacco.getDescription().trim() +
                        "\n[_](" + tasteTobacco.getImg() + ")");
            } else {
                count = 1;
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("t&" + id + "?" + taste + "?" + count)
                        .endRow()
                        .row()
                        .button("🔙 Назад", "tid" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + tasteTobacco.getName() + "*  (`" + taste + "`)" +
                        "\n💵 " + count + " _шт._ `X` " + (tasteTobacco.getPrice()) + " _руб._" +
                        " = " + (tasteTobacco.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            }
        } else if (text.contains("del")) {
            Tobacco delTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("del", "")));
            CART_SERVICE.deleteFromCart(delTobacco, chat_id);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 В корзину", "t" + delTobacco.getId() + "crt")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "tnal")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("🏷 *" + delTobacco.getName() + "*" +
                    "\n💵 *" + delTobacco.getPrice() + " руб.*" +
                    "\n\n" + delTobacco.getDescription().trim() +
                    "\n[_](" + delTobacco.getImg() + ")");
        } else if (text.contains("id")) {
            Tobacco idTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("id", "")));
            if (idTobacco.isAvailable()) {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("🛒 В корзину", "t" + idTobacco.getId() + "cart")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "tТабаки")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + idTobacco.getName() + "*" +
                        "\n💵 *" + idTobacco.getPrice() + " руб.*" +
                        "\n\n" + idTobacco.getDescription().trim() +
                        "\n[_](" + idTobacco.getImg() + ")");
            } else {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("🔙 Назад", "tТабаки")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + idTobacco.getName() + "* \n_(нет в наличии)_" +
                        "\n💵 *" + idTobacco.getPrice() + " руб.*" +
                        "\n\n" + idTobacco.getDescription().trim() +
                        "\n[_](" + idTobacco.getImg() + ")");
            }
        } else if (text.contains("Tastes")) {
            Tobacco addressTobacco;
            if (text.contains("Rad")) {
                addressTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("RadTastes", "")));
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(addressTobacco.getRadonejskayaTastes(), "t&" + addressTobacco.getId() + "?")
                        .row()
                        .button("🔙 Назад", "tid" + addressTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*Наличие вкусов по адресу: Радонежская 1*\nВыберите вкус табака:");
            } else {
                addressTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("KarTastes", "")));
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(addressTobacco.getKarlaMarksaTastes(), "t&" + addressTobacco.getId() + "?")
                        .row()
                        .button("🔙 Назад", "tid" + addressTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*Наличие вкусов по адресу: Проспект Карла Маркса 196*\nВыберите вкус табака:");
            }
        } else {
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(allTobaccoFortresses, "t")
                    .row()
                    .button("🔙 Назад", "mКаталог")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Каталог / Табаки*\nВыберите крепость табака:");
        }
        return editMessage;
    }

    public EditMessageText cartHandle(String text, long chat_id, long mes_id) {
        text = text.substring(1);
        EditMessageText editMessage;
        if (text.equals("clear")) {
            CART_SERVICE.clearUserCart(chat_id);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("Вернуться к покупкам", "mКаталог")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "cКорзина")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("Ваша корзина успешно очищена!\nВоспользуйтесь каталогом чтобы найти всё что нужно");
        } else if (text.contains("&")) {
            ArrayList<Product> cart = CART_SERVICE.getUserCart(chat_id).getCart();
            int position;
            if (text.contains("up")) {
                position = Integer.parseInt(text.split("&")[1].split("\\?")[0]);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .positionButtons("c&" + ++position, cart.size())
                        .endRow()
                        .row()
                        .button("Изменить количество", "cdel")
                        .endRow()
                        .row()
                        .button("Удалить из корзины", "cdel")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "cКорзина")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText(CART_SERVICE.getUserCart(chat_id).toStringEdit(position) +
                        "\n\nПожалуйста, выберите номер позиции, которую бы Вы хотели изменить:");
            } else if (text.contains("down")) {
                position = Integer.parseInt(text.split("&")[1].split("\\?")[0]);
                if (position == 1) {
                    editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                            .positionButtons("c&" + --position, cart.size())
                            .row()
                            .button("Очистить всю корзину", "cclear")
                            .endRow()
                            .row()
                            .button("🔙 Назад", "cКорзина")
                            .endRow()
                            .rebuild(mes_id);
                    editMessage.setText(CART_SERVICE.getUserCart(chat_id).toString() +
                            "\n\nПожалуйста, выберите номер позиции, которую бы Вы хотели изменить:");
                } else {
                    editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                            .row()
                            .positionButtons("c&" + --position, cart.size())
                            .endRow()
                            .row()
                            .button("Изменить количество", "ccount&" + position)
                            .endRow()
                            .row()
                            .button("Удалить из корзины", "cdel&" + position)
                            .endRow()
                            .row()
                            .button("🔙 Назад", "cКорзина")
                            .endRow()
                            .rebuild(mes_id);
                    editMessage.setText(CART_SERVICE.getUserCart(chat_id).toStringEdit(position) +
                            "\n\nПожалуйста, выберите номер позиции, которую бы Вы хотели изменить:");
                }
            }
/*            else if (text.contains("changePos")) {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("c&" + ++position + "?")
                        .endRow()
                        .row()
                        .button("Изменить количество", "cdel")
                        .endRow()
                        .row()
                        .button("Удалить из корзины", "cdel")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "cКорзина")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + product.getName() +
                        "\n\nКоличество: *" + product.getCount() + "*" +
                        "\n💵 *" + product.getCount() + " X " + product.getPrice() + " руб.*" +
                        "\n\nВыберите необходимое действие:");
            }*/
            else {
                position = 0;
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .positionButtons("c&" + position, cart.size())
                        .row()
                        .button("Очистить всю корзину", "cclear")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "cКорзина")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText(CART_SERVICE.getUserCart(chat_id).toString() +
                        "\n\nПожалуйста, выберите номер позиции, которую бы Вы хотели изменить:");
            }
        }
/*        else if (text.equals("makeOrder")) {
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🔙 Назад", "tnal")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("🏷 *" + delTobacco.getName() + "*" +
                    "\n💵 *" + delTobacco.getPrice() + " руб.*" +
                    "\n\n" + delTobacco.getDescription().trim() +
                    "\n[_](" + delTobacco.getImg() + ")");
        }*/
        else {
            if (CART_SERVICE.getUserCart(chat_id).getCart().size() == 0) {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .rebuild(mes_id);
                editMessage.setText(CART_SERVICE.getUserCart(chat_id).toString());
            } else {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("\uD83D\uDCE6 Оформить заказ", "cmakeOrder")
                        .endRow()
                        .row()
                        .button("Изменить корзину", "c&edit")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText(CART_SERVICE.getUserCart(chat_id).toString());
            }
        }
        return editMessage;
    }

    public synchronized AnswerCallbackQuery answerCallbackQuery(String callbackId, String message) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(callbackId);
        answer.setShowAlert(false);
        answer.setText(message);
        answer.setShowAlert(true);
        return answer;
    }

    public void updateHandle(Update update) throws TelegramApiException {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message inMessage = update.getMessage();
            long chat_id = inMessage.getChatId();
            SendMessage outMessage = new SendMessage().setChatId(chat_id);
            String text = update.getMessage().getText();
            System.out.println(new Date() + ": " + inMessage.getFrom().getFirstName() + " " +
                    inMessage.getFrom().getLastName() + " (" + inMessage.getFrom().getUserName() +
                    "): " + text);
            execute(messageStarter(text, outMessage).setParseMode("Markdown"));
        } else if (update.hasCallbackQuery()) {
            Message inMessage = update.getCallbackQuery().getMessage();
            long chat_id = inMessage.getChatId();
            long mes_id = inMessage.getMessageId();
            String text = update.getCallbackQuery().getData();
            System.out.println(new Date() + ": " + update.getCallbackQuery().getFrom().getFirstName() + " " +
                    update.getCallbackQuery().getFrom().getLastName() + " (" + update.getCallbackQuery().getFrom().getUserName() +
                    "): " + text);
            if (text.contains("crt"))
                execute(answerCallbackQuery(update.getCallbackQuery().getId(), "Товар успешно добавлен в корзину!"));
            if (text.contains("del"))
                execute(answerCallbackQuery(update.getCallbackQuery().getId(), "Товар успешно удалён из корзины!"));

            if (text.startsWith("c"))
                execute(cartHandle(text, chat_id, mes_id).setParseMode("Markdown"));
            else if (text.startsWith("h")) {
                if (text.contains("hA/"))
                    execute(availableHookahHandle(text, chat_id, mes_id).setParseMode("Markdown"));
                else
                    execute(hookahHandle(text, chat_id, mes_id).setParseMode("Markdown"));
            } else if (text.startsWith("t")) {
                if (text.contains("tA/"))
                    execute(availableTobaccoHandle(text, chat_id, mes_id).setParseMode("Markdown"));
                else
                    execute(tobaccoHandle(text, chat_id, mes_id).setParseMode("Markdown"));
            } else if (text.startsWith("m"))
                execute(messageHandle(text, chat_id, mes_id).setParseMode("Markdown"));
            else if (text.startsWith("a"))
                if (text.contains("aA/"))
                    execute(availableAccessoryHandle(text, chat_id, mes_id).setParseMode("Markdown"));
                else
                execute(accessoryHandle(text, chat_id, mes_id).setParseMode("Markdown"));
            else if (text.startsWith("u"))
                if (text.contains("uA/"))
                    execute(availableCharcoalHandle(text, chat_id, mes_id).setParseMode("Markdown"));
                else
                    execute(charcoalHandle(text, chat_id, mes_id).setParseMode("Markdown"));
        }
    }

    public String getBotUsername() {
        return "Grizzly_Shop_Bot";
    }

    public String getBotToken() {
        return "1238275097:AAGnnDKQpxjxCfjey6zxx0kVgm1EdDNMvak";
    }
}
