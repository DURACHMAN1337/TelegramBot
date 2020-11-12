package Bot.Keyboards;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Helps to build special keyboard with predefined reply options.
 */
public class ReplyKeyboardMarkupBuilder implements KeyboardMarkupBuilder {

    private Long chatId;
    private String text;

    private final List<KeyboardRow> keyboard = new ArrayList<>();
    private KeyboardRow row;

    private ReplyKeyboardMarkupBuilder(){
    }

    @Override
    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    @Override
    public ReplyKeyboardMarkupBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public static ReplyKeyboardMarkupBuilder create() {
        return new ReplyKeyboardMarkupBuilder();
    }

    public static ReplyKeyboardMarkupBuilder create(Long chatId) {
        ReplyKeyboardMarkupBuilder builder = new ReplyKeyboardMarkupBuilder();
        builder.setChatId(chatId);
        return builder;
    }

    @Override
    public ReplyKeyboardMarkupBuilder row() {
        this.row = new KeyboardRow();
        return this;
    }

    public ReplyKeyboardMarkupBuilder button(String text) {
        row.add(text);
        return this;
    }

    public ReplyKeyboardMarkupBuilder buttonWithContactRequest(String text) {
        KeyboardButton button = new KeyboardButton().setRequestContact(true);
        row.add(button.setText(text));
        return this;
    }

    @Override
    public ReplyKeyboardMarkupBuilder endRow() {
        this.keyboard.add(this.row);
        this.row = null;
        return this;
    }

    @Override
    public SendMessage build() {
        SendMessage message = new SendMessage();
        message.setChatId(chatId).setText(text);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(false)
                .setKeyboard(keyboard);

        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

}
