package pro.sky.telegrambot.command;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Getter;

public enum BotCommand {

    START("/start", (chatId, messageText, telegramBot) -> {
        String welcomeText = """
                Привет!
                Это бот для отправки напоминаний.
                
                Чтобы создать напоминание, отправьте сообщение в формате:
                DD.MM.YYYY HH:mm текст_сообщения
                """;
        telegramBot.execute(new SendMessage(chatId, welcomeText));
    }),

    HELP("/help", (chatId, messageText, telegramBot) -> {
        String helpText = """
                Список доступных команд:
                /start - Начало работы
                /help - Помощь
                """;
        telegramBot.execute(new SendMessage(chatId, helpText));
    });

    @Getter
    private final String command;

    private final CommandHandler handler;

    BotCommand(String command, CommandHandler handler) {
        this.command = command;
        this.handler = handler;
    }

    public void execute(long chatId, String messageText, TelegramBot telegramBot) {
        handler.handle(chatId, messageText, telegramBot);
    }

    public static BotCommand fromString(String command) {
        for (BotCommand botCommand : values()) {
            if (botCommand.getCommand().equals(command)) {
                return botCommand;
            }
        }
        return null;
    }

    @FunctionalInterface
    interface CommandHandler {
        void handle(long chatId, String messageText, TelegramBot telegramBot);
    }
}