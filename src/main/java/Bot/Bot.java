package Bot;

import Bot.Keyboards.InlineKeyboardMarkupBuilder;
import Bot.Keyboards.ReplyKeyboardMarkupBuilder;
import Service.CartService;
import Service.HookahService;
import Service.TobaccoService;
import Models.Products.Hookah;
import Models.Products.Tobacco;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
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
    private static final ArrayList<String> allHookahBrands = HOOKAH_SERVICE.getAllBrandsList();
    private static final ArrayList<String> availableBrands = HOOKAH_SERVICE.getAvailableBrandsList();
    private static final ArrayList<String> allTobaccoFortresses = TOBACCO_SERVICE.getAllFortresses();
    private static final ArrayList<String> availableFortressesRad = TOBACCO_SERVICE.getAvailableFortressesByStreet("Rad");
    private static final ArrayList<String> availableFortressesKar = TOBACCO_SERVICE.getAvailableFortressesByStreet("Kar");


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
                        .button("Помощь", "mПомощь")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Каталог GRIZZLY SHOP*\nВыберите категорию интересующего товара:");
                return editMessageText;
            case "Помощь":
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Табаки", "mТабаки")
                        .button("Кальяны", "mКальяны")
                        .endRow()
                        .row()
                        .button("Акссесуары", "mАкссесуары")
                        .button("Уголь", "mУголь")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Каталог GRIZZLY SHOP*\n\nДля совершения заказа необходимо:\n-Выбрать интересующий товар из каталога" +
                        "\n-Добавить его в корзину\n-Оформить заказ, сформировав необходимые данные по доставке");
                return editMessageText;
            case "Наличие":
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Табаки", "mnalT")
                        .button("Кальяны", "hnal")
                        .endRow()
                        .row()
                        .button("Акссесуары", "anal")
                        .button("Уголь", "unal")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Наличие GRIZZLY SHOP*\nВыберите категорию интересующего товара:");
                return editMessageText;
            case "nalT":
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Ул. Радонежская 1", "tnalRad")
                        .endRow()
                        .row()
                        .button("Ул. Проспект Карла Маркса 196", "tnalKar")
                        .endRow()
                        .row()
                        .button("Назад", "mНаличие")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Наличие / Табаки*\nТабаки будут сформированы по наличию вкусов в " +
                        "выбранном магазине.\nВыберите магазин:");
                return editMessageText;
            case "Кальяны":
                return hookahHandle(text, chat_id, mes_id);
            case "Табаки":
                return tobaccoHandle(text, chat_id, mes_id);
            case "Корзина":
                return cartHandle(text, chat_id, mes_id);
        }
        return InlineKeyboardMarkupBuilder.create(chat_id)
                .rebuild(mes_id).setText("*Данный функционал находится в разработке*");
    }

    public SendMessage messageStarter(String text, SendMessage sendMessage) {
        long chat_id = Long.parseLong(sendMessage.getChatId());
        switch (text) {
            case "Назад":
            case "Главное меню":
            case "/start":
                sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                        .setText("*Добро пожаловать в GRIZZLY SHOP!*" +
                                "\nУ нас вы найдёте большой ассортимент кальянной " +
                                "продукции, включая всевозможные аксесуары и огромный выбор табака!" +
                                "\n\nИспользуйте кнопки на клавиатуре снизу для навигации")
                        .row()
                        .button("Каталог")
                        .button("Наличие")
                        .endRow()
                        .row()
                        .button("Корзина")
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
                        .button("Помощь", "mПомощь")
                        .endRow()
                        .build();
                break;
            case "Наличие":
                sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .setText("*Наличие GRIZZLY SHOP*\nВыберите категорию интересующего товара:")
                        .row()
                        .button("Табаки", "mnalT")
                        .button("Кальяны", "hnal")
                        .endRow()
                        .row()
                        .button("Акссесуары", "anal")
                        .button("Уголь", "unal")
                        .endRow()
                        .build();
                break;
            case "Корзина":
                sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .setText(CART_SERVICE.getUserCart(chat_id).toString())
                        .row()
                        .button("Оформить заказ", "cОформить заказ")
                        .endRow()
                        .row()
                        .button("Очистить корзину", "cОчистить корзину")
                        .endRow()
                        .build();
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

    public EditMessageText hookahHandle(String text, long chat_id, long mes_id) {
        text = text.substring(1);
        EditMessageText editMessage;
        Hookah currHookah;

        if (allHookahBrands.contains(text)) {
            ArrayList<Hookah> hookahs = HOOKAH_SERVICE.getHookahsByBrand(text);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .hookahButtons(hookahs)
                    .row()
                    .button("Назад", "hКальяны")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("Товары бренда " + text + ": ");
        }
        else if (text.contains("crt")) {
            currHookah = HOOKAH_SERVICE.getHookahById(Long.parseLong(text.replace("crt", "")));
            CART_SERVICE.getUserCart(chat_id).getCart().add(currHookah);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("Назад", "hnal")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("Товар: " + currHookah.getBrand() + " " + currHookah.getName() + "\nУспешно добавлен в корзину");
        }
        else if (text.contains("id")) {
            currHookah = HOOKAH_SERVICE.getHookahById(Long.parseLong(text.replace("id", "")));
            if (currHookah.isAvailable()) {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("В корзину", "h" + currHookah.getId() + "crt")
                        .endRow()
                        .row()
                        .button("Назад", "hnal")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*" + currHookah.getBrand() + "* | " + currHookah.getName() +
                        "\nЦена: " + currHookah.getPrice() + " руб." +
                        "\n\n" + currHookah.getDescription().trim() +
                        "\n[_](" + currHookah.getImg() + ")");
            }
            else {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Назад", "hКальяны")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*" + currHookah.getBrand() + "* | " + currHookah.getName() + " \n_(нет в наличии)_" +
                        "\nЦена: " + currHookah.getPrice() + " руб." +
                        "\n\n" + currHookah.getDescription().trim() +
                        "\n[_](" + currHookah.getImg() + ")");
            }
        }
        else if (text.contains("nal")) {
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(availableBrands, "havl")
                    .row()
                    .button("Назад", "mНаличие")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Наличие / Кальяны*\nВыберите бренд кальяна:");
        }
        else if (text.contains("avl")) {
            ArrayList<Hookah> hookahs = HOOKAH_SERVICE.getAvailableHookahsByBrand(text.replace("avl",""));
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .hookahButtons(hookahs)
                    .row()
                    .button("Назад", "hnal")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Наличие / Кальяны*\nТовары бренда " + text + ": ");
        }
        else {
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(allHookahBrands, "h")
                    .row()
                    .button("Назад", "mКаталог")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Каталог / Кальяны*\nВыберите бренд кальяна:");
        }
        return editMessage.setParseMode("Markdown");
    }

    public EditMessageText tobaccoHandle(String text, long chat_id, long mes_id) {
        text = text.substring(1);
        EditMessageText editMessageText;
        Tobacco currTobacco;

        if (allTobaccoFortresses.contains(text)) {
            ArrayList<Tobacco> tobaccos = TOBACCO_SERVICE.getTobaccoByFortress(text);
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .tobaccoButtons(tobaccos, "")
                    .row()
                    .button("Назад", "tТабаки")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("*Каталог / Табаки*\nТабаки крепости: " + text);
        }
        else if (text.contains("crt")) {
            if (text.contains("RRR")) {
                currTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("crtRRR", "")));
                ArrayList<String> allTastes = currTobacco.getRadonejskayaTastes();
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(allTastes, "t" + currTobacco.getId() + "&")
                        .row()
                        .button("Назад", "tid" + currTobacco.getId() + "RRR")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("Выберите интересующий вкус:");
            }
            else if (text.contains("KKK")) {
                currTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("crtKKK", "")));
                ArrayList<String> allTastes = currTobacco.getKarlaMarksaTastes();
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(allTastes, "t" + currTobacco.getId() + "&")
                        .row()
                        .button("Назад", "tid" + currTobacco.getId() + "KKK")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("Выберите интересующий вкус:");
            }
            else {
                currTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("crt", "")));
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Ул. Радонежская 1", "t" + currTobacco.getId() + "crtRRR")
                        .endRow()
                        .row()
                        .button("Ул. Проспект Карла Маркса 196", "t" + currTobacco.getId() + "crtKKK")
                        .endRow()
                        .row()
                        .button("Назад", "tid" + currTobacco.getId())
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("Для данного табака в наличии есть следующие вкусы:\n\n"
                        + "*ул. Радонежская 1* | `" + currTobacco.getRadonejskayaTastes().toString().replace("{","")
                        .replace("}","") + "`\n\n*Проспект Карла Маркса 196* | `"
                        + currTobacco.getKarlaMarksaTastes().toString().replace("{","")
                        .replace("}","") + "`\nВыберите магазин:");
            }
        }
        else if (text.contains("&")) {
            String[] arr = text.split("&");
            currTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(arr[0]));
            currTobacco.setTaste(arr[1]);
            CART_SERVICE.getUserCart(chat_id).getCart().add(currTobacco);
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("Назад", "tТабаки")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("Товар: " + currTobacco.getName() + " (" + currTobacco.getTaste() + ") был успешно добавлен в корзину!");
        }
        else if (text.contains("id")) {
            if (text.contains("RRR")) {
                Tobacco t = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("id", "").replace("RRR", "")));
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("В корзину", "t" + t.getId() + "crtRRR")
                        .endRow()
                        .row()
                        .button("Назад", "tavlbRad")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*" + t.getName() + "*" +
                        "\nЦена: " + t.getPrice() + " руб." +
                        "\n\n" + t.getDescription().trim() +
                        "\n[_](" + t.getImg() + ")");
            }
            else if (text.contains("KKK")) {
                Tobacco t = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("id", "").replace("KKK", "")));
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("В корзину", "t" + t.getId() + "crtKKK")
                        .endRow()
                        .row()
                        .button("Назад", "tavlbKar")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*" + t.getName() + "*" +
                        "\nЦена: " + t.getPrice() + " руб." +
                        "\n\n" + t.getDescription().trim() +
                        "\n[_](" + t.getImg() + ")");
            }
            else {
                Tobacco t = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("id", "")));
                if (t.isAvailable()) {
                    editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                            .row()
                            .button("В корзину", "t" + t.getId() + "crt")
                            .endRow()
                            .row()
                            .button("Назад", "tТабаки")
                            .endRow()
                            .rebuild(mes_id);
                    editMessageText.setText("*" + t.getName() + "*" +
                            "\nЦена: " + t.getPrice() + " руб." +
                            "\n\n" + t.getDescription().trim() +
                            "\n[_](" + t.getImg() + ")");
                }
                else {
                    editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                            .row()
                            .button("Назад", "tТабаки")
                            .endRow()
                            .rebuild(mes_id);
                    editMessageText.setText("*" + t.getName() + "* \n_(нет в наличии)_" +
                            "\n\nЦена: " + t.getPrice() + " руб." +
                            "\n\n" + t.getDescription().trim() +
                            "\n[_](" + t.getImg() + ")");
                }
            }
        }
        else if (text.contains("nal")) {
            if (text.contains("Rad")) {
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(availableFortressesRad, "tavlbRad")
                        .row()
                        .button("Назад", "mnalT")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Наличие / Табаки / Радонежская 1*\nВыберите крепость табака:");
            }
            else {
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(availableFortressesKar, "tavlbKar")
                        .row()
                        .button("Назад", "mnalT")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Наличие / Табаки / Проспект Карла Маркса 196*\nВыберите крепость табака:");
            }
        }
        else if (text.contains("avlb")) {
            ArrayList<Tobacco> avlTobacco;
            if (text.contains("Rad")) {
                avlTobacco = TOBACCO_SERVICE.getAvailableTobaccoByFortress(text.replace("avlbRad", ""), "Rad");
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .tobaccoButtons(avlTobacco, "RRR")
                        .row()
                        .button("Назад", "tnalRad")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Наличие / Табаки / Радонежская 1*\nТабаки крепости: " + text);
            }
            else {
                avlTobacco =  TOBACCO_SERVICE.getAvailableTobaccoByFortress(text.replace("avlbKar", ""), "Kar");
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .tobaccoButtons(avlTobacco, "KKK")
                        .row()
                        .button("Назад", "tnalKar")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Наличие / Табаки / Проспект Карла Маркса 196*\nТабаки крепости: " + text);
            }
        }
        else {
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(allTobaccoFortresses, "t")
                    .row()
                    .button("Назад", "mКаталог")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("*Каталог / Табаки*\nВыберите крепость табака:");
        }
        return editMessageText;
    }

    public EditMessageText cartHandle(String text, long chat_id, long mes_id) {
        text = text.substring(1);
        EditMessageText editMessageText;
        if (text.equals("Корзина")) {
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText(CART_SERVICE.getUserCart(chat_id).toString())
                    .row()
                    .button("Очистить корзину", "cОчистить корзину")
                    .endRow()
                    .row()
                    .button("Назад", "mКаталог")
                    .endRow()
                    .rebuild(mes_id);
        }
        else if (text.equals("Очистить корзину")) {
            CART_SERVICE.clearUserCart(chat_id);
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("Вернуться к покупкам", "mКаталог")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("Ваша корзина успешно очищена!");
        }
        else {
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText("Оформление заказа\nТут что-то должно быть")
                    .row()
                    .button("Назад", "cКорзина")
                    .endRow()
                    .rebuild(mes_id);
        }
        return editMessageText;
    }

    public void updateHandle(Update update) throws TelegramApiException {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message inMessage = update.getMessage();
            long chat_id = inMessage.getChatId();
            long mes_id = inMessage.getMessageId();
            SendMessage outMessage = new SendMessage().setChatId(chat_id);
            String text = update.getMessage().getText();
            System.out.println(new Date() + ": " + inMessage.getFrom().getFirstName() + " " +
                    inMessage.getFrom().getLastName() + " (" + inMessage.getFrom().getUserName() +
                    ") совершил действие: " + text);
            execute(messageStarter(text, outMessage).setParseMode("Markdown"));
        }
        else if (update.hasCallbackQuery()) {
            Message inMessage = update.getCallbackQuery().getMessage();
            long chat_id = inMessage.getChatId();
            long mes_id = inMessage.getMessageId();
            String text = update.getCallbackQuery().getData();
            System.out.println(new Date() + ": " + update.getCallbackQuery().getFrom().getFirstName() + " " +
                    update.getCallbackQuery().getFrom().getLastName() + " (" + update.getCallbackQuery().getFrom().getUserName() +
                    ") совершил действие: " + text);
            if (text.startsWith("c"))
                execute(cartHandle(text, chat_id, mes_id).setParseMode("Markdown"));
            else if (text.startsWith("h"))
                execute(hookahHandle(text, chat_id, mes_id).setParseMode("Markdown"));
            else if (text.startsWith("t"))
                execute(tobaccoHandle(text, chat_id, mes_id).setParseMode("Markdown"));
            else if (text.startsWith("m"))
                execute(messageHandle(text, chat_id, mes_id).setParseMode("Markdown"));
        }
    }

    public String getBotUsername() {
        return "Grizzly_Shop_Bot";
    }

    public String getBotToken() {
        return "1238275097:AAGnnDKQpxjxCfjey6zxx0kVgm1EdDNMvak";
    }
}
