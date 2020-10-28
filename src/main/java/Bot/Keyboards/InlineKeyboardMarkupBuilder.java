package Bot.Keyboards;

import Models.Products.Hookah;
import Models.Products.Tobacco;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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

    public InlineKeyboardMarkupBuilder buttons(ArrayList<String> text, String handle) {
        ArrayList<List<InlineKeyboardButton>> rows = new ArrayList<>();
        ArrayList<InlineKeyboardButton> row = new ArrayList<>();
        for (String s : text) {
            if (row.size() < 2) {
                row.add(new InlineKeyboardButton()
                        .setText(s)
                        .setCallbackData(handle + s));
            } else {
                rows.add(row);
                row = new ArrayList<>();
                row.add(new InlineKeyboardButton()
                        .setText(s)
                        .setCallbackData(handle + s));
            }
        }
        if (!row.isEmpty())
            rows.add(row);
        this.keyboard.addAll(rows);
        return this;
    }

    public InlineKeyboardMarkupBuilder hookahButtons(ArrayList<Hookah> hookahs, String brand) {
        for (Hookah hookah : hookahs) {
            this.row = new ArrayList<>();
            row.add(new InlineKeyboardButton()
                    .setText(hookah.getName().replace(brand, "").trim().replace("X","").trim() + " | " + hookah.getPrice() + " руб.")
                    .setCallbackData("h" + hookah.getName()));
            this.keyboard.add(this.row);
            this.row = null;
        }
        return this;
    }
    public InlineKeyboardMarkupBuilder tobaccoButtons(ArrayList<Tobacco> tobacco) {
        for (Tobacco t : tobacco) {
            this.row = new ArrayList<>();
            row.add(new InlineKeyboardButton()
                    .setText(t.getName() + " | " + t.getPrice() + " руб.")
                    .setCallbackData("tid" + t.getId()));
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
