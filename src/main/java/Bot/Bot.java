package Bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
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
            setKeyboard(s);
            execute(s);


        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }


    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();
        chat_id = message.getChatId();
        if (message != null && message.hasText()) {
            sendMsg(message, Input(message.getText()));

        }

    }

    public String Input(String text) {
        if (text != null) {
            switch (text) {
                case "/start":
                    return "Привет дружище";
                case "/help":
                    return "Чем могу помочь?";

                case "/settings":
                    return "Что будем настраивать?123";

                case "/jopa":
                    return getInfoHookah();

            }
        }
        return null;

    }


    public void setKeyboard(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/settings"));
        keyboardSecondRow.add(new KeyboardButton("/jopa"));


        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
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
        try  {

            sendPhoto.setChatId(chat_id);

            sendPhoto.setPhoto(hookah.getImg());
            execute(sendPhoto);


        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        String info = "Цена : " + hookah.getPrice()
                +"\n Описание : " + hookah.getDescription();
     return info;
    }
}
