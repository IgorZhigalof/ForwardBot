import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostTextConverter implements Post {

    private final Post post;

    public PostTextConverter(Post post) {
        this.post = post;
    }

    @Override
    public String getText() {
        String clubRegex = "\\[club(\\d+)\\|([^\\]]+)]";
        String idRegex = "\\[id(\\d+)\\|([^\\]]+)]";
        Pattern clubPattern = Pattern.compile(clubRegex);
        Matcher clubMatcher = clubPattern.matcher(post.getText());
        StringBuilder result = new StringBuilder();
        while (clubMatcher.find()) {
            String id = clubMatcher.group(1);
            String name = clubMatcher.group(2);
            String replacement = "[" + name + "](https://vk.com/club" + id + ")";
            clubMatcher.appendReplacement(result, replacement);
        }
        clubMatcher.appendTail(result);
        Pattern idPattern = Pattern.compile(idRegex);
        Matcher idMatcher = idPattern.matcher(result.toString());
        result = new StringBuilder();
        while (idMatcher.find()) {
            String id = idMatcher.group(1);
            String name = idMatcher.group(2);
            String replacement = "[" + name + "](https://vk.com/" + id + ")";
            idMatcher.appendReplacement(result, replacement);
        }
        idMatcher.appendTail(result);
        return result.toString();
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
