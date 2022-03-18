package com.annieryannel.recommendationsapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {
    private Long reviewId;
    private String title;
    private String authorName;
    private String text;
    private Long likes;
    private boolean liked;
    //private boolean isReadOnlyMode;
}
