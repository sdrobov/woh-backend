package ru.woh.api.views;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.woh.api.models.Tag;

@NoArgsConstructor
@Getter
@Setter
public class TagView {
    protected Long id;
    protected String name;

    public TagView(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Tag model()
    {
        Tag tag = new Tag();
        tag.setName(this.getName());

        return tag;
    }
}
