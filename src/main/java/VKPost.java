import com.vk.api.sdk.objects.photos.PhotoSizes;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.WallpostFull;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class VKPost implements Post {
    private WallpostFull item;

    public VKPost(WallpostFull item) {
        this.item = item;
    }
    @Override
    public String getText() {
        return item.getText();
    }

    @Override
    public List<String> getPhotos() {
        if (item.getAttachments() == null) {
            return null;
        }
        return item.getAttachments().stream()
                .filter(x -> "photo".equalsIgnoreCase(x.getType().toString()))
                .map(VKPost::photoToUrlString)
                .toList();
    }

    public List<String> getLinks() {
        if (item.getAttachments() == null) {
            return null;
        }
        return item.getAttachments().stream()
                .filter(x -> "link".equalsIgnoreCase(x.getType().toString()))
                .map(VKPost::linkToUrlString)
                .toList();
    }

    public List<String> getVideos() {
        if (item.getAttachments() == null) {
            return null;
        }
        return item.getAttachments().stream()
                .filter(x -> "video".equalsIgnoreCase(x.getType().toString()))
                .map(VKPost::videoToUrlString)
                .toList();
    }

    private static String photoToUrlString(WallpostAttachment attachment) {
        return Objects.requireNonNull(attachment
                        .getPhoto()
                        .getSizes()
                        .stream()
                        .max(Comparator.comparingInt(PhotoSizes::getHeight))
                        .orElse(null))
                .getUrl()
                .toString();
    }

    private static String videoToUrlString(WallpostAttachment attachment) {
        return "https://vk.com/clip"
                + attachment.getVideo().getOwnerId()
                + "_"
                + attachment.getVideo().getId();
    }

    private static String linkToUrlString(WallpostAttachment attachment) {
        return attachment.getLink().getUrl();
    }
}
