package org.tgbot.mathunsinn.command;

import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.tgbot.mathunsinn.util.http.HttpUtils;

import java.io.IOException;


public class CommandTopicApiSignUp implements CommandHandler {

    private static final Logger log = LoggerFactory.getLogger(CommandTopicApiSignUp.class);

    private static final String URL = "http://localhost:8080/api";

    private Credentials credentials;

    private String query;

    @Override
    public void parse(String input) {
        String[] split = input.split(";");
        if (split.length != 3) {
            log.warn("Incorrect number of arguments");
            throw new RuntimeException();
        }
        credentials = new Credentials(split[0], split[1], split[2]);
        query = URL + "/auth/signup";
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

    public static class Credentials {

        private String username;
        private String password;
        private String email;

        public Credentials(String username, String password, String email) {
            this.username = username;
            this.password = password;
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }

        public JSONObject getJSON() {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("email", email);
            return jsonObject;
        }
    }
}
