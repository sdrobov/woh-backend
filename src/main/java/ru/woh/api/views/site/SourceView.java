package ru.woh.api.views.site;

import ru.woh.api.models.Source;

public class SourceView {
    private Long id;
    private String name;
    private String url;

    public SourceView() {}

    public SourceView(Long id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public static SourceView fromSource(Source source) {
        var view = new SourceView();

        view.id = source.getId();
        view.name = source.getName();
        view.url = source.getUrl();

        return view;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
