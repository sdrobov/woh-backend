package ru.woh.api.views;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.woh.api.models.Post;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class SourceView {
    @NoArgsConstructor
    @Getter
    @Setter
    public static class SourceSettings {
        private String rssUrl;
        private String mainUrl;
        private String linksSelector;
        private String descriptionSelector;
        private String titlesSelector;
        private String titleSelector;
        private String datesSelector;
        private String dateFormat;
        private String nextSelector;
        private String contentSelector;
        private String nextContentSelector;
        private String previewSelector;

        static SourceSettings fromJson(String json) {
            ObjectMapper mapper = new ObjectMapper();

            try {
                return mapper.readValue(json, SourceSettings.class);
            } catch (IOException e) {
                return new SourceSettings();
            }
        }
    }

    private Long id;
    private String name;
    private String url;
    private SourceSettings settings;
    private Date createdAt;
    private Date lastPostDate;
    private Boolean isLocked;
    private List<PostView> posts;

    public SourceView(
        Long id,
        String name,
        String url,
        String settings,
        Date createdAt,
        Date lastPostDate,
        Boolean isLocked,
        Set<Post> posts
    ) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.settings = SourceSettings.fromJson(settings);
        this.createdAt = createdAt;
        this.lastPostDate = lastPostDate;
        this.isLocked = isLocked;
        this.posts = posts.stream().map(Post::view).collect(Collectors.toList());
    }
}
