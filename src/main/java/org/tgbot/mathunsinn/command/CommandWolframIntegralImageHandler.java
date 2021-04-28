package org.tgbot.mathunsinn.command;

import org.apache.http.HttpStatus;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.tgbot.mathunsinn.util.http.HttpUtils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

public class CommandWolframIntegralImageHandler implements CommandHandler {

    private static final Logger log = LoggerFactory.getLogger(CommandWolframIntegralImageHandler.class);

    private static final String HTTP_QUERY_FORMAT = "http://api.wolframalpha.com/v2/query?appid=HH7QGY-J8JVP3U857&input=integrate+%s&output=json";

    private String query;

    @Override
    public void parse(String input) {
        try {
            input = URLEncoder.encode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("Exception during encode URL: ", e);
            throw new RuntimeException(e);
        }
        query = String.format(HTTP_QUERY_FORMAT, input);
    }

    @Override
    public PartialBotApiMethod<Message> getAnswer(Long chatId) {
        try (CloseableHttpClient httpClient = HttpUtils.getHttpClient(null, null)) {
            HttpUtils.HttpResponse httpResponse = HttpUtils.sendHttpGetRequest(httpClient, query);
            log.info("Request: {}, Status code of answer: {}, Answer: {}", query, httpResponse.getStatusCode(), httpResponse.getBody());
            if (httpResponse.getStatusCode() == HttpStatus.SC_OK) {
                JSONObject jsonObject = new JSONObject(httpResponse.getBody());

                JSONObject answerJson = (JSONObject) jsonObject
                        .getJSONObject("queryresult")
                        .getJSONArray("pods")
                        .get(0);
                answerJson = answerJson.getJSONArray("subpods")
                        .getJSONObject(0)
                        .getJSONObject("img");

                String src = answerJson.getString("src").replace("\\", "");
                String title = answerJson.getString("title");

                URL url = new URL(src);
                InputStream is = url.openStream();
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setPhoto(new InputFile(is, title));
                sendPhoto.setChatId(chatId + "");

                return sendPhoto;
            }
        } catch (Throwable e) {
            log.warn("Exception during fetching data: ", e);
            throw new RuntimeException(e);
        }
        return null;
    }
}
