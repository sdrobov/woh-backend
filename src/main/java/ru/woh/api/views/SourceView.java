package ru.woh.api.views;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.woh.api.models.Post;
import ru.woh.api.models.Source;

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
        private String url;
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
        private Boolean isApproved;
        private Integer type;

        static SourceSettings fromJson(String json) {
            ObjectMapper mapper = new ObjectMapper();

            SourceSettings sourceSettings;
            try {
                sourceSettings = mapper.readValue(json, SourceSettings.class);
            } catch (IOException e) {
                sourceSettings = new SourceSettings();
            }

            return sourceSettings;
        }

        public String toString() {
            ObjectMapper mapper = new ObjectMapper();
            String result;

            try {
                result = mapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                result = "";
            }

            return result;
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
        this.posts = posts != null
            ? posts.stream()
                .map(Post::view)
                .collect(Collectors.toList())
            : null;
    }

    public Source model() {
        Source source = new Source();

        source.setId(this.id);
        source.setName(this.name);
        source.setUrl(this.url);
        source.setCreatedAt(this.createdAt);
        source.setLastPostDate(this.lastPostDate);
        source.setIsLocked(this.isLocked ? 1 : 0);
        source.setSettings(this.settings.toString());

        return source;
    }
}
