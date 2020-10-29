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
import java.util.HashSet;
import java.util.Set;

public class Bot extends TelegramLongPollingBot {

    private static final HookahService HOOKAH_SERVICE = new HookahService();
    private static final TobaccoService TOBACCO_SERVICE = new TobaccoService();
    private static final CartService CART_SERVICE = new CartService();
    private static final ArrayList<String> allHookahBrands = HOOKAH_SERVICE.getAllBrandsList();
    private static final ArrayList<String> allTobaccoFortresses = TOBACCO_SERVICE.getAllFortresses();


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

    public SendMessage sendMessageHandle(String text, SendMessage sendMessage) {
        long chat_id = Long.parseLong(sendMessage.getChatId());
        switch (text) {
            case "/start":

            case "Назад":
                sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                        .setText("С этим надо что-то делать. Возможно перевести в InlineKeyboard")
                        .row()
                        .button("Сделать заказ")
                        .button("Наличие")
                        .endRow()
                        .row()
                        .button("Наши Контакты")
                        .button("Помощь")
                        .endRow()
                        .build();
                return sendMessage;

            case "Наличие":
                sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                        .setText("Выберите Магазин")
                        .row()
                        .button("Ул. Радонежская 1")
                        .button("Ул. Проспект Карла Маркса 196")
                        .endRow()
                        .row()
                        .button("Назад")
                        .endRow()
                        .build();
                return sendMessage;

            case "Наши Контакты":
                sendMessage
                        .setText("г. Самара, ул. Радонежская, 1\n" +
                                "Часы работы: ПН – ВС, с 12.00 до 24.00\n" +
                                "\n" +
                                "Телефон: 8 (927) 002-75-57" +
                                "\n" +
                                "\nг. Самара, пр. Карла Маркса, 196 (ЖК Центральный)\n" +
                                "Часы работы: ПН – ВС, с 12.00 до 24.00\n" +
                                "\n" +
                                "Телефон: 8 (927) 760-11-17");
                return sendMessage;

            case "Помощь":
                sendMessage.setText("C Помощью данного Бота, вы можете сделать заказ, посмотреть наличие в магазинах и узнать наши контакты." +
                                " Чтобы начать напишите : \"/start\" ");
                return sendMessage;

            case "Сделать заказ":
                sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                        .setText("С этим надо что-то делать. Возможно перевести в InlineKeyboard")
                        .row()
                        .button("Каталог")
                        .button("Корзина")
                        .endRow()
                        .row()
                        .button("Оформить заказ")
                        .endRow()
                        .row()
                        .button("Назад")
                        .endRow()
                        .build();
                return sendMessage;

            case "Каталог":
                sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .setText("Выберите категорию товара")
                        .row()
                        .button("Табаки", "Табаки")
                        .button("Кальяны", "Кальяны")
                        .endRow()
                        .row()
                        .button("Акссесуары", "Акссесуары")
                        .button("Уголь", "Уголь")
                        .endRow()
                        .build();
                return sendMessage;

            case "Ул. Радонежская 1":
                sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .setText("\"Ул. Радонежска 1\"")
                        .row()
                        .button("Табаки", "Табаки")
                        .button("Кальяны", "Кальяны")
                        .endRow()
                        .row()
                        .button("Акссесуары", "Акссесуары")
                        .button("Уголь", "Уголь")
                        .endRow()
                        .build();
                return sendMessage;

            case "Ул. Проспект Карла Маркса 196":
                sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .setText("\"Ул. Проспект Карла Маркса 196\"")
                        .row()
                        .button("Табаки", "Табаки")
                        .button("Кальяны", "Кальяны")
                        .endRow()
                        .row()
                        .button("Акссесуары", "Акссесуары")
                        .button("Уголь", "Уголь")
                        .endRow()
                        .build();
                return sendMessage;

            case "Кальяны":
                return hookahMarkup(sendMessage);

            case "Табаки":
                return tobaccoMarkup(sendMessage);

            case "Корзина":
                return cartMarkup(sendMessage);
        }
        return sendMessage.setText("*Данный функционал находится в разработке*");
    }

    public SendMessage hookahMarkup(SendMessage sendMessage) {
        long chat_id = Long.parseLong(sendMessage.getChatId());
        sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                .setText("Выберите бренд кальяна:")
                .buttons(allHookahBrands, "h")
                .row()
                .button("Назад", "Каталог")
                .endRow()
                .build();
        return sendMessage.setParseMode("Markdown");
    }

