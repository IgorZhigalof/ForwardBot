import com.vk.api.sdk.objects.wall.WallItem;

import java.util.Objects;

public class PostFabric {
    public static Post getPost(WallItem item) {
        Post post;
        if (Objects.isNull(item.getCopyHistory())) {
            post = new VKPost(item);
        } else {
            post = new VKRepost(item);
        }
        return new PostTextConverter(post);
    }
}
