import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface Output {
    void send(Post post) throws TelegramApiException;
}
