package Bot;

import Bot.Keyboard.InlineKeyboardMarkupBuilder;
import Bot.Keyboard.ReplyKeyboardMarkupBuilder;
import Models.Cart;
import Service.CartService;
import Service.HookahService;
import Service.TobaccoService;
import Models.Products.Hookah;
import Models.Products.Tobacco;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;

public class Bot extends TelegramLongPollingBot {

    private static final HookahService HOOKAH_SERVICE = new HookahService();
    private static final TobaccoService TOBACCO_SERVICE = new TobaccoService();
    private static final CartService CART_SERVICE = new CartService();
    private static final ArrayList<String> allHookahBrands = HOOKAH_SERVICE.getAllBrandsList();
    private static final ArrayList<String> allTobaccoBrands = TOBACCO_SERVICE.getAllNamesList();


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


    public BotApiMethod sendMessageHandle(String text, SendMessage sendMessage) {


        long chat_id = Long.parseLong(sendMessage.getChatId());
        switch (text) {
            case "/start":
                sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                        .setText("Открыто Главное Меню")
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

            case "Назад":
                sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                        .setText("Вы Вернулись Назад в Главное Меню")
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
                sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                        .setText("г. Самара, ул. Радонежская, 1\n" +
                                "Часы работы: ПН – ВС, с 12.00 до 24.00\n" +
                                "\n" +
                                "Телефон: 8 (927) 002-75-57" +
                                "\n" +
                                "\nг. Самара, пр. Карла Маркса, 196 (ЖК Центральный)\n" +
                                "Часы работы: ПН – ВС, с 12.00 до 24.00\n" +
                                "\n" +
                                "Телефон: 8 (927) 760-11-17")
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

            case "Помощь":
                sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                        .setText("C Помощью данного Бота, вы можете сделать заказ, посмотреть наличие в магазинах и узнать наши контакты." +
                                " Чтобы начать напишите : \"/start\" ")
                        .row()
                        .button("\uD83D\uDCE6 Сделать заказ")
                        .button("Наличие")
                        .endRow()
                        .row()
                        .button("Наши Контакты")
                        .button("Помощь")
                        .endRow()
                        .build();
                return sendMessage;

            case "Сделать заказ":
                sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                        .setText("Вы вошли в меню заказа")
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
                return hookahHandle(text, sendMessage);

            case "Табаки":
                return tobaccoHandle(text, sendMessage);

            case "Корзина":
                return cartHandle("c" + text, sendMessage);
        }
        return sendMessage.setText("Не понял");
    }

    public BotApiMethod hookahHandle(String text, SendMessage sendMessage) {
        text = text.substring(1);
        long chat_id = Long.parseLong(sendMessage.getChatId());
        ArrayList<String> hookahsNames = HOOKAH_SERVICE.getAllNamesList();
        Hookah currHookah = null;
        SendPhoto sendPhoto = new SendPhoto();

        if (allHookahBrands.contains(text)) {
            ArrayList<Hookah> hookahs = HOOKAH_SERVICE.getHookahsByBrand(text);
            sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText("Товары бренда " + text + ": ")
                    .productButtons(hookahs, text)
                    .row()
                    .button("Назад", "Кальяны")
                    .endRow()
                    .build();
            return sendMessage;
        } else if (text.contains("В корзину")) {
            currHookah = HOOKAH_SERVICE.getHookahByName(text.replace(" В корзину", ""));
            CART_SERVICE.getUserCart(chat_id).getCart().add(currHookah);
            sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText("Товар: " + currHookah.getName() + "\nУспешно добавлен в корзину")
                    .row()
                    .button("Назад", "Кальяны")
                    .endRow()
                    .build();
            return sendMessage;
        } else if (hookahsNames.contains(text)) {
            currHookah = HOOKAH_SERVICE.getHookahByName(text);
            sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText("Товар: " + text + "\nЦена: " + currHookah.getPrice() + " руб.\nОписание товара бла бла.\n" + currHookah.getImg())
                    .row()
                    .button("В корзину", "h" + currHookah.getName() + " В корзину")
                    .endRow()
                    .row()
                    .button("Назад", "Кальяны")
                    .endRow()
                    .build();
            return sendMessage;
        } else {
            sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText("Выберите бренд кальяна:")
                    .buttons(allHookahBrands, "h")
                    .row()
                    .button("Назад", "Каталог")
                    .endRow()
                    .build();
            return sendMessage;
        }
    }

