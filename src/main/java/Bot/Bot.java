package Bot;

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

//        sendMessage.setReplyMarkup(replyKeyboardMarkup);
//        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        String text = update.getMessage().getText();

        try {
            sendMessage.setText(getMessage(text, sendMessage));
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public String getMessage(String text, SendMessage sendMessage) {

        List<List<InlineKeyboardButton>> inlineKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtonsRow2 = new ArrayList<>();


        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        if (text.equals("/start") || text.equals("Назад")) {
            keyboardFirstRow.add(new KeyboardButton("Сделать заказ"));
            keyboardFirstRow.add(new KeyboardButton("Наличие"));
            keyboardSecondRow.add(new KeyboardButton("Наши Контакты"));
            keyboardSecondRow.add(new KeyboardButton("Помощь"));


            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            return "Открыто Главное Меню Бота.";
        }

        if (text.equals("Наличие")) {

            keyboard.clear();
            keyboardFirstRow.clear();
            keyboardSecondRow.clear();
            keyboardFirstRow.add(new KeyboardButton("Ул. Радонежска 1"));
            keyboardFirstRow.add(new KeyboardButton("Ул. Проспект Карла Маркса 196"));
            keyboardSecondRow.add(new KeyboardButton("Назад"));

            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            return "Выберите Магазин...";
        }

        if (text.equals("Наши Контакты")) {
            keyboardFirstRow.add(new KeyboardButton("Сделать заказ"));
            keyboardFirstRow.add(new KeyboardButton("Наличие"));
            keyboardSecondRow.add(new KeyboardButton("Наши Контакты"));
            keyboardSecondRow.add(new KeyboardButton("Помощь"));


            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            return "г. Самара, ул. Радонежская, 1\n" +
                    "Часы работы: ПН – ВС, с 12.00 до 24.00\n" +
                    "\n" +
                    "Телефон: 8 (927) 002-75-57" +
                    "\n" +
                    "\nг. Самара, пр. Карла Маркса, 196 (ЖК Центральный)\n" +
                    "Часы работы: ПН – ВС, с 12.00 до 24.00\n" +
                    "\n" +
                    "Телефон: 8 (927) 760-11-17";
        }

        if (text.equals("Помощь")) {
            keyboardFirstRow.add(new KeyboardButton("Сделать заказ"));
            keyboardFirstRow.add(new KeyboardButton("Наличие"));
            keyboardSecondRow.add(new KeyboardButton("Наши Контакты"));
            keyboardSecondRow.add(new KeyboardButton("Помощь"));


            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);


            return "C Помощью данного Бота, вы можете сделать заказ, посмотреть наличие в магазинах и узнать наши контакты." +
                    " Чтобы начать напишите : \"/start\" ";

        }
        if (text.equals("Сделать заказ")) {



            InlineKeyboardButton button1 = new InlineKeyboardButton("Табаки");
            InlineKeyboardButton button2 = new InlineKeyboardButton("Кальяны");
            InlineKeyboardButton button3 = new InlineKeyboardButton("Акссесуары");
            InlineKeyboardButton button4 = new InlineKeyboardButton("Уголь");

            button1.setCallbackData("Табаки");
            button2.setCallbackData("Кальяны");
            button3.setCallbackData("Акссесуары");
            button4.setCallbackData("Уголь");

            inlineKeyboardButtonsRow1.add(button1);
            inlineKeyboardButtonsRow1.add(button2);
            inlineKeyboardButtonsRow2.add(button3);
            inlineKeyboardButtonsRow2.add(button4);
            inlineKeyboard.add(inlineKeyboardButtonsRow1);
            inlineKeyboard.add(inlineKeyboardButtonsRow2);

            inlineKeyboardMarkup.setKeyboard(inlineKeyboard);
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);

            return "Выберите Категорию Товара";
        }

        if (text.equals("Ул. Радонежска 1")){

            inlineKeyboard.clear();
            inlineKeyboardButtonsRow1.clear();
            inlineKeyboardButtonsRow2.clear();

            InlineKeyboardButton button1 = new InlineKeyboardButton("Табаки");
            InlineKeyboardButton button2 = new InlineKeyboardButton("Кальяны");
            InlineKeyboardButton button3 = new InlineKeyboardButton("Акссесуары");
            InlineKeyboardButton button4 = new InlineKeyboardButton("Уголь");

            button1.setCallbackData("Табаки");
            button2.setCallbackData("Кальяны");
            button3.setCallbackData("Акссесуары");
            button4.setCallbackData("Уголь");

            inlineKeyboardButtonsRow1.add(button1);
            inlineKeyboardButtonsRow1.add(button2);
            inlineKeyboardButtonsRow2.add(button3);
            inlineKeyboardButtonsRow2.add(button4);
            inlineKeyboard.add(inlineKeyboardButtonsRow1);
            inlineKeyboard.add(inlineKeyboardButtonsRow2);

            inlineKeyboardMarkup.setKeyboard(inlineKeyboard);
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);

            return "Выберите Категорию Товара";

        }

        if (text.equals("Ул. Проспект Карла Маркса 196")){


            inlineKeyboard.clear();
            inlineKeyboardButtonsRow1.clear();
            inlineKeyboardButtonsRow2.clear();

            InlineKeyboardButton button1 = new InlineKeyboardButton("Табаки");
            InlineKeyboardButton button2 = new InlineKeyboardButton("Кальяны");
            InlineKeyboardButton button3 = new InlineKeyboardButton("Акссесуары");
            InlineKeyboardButton button4 = new InlineKeyboardButton("Уголь");

            button1.setCallbackData("Табаки");
            button2.setCallbackData("Кальяны");
            button3.setCallbackData("Акссесуары");
            button4.setCallbackData("Уголь");

            inlineKeyboardButtonsRow1.add(button1);
            inlineKeyboardButtonsRow1.add(button2);
            inlineKeyboardButtonsRow2.add(button3);
            inlineKeyboardButtonsRow2.add(button4);
            inlineKeyboard.add(inlineKeyboardButtonsRow1);
            inlineKeyboard.add(inlineKeyboardButtonsRow2);

            inlineKeyboardMarkup.setKeyboard(inlineKeyboard);
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);

            return "Выберите Категорию Товара";

        }
        return "Не понял";


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
