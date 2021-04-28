package org.tgbot.mathunsinn.command;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class CommandCar implements CommandHandler {

    @Override
    public void parse(String input) {

    }

    @Override
    public PartialBotApiMethod<Message> getAnswer(Long chatId) {
        BufferedImage img = null;
        InputStream is = null;
        try {
            img = ImageIO.read(new File("src/images.jfif"));
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", os);                          // Passing: ​(RenderedImage im, String formatName, OutputStream output)
            is = new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e){
            System.out.println(e);
        }
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(new InputFile(is, "mashcina"));
        sendPhoto.setChatId(chatId + "");

        return sendPhoto;
    }
}
