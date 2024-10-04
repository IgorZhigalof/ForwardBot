import com.vk.api.sdk.objects.wall.WallpostFull;

import java.util.List;

public class VKRepost implements Post {
    private WallpostFull item;
    private VKPost post;

    public VKRepost(WallpostFull item) {
        this.item = item;
        this.post = new VKPost(item.getCopyHistory().get(0));
    }

    @Override
    public String getText() {
        String ln = System.lineSeparator();
        return item.getText()
                + ln
                + ln
                + post.getText();
    }

    @Override
    public List<String> getPhotos() {
        return post.getPhotos();
    }

    @Override
    public List<String> getVideos() {
        return post.getVideos();
    }

    @Override
    public List<String> getLinks() {
        return post.getLinks();
    }
}
