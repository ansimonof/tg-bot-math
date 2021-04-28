package org.tgbot.mathunsinn.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.tgbot.mathunsinn.command.CommandResolver;

public class MathUnsinnBot extends TelegramLongPollingBot {

    private static final Logger log = LoggerFactory.getLogger(MathUnsinnBot.class);

    private static final String BOT_TOKEN = "1584788780:AAFLjOZt7aiyu4YpRTlBckBWalQehrEzQh8";

    private static final String BOT_USERNAME = "math_unsinn_bot";

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        PartialBotApiMethod<Message> answer = null;
        try {
            if (update.hasMessage()) {
                log.info("Got new message: {}", update.getMessage().getText());
                String command = update.getMessage().getText();
                answer = new CommandResolver(command, update.getMessage().getChatId()).getCommandAnswer();
            }
        } catch (Throwable e) {
            log.warn("Exception during parsing message: ", e);
        }

        if (answer != null) {
            try {
                if (answer instanceof BotApiMethod) {
                    execute((BotApiMethod<Message>) answer);
                } else if (answer instanceof SendPhoto) {
                    execute((SendPhoto) answer);
                }
            } catch (TelegramApiException e) {
                log.warn("Exception during sending message: ", e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }
}
