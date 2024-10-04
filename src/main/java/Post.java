import java.util.List;

public interface Post {
    String getText();
    List<String> getPhotos();
    List<String> getVideos();
    List<String> getLinks();
}
