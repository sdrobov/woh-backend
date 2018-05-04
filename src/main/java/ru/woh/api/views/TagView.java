package ru.woh.api.views;

import lombok.NoArgsConstructor;
import ru.woh.api.models.TagModel;

@NoArgsConstructor
public class TagView {
    protected Long id;
    protected String name;

    public TagView(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TagModel model()
    {
        TagModel tag = new TagModel();
        tag.setName(this.getName());

        return tag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
