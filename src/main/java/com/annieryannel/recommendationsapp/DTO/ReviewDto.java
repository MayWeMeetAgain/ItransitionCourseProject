package com.annieryannel.recommendationsapp.DTO;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private String title;
    private String text;
    private String authorName;
    private Integer likes;
    private boolean liked;
    //private boolean isReadOnlyMode;
}