    public BotApiMethod tobaccoHandle(String text, SendMessage sendMessage) {
        text = text.substring(1);
        long chat_id = Long.parseLong(sendMessage.getChatId());
        ArrayList<String> tobaccoNames = TOBACCO_SERVICE.getAllNamesList();
        Tobacco currTobbaco = null;
        SendPhoto sendPhoto = new SendPhoto();

        if (tobaccoNames.contains(text)) {
            currTobbaco = TOBACCO_SERVICE.getTobaccoByName(text);

            sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText("Товар: " + text + "\nЦена: " + currTobbaco.getPrice() + " руб.\nОписание товара бла бла.\n" + currTobbaco.getImg())
                    .row()
                    .button("В корзину", "t" + currTobbaco.getName() + " В корзину")
                    .endRow()
                    .row()
                    .button("Назад", "Кальяны")
                    .endRow()
                    .build();
            return sendMessage;
        } else if (text.contains("В корзину")) {
            currTobbaco = TOBACCO_SERVICE.getTobaccoByName(text.replace(" В корзину", ""));
            CART_SERVICE.getUserCart(chat_id).getCart().add(currTobbaco);
            sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText("Товар: " + currTobbaco.getName() + "\nУспешно добавлен в корзину")
                    .row()
                    .button("Назад", "Кальяны")
                    .endRow()
                    .build();
            return sendMessage;
        } else {


            sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText("Выберите крепость табака")
                    .row()
                    .button("Безтабачная смесь","Безтабачная смесь")
                    .button("Лёгкий","Лёгкий")
                    .endRow()
                    .row()
                    .button("Средний","Средний")
                    .button("Крепкий","Крепкий")
                    .endRow()
                    .row()
                    .button("Назад", "Каталог")
                    .endRow()
                    .build();
            return sendMessage;

        }

    }

    public SendMessage cartHandle(String text, SendMessage sendMessage) {
        text = text.substring(1);
        long chat_id = Long.parseLong(sendMessage.getChatId());
        if (text.equals("Корзина")) {
            sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText(CART_SERVICE.getUserCart(chat_id).toString())
                    .row()
                    .button("Назад", "Каталог")
                    .endRow()
                    .build();
        } else {
            sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText("Оформление заказа\nТут что-то должно быть")
                    .row()
                    .button("Назад", "cКорзина")
                    .endRow()
                    .build();
        }
        return sendMessage;
    }

    public void updateHandle(Update update) throws TelegramApiException {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message inMessage = update.getMessage();
            long chat_id = inMessage.getChatId();


            SendMessage outMessage = new SendMessage().setChatId(chat_id);
            String text = update.getMessage().getText();
            try {
                execute(sendMessageHandle(text, outMessage));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            Message inMessage = update.getCallbackQuery().getMessage();
            long chat_id = inMessage.getChatId();
            SendMessage outMessage = new SendMessage().setChatId(chat_id);


            String text = update.getCallbackQuery().getData();
            System.out.println(text);
            char[] inMes = text.toCharArray();
            if (inMes[0] == 'c')
                execute(cartHandle(text, outMessage));
            else if (inMes[0] == 'h')
                execute(hookahHandle(text, outMessage));
            else if (inMes[0] == 't')
                execute(tobaccoHandle(text, outMessage));
            else
                execute(sendMessageHandle(text, outMessage));

        }
    }

    public String getBotUsername() {
        return "Grizzly_Shop_Bot";
    }

    public String getBotToken() {
        return "1238275097:AAGnnDKQpxjxCfjey6zxx0kVgm1EdDNMvak";
    }
}
