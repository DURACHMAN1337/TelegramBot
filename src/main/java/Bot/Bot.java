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
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
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
    private static final ArrayList<String> allTobaccoFortresses = new ArrayList<>(TOBACCO_SERVICE.getAllFortresses());


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
                return hookahHandle("h" + text, sendMessage);

            case "Табаки":
                return tobaccoHandle("t" + text,sendMessage);

            case "Корзина":
                return cartHandle("c" + text, sendMessage);
        }
        return sendMessage.setText("Не понял");
    }

    public SendMessage hookahHandle(String text, SendMessage sendMessage) {
        text = text.substring(1);
        long chat_id = Long.parseLong(sendMessage.getChatId());
        ArrayList<String> hookahsNames = HOOKAH_SERVICE.getAllNamesList();
        Hookah currHookah = null;
        SendPhoto sendPhoto = new SendPhoto();

        if (allHookahBrands.contains(text)) {
            ArrayList<Hookah> hookahs = HOOKAH_SERVICE.getHookahsByBrand(text);
            sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText("Товары бренда " + text + ": ")
                    .hookahButtons(hookahs, text)
                    .row()
                    .button("Назад", "Кальяны")
                    .endRow()
                    .build();
            return sendMessage;
        }
        else if (text.contains("В корзину")) {
            currHookah = HOOKAH_SERVICE.getHookahByName(text.replace(" В корзину", ""));
            CART_SERVICE.getUserCart(chat_id).getCart().add(currHookah);
            sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText("Товар: " + currHookah.getName() + "\nУспешно добавлен в корзину")
                    .row()
                    .button("Назад", "Кальяны")
                    .endRow()
                    .build();
            return sendMessage;
        }
        else if (hookahsNames.contains(text)) {
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
        }
        else {
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

    public SendMessage tobaccoHandle(String text, SendMessage sendMessage){
        text = text.substring(1);
        long chat_id = Long.parseLong(sendMessage.getChatId());
        ArrayList<String> tobaccoNames = TOBACCO_SERVICE.getAllNamesList();
        Tobacco currTobacco;

        if (allTobaccoFortresses.contains(text)) {
            ArrayList<Tobacco> tobaccos = TOBACCO_SERVICE.getTobaccoByFortress(text);
            sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText("Табаки крепости: " + text)
                    .tobaccoButtons(tobaccos)
                    .row()
                    .button("Назад", "Табаки")
                    .endRow()
                    .build();
            return sendMessage;
        }
        else if (text.contains("В корзину")) {
            currTobacco = TOBACCO_SERVICE.getTobaccoByName(text.replace(" В корзину", ""));
/*            try {
                ArrayList<String> tastes = TOBACCO_SERVICE.getTobaccoTastes(currTobacco);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            CART_SERVICE.getUserCart(chat_id).getCart().add(currTobacco);
            sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText("Товар: " + currTobacco.getName() + "\nУспешно добавлен в корзину")
                    .row()
                    .button("Назад", "Табаки")
                    .endRow()
                    .build();
        }
        else if (tobaccoNames.contains(text)) {
            Tobacco t = TOBACCO_SERVICE.getTobaccoByName(text);
            sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText("Товар: " + text + "\nЦена: " + t.getPrice() + " руб.\nОписание:\n" + t.getDescription() + "\n" + t.getImg())
                    .row()
                    .button("В корзину", "t" + t.getName() + " В корзину")
                    .endRow()
                    .row()
                    .button("Назад", "Табаки")
                    .endRow()
                    .build();
        }
        else {
            sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText("Выберите крепость табака:")
                    .buttons(allTobaccoFortresses, "t")
                    .row()
                    .button("Назад", "Каталог")
                    .endRow()
                    .build();
        }
        return sendMessage;
    }

    public SendMessage cartHandle(String text, SendMessage sendMessage) {
        text = text.substring(1);
        long chat_id = Long.parseLong(sendMessage.getChatId());
        if (text.equals("Корзина")) {
            sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText(CART_SERVICE.getUserCart(chat_id).toString())
                    .row()
                    .button("Очистить корзину", "cОчистить корзину")
                    .endRow()
                    .row()
                    .button("Назад", "Каталог")
                    .endRow()
                    .build();
        }
        else if (text.equals("Очистить корзину")) {
            CART_SERVICE.clearUserCart(chat_id);
            sendMessage.setText("Ваша корзина успешно очищена!");
        }
        else {
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
            SendMessage outMessage = new SendMessage().setChatId(chat_id);
            String text = update.getCallbackQuery().getData();
            System.out.println(new Date() + ": " + update.getCallbackQuery().getFrom().getFirstName() + " " +
                    update.getCallbackQuery().getFrom().getLastName() + " (" + update.getCallbackQuery().getFrom().getUserName() +
                    ") совершил действие: " + text);
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
