package org.tgbot.mathunsinn.command;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface CommandHandler {

    void parse(String input);

    PartialBotApiMethod<Message> getAnswer(Long chatId);
}
