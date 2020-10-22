package Bot;

import Bot.Keyboard.InlineKeyboardMarkupBuilder;
import Bot.Keyboard.ReplyKeyboardMarkupBuilder;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    private long chat_id;


    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

    }


    public void sendMsg(Message message, String text) {

        SendMessage s = new SendMessage();
        s.enableMarkdown(true);
        s.setChatId(message.getChatId().toString());
        s.setReplyToMessageId(message.getMessageId());
        s.setText(text);
        try {

            execute(s);


        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }


    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
        chat_id = message.getChatId();

        String text = update.getMessage().getText();

        try {

//            sendMessage.setText(getMessage(text, sendMessage));
            execute(getMessage(text, sendMessage));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public SendMessage getMessage(String text, SendMessage sendMessage) {

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

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
                        .button("Сделать заказ")
                        .button("Наличие")
                        .endRow()
                        .row()
                        .button("Наши Контакты")
                        .button("Помощь")
                        .endRow()
                        .build();

                return sendMessage;

            case "Сделать заказ":

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

        }

//        if (text.equals("/start") || text.equals("Назад")) {
//            sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
//                    .setText("Открыто Главное Меню")
//                    .row()
//                    .button("Сделать заказ")
//                    .button("Наличие")
//                    .endRow()
//                    .row()
//                    .button("Наши Контакты")
//                    .button("Помощь")
//                    .endRow()
//                    .build();
//
//            return sendMessage;
//        }

//        if (text.equals("Наличие")) {
//
//            sendMessage = ReplyKeyboardMarkupBuilder
//                    .create(chat_id)
//                    .setText("Выберите Магазин")
//                    .row()
//                    .button("Ул. Радонежская 1")
//                    .button("Ул. Проспект Карла Маркса 196")
//                    .endRow()
//                    .row()
//                    .button("Назад")
//                    .endRow()
//                    .build();
//
//
//            return sendMessage;
//        }

//        if (text.equals("Наши Контакты")) {
//
//            sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
//                    .setText("г. Самара, ул. Радонежская, 1\n" +
//                            "Часы работы: ПН – ВС, с 12.00 до 24.00\n" +
//                            "\n" +
//                            "Телефон: 8 (927) 002-75-57" +
//                            "\n" +
//                            "\nг. Самара, пр. Карла Маркса, 196 (ЖК Центральный)\n" +
//                            "Часы работы: ПН – ВС, с 12.00 до 24.00\n" +
//                            "\n" +
//                            "Телефон: 8 (927) 760-11-17")
//                    .row()
//                    .button("Сделать заказ")
//                    .button("Наличие")
//                    .endRow()
//                    .row()
//                    .button("Наши Контакты")
//                    .button("Помощь")
//                    .endRow()
//                    .build();
//
//            return sendMessage;
//        }

//        if (text.equals("Помощь")) {
//
//            sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
//                    .setText("C Помощью данного Бота, вы можете сделать заказ, посмотреть наличие в магазинах и узнать наши контакты." +
//                            " Чтобы начать напишите : \"/start\" ")
//                    .row()
//                    .button("Сделать заказ")
//                    .button("Наличие")
//                    .endRow()
//                    .row()
//                    .button("Наши Контакты")
//                    .button("Помощь")
//                    .endRow()
//                    .build();
//
//            return sendMessage;
//
//        }
//        if (text.equals("Сделать заказ")) {
//
//            sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
//                    .setText("Выберите категорию товара")
//                    .row()
//                    .button("Табаки", "Табаки")
//                    .button("Кальяны", "Кальяны")
//                    .endRow()
//                    .row()
//                    .button("Акссесуары", "Акссесуары")
//                    .button("Уголь", "Уголь")
//                    .endRow()
//                    .build();
//
//
//
//            return sendMessage;
//        }

//        if (text.equals("Ул. Радонежская 1")) {
//
//            sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
//                    .setText("\"Ул. Радонежска 1\"")
//                    .row()
//                    .button("Табаки", "Табаки")
//                    .button("Кальяны", "Кальяны")
//                    .endRow()
//                    .row()
//                    .button("Акссесуары", "Акссесуары")
//                    .button("Уголь", "Уголь")
//                    .endRow()
//                    .build();
//
//
//
//            return sendMessage;
//
//        }

//        if (text.equals("Ул. Проспект Карла Маркса 196")) {
//
//            sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
//                    .setText("\"Ул. Проспект Карла Маркса 196\"")
//                    .row()
//                    .button("Табаки", "Табаки")
//                    .button("Кальяны", "Кальяны")
//                    .endRow()
//                    .row()
//                    .button("Акссесуары", "Акссесуары")
//                    .button("Уголь", "Уголь")
//                    .endRow()
//                    .build();
//
//
//            return sendMessage;
//        }
        return sendMessage.setText("Не понял");


    }


    public String getBotUsername() {
        return "Grizzly_Shop_Bot";
    }

    public String getBotToken() {
        return "1238275097:AAGnnDKQpxjxCfjey6zxx0kVgm1EdDNMvak";
    }

    public String getInfoHookah() {
        Hookah hookah = new Hookah();
        SendPhoto sendPhoto = new SendPhoto();
        try {

            sendPhoto.setChatId(chat_id);

            sendPhoto.setPhoto(hookah.getImg());
            execute(sendPhoto);


        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        String info = "Цена : " + hookah.getPrice()
                + "\n Описание : " + hookah.getDescription();
        return info;
    }
}
