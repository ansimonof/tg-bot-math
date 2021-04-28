package org.tgbot.mathunsinn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.tgbot.mathunsinn.bot.MathUnsinnBot;

public class Main{

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MathUnsinnBot());
        } catch (TelegramApiException e) {
            log.error("Exception during startup: ", e);
            System.exit(-1);
        }
    }
}
