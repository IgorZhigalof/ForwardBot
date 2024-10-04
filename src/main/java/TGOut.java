import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;


import java.util.ArrayList;
import java.util.List;

public class TGOut implements Output {
    private TelegramClient telegramClient;
    private long chatId;

    public TGOut(String securityKey, long chatId) {
        this.telegramClient = new OkHttpTelegramClient(securityKey);
        this.chatId = chatId;
    }

    @Override
    public void send(Post post) throws TelegramApiException {
        if (post.getVideos() != null) {
            sendVideos(post);
        }
        if (post.getPhotos() != null) {
            sendPhotos(post);
        }
        if (post.getText() != null) {
            sendText(post);
        }
    }

    private void sendVideos(Post post) throws TelegramApiException {
        List<String> atts = post.getVideos();
        if (atts == null) {
            return;
        }
        if (atts.size() == 1) {
            telegramClient.execute(SendVideo
                    .builder()
                    .chatId(chatId)
                    .video(new InputFile(atts.get(0)))
                    .build()
            );
        } else if (atts.size() > 1) {
            SendMediaGroup group = SendMediaGroup
                    .builder()
                    .chatId(chatId)
                    .medias(atts.stream().map(InputMediaVideo::new).toList())
                    .build();
            telegramClient.execute(group);
        }
    }

    private void sendPhotos(Post post) throws TelegramApiException {
        List<String> atts = post.getPhotos();
        if (atts.size() == 1) {
            telegramClient.execute(SendPhoto
                    .builder()
                    .chatId(chatId)
                    .photo(new InputFile(atts.get(0)))
                    .build()
            );
        } else if (atts.size() > 1) {
            SendMediaGroup group = SendMediaGroup
                    .builder()
                    .chatId(chatId)
                    .medias(atts.stream().map(InputMediaPhoto::new).toList())
                    .build();
            telegramClient.execute(group);
        }
    }

    private void sendText(Post post) throws TelegramApiException {
        List<String> msgs = splitString(post.getText(), 4096);
        for (String msg : msgs) {
            telegramClient.execute(SendMessage
                    .builder()
                    .chatId(chatId)
                    .text(msg)
                    .build());
        }
    }

    public static List<String> splitString(String text, int maxSize) {
        List<String> result = new ArrayList<>();
        String[] words = text.split(" ");

        StringBuilder currentPart = new StringBuilder();

        for (String word : words) {
            if (currentPart.length() + word.length() + 1 > maxSize) {
                result.add(currentPart.toString());
                currentPart = new StringBuilder();
            }

            if (currentPart.length() > 0) {
                currentPart.append(" ");
            }
            currentPart.append(word);
        }
        if (currentPart.length() > 0) {
            result.add(currentPart.toString());
        }
        return result;
    }
}
