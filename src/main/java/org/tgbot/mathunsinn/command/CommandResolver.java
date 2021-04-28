package org.tgbot.mathunsinn.command;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.Map;

public class CommandResolver {

    private static Map<String, CommandHandler> commands = new HashMap<>();

    static {
        commands.put("/int", new CommandWolframIntegralHandler());
        commands.put("/intm", new CommandWolframIntegralImageHandler());
        commands.put("/sign_up", new CommandTopicApiSignUp());
        commands.put("/login", new CommandTopicApiLogin());
    }

    private final String input;
    private final Long chatId;

    public CommandResolver(String input, Long chatId) {
        this.input = input;
        this.chatId = chatId;
    }

    public PartialBotApiMethod<Message> getCommandAnswer() {
        if (input == null || input.isEmpty()) {
            throw new IllegalStateException("command cannot be null or empty");
        }

        String[] args = input.split(" ");
        if (args.length < 2) {
            throw new IllegalStateException("incorrect command");
        }

        CommandHandler commandHandler = commands.get(args[0]);
        if (commandHandler == null) {
            throw new IllegalStateException("no command for input: " + input);
        }

        commandHandler.parse(input.replace(args[0], "").trim());
        return commandHandler.getAnswer(chatId);
    }

}
