package Bot;

import Bot.Keyboard.InlineKeyboardMarkupBuilder;
import Bot.Keyboard.ReplyKeyboardMarkupBuilder;
import Models.AllHookahs;
import Models.Hookah;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;

public class Bot extends TelegramLongPollingBot {
    private long chat_id;
    private static final AllHookahs allHookahs = new AllHookahs();


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
        updateHandle(update);
    }

    public SendMessage sendMessageHandle(String text, SendMessage sendMessage) {

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
                sendMessage = ReplyKeyboardMarkupBuilder
                        .create(chat_id)
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
                        .setText("Выберите категориютовара")
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
                ArrayList<String> brands = allHookahs.getAllBrandsList();
                sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .setText("Выберите бренд кальяна:")
                        .buttons(brands)
                        .row()
                        .button("Назад", "Кальяны")
                        .endRow()
                        .build();
                return sendMessage;

//            case "PANDORA":
//                ArrayList<Hookah> hookahs = allHookahs.getHookahsByBrand("PANDORA");
//                sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
//                        .setText("Товары по запросу PANDORA: ")
//                        .sendProducts(hookahs)
//                        .build();
//                return sendMessage;
        }
        return sendMessage.setText("Не понял");
    }

    public void updateHandle(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message inMessage = update.getMessage();
            chat_id = inMessage.getChatId();
            SendMessage outMessage = new SendMessage().setChatId(chat_id);
            String text = update.getMessage().getText();
            try {
                execute(sendMessageHandle(text, outMessage));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            Message inMessage = update.getCallbackQuery().getMessage();
            chat_id = inMessage.getChatId();
            SendMessage outMessage = new SendMessage().setChatId(chat_id);
            String text = update.getCallbackQuery().getData();
            try {
                execute(sendMessageHandle(text, outMessage));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public String getBotUsername() {
        return "Grizzly_Shop_Bot";
    }

    public String getBotToken() {
        return "1238275097:AAGnnDKQpxjxCfjey6zxx0kVgm1EdDNMvak";
    }
}
