import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.WallItem;
import com.vk.api.sdk.objects.wall.responses.GetExtendedResponse;

import java.util.Iterator;
import java.util.List;

public class VKWallReader implements Iterator<WallItem> {
    private final VkApiClient vk = new VkApiClient(new HttpTransportClient());
    private final String securityKey;
    private int offset = 0;
    private List<WallItem> posts;
    private int position = 99;

    public VKWallReader(String secKey) {
        this.securityKey = secKey;

    }

    private void readNextPosts() {
        GetExtendedResponse wall;
        try {
             wall = vk
                    .wall()
                    .getExtended(new ServiceActor(52347151, securityKey))
                    .offset(offset)
                    .count(100)
                    .domain("-171396054")
                    .execute();
        } catch (ClientException | ApiException e) {
            throw new RuntimeException(e);
        }
        offset += 100;
        posts = wall.getItems();
    }


    @Override
    public boolean hasNext() {
        if (posts == null && position != 0) {
            position = 0;
            readNextPosts();
        }
        return posts != null && !posts.isEmpty();
    }

    @Override
    public WallItem next() {
        position++;
        if (!hasNext()) {
            throw new RuntimeException("There are no posts left");
        }
        return posts.get(position);
    }
}
