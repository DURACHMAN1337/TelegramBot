package Bot.Keyboards;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Special telegram keyboards builder.
 */
public interface KeyboardMarkupBuilder {

    void setChatId(Long chatId);

    KeyboardMarkupBuilder setText(String text);

    KeyboardMarkupBuilder row();

    KeyboardMarkupBuilder endRow();

    SendMessage build();

}
