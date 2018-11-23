package ru.woh.api.views.site;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RatingView {
    private Long count;
    private Boolean like;
    private Boolean dislike;
}