    public SendMessage tobaccoMarkup(SendMessage sendMessage) {
        long chat_id = Long.parseLong(sendMessage.getChatId());
        sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                .buttons(allTobaccoFortresses, "t")
                .row()
                .button("Назад", "Каталог")
                .endRow()
                .build();
        sendMessage.setText("Выберите крепость табака:");
        return sendMessage;
    }

    public SendMessage cartMarkup(SendMessage sendMessage) {
        long chat_id = Long.parseLong(sendMessage.getChatId());
        sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                .setText(CART_SERVICE.getUserCart(chat_id).toString())
                .row()
                .button("Очистить корзину", "cОчистить корзину")
                .endRow()
                .row()
                .button("Назад", "Каталог")
                .endRow()
                .build();
        return sendMessage.setParseMode("Markdown");
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
                    .button("Назад", "hКальяны")
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
                        .button("Назад", "hКальяны")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*" + currHookah.getBrand() + "* | " + currHookah.getName() +
                        "\nЦена: " + currHookah.getPrice() + " руб." +
                        "\n\n" + currHookah.getDescription() +
                        "\n\n[Изображение]" + "(" + currHookah.getImg() + ")");
            }
            else {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Назад", "hКальяны")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*" + currHookah.getBrand() + "* | " + currHookah.getName() + " (нет в наличии)" +
                        "\nЦена: " + currHookah.getPrice() + " руб." +
                        "\n\n" + currHookah.getDescription() +
                        "\n\n[Изображение]" + "(" + currHookah.getImg() + ")");
            }
        }
        else {
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(allHookahBrands, "h")
                    .row()
                    .button("Назад", "Каталог")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("Выберите бренд кальяна:");
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
                    .tobaccoButtons(tobaccos)
                    .row()
                    .button("Назад", "tТабаки")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("Табаки крепости: " + text);
        }
        else if (text.contains("crt")) {
            currTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("crt", "")));
            Set<String> allTastes = new HashSet<>();
            if (currTobacco.getRadonejskayaTastes() != null) {
                allTastes.addAll(currTobacco.getRadonejskayaTastes());
            }
            if (currTobacco.getKarlaMarksaTastes() != null) {
                allTastes.addAll(currTobacco.getKarlaMarksaTastes());
            }
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(new ArrayList<>(allTastes), "t" + currTobacco.getId() + "&")
                    .row()
                    .button("Назад", "tТабаки")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("Выберите интересующий вкус:");
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
            Tobacco t = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("id","")));
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
                        "\n\n" + t.getDescription() +
                        "\n\n[Изображение]" + "(" + t.getImg() + ")");
            }
            else {
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Назад", "tТабаки")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*" + t.getName() + "* (нет в наличии)" +
                        "\n\nЦена: " + t.getPrice() + " руб." +
                        "\n\n" + t.getDescription() +
                        "\n\n[Изображение]" + "(" + t.getImg() + ")");
            }
        }
        else {
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(allTobaccoFortresses, "t")
                    .row()
                    .button("Назад", "Каталог")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("Выберите крепость табака:");
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
                    .button("Назад", "Каталог")
                    .endRow()
                    .rebuild(mes_id);
        }
        else if (text.equals("Очистить корзину")) {
            CART_SERVICE.clearUserCart(chat_id);
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText("Ваша корзина успешно очищена!")
                    .row()
                    .button("Вернуться к покупкам", "Каталог")
                    .endRow()
                    .rebuild(mes_id);
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
            SendMessage outMessage = new SendMessage().setChatId(chat_id);
            String text = update.getMessage().getText();
            System.out.println(new Date() + ": " + inMessage.getFrom().getFirstName() + " " +
                    inMessage.getFrom().getLastName() + " (" + inMessage.getFrom().getUserName() +
                    ") совершил действие: " + text);
            try {
                execute(sendMessageHandle(text, outMessage));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            Message inMessage = update.getCallbackQuery().getMessage();
            long chat_id = inMessage.getChatId();
            long mes_id = inMessage.getMessageId();
            SendMessage outMessage = new SendMessage().setChatId(chat_id);
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
            else
                execute(sendMessageHandle(text, outMessage).setParseMode("Markdown"));
        }
    }

    public String getBotUsername() {
        return "Grizzly_Shop_Bot";
    }

    public String getBotToken() {
        return "1238275097:AAGnnDKQpxjxCfjey6zxx0kVgm1EdDNMvak";
    }
}
