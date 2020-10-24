package Bot.Keyboard;

import Models.Hookah;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;


/**
 * Helps to build inline keyboard (menu) attached to the message.
 */
public class InlineKeyboardMarkupBuilder implements KeyboardMarkupBuilder {

    private Long chatId;
    private String text;

    private List<InlineKeyboardButton> row = null;
    private final List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

    private InlineKeyboardMarkupBuilder() {
    }

    @Override
    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    @Override
    public InlineKeyboardMarkupBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public static InlineKeyboardMarkupBuilder create() {
        return new InlineKeyboardMarkupBuilder();
    }

    public static InlineKeyboardMarkupBuilder create(Long chatId) {
        InlineKeyboardMarkupBuilder builder = new InlineKeyboardMarkupBuilder();
        builder.setChatId(chatId);
        return builder;
    }

    @Override
    public InlineKeyboardMarkupBuilder row() {
        this.row = new ArrayList<>();
        return this;
    }

    public InlineKeyboardMarkupBuilder button(String text, String callbackData) {
        row.add(new InlineKeyboardButton()
                .setText(text)
                .setCallbackData(callbackData));
        return this;
    }

    public InlineKeyboardMarkupBuilder buttons(ArrayList<String> text) {
        int counter = 0;
        for (String s : text) {
            counter++;
            if (counter % 2 == 1) {
                this.row = new ArrayList<>();
            }
            row.add(new InlineKeyboardButton()
                    .setText(s)
                    .setCallbackData(s));
            if (counter % 2 == 0) {
                this.keyboard.add(this.row);
                this.row = null;
            }
        }
        return this;
    }

    public InlineKeyboardMarkupBuilder productButtons(ArrayList<Hookah> hookahs, String brand) {
        for (Hookah hookah : hookahs) {
            this.row = new ArrayList<>();
            row.add(new InlineKeyboardButton()
                    .setText(hookah.getName().replace(brand, "").trim().replace("X","").trim() + " | " + hookah.getPrice() + " руб.")
                    .setCallbackData(hookah.getName()));
            this.keyboard.add(this.row);
            this.row = null;
        }
        return this;
    }

    public InlineKeyboardMarkupBuilder buttonWithURL(String text, String URL) {
        row.add(new InlineKeyboardButton()
                .setText(text)
                .setUrl(URL));
        return this;
    }

    @Override
    public InlineKeyboardMarkupBuilder endRow() {
        this.keyboard.add(this.row);
        this.row = null;
        return this;
    }

    @Override
    public SendMessage build() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboard);
        return new SendMessage()
                .setChatId(chatId)
                .setText(text)
                .setReplyMarkup(keyboardMarkup);
    }

    public EditMessageText rebuild(Long messageId) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboard);
        return new EditMessageText()
                .setChatId(chatId)
                .setMessageId(toIntExact(messageId))
                .setReplyMarkup(keyboardMarkup);
    }

}
