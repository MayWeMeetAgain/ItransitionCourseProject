package com.annieryannel.recommendationsapp.DTO;

import com.annieryannel.recommendationsapp.utils.Category;
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
    private UserDto author;
    private Integer authorRating;
    private Category category;
    private String picture;
    private Float usersRating;
    private boolean isRated;
    private Integer likes;
    private boolean liked;
    private boolean isReadOnlyMode;
}
