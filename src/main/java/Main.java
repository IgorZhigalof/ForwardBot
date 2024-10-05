import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    private static long tgChatId = -1002358212315L;
    private static String vkGroupId = "-171396054";

    public static void main(String[] args) throws TelegramApiException, InterruptedException {
        Properties properties = getProperties();
        VKWallReader reader = new VKWallReader(properties.getProperty("VKSecurityKey"), vkGroupId);
        TGOut out = new TGOut(properties.getProperty("TGSecurityKey"), tgChatId);
        int counter = 0;
        while (reader.hasNext()) {
            counter++;
            if (counter == 10) {
                break;
            }
            Post post = PostFabric.getPost(reader.next());
            out.send(post);
            Thread.sleep(2000);
        }
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("src/main/resources/bot_params.properties")) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }




}
