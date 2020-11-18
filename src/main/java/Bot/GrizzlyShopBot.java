package Bot;

import Bot.Keyboards.InlineKeyboardMarkupBuilder;
import Bot.Keyboards.ReplyKeyboardMarkupBuilder;
import Bot.Models.Order;
import Bot.Models.Products.*;
import Bot.Service.*;
import Bot.Service.ServiceXML.XMLWriter;
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

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Date;

public class GrizzlyShopBot extends TelegramLongPollingBot {

    private static final String BOT_TOKEN = System.getenv("TOKEN");
    private static final HookahService HOOKAH_SERVICE = new HookahService();
    private static final TobaccoService TOBACCO_SERVICE = new TobaccoService();
    private static final CartService CART_SERVICE = new CartService();
    private static final AccessoriesService ACCESSORIES_SERVICE = new AccessoriesService();
    private static final CharcoalService CHARCOAL_SERVICE = new CharcoalService();
    private static final VaporizerService VAPORIZER_SERVICE = new VaporizerService();
    private static final OrderService ORDER_SERVICE = new OrderService();
    private static final ArrayList<Vaporizer> allVaporizers = VAPORIZER_SERVICE.getAllVaporizers();
    private static final ArrayList<Vaporizer> allAvailableVaporizers = VAPORIZER_SERVICE.getAvailableVaporizers();
    private static final ArrayList<String> allAccessoryTypes = ACCESSORIES_SERVICE.getAllTypes();
    private static final ArrayList<String> allAccessoryBowlBrands = ACCESSORIES_SERVICE.getBowlsBrandList();
    private static final ArrayList<String> allAccessoryAvailableBowlBrands = ACCESSORIES_SERVICE.getAvailableBowlsBrandList();
    private static final ArrayList<String> allHookahBrands = HOOKAH_SERVICE.getAllBrandsList();
    private static final ArrayList<String> availableHookahBrands = HOOKAH_SERVICE.getAvailableBrandsList();
    private static final ArrayList<Tobacco> allTobaccos = TOBACCO_SERVICE.getAllTobacco();
    private static final ArrayList<Tobacco> allAvailableTobaccos = TOBACCO_SERVICE.getAllAvailableTobacco();
    private static final ArrayList<String> allTobaccoFortresses = TOBACCO_SERVICE.getAllFortresses();
    private static final ArrayList<String> allAvailableTobaccoFortresses = TOBACCO_SERVICE.getAllAvailableFortresses();

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new GrizzlyShopBot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }


    public synchronized void onUpdateReceived(Update update) {
        try {
            updateHandle(update);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public synchronized EditMessageText messageHandle(String text, long chat_id, long mes_id) {
        text = text.substring(1);
        EditMessageText editMessageText;
        switch (text) {
            case "📞 Контакты":
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
            case "📓 Каталог":
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
                        .button("Электронные испарители", "mЭлектронные испарители")
                        .endRow()
                        .row()
                        .button("📌 Помощь", "mПомощьКаталог")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Каталог - GRIZZLY SHOP -*\n\nВыберите категорию интересующего товара:");
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
                        .button("Электронные испарители", "mЭлектронные испарители")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Каталог - GRIZZLY SHOP -*\n\nДля совершения заказа необходимо:\n\n☑ Выбрать интересующий товар из каталога" +
                        "\n☑ Добавить его в корзину\n☑ Оформить заказ, сформировав необходимые данные по доставке");
                return editMessageText;
            case "📔 Наличие":
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Табаки", "tA/Табаки")
                        .button("Кальяны", "hA/Кальяны")
                        .endRow()
                        .row()
                        .button("Акссесуары", "aA/Акссесуары")
                        .button("Уголь", "uA/Уголь")
                        .endRow()
                        .row()
                        .button("Электронные испарители", "vA/Электронные испарители")
                        .endRow()
                        .row()
                        .button("📌 Помощь", "mПомощьНаличие")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Наличие - GRIZZLY SHOP -*\n\nВыберите категорию интересующего товара:");
                return editMessageText;
            case "ПомощьНаличие":
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Табаки", "tA/Табаки")
                        .button("Кальяны", "hA/Кальяны")
                        .endRow()
                        .row()
                        .button("Акссесуары", "aA/Акссесуары")
                        .button("Уголь", "uA/Уголь")
                        .endRow()
                        .row()
                        .button("Электронные испарители", "vA/Электронные испарители")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Наличие - GRIZZLY SHOP -*\n\nДля совершения заказа необходимо:\n\n☑ Выбрать интересующий товар из каталога" +
                        "\n☑ Добавить его в корзину\n☑ Оформить заказ, сформировав необходимые данные по доставке");
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
            case "Электронные испарители":
                return vaporizerHandle(text, chat_id, mes_id);
        }
        return InlineKeyboardMarkupBuilder.create(chat_id)
                .rebuild(mes_id).setText("*Данный функционал находится в разработке*");
    }

    public synchronized SendMessage messageStarter(String text, SendMessage sendMessage) {
        long chat_id = Long.parseLong(sendMessage.getChatId());
        if (text.contains("Ул.") || text.contains("ул.") || text.contains("ул ") || text.contains("Ул ") ||
                text.contains("район") || text.contains("Улица") || text.contains("улица") || text.contains("г.")
                && text.contains("город") || text.contains("г ") || text.contains("дом") || text.contains("д.")
                && text.contains("д ") || text.contains("квартира") || text.contains("кв.") || text.contains("кв ") ||
                text.contains("Самара")) {
            ORDER_SERVICE.getOrder(chat_id).setAddress(text);
            sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                    .setText("*Оформление заказа / Доставка*\n\nПожалуйста, проверьте указанный адрес доставки." +
                            "\n\n\uD83C\uDFE0 Доставка по адресу:\n" + text)
                    .row()
                    .button("✅ Адрекс указан верно")
                    .endRow()
                    .row()
                    .button("❎ Изменить адрес доставки")
                    .endRow()
                    .build();
        }
        else {
            switch (text) {
                case "/start":
                    sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                            .setText("*Добро пожаловать в GRIZZLY SHOP!*\n" +
                                    "У нас вы найдёте большой ассортимент кальянной продукции, " +
                                    "включая всевозможные аксесуары и огромный выбор табака!" +
                                    "\n\n\uD83D\uDD1E *Данный магазин предназначен для лиц " +
                                    "старше 18 лет.*\nНажимая кнопку \"Продолжить\", " +
                                    "Вы подтверждаете, что Вам исполнилось 18 лет!\n" +
                                    "[_](https://sun9-19.userapi.com/c850720/v850720693/1666fd/HhIdoIXc2rI.jpg)")
                            .row()
                            .button("Продолжить")
                            .endRow()
                            .build();
                    break;
                case "Назад":
                case "Продолжить":
                case "Главное меню":
                case "\uD83D\uDD19 Вернуться в главное меню":
                    sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                            .setText("*Главное меню магазина*" +
                                    "\n\nИспользуйте кнопки на клавиатуре снизу для навигации")
                            .row()
                            .button("📓 Каталог")
                            .button("📔 Наличие")
                            .endRow()
                            .row()
                            .button("🛒 Корзина")
                            .button("📞 Контакты")
                            .endRow()
                            .build();
                    break;
                case "📓 Каталог":
                    sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                            .setText("*Каталог - GRIZZLY SHOP -*\n\nВыберите категорию интересующего товара:")
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
                case "📔 Наличие":
                    sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                            .setText("*Наличие - GRIZZLY SHOP -*\n\nВыберите категорию интересующего товара:")
                            .row()
                            .button("Табаки", "tA/Табаки")
                            .button("Кальяны", "hA/Кальяны")
                            .endRow()
                            .row()
                            .button("Акссесуары", "aA/Аксесуары")
                            .button("Уголь", "uA/Уголь")
                            .endRow()
                            .row()
                            .button("Электронные испарители", "vA/Испарители")
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
                                .row()
                                .button("📓 Каталог", "m📓 Каталог")
                                .endRow()
                                .build();
                    } else {
                        sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                                .setText(CART_SERVICE.getUserCart(chat_id).toString())
                                .row()
                                .button("\uD83D\uDCE6 Оформить заказ", "cmakeOrd")
                                .endRow()
                                .row()
                                .button("📝 Изменить корзину", "c&edit")
                                .endRow()
                                .build();
                    }
                    break;
                case "📞 Контакты":
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
                case "\uD83D\uDCE6 Оформить заказ":
                case "\uD83D\uDD19 Изменить способ получения":
                    sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                            .setText("\uD83D\uDCE6 *Оформление заказа*\n\nДля успешного оформления заказа мы" +
                                    " попросим ответить вас на несколько важных уточняющих вопросов." +
                                    "\nДля ответов используйте кнопки клавиатуры снизу" +
                                    "\n\nВыберите удобный способ получения:")
                            .row()
                            .button("🚚 Доставка")
                            .button("🏬 Самовывоз")
                            .endRow()
                            .row()
                            .button("\uD83D\uDD19 Вернуться в главное меню")
                            .endRow()
                            .build();
                    break;
                case "🚚 Доставка":
                case "❎ Изменить адрес доставки":
                    ORDER_SERVICE.getOrder(chat_id).setDeliveryMethod("Доставка");
                    sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                            .setText("*Оформление заказа / Доставка*\n\n" +
                                    "Пожалуйста, введите в чат адрес по которому необходимо будет " +
                                    "совершить доставку. Доставка осуществляется по городу Самара." +
                                    "\nАдрес записывается в формате:" +
                                    "\n\n`Ул. Название улицы, д. Номер дома, кв. Номер квартиры`")
                            .row()
                            .button("\uD83D\uDD19 Вернуться в главное меню")
                            .endRow()
                            .build();
                    break;
                case "🏬 Самовывоз":
                case "\uD83D\uDD19 Изменить адрес самовывоза":
                    ORDER_SERVICE.getOrder(chat_id).setDeliveryMethod("Самовывоз");
                    sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                            .setText("*Оформление заказа / Самовывоз*\n\nСамовывоз осуществляется в одном из наших магазинов. 🏬" +
                                    "\nПожалуйста, выберите удобный для Вас адрес магазина:")
                            .row()
                            .button("Радонежская 1")
                            .endRow()
                            .row()
                            .button("Проспект Карла Маркса 196")
                            .endRow()
                            .row()
                            .button("\uD83D\uDD19 Изменить способ получения")
                            .endRow()
                            .build();
                    break;
                case "Радонежская 1":
                    ORDER_SERVICE.getOrder(chat_id).setAddress("Магазин на ул. Радонежская 1");
                    sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                            .setText("*Оформление заказа / Самовывоз*\n\nЗаказ практически оформлен. Пожалуйста, " +
                                    "поделитесь с нами своим номером телефона для согласования времени для самовывоза" +
                                    " и получения дополнительной информации.\n\nУверяем Вас, телефон будет " +
                                    "использован исключительно в данных целях")
                            .row()
                            .buttonWithContactRequest("\uD83D\uDCDE Поделиться номером телефона")
                            .endRow()
                            .row()
                            .button("\uD83D\uDD19 Изменить адрес самовывоза")
                            .endRow()
                            .build();
                    break;
                case "Проспект Карла Маркса 196":
                    ORDER_SERVICE.getOrder(chat_id).setAddress("Магазин на ул. Проспект Карла Маркса 196");
                    sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                            .setText("*Оформление заказа / Самовывоз*\n\nЗаказ практически оформлен. Пожалуйста, " +
                                    "поделитесь с нами своим номером телефона для согласования времени для самовывоза" +
                                    " и получения дополнительной информации.\n\nУверяем Вас, телефон будет " +
                                    "использован исключительно в данных целях")
                            .row()
                            .buttonWithContactRequest("\uD83D\uDCDE Поделиться номером телефона")
                            .endRow()
                            .row()
                            .button("\uD83D\uDD19 Изменить адрес самовывоза")
                            .endRow()
                            .build();
                    break;
                case "Да":
                case "Верно":
                case "✅ Адрекс указан верно":
                    sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                            .setText("*Оформление заказа / Доставка*\n\nЗаказ практически оформлен. Пожалуйста, поделитесь с " +
                                    "нами своим номером телефона для согласования времени доставки и получения дополнительной " +
                                    "информации.\n\nУверяем Вас, телефон будет использован исключительно в данных целях")
                            .row()
                            .buttonWithContactRequest("Поделиться номером телефона")
                            .endRow()
                            .row()
                            .buttonWithContactRequest("\uD83D\uDD19 Вернуться в главное меню")
                            .endRow()
                            .build();
                    break;
                case "Поделились номером телефона":
                    Order order = ORDER_SERVICE.getOrder(chat_id);
                    order.setCustomerCart(CART_SERVICE.getUserCart(chat_id).toStringOrder());
                    sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                            .setText("*" + order.getCustomerName() + ", Ваш заказ успешно сформирован и передан нашим " +
                                    "сотрудникам.*\nВ ближайшее время с Вами свяжутся для согласования уточняющей информации." +
                                    "\n\nСпасибо за то что выбрали нас!")
                            .row()
                            .button("Главное меню")
                            .endRow()
                            .build();
                    break;
                case "Не поделились номером телефона":
                    sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                            .setText("Плохо, что вы не поделились номером. Теперь мы найдём и убьём вас")
                            .build();
                    break;
            }
        }
        return sendMessage.setParseMode("Markdown");
    }

    public synchronized EditMessageText availableHookahHandle(String text, long chat_id, long mes_id) {
        text = text.substring(3);
        EditMessageText editMessage;
        if (availableHookahBrands.contains(text)) {
            ArrayList<Hookah> hookahs = HOOKAH_SERVICE.getAvailableHookahsByBrand(text);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .hookahButtons(hookahs, "hA/")
                    .row()
                    .button("🔙 Назад", "hA/Кальяны")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Наличие / Кальяны*\n\nКальяны бренда *" + text + "*: ");
        }
        else if (text.contains("&")) {
            Hookah cartHookah;
            long id = Long.parseLong(text.split("&")[1].split("\\?")[0]);
            int count;
            cartHookah = HOOKAH_SERVICE.getHookahById(id);
            if (text.contains("?up")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("hA/&" + id + "?" + ++count,"crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "hA/?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + cartHookah.getBrand() + "* | " + cartHookah.getName() +
                        "\n💵 " + count + " _шт._ `X` " + (cartHookah.getPrice()) + " _руб._" +
                        " = " + (cartHookah.getPrice()*count) + " _руб._" +
                        "\n`(цена указана за полный комлект)`" +
                        "\n\nВыберите необходимое количество:");
            }
            else if (text.contains("?down")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("hA/&" + id + "?" + --count,"crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "hA/?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + cartHookah.getBrand() + "* | " + cartHookah.getName() +
                        "\n💵 " + count + " _шт._ `X` " + (cartHookah.getPrice()) + " _руб._" +
                        " = " + (cartHookah.getPrice()*count) + " _руб._" +
                        "\n`(цена указана за полный комлект)`" +
                        "\n\nВыберите необходимое количество:");
            }
            else if (text.contains("?crt")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                cartHookah.setCount(count);
                CART_SERVICE.addToCart(cartHookah, chat_id);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("⛔ Убрать из корзины", "hA/" + id + "?del")
                        .endRow()
                        .row()
                        .button("🛒 Перейти в корзину", "cКорзина")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "hA/Кальяны")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + cartHookah.getBrand() + "* | " + cartHookah.getName() +
                        "\n💵 *" + cartHookah.getPrice() + " руб. *\n`(цена указана за полный комлект)`" +
                        "\n\n" + cartHookah.getDescription().trim() +
                        "\n[_](" + cartHookah.getImg() + ")");
            }
            else {
                count = 1;
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("hA/&" + id + "?" + "1","crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "hA/?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + cartHookah.getBrand() + "* | " + cartHookah.getName() +
                        "\n💵 " + count + " _шт._ `X` " + (cartHookah.getPrice()) + " _руб._" +
                        " = " + (cartHookah.getPrice()*count) + " _руб._" +
                        "\n`(цена указана за полный комлект)`" +
                        "\n\nВыберите необходимое количество:");
            }
        }
        else if (text.contains("?id")) {
            Hookah idHookah = HOOKAH_SERVICE.getHookahById(Long.parseLong(text.replace("?id", "")));
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 Добавить в корзину", "hA/&" + idHookah.getId() + "?")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "hA/Кальяны")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("🏷 *" + idHookah.getBrand() + "* | " + idHookah.getName() +
                    "\n💵 *" + idHookah.getPrice() + " руб. *\n`(цена указана за полный комлект)`" +
                    "\n\n" + idHookah.getDescription().trim() +
                    "\n[_](" + idHookah.getImg() + ")");
        }
        else if (text.contains("?del")) {
            Hookah delHookah = HOOKAH_SERVICE.getHookahById(Long.parseLong(text.replace("?del", "")));
            CART_SERVICE.deleteFromCart(delHookah, chat_id);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 Добавить в корзину", "hA/&" + delHookah.getId() + "?")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "hA/Кальяны")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("🏷 *" + delHookah.getName() + "*" +
                    "\n💵 *" + delHookah.getPrice() + " руб.*" +
                    "\n\n" + delHookah.getDescription().trim() +
                    "\n[_](" + delHookah.getImg() + ")");
        }
        else {
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(availableHookahBrands, "hA/")
                    .row()
                    .button("🔙 Назад", "m📔 Наличие")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Наличие / Кальяны*\n\nВыберите бренд кальяна:");
        }
        return editMessage.setParseMode("Markdown");
    }

    public synchronized EditMessageText hookahHandle(String text, long chat_id, long mes_id) {
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
            editMessage.setText("*Каталог / Кальяны*\n\nКальяны бренда *" + text + "*: ");
        }
        else if (text.contains("&")) {
            Hookah cartHookah;
            long id = Long.parseLong(text.split("&")[1].split("\\?")[0]);
            int count;
            cartHookah = HOOKAH_SERVICE.getHookahById(id);
            if (text.contains("?up")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("h&" + id + "?" + ++count,"crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "h?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + cartHookah.getBrand() + "* | " + cartHookah.getName() +
                        "\n💵 " + count + " _шт._ `X` " + (cartHookah.getPrice()) + " _руб._" +
                        " = " + (cartHookah.getPrice()*count) + " _руб._" +
                        "\n`(цена указана за полный комлект)`" +
                        "\n\nВыберите необходимое количество:");
            }
            else if (text.contains("?down")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("h&" + id + "?" + --count,"crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "h?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + cartHookah.getBrand() + "* | " + cartHookah.getName() +
                        "\n💵 " + count + " _шт._ `X` " + (cartHookah.getPrice()) + " _руб._" +
                        " = " + (cartHookah.getPrice()*count) + " _руб._" +
                        "\n`(цена указана за полный комлект)`" +
                        "\n\nВыберите необходимое количество:");
            }
            else if (text.contains("?crt")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                cartHookah.setCount(count);
                CART_SERVICE.addToCart(cartHookah, chat_id);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("⛔ Убрать из корзины", "h" + id + "?del")
                        .endRow()
                        .row()
                        .button("🛒 Перейти в корзину", "cКорзина")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "hКальяны")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + cartHookah.getBrand() + "* | " + cartHookah.getName() +
                        "\n💵 *" + cartHookah.getPrice() + " руб. *\n`(цена указана за полный комлект)`" +
                        "\n\n" + cartHookah.getDescription().trim() +
                        "\n[_](" + cartHookah.getImg() + ")");
            }
            else {
                count = 1;
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("h&" + id + "?" + "1","crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "h?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + cartHookah.getBrand() + "* | " + cartHookah.getName() +
                        "\n💵 " + count + " _шт._ `X` " + (cartHookah.getPrice()) + " _руб._" +
                        " = " + (cartHookah.getPrice()*count) + " _руб._" +
                        "\n`(цена указана за полный комлект)`" +
                        "\n\nВыберите необходимое количество:");
            }
        }
        else if (text.contains("?id")) {
            currHookah = HOOKAH_SERVICE.getHookahById(Long.parseLong(text.replace("?id", "")));
            if (currHookah.isAvailable()) {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("🛒 Добавить в корзину", "h&" + currHookah.getId() + "?")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "hКальяны")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + currHookah.getBrand() + "* | " + currHookah.getName() +
                        "\n💵 *" + currHookah.getPrice() + " руб. *\n`(цена указана за полный комлект)`" +
                        "\n\n" + currHookah.getDescription().trim() +
                        "\n[_](" + currHookah.getImg() + ")");
            }
            else {
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
        }
        else if (text.contains("?del")) {
            Hookah delHookah = HOOKAH_SERVICE.getHookahById(Long.parseLong(text.replace("?del", "")));
            CART_SERVICE.deleteFromCart(delHookah, chat_id);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 Добавить в корзину", "h&" + delHookah.getId() + "?")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "hКальяны")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("🏷 *" + delHookah.getName() + "*" +
                    "\n💵 *" + delHookah.getPrice() + " руб.*" +
                    "\n\n" + delHookah.getDescription().trim() +
                    "\n[_](" + delHookah.getImg() + ")");
        }
        else {
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(allHookahBrands, "h")
                    .row()
                    .button("🔙 Назад", "m📓 Каталог")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Каталог / Кальяны*\n\nВыберите бренд кальяна:");
        }
        return editMessage.setParseMode("Markdown");
    }

    public synchronized EditMessageText availableCharcoalHandle(String text, long chat_id, long mes_id) {
        text = text.substring(3);
        EditMessageText editMessageText;
        if (text.contains("&")) {
            Charcoal cartCharcoal;
            long id = Long.parseLong(text.split("&")[1].split("\\?")[0]);
            int count;
            cartCharcoal = CHARCOAL_SERVICE.getCharcoalById(id);
            if (text.contains("?up")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("uA/&" + id + "?" + ++count, "crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "uA/?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartCharcoal.getName() + "*" +
                        "\n💵 " + count + " _шт._ `X` " + (cartCharcoal.getPrice()) + " _руб._" +
                        " = " + (cartCharcoal.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("?down")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("uA/&" + id + "?" + --count, "crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "uA/?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartCharcoal.getName() + "*" +
                        "\n💵 " + count + " _шт._ `X` " + (cartCharcoal.getPrice()) + " _руб._" +
                        " = " + (cartCharcoal.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("?crt")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                cartCharcoal.setCount(count);
                CART_SERVICE.addToCart(cartCharcoal, chat_id);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("⛔ Убрать из корзины", "uA/" + id + "?del")
                        .endRow()
                        .row()
                        .button("🛒 Перейти в корзину", "cКорзина")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "uA/Уголь")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartCharcoal.getName() + "*" +
                        "\n💵 *" + cartCharcoal.getPrice() + " руб. *" +
                        "\n\n" + cartCharcoal.getDescription().trim() +
                        "\n[_](" + cartCharcoal.getImg() + ")");

            } else {
                count = 1;
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("uA/&" + id + "?" + "1", "crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "uA/?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartCharcoal.getName() + "*" +
                        "\n💵 " + count + " _шт._ `X` " + (cartCharcoal.getPrice()) + " _руб._" +
                        " = " + (cartCharcoal.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            }

        } else if (text.contains("?id")) {
            Charcoal idCharcoal = CHARCOAL_SERVICE.getCharcoalById(Long.parseLong(text.replace("?id", "")));
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 В корзину", "uA/&" + idCharcoal.getId() + "?")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "uA/Уголь")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("🏷 *" + idCharcoal.getName() + "*" +
                    "\n💵 *" + idCharcoal.getPrice() + " руб. *" +
                    "\n\n" + idCharcoal.getDescription().trim() +
                    "\n[_](" + idCharcoal.getImg() + ")");

        } else if (text.contains("?del")) {
            Charcoal delCharcoal = CHARCOAL_SERVICE.getCharcoalById(Long.parseLong(text.replace("?del", "")));
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
                    .button("🔙 Назад", "m📔 Наличие")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("*Наличие / Уголь*\n\nВыберите интересующий уголь:");
        }

        return editMessageText.setParseMode("Markdown");

    }

    public synchronized EditMessageText charcoalHandle(String text, long chat_id, long mes_id) {
        text = text.substring(1);
        EditMessageText editMessageText;
        Charcoal currCharcoal;
        if (text.contains("&")) {
            Charcoal cartCharcoal;
            long id = Long.parseLong(text.split("&")[1].split("\\?")[0]);
            int count;
            cartCharcoal = CHARCOAL_SERVICE.getCharcoalById(id);
            if (text.contains("?up")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("u&" + id + "?" + ++count, "crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "u?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartCharcoal.getName() + "*" +
                        "\n💵 " + count + " _шт._ `X` " + (cartCharcoal.getPrice()) + " _руб._" +
                        " = " + (cartCharcoal.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");

            } else if (text.contains("?down")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("u&" + id + "?" + --count, "crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "u?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartCharcoal.getName() + "*" +
                        "\n💵 " + count + " _шт._ `X` " + (cartCharcoal.getPrice()) + " _руб._" +
                        " = " + (cartCharcoal.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("?crt")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                cartCharcoal.setCount(count);
                CART_SERVICE.addToCart(cartCharcoal, chat_id);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("⛔ Убрать из корзины", "u" + id + "?del")
                        .endRow()
                        .row()
                        .button("🛒 Перейти в корзину", "cКорзина")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "uУголь")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartCharcoal.getName() + "*" +
                        "\n💵 *" + cartCharcoal.getPrice() + " руб. *" +
                        "\n\n" + cartCharcoal.getDescription().trim() +
                        "\n[_](" + cartCharcoal.getImg() + ")");
            } else {
                count = 1;
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("u&" + id + "?" + "1","crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "u?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartCharcoal.getName() + "*" +
                        "\n💵 " + count + " _шт._ `X` " + (cartCharcoal.getPrice()) + " _руб._" +
                        " = " + (cartCharcoal.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            }

        } else if (text.contains("?id")) {
            currCharcoal = CHARCOAL_SERVICE.getCharcoalById(Long.parseLong(text.replace("?id", "")));
            if (currCharcoal.isAvailable()) {
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("🛒 В корзину", "u&" + currCharcoal.getId() + "?")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "uУголь")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + currCharcoal.getName() + "*" +
                        "\n💵 *" + currCharcoal.getPrice() + " руб.*" +
                        "\n\n" + currCharcoal.getDescription().trim() +
                        "\n[_](" + currCharcoal.getImg() + ")");
            } else {
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("🔙 Назад", "uУголь")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + currCharcoal.getName() + "*" + " \n_(нет в наличии)_" +
                        "\n💵 *" + currCharcoal.getPrice() + " руб.*" +
                        "\n\n" + currCharcoal.getDescription().trim() +
                        "\n[_](" + currCharcoal.getImg() + ")");
            }
        } else if (text.contains("?del")) {
            Charcoal delCharcoal = CHARCOAL_SERVICE.getCharcoalById(Long.parseLong(text.replace("?del", "")));
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
                    .button("🔙 Назад", "m📓 Каталог")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("*Каталог / Уголь*\n\nВыберите интересующий уголь:");
        }
        return editMessageText.setParseMode("Markdown");
    }

    public synchronized EditMessageText availableAccessoryHandle(String text, long chat_id, long mes_id) {
        text = text.substring(3);
        EditMessageText editMessageText;
        if (allAccessoryTypes.contains(text)) {
            if (text.contains("Чаша")) {
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(allAccessoryAvailableBowlBrands, "aA/")
                        .row()
                        .button("🔙 Назад", "aA/Акссесуары")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Наличие / Акссесуары / Чаши*\n\nВыберите необходимый бренд:");
            }
            else {
                ArrayList<Accessory> accessories = ACCESSORIES_SERVICE.getAccessoriesByType(text);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .accessoryButtons(accessories, "aA/")
                        .row()
                        .button("🔙 Назад", "aA/Акссесуары")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Наличие / Аксесуары*\n\nВыберите необходимый товар:");
            }
        }
        else if (allAccessoryAvailableBowlBrands.contains(text)) {
            ArrayList<Accessory> accessories = ACCESSORIES_SERVICE.getAvailableAccessoriesBowlsByBrand(text);
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .accessoryButtons(accessories, "aA/")
                    .row()
                    .button("🔙 Назад", "aA/Чаша")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("*Наличие / Акссесуары / Чаши*\n\nТовары бренда: *" + text + "*");
        }
        else if (text.contains("&")) {
            long id = Long.parseLong(text.split("&")[1].split("\\?")[0]);
            int count;
            Accessory cartAccessory = ACCESSORIES_SERVICE.getAccessoryById(id);
            String fullName = cartAccessory.getBrand() + " " + cartAccessory.getName();
            if (text.contains("?up")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("aA/&" + id + "?" + ++count, "crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "aA/?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartAccessory.getType() + " | " +  fullName.replace("null ", "").trim() + "*" +
                        "\n💵 " + count + " _шт._ `X` " + (cartAccessory.getPrice()) + " _руб._" +
                        " = " + (cartAccessory.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("?down")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("aA/&" + id + "?" + --count, "crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "aA/?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartAccessory.getType() + " | " +  fullName.replace("null ", "").trim() + "*" +
                        "\n💵 " + count + " _шт._ `X` " + (cartAccessory.getPrice()) + " _руб._" +
                        " = " + (cartAccessory.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");

            } else if (text.contains("?crt")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                cartAccessory.setCount(count);
                CART_SERVICE.addToCart(cartAccessory, chat_id);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("⛔ Убрать из корзины", "aA/" + id + "?del")
                        .endRow()
                        .row()
                        .button("🛒 Перейти в корзину", "cКорзина")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "aA/" + cartAccessory.getType())
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + fullName.replace("null ", "").trim() + "*" +
                        "\n💵 *" + cartAccessory.getPrice() + " руб. *" +
                        "\n\n" + cartAccessory.getDescription().trim() +
                        "\n[_](" + cartAccessory.getImg() + ")");
            } else {
                count = 1;
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("aA/&" + id + "?" + "1", "crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "aA/?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartAccessory.getType() + " | " + fullName.replace("null ", "").trim() + "*" +
                        "\n💵 " + count + " _шт._ `X` " + (cartAccessory.getPrice()) + " _руб._" +
                        " = " + (cartAccessory.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            }
        } else if (text.contains("?id")) {
            Accessory idAccessory = ACCESSORIES_SERVICE.getAccessoryById(Long.parseLong(text.replace("?id", "")));
            String fullName = idAccessory.getBrand() + " " + idAccessory.getName();
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 В корзину", "aA/&" + idAccessory.getId() + "?")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "aA/" + idAccessory.getType())
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("🏷 *" + fullName.replace("null ", "").trim() + "*" +
                    "\n💵 *" + idAccessory.getPrice() + " руб. *" +
                    "\n\n" + idAccessory.getDescription().trim() +
                    "\n[_](" + idAccessory.getImg() + ")");

        }
        else if (text.contains("?del")){
            Accessory delAccessory = ACCESSORIES_SERVICE.getAccessoryById(Long.parseLong(text.replace("?del", "")));
            String fullName = delAccessory.getBrand() + " " + delAccessory.getName();
            CART_SERVICE.deleteFromCart(delAccessory, chat_id);
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 В корзину", "aA/&" + delAccessory.getId() + "?")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "aA/" + delAccessory.getType())
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("🏷 *" + fullName.replace("null ", "").trim() + "*" +
                    "\n💵 *" + delAccessory.getPrice() + " руб.*" +
                    "\n\n" + delAccessory.getDescription().trim() +
                    "\n[_](" + delAccessory.getImg() + ")");
        }
        else {
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(allAccessoryTypes, "aA/")
                    .row()
                    .button("🔙 Назад", "m📔 Наличие")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("*Наличие / Акссесуары*\n\nВыберите категорию интересующего товара:");
        }
        return editMessageText.setParseMode("Markdown");
    }

    public synchronized EditMessageText accessoryHandle(String text, long chat_id, long mes_id) {
        text = text.substring(1);
        EditMessageText editMessageText;
        Accessory currAccessory;
        if (allAccessoryTypes.contains(text)) {
            if (text.contains("Чаша")) {
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(allAccessoryBowlBrands, "a")
                        .row()
                        .button("🔙 Назад", "aАкссесуары")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Каталог / Акссесуары / Чаши*\n\nВыберите необходимый бренд:");
            }
            else {
                ArrayList<Accessory> accessories = ACCESSORIES_SERVICE.getAccessoriesByType(text);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .accessoryButtons(accessories, "a")
                        .row()
                        .button("🔙 Назад", "aАкссесуары")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Каталог / Аксесуары*\n\nВыберите необходимый товар:");
            }
        }
        else if (allAccessoryBowlBrands.contains(text)) {
            ArrayList<Accessory> accessories = ACCESSORIES_SERVICE.getAccessoriesBowlsByBrand(text);
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .accessoryButtons(accessories, "a")
                    .row()
                    .button("🔙 Назад", "aЧаша")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("*Каталог / Акссесуары / Чаши*\n\nТовары бренда: *" + text + "*");
        }
        else if (text.contains("&")) {
            Accessory cartAccessory;
            long id = Long.parseLong(text.split("&")[1].split("\\?")[0]);
            int count;
            cartAccessory = ACCESSORIES_SERVICE.getAccessoryById(id);
            String fullName = cartAccessory.getBrand() + " " + cartAccessory.getName();
            if (text.contains("?up")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("a&" + id + "?" + ++count, "crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "a?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartAccessory.getType() + " | " + fullName.replace("null ", "").trim() + "*" +
                        "\n💵 " + count + " _шт._ `X` " + (cartAccessory.getPrice()) + " _руб._" +
                        " = " + (cartAccessory.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            }
            else if (text.contains("?down")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("a&" + id + "?" + --count, "crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "a?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartAccessory.getType() + " | " + fullName.replace("null ", "").trim() + "*" +
                        "\n💵 " + count + " _шт._ `X` " + (cartAccessory.getPrice()) + " _руб._" +
                        " = " + (cartAccessory.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("?crt")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                cartAccessory.setCount(count);
                CART_SERVICE.addToCart(cartAccessory, chat_id);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("⛔ Убрать из корзины", "a" + id + "?del")
                        .endRow()
                        .row()
                        .button("🛒 Перейти в корзину", "cКорзина")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "a" + cartAccessory.getType())
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + fullName.replace("null ", "").trim() + "*" +
                        "\n💵 *" + cartAccessory.getPrice() + " руб. *" +
                        "\n\n" + cartAccessory.getDescription().trim() +
                        "\n[_](" + cartAccessory.getImg() + ")");

            } else {
                count = 1;
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("a&" + id + "?" + "1", "crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "a?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + cartAccessory.getType() + " | " + fullName.replace("null ", "").trim() + "*" +
                        "\n💵 " + count + " _шт._ `X` " + (cartAccessory.getPrice()) + " _руб._" +
                        " = " + (cartAccessory.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            }
        }
        else if (text.contains("?id")) {
            currAccessory = ACCESSORIES_SERVICE.getAccessoryById(Long.parseLong(text.replace("?id", "")));
            String fullName = currAccessory.getBrand() + " " + currAccessory.getName();
            if (currAccessory.isAvailable()) {
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("🛒 В корзину", "a&" + currAccessory.getId() + "?")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "a" + currAccessory.getType())
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + fullName.replace("null ", "").trim() + "*" +
                        "\n💵 *" + currAccessory.getPrice() + " руб. *" +
                        "\n\n" + currAccessory.getDescription().trim() +
                        "\n[_](" + currAccessory.getImg() + ")");
            } else {
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("🔙 Назад", "a" + currAccessory.getType())
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + fullName.replace("null ", "").trim() + "*" + " " +
                        "\n_(нет в наличии)_" +
                        "\n💵 *" + currAccessory.getPrice() + " руб. *" +
                        "\n\n" + currAccessory.getDescription().trim() +
                        "\n[_](" + currAccessory.getImg() + ")");
            }
        }
        else if (text.contains("?del")) {
            Accessory delAccessory = ACCESSORIES_SERVICE.getAccessoryById(Long.parseLong(text.replace("?del", "")));
            CART_SERVICE.deleteFromCart(delAccessory, chat_id);
            String fullName = delAccessory.getBrand() + " " + delAccessory.getName();
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 В корзину", "a&" + delAccessory.getId() + "?")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "a" + delAccessory.getType())
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("🏷 *" + fullName.replace("null ", "").trim() + "*" +
                    "\n💵 *" + delAccessory.getPrice() + " руб.*" +
                    "\n\n" + delAccessory.getDescription().trim() +
                    "\n[_](" + delAccessory.getImg() + ")");

        } else {
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(allAccessoryTypes, "a")
                    .row()
                    .button("🔙 Назад", "m📓 Каталог")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("*Каталог / Акссесуары*\n\nВыберите категорию интересующего товара:");
        }
        return editMessageText.setParseMode("Markdown");
    }

    public synchronized EditMessageText availableTobaccoHandle(String text, long chat_id, long mes_id) {
        text = text.substring(3);
        EditMessageText editMessage;
        if (allAvailableTobaccoFortresses.contains(text)) {
            ArrayList<Tobacco> tobaccos = TOBACCO_SERVICE.getTobaccoByFortress(text, allAvailableTobaccos);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .tobaccoButtons(tobaccos,"tA/", "")
                    .row()
                    .button("🔙 Назад", "tA/Табаки")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Наличие / Табаки*\n\nТабаки крепости: *" + text + "*");
        }
        else if (text.contains("cart")) {
            Tobacco crtTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("cart", "")));
            if (crtTobacco.getRadonejskayaTastes() != null && !crtTobacco.getRadonejskayaTastes().isEmpty()
                    && !crtTobacco.getRadonejskayaTastes().get(0).equals("null") && crtTobacco.getKarlaMarksaTastes() != null
                    && !crtTobacco.getKarlaMarksaTastes().isEmpty() && !crtTobacco.getKarlaMarksaTastes().get(0).equals("null")) {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Ул. Радонежская 1", "tA/" + crtTobacco.getId() + "?RadTastes")
                        .endRow()
                        .row()
                        .button("Ул. Проспект Карла Маркса 196", "tA/" + crtTobacco.getId() + "?KarTastes")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "tA/?id" + crtTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("Для данного табака в наличии есть следующие вкусы:\n\n"
                        + "*ул. Радонежская 1* | `" + crtTobacco.getRadonejskayaTastes().toString().replace("{", "")
                        .replace("}", "") + "`\n\n*Проспект Карла Маркса 196* | `"
                        + crtTobacco.getKarlaMarksaTastes().toString().replace("{", "")
                        .replace("}", "") + "`\n\nВыберите магазин:");
            }
            else if (crtTobacco.getKarlaMarksaTastes() == null || crtTobacco.getKarlaMarksaTastes().isEmpty()
                    || crtTobacco.getKarlaMarksaTastes().get(0).equals("null")) {
                ArrayList<String> allTastes = crtTobacco.getRadonejskayaTastes();
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(allTastes, "tA/&" + crtTobacco.getId() + "?")
                        .row()
                        .button("🔙 Назад", "tA/?id" + crtTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("Данный табак доступен по адресу *Ул. Радонежская 1*.\n\n" +
                        "Выберите интересующий вкус:");
            }
            else {
                ArrayList<String> allTastes = crtTobacco.getKarlaMarksaTastes();
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(allTastes, "tA/&" + crtTobacco.getId() + "?")
                        .row()
                        .button("🔙 Назад", "tA/?id" + crtTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("Данный табак доступен по адресу *Проспект Карла Маркса 196*.\n\n" +
                        "Выберите интересующий вкус:");
            }
        }
        else if (text.contains("&")) {
            Tobacco tasteTobacco;
            long id = Long.parseLong(text.split("&")[1].split("\\?")[0]);
            int count;
            String taste = text.split("&")[1].split("\\?")[1];
            tasteTobacco = TOBACCO_SERVICE.getTobaccoById(id);
            tasteTobacco.setTaste(taste);
            if (text.contains("?up")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[2]);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("tA/&" + id + "?" + taste + "?" + ++count,"crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "t?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + tasteTobacco.getName() + "*  (`" + taste + "`)" +
                        "\n💵 " + count + " _шт._ `X` " + (tasteTobacco.getPrice()) + " _руб._" +
                        " = " + (tasteTobacco.getPrice()*count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            }
            else if (text.contains("?down")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[2]);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("tA/&" + id + "?" + taste + "?" + --count,"crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "t?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + tasteTobacco.getName() + "*  (`" + taste + "`)" +
                        "\n💵 " + count + " _шт._ `X` " + (tasteTobacco.getPrice()) + " _руб._" +
                        " = " + (tasteTobacco.getPrice()*count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            }
            else if (text.contains("?crt")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[2]);
                tasteTobacco.setCount(count);
                CART_SERVICE.addToCart(tasteTobacco, chat_id);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("⛔ Убрать из корзины", "t" + id + "?del")
                        .endRow()
                        .row()
                        .button("🛒 Перейти в корзину", "cКорзина")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "tA/Табаки")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + tasteTobacco.getName() + "*" +
                        "\n💵 *" + tasteTobacco.getPrice() + " руб.*" +
                        "\n\n" + tasteTobacco.getDescription().trim() +
                        "\n[_](" + tasteTobacco.getImg() + ")");
            }
            else {
                count = 1;
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("tA/&" + id + "?" + taste + "?" + count,"crt")
                        .endRow()
                        .row()
                        .button("Добавить в корзину", "")
                        .row()
                        .button("🔙 Назад", "t?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + tasteTobacco.getName() + "*  (`" + taste + "`)" +
                        "\n💵 " + count + " _шт._ `X` " + (tasteTobacco.getPrice()) + " _руб._" +
                        " = " + (tasteTobacco.getPrice()*count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            }
        }
        else if (text.contains("?del")) {
            Tobacco delTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("?del", "")));
            CART_SERVICE.deleteFromCart(delTobacco, chat_id);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 Добавить в корзину", "tA/" + delTobacco.getId() + "crt")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "tA/Табаки")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("🏷 *" + delTobacco.getName() + "*" +
                    "\n💵 *" + delTobacco.getPrice() + " руб.*" +
                    "\n\n" + delTobacco.getDescription().trim() +
                    "\n[_](" + delTobacco.getImg() + ")");
        }
        else if (text.contains("?id")) {
            Tobacco idTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("?id", "")));
            if (idTobacco.isAvailable()) {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("🛒 Добавить в корзину", "tA/" + idTobacco.getId() + "cart")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "tA/Табаки")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + idTobacco.getName() + "*" +
                        "\n💵 *" + idTobacco.getPrice() + " руб.*" +
                        "\n\n" + idTobacco.getDescription().trim() +
                        "\n[_](" + idTobacco.getImg() + ")");
            }
            else {
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
        }
        else if (text.contains("?RadTastes") || text.contains("?KarTastes")) {
            Tobacco addressTobacco;
            if (text.contains("?RadTastes")) {
                addressTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("?RadTastes","")));
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(addressTobacco.getRadonejskayaTastes(), "tA/&" + addressTobacco.getId() + "?")
                        .row()
                        .button("🔙 Назад", "tA/?id" + addressTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*Наличие вкусов по адресу: Радонежская 1*\n\nВыберите вкус табака:");
            }
            else {
                addressTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("?KarTastes","")));
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(addressTobacco.getKarlaMarksaTastes(), "tA/&" + addressTobacco.getId() + "?")
                        .row()
                        .button("🔙 Назад", "tA/?id" + addressTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*Наличие вкусов по адресу: Проспект Карла Маркса 196*\n\nВыберите вкус табака:");
            }
        }
        else {
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(allAvailableTobaccoFortresses, "tA/")
                    .row()
                    .button("🔙 Назад", "m📔 Наличие")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Наличие / Табаки*\n\nВыберите крепость интересующего табака:");
        }
        return editMessage;
    }

    public synchronized EditMessageText tobaccoHandle(String text, long chat_id, long mes_id) {
        text = text.substring(1);
        EditMessageText editMessage;
        if (allTobaccoFortresses.contains(text)) {
            ArrayList<Tobacco> tobaccos = TOBACCO_SERVICE.getTobaccoByFortress(text, allTobaccos);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .tobaccoButtons(tobaccos,"t","")
                    .row()
                    .button("🔙 Назад", "tТабаки")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Каталог / Табаки*\n\nТабаки крепости: *" + text + "*");
        }
        else if (text.contains("cart")) {
            Tobacco crtTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("cart", "")));
            if (crtTobacco.getRadonejskayaTastes() != null && !crtTobacco.getRadonejskayaTastes().isEmpty()
                    && !crtTobacco.getRadonejskayaTastes().get(0).equals("null") && crtTobacco.getKarlaMarksaTastes() != null
                    && !crtTobacco.getKarlaMarksaTastes().isEmpty() && !crtTobacco.getKarlaMarksaTastes().get(0).equals("null")) {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Ул. Радонежская 1", "t" + crtTobacco.getId() + "?RadTastes")
                        .endRow()
                        .row()
                        .button("Ул. Проспект Карла Маркса 196", "t" + crtTobacco.getId() + "?KarTastes")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "t?id" + crtTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("Для данного табака в наличии есть следующие вкусы:\n\n"
                        + "*ул. Радонежская 1* | `" + crtTobacco.getRadonejskayaTastes().toString().replace("[", "")
                        .replace("]", "") + "`\n\n*Проспект Карла Маркса 196* | `"
                        + crtTobacco.getKarlaMarksaTastes().toString().replace("[", "")
                        .replace("]", "") + "`\n\nВыберите магазин:");
            }
            else if (crtTobacco.getKarlaMarksaTastes() == null || crtTobacco.getKarlaMarksaTastes().isEmpty()
                    || crtTobacco.getKarlaMarksaTastes().get(0).equals("null")) {
                ArrayList<String> allTastes = crtTobacco.getRadonejskayaTastes();
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(allTastes, "t&" + crtTobacco.getId() + "?")
                        .row()
                        .button("🔙 Назад", "t?id" + crtTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("Данный табак доступен по адресу *Ул. Радонежская 1*.\n\n" +
                        "Выберите интересующий вкус:");
            }
            else {
                ArrayList<String> allTastes = crtTobacco.getKarlaMarksaTastes();
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(allTastes, "t&" + crtTobacco.getId() + "?")
                        .row()
                        .button("🔙 Назад", "t?id" + crtTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("Данный табак доступен по адресу *Проспект Карла Маркса 196*.\n\n" +
                        "Выберите интересующий вкус:");
            }
        }
        else if (text.contains("&")) {
            Tobacco tasteTobacco;
            long id = Long.parseLong(text.split("&")[1].split("\\?")[0]);
            int count;
            String taste = text.split("&")[1].split("\\?")[1];
            tasteTobacco = TOBACCO_SERVICE.getTobaccoById(id);
            tasteTobacco.setTaste(taste);
            if (text.contains("?up")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[2]);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("t&" + id + "?" + taste + "?" + ++count,"crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "t?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + tasteTobacco.getName() + "*  (`" + taste + "`)" +
                        "\n💵 " + count + " _шт._ `X` " + (tasteTobacco.getPrice()) + " _руб._" +
                        " = " + (tasteTobacco.getPrice()*count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            }
            else if (text.contains("?down")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[2]);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("t&" + id + "?" + taste + "?" + --count, "crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "t?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + tasteTobacco.getName() + "*  (`" + taste + "`)" +
                        "\n💵 " + count + " _шт._ `X` " + (tasteTobacco.getPrice()) + " _руб._" +
                        " = " + (tasteTobacco.getPrice()*count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            }
            else if (text.contains("?crt")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[2]);
                tasteTobacco.setCount(count);
                CART_SERVICE.addToCart(tasteTobacco, chat_id);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("⛔ Убрать из корзины", "t" + id + "?del")
                        .endRow()
                        .row()
                        .button("🛒 Перейти в корзину", "cКорзина")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "tТабаки")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + tasteTobacco.getName() + "*" +
                        "\n💵 *" + tasteTobacco.getPrice() + " руб.*" +
                        "\n\n" + tasteTobacco.getDescription().trim() +
                        "\n[_](" + tasteTobacco.getImg() + ")");
            }
            else {
                count = 1;
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("t&" + id + "?" + taste + "?" + count, "crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "t?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + tasteTobacco.getName() + "*  (`" + taste + "`)" +
                        "\n💵 " + count + " _шт._ `X` " + (tasteTobacco.getPrice()) + " _руб._" +
                        " = " + (tasteTobacco.getPrice()*count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            }
        }
        else if (text.contains("?del")) {
            Tobacco delTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("?del", "")));
            CART_SERVICE.deleteFromCart(delTobacco, chat_id);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 Добавить в корзину", "t" + delTobacco.getId() + "crt")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "tnal")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("🏷 *" + delTobacco.getName() + "*" +
                    "\n💵 *" + delTobacco.getPrice() + " руб.*" +
                    "\n\n" + delTobacco.getDescription().trim() +
                    "\n[_](" + delTobacco.getImg() + ")");
        }
        else if (text.contains("?id")) {
            Tobacco idTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("?id", "")));
            if (idTobacco.isAvailable()) {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("🛒 Добавить в корзину", "t" + idTobacco.getId() + "cart")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "tТабаки")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("🏷 *" + idTobacco.getName() + "*" +
                        "\n💵 *" + idTobacco.getPrice() + " руб.*" +
                        "\n\n" + idTobacco.getDescription().trim() +
                        "\n[_](" + idTobacco.getImg() + ")");
            }
            else {
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
        }
        else if (text.contains("?RadTastes") || text.contains("?KarTastes")) {
            Tobacco addressTobacco;
            if (text.contains("?RadTastes")) {
                addressTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("?RadTastes","")));
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(addressTobacco.getRadonejskayaTastes(), "t&" + addressTobacco.getId() + "?")
                        .row()
                        .button("🔙 Назад", "t?id" + addressTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*Наличие вкусов по адресу: Радонежская 1*\n\nВыберите вкус табака:");
            }
            else {
                addressTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("?KarTastes","")));
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(addressTobacco.getKarlaMarksaTastes(), "t&" + addressTobacco.getId() + "?")
                        .row()
                        .button("🔙 Назад", "t?id" + addressTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*Наличие вкусов по адресу: Проспект Карла Маркса 196*\n\nВыберите вкус табака:");
            }
        }
        else {
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(allTobaccoFortresses, "t")
                    .row()
                    .button("🔙 Назад", "m📓 Каталог")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Каталог / Табаки*\n\nВыберите крепость интересующего табака:");
        }
        return editMessage;
    }

    public synchronized EditMessageText availableVaporizerHandle(String text, long chat_id, long mes_id) {
        text = text.substring(3);
        EditMessageText editMessageText;
        if (text.contains("&")) {
            Vaporizer vaporizer;
            long id = Long.parseLong(text.split("&")[1].split("\\?")[0]);
            int count;
            vaporizer = VAPORIZER_SERVICE.getVaporizerById(id);
            String fullName = "Эл. испаритель | " + vaporizer.getName();
            if (text.contains("?up")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("vA/&" + id + "?" + ++count, "crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "vA/?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + fullName + "*" +
                        "\n💵 " + count + " _шт._ `X` " + (vaporizer.getPrice()) + " _руб._" +
                        " = " + (vaporizer.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("?down")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("vA/&" + id + "?" + --count, "crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "vA/?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + fullName + "*" +
                        "\n💵 " + count + " _шт._ `X` " + (vaporizer.getPrice()) + " _руб._" +
                        " = " + (vaporizer.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("?crt")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                vaporizer.setCount(count);
                CART_SERVICE.addToCart(vaporizer, chat_id);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("⛔ Убрать из корзины", "vA/" + id + "?del")
                        .endRow()
                        .row()
                        .button("🛒 Перейти в корзину", "cКорзина")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "vA/Вейпы")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + vaporizer.getName() + "*" +
                        "\n💵 *" + vaporizer.getPrice() + " руб. *" +
                        "\n\n" + vaporizer.getDescription().trim() +
                        "\n[_](" + vaporizer.getImg() + ")");

            } else {
                count = 1;
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("vA/&" + id + "?" + "1", "crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "vA/?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + fullName + "*" +
                        "\n💵 " + count + " _шт._ `X` " + (vaporizer.getPrice()) + " _руб._" +
                        " = " + (vaporizer.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            }

        } else if (text.contains("?id")) {
            Vaporizer idVaporizer = VAPORIZER_SERVICE.getVaporizerById(Long.parseLong(text.replace("?id", "")));
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 В корзину", "vA/&" + idVaporizer.getId() + "?")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "vA/Вейпы")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("🏷 *" + idVaporizer.getName() + "*" +
                    "\n💵 *" + idVaporizer.getPrice() + " руб. *" +
                    "\n\n" + idVaporizer.getDescription().trim() +
                    "\n[_](" + idVaporizer.getImg() + ")");

        } else if (text.contains("?del")) {
            Vaporizer delVaporizer = VAPORIZER_SERVICE.getVaporizerById(Long.parseLong(text.replace("?del", "")));
            CART_SERVICE.deleteFromCart(delVaporizer, chat_id);
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 В корзину", "vA/&" + delVaporizer.getId() + "?")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "vA/Вейпы")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("🏷 *" + delVaporizer.getName() + "*" +
                    "\n💵 *" + delVaporizer.getPrice() + " руб.*" +
                    "\n\n" + delVaporizer.getDescription().trim() +
                    "\n[_](" + delVaporizer.getImg() + ")");
        } else {
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .vaporizerButtons(allAvailableVaporizers)
                    .row()
                    .button("🔙 Назад", "m📔 Наличие")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("*Наличие / Электронные испарители*\n\nВыберите интересующий испаритель:");
        }

        return editMessageText.setParseMode("Markdown");

    }

    public synchronized EditMessageText vaporizerHandle(String text, long chat_id, long mes_id) {
        text = text.substring(1);
        EditMessageText editMessageText;
        if (text.contains("&")) {
            Vaporizer vaporizer;
            long id = Long.parseLong(text.split("&")[1].split("\\?")[0]);
            int count;
            vaporizer = VAPORIZER_SERVICE.getVaporizerById(id);
            String fullName = "Эл. испаритель | " + vaporizer.getName();
            if (text.contains("?up")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("v&" + id + "?" + ++count, "crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "v?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + fullName + "*" +
                        "\n💵 " + count + " _шт._ `X` " + (vaporizer.getPrice()) + " _руб._" +
                        " = " + (vaporizer.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");

            } else if (text.contains("?down")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("v&" + id + "?" + --count, "crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "v?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + fullName + "*" +
                        "\n💵 " + count + " _шт._ `X` " + (vaporizer.getPrice()) + " _руб._" +
                        " = " + (vaporizer.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            } else if (text.contains("?crt")) {
                count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                vaporizer.setCount(count);
                CART_SERVICE.addToCart(vaporizer, chat_id);
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("⛔ Убрать из корзины", "v" + id + "?del")
                        .endRow()
                        .row()
                        .button("🛒 Перейти в корзину", "cКорзина")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "vВейпы")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + vaporizer.getName() + "*" +
                        "\n💵 *" + vaporizer.getPrice() + " руб. *" +
                        "\n\n" + vaporizer.getDescription().trim() +
                        "\n[_](" + vaporizer.getImg() + ")");
            } else {
                count = 1;
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .countButtons("v&" + id + "?" + "1","crt")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "v?id" + id)
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + fullName + "*" +
                        "\n💵 " + count + " _шт._ `X` " + (vaporizer.getPrice()) + " _руб._" +
                        " = " + (vaporizer.getPrice() * count) + " _руб._" +
                        "\n\nВыберите необходимое количество:");
            }
        }
        else if (text.contains("?id")) {
            Vaporizer idVaporizer = VAPORIZER_SERVICE.getVaporizerById(Long.parseLong(text.replace("?id", "")));
            if (idVaporizer.isAvailable()) {
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("🛒 В корзину", "v&" + idVaporizer.getId() + "?")
                        .endRow()
                        .row()
                        .button("🔙 Назад", "vВейпы")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + idVaporizer.getName() + "*" +
                        "\n💵 *" + idVaporizer.getPrice() + " руб.*" +
                        "\n\n" + idVaporizer.getDescription().trim() +
                        "\n[_](" + idVaporizer.getImg() + ")");
            } else {
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("🔙 Назад", "vВейпы")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("🏷 *" + idVaporizer.getName() + "*" + " \n_(нет в наличии)_" +
                        "\n💵 *" + idVaporizer.getPrice() + " руб.*" +
                        "\n\n" + idVaporizer.getDescription().trim() +
                        "\n[_](" + idVaporizer.getImg() + ")");
            }
        } else if (text.contains("?del")) {
            Vaporizer delVaporizer = VAPORIZER_SERVICE.getVaporizerById(Long.parseLong(text.replace("?del", "")));
            CART_SERVICE.deleteFromCart(delVaporizer, chat_id);
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("🛒 В корзину", "v&" + delVaporizer.getId() + "?")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "vВейпы")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("🏷 *" + delVaporizer.getName() + "*" +
                    "\n💵 *" + delVaporizer.getPrice() + " руб.*" +
                    "\n\n" + delVaporizer.getDescription().trim() +
                    "\n[_](" + delVaporizer.getImg() + ")");
        } else {
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .vaporizerButtons(allVaporizers)
                    .row()
                    .button("🔙 Назад", "m📓 Каталог")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("*Каталог / Электронные испарители*\n\nВыберите интересующий испаритель:");
        }
        return editMessageText.setParseMode("Markdown");
    }

    public synchronized EditMessageText cartHandle(String text, long chat_id, long mes_id) {
        text = text.substring(1);
        EditMessageText editMessage;
        if (text.equals("clear")) {
            CART_SERVICE.clearUserCart(chat_id);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("Вернуться к покупкам", "m📓 Каталог")
                    .endRow()
                    .row()
                    .button("🔙 Назад", "cКорзина")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("Ваша корзина успешно очищена!\nВоспользуйтесь каталогом чтобы найти всё что нужно");
        }
        else if (text.contains("&")) {
            ArrayList<Product> cart = CART_SERVICE.getUserCart(chat_id).getCart();
            int position;
            if (text.contains("count")) {
                position = Integer.parseInt(text.split("&")[1].split("\\?")[0]);
                Product product = cart.get(position-1);
                int count;
                if (text.contains("?up")) {
                    count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                    cart.get(position-1).setCount(count+1);
                    editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                            .row()
                            .countButtons("ccount&" + position + "?" + ++count,"accept")
                            .endRow()
                            .rebuild(mes_id);
                    editMessage.setText(CART_SERVICE.getUserCart(chat_id).toStringEdit(position) +
                            "\nВыберите необходимое количество:");
                }
                else if (text.contains("?down")) {
                    count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                    cart.get(position-1).setCount(count-1);
                    editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                            .row()
                            .countButtons("ccount&" + position + "?" + --count,"accept")
                            .endRow()
                            .rebuild(mes_id);
                    editMessage.setText(CART_SERVICE.getUserCart(chat_id).toStringEdit(position) +
                            "\nВыберите необходимое количество:");
                }
                else if (text.contains("accept")) {
                    count = Integer.parseInt(text.split("&")[1].split("\\?")[1]);
                    cart.get(position-1).setCount(count);
                    editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                            .row()
                            .positionButtons("c&" + position, cart.size())
                            .endRow()
                            .row()
                            .button("📝 Изменить количество", "ccount&" + position + "?")
                            .endRow()
                            .row()
                            .button("⛔ Убрать из корзины", "calert&" + position)
                            .endRow()
                            .row()
                            .button("🔙 Назад", "cКорзина")
                            .endRow()
                            .rebuild(mes_id);
                    editMessage.setText(CART_SERVICE.getUserCart(chat_id).toStringEdit(position) +
                            "\nПожалуйста, выберите номер позиции, которую бы Вы хотели изменить:");
                }
                else {
                    count = product.getCount();
                    editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                            .row()
                            .countButtons("ccount&" + position + "?" + count,"accept")
                            .endRow()
                            .rebuild(mes_id);
                    editMessage.setText(CART_SERVICE.getUserCart(chat_id).toStringEdit(position) +
                            "\nВыберите необходимое количество:");
                }
            }
            else {
                if (text.contains("?up")) {
                    position = Integer.parseInt(text.split("&")[1].split("\\?")[0]);
                    editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                            .row()
                            .positionButtons("c&" + ++position, cart.size())
                            .endRow()
                            .row()
                            .button("📝 Изменить количество", "ccount&" + position + "?")
                            .endRow()
                            .row()
                            .button("⛔ Убрать из корзины", "calert&" + position)
                            .endRow()
                            .row()
                            .button("🔙 Назад", "cКорзина")
                            .endRow()
                            .rebuild(mes_id);
                    editMessage.setText(CART_SERVICE.getUserCart(chat_id).toStringEdit(position) +
                            "\nПожалуйста, выберите номер позиции, которую бы Вы хотели изменить:");
                }
                else if (text.contains("?down")) {
                    position = Integer.parseInt(text.split("&")[1].split("\\?")[0]);
                    if (position == 1) {
                        editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                                .positionButtons("c&" + --position, cart.size())
                                .row()
                                .button("⛔ Очистить всю корзину", "cclear")
                                .endRow()
                                .row()
                                .button("🔙 Назад", "cКорзина")
                                .endRow()
                                .rebuild(mes_id);
                        editMessage.setText(CART_SERVICE.getUserCart(chat_id).toString() +
                                "\nПожалуйста, выберите номер позиции, которую бы Вы хотели изменить:");
                    }
                    else {
                        editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                                .row()
                                .positionButtons("c&" + --position, cart.size())
                                .endRow()
                                .row()
                                .button("📝 Изменить количество", "ccount&" + position + "?")
                                .endRow()
                                .row()
                                .button("⛔ Убрать из корзины", "calert&" + position)
                                .endRow()
                                .row()
                                .button("🔙 Назад", "cКорзина")
                                .endRow()
                                .rebuild(mes_id);
                        editMessage.setText(CART_SERVICE.getUserCart(chat_id).toStringEdit(position) +
                                "\nПожалуйста, выберите номер позиции, которую бы Вы хотели изменить:");
                    }
                }
                else if (text.contains("alert")) {
                    position = Integer.parseInt(text.split("&")[1]);
                    Product product = CART_SERVICE.getUserCart(chat_id).getCart().get(position - 1);
                    if (text.contains("?del")) {
                        CART_SERVICE.deleteFromCart(product, chat_id);
                        position = 0;
                        if (cart.size() == 0) {
                            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                                    .row()
                                    .button("📓 Каталог", "m📓 Каталог")
                                    .endRow()
                                    .rebuild(mes_id);
                            editMessage.setText(CART_SERVICE.getUserCart(chat_id).toString());
                        } else {
                            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                                    .positionButtons("c&" + position, cart.size())
                                    .row()
                                    .button("⛔ Очистить всю корзину", "cclear")
                                    .endRow()
                                    .row()
                                    .button("🔙 Назад", "cКорзина")
                                    .endRow()
                                    .rebuild(mes_id);
                            editMessage.setText(CART_SERVICE.getUserCart(chat_id).toString() +
                                    "\nПожалуйста, выберите номер позиции, которую бы Вы хотели изменить:");
                        }
                    }
                    else {
                        String brand = product.getBrand();
                        if (brand == null)
                            brand = "";
                        editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                                .row()
                                .button("Удалить", "calert?del&" + position)
                                .endRow()
                                .row()
                                .button("🔙 Назад", "c&edit")
                                .endRow()
                                .rebuild(mes_id);
                        editMessage.setText("Вы действительно хотите удалить *" + brand + " "
                                + product.getName() + "* из корзины?");
                    }
                }
                else {
                    position = 0;
                    editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                            .positionButtons("c&" + position, cart.size())
                            .row()
                            .button("⛔ Очистить всю корзину", "cclear")
                            .endRow()
                            .row()
                            .button("🔙 Назад", "cКорзина")
                            .endRow()
                            .rebuild(mes_id);
                    editMessage.setText(CART_SERVICE.getUserCart(chat_id).toString() +
                            "\nПожалуйста, выберите номер позиции, которую бы Вы хотели изменить:");
                }
            }
        }
        else {
            if (CART_SERVICE.getUserCart(chat_id).getCart().size() == 0) {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("📓 Каталог", "m📓 Каталог")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText(CART_SERVICE.getUserCart(chat_id).toString());
            }
            else {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("\uD83D\uDCE6 Оформить заказ", "cmakeOrd")
                        .endRow()
                        .row()
                        .button("📝 Изменить корзину", "c&edit")
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

    public synchronized void sendMailToEmployee(Order order) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.mail.ru");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("grizzly_shop_bot@mail.ru","oTT*y31poUpT");
                    }
                });
        try {
            javax.mail.Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("grizzly_shop_bot@mail.ru"));
            message.setRecipients(javax.mail.Message.RecipientType.TO,
                    InternetAddress.parse("kozikov.dmitrii@mail.ru"));
            message.setSubject("Новый заказ");
            message.setContent(order.toStringHTML());
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        long dmt_chat_id = 757517710;
        long den_chat_id = 416481326;
        SendMessage sendMessage1 = new SendMessage().setChatId(dmt_chat_id);
        SendMessage sendMessage2 = new SendMessage().setChatId(den_chat_id);
        sendMessage1.setText(order.toString());
        sendMessage1.setParseMode("Markdown");
        sendMessage2.setText(order.toString());
        sendMessage2.setParseMode("Markdown");
        try {
            execute(sendMessage1);
            execute(sendMessage2);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateHandle(Update update) throws TelegramApiException {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message inMessage = update.getMessage();
            long chat_id = inMessage.getChatId();
            SendMessage outMessage = new SendMessage().setChatId(chat_id);
            String text = update.getMessage().getText();
            System.out.println(new Date() + ": " + inMessage.getFrom().getFirstName() + " " +
                    inMessage.getFrom().getLastName() + " (" + inMessage.getFrom().getUserName() +
                    "): " + text);
            execute(messageStarter(text, outMessage).setParseMode("Markdown"));
        }
        else if (update.hasCallbackQuery()) {
            Message inMessage = update.getCallbackQuery().getMessage();
            long chat_id = inMessage.getChatId();
            long mes_id = inMessage.getMessageId();
            String text = update.getCallbackQuery().getData();
            System.out.println(new Date() + ": " + update.getCallbackQuery().getFrom().getFirstName() + " " +
                    update.getCallbackQuery().getFrom().getLastName() + " (" + update.getCallbackQuery().getFrom().getUserName() +
                    "): " + text);
            if (text.contains("?crt"))
                execute(answerCallbackQuery(update.getCallbackQuery().getId(), "Товар успешно добавлен в корзину!"));
            if (text.contains("?del"))
                execute(answerCallbackQuery(update.getCallbackQuery().getId(), "Товар успешно удалён из корзины!"));
            if (text.startsWith("c")) {
                if (text.contains("makeOrd")) {
                    SendMessage outMessage = new SendMessage().setChatId(chat_id);
                    execute(messageStarter("\uD83D\uDCE6 Оформить заказ", outMessage));
                }
                else
                    execute(cartHandle(text, chat_id, mes_id).setParseMode("Markdown"));
            }
            else if (text.startsWith("h")) {
                if (text.contains("hA/"))
                    execute(availableHookahHandle(text, chat_id, mes_id).setParseMode("Markdown"));
                else
                    execute(hookahHandle(text, chat_id, mes_id).setParseMode("Markdown"));
            }
            else if (text.startsWith("t")) {
                if (text.contains("tA/"))
                    execute(availableTobaccoHandle(text, chat_id, mes_id).setParseMode("Markdown"));
                else
                    execute(tobaccoHandle(text, chat_id, mes_id).setParseMode("Markdown"));
            }
            else if (text.startsWith("a")) {
                if (text.contains("aA/"))
                    execute(availableAccessoryHandle(text, chat_id, mes_id).setParseMode("Markdown"));
                else
                    execute(accessoryHandle(text, chat_id, mes_id).setParseMode("Markdown"));
            }
            else if (text.startsWith("u")) {
                if (text.contains("uA/"))
                    execute(availableCharcoalHandle(text, chat_id, mes_id).setParseMode("Markdown"));
                else
                    execute(charcoalHandle(text, chat_id, mes_id).setParseMode("Markdown"));
            }
            else if (text.startsWith("v")) {
                if (text.contains("vA/"))
                    execute(availableVaporizerHandle(text, chat_id, mes_id).setParseMode("Markdown"));
                else
                    execute(vaporizerHandle(text, chat_id, mes_id).setParseMode("Markdown"));
            }
            else if (text.startsWith("m")) {
                execute(messageHandle(text, chat_id, mes_id).setParseMode("Markdown"));
            }
        }
        else {
            long chat_id = update.getMessage().getChatId();
            if (update.getMessage().hasContact()) {
                if (CART_SERVICE.getUserCart(chat_id).getCart().size() != 0) {
                    ORDER_SERVICE.getOrder(chat_id).setCustomerPhone(update.getMessage().getContact().getPhoneNumber());
                    ORDER_SERVICE.getOrder(chat_id).setCustomerName(update.getMessage().getContact().getFirstName());
                    ORDER_SERVICE.getOrder(chat_id).setCustomerSurname(update.getMessage().getContact().getLastName());
                    SendMessage sendMessage = new SendMessage().setChatId(chat_id);
                    execute(messageStarter("Поделились номером телефона", sendMessage));
                    Order order = ORDER_SERVICE.getOrder(chat_id);
                    order.setCustomerCart(CART_SERVICE.getUserCart(chat_id).toStringOrder());
                    order.setCustomerCartHTML(CART_SERVICE.getUserCart(chat_id).toStringOrderHTML());
                    sendMailToEmployee(order);
                    CART_SERVICE.clearUserCart(chat_id);
                }
            }
        }
    }

    public String getBotUsername() {
        return "Grizzly_Shop_Bot";
    }

    public String getBotToken() {
        return BOT_TOKEN;
    }
}
