package org.tgbot.mathunsinn.command;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.tgbot.mathunsinn.util.http.HttpUtils;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class CommandTopicApiLogin implements CommandHandler {

    private static final Logger log = LoggerFactory.getLogger(CommandTopicApiSignUp.class);

    private static final String URL = "http://localhost:8080/api";

    private static ConcurrentHashMap<Long, CommandTopicApiSignUp.Credentials> userCredentials = new ConcurrentHashMap<>();

    private CommandTopicApiSignUp.Credentials credentials;

    private String query;


    public static CommandTopicApiSignUp.Credentials getCredentialsByChatId(Long chatId) {
        return userCredentials.get(chatId);
    }

    @Override
    public void parse(String input) {
        String[] split = input.split(";");
        if (split.length != 2) {
            log.warn("Incorrect number of arguments");
            throw new RuntimeException();
        }
        credentials = new CommandTopicApiSignUp.Credentials(split[0], split[1], null);
        query = URL + "/auth/login";
    }

    @Override
    public PartialBotApiMethod<Message> getAnswer(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId + "");
        try (CloseableHttpClient httpClient = HttpUtils.getHttpClient(credentials.getUsername(), credentials.getPassword())) {
            HttpUtils.HttpResponse httpResponse = HttpUtils.sendHttpPostRequest(httpClient, query, credentials.getJSON().toString());
            sendMessage.setText(String.format("Request: %s, Status code of answer: %d, Answer: %s", query, httpResponse.getStatusCode(), httpResponse.getBody()));
            log.info("Request: {}, Status code of answer: {}, Answer: {}", query, httpResponse.getStatusCode(), httpResponse.getBody());
        } catch (IOException e) {
            log.error("Exception: ", e);
            throw new RuntimeException(e);
        }

        return sendMessage;
    }
}
