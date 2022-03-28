package com.annieryannel.recommendationsapp.models;

import com.annieryannel.recommendationsapp.utils.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;



import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reviews")
@Indexed(index = "index_review")
@Getter
@Setter
@NoArgsConstructor
public class Review {
    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @FullTextField
    @Column(name = "title")
    @NotBlank
    @Size(max = 40, message = "Title must be under 40 characters")
    private String title;

    @FullTextField
    @NotBlank
    @Column(name = "text")
    private String text;

    @Column(name = "picture")
    private String picture;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @NotBlank
    @Min(value = 1, message = "Rating should not be less than 1")
    @Max(value = 5, message = "Rating should not be greater than 5")
    @Column(name = "author_rating")
    private Integer authorRating;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @NotBlank
    @Min(value = 1, message = "Rating should not be less than 1")
    @Max(value = 5, message = "Rating should not be greater than 5")
    @Column(name = "users_rating")
    private Float usersRating;

    @ManyToMany
    @JoinTable (
            name = "review_rates",
            joinColumns = { @JoinColumn(name = "review_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> raters = new HashSet<>();

    @ManyToMany
    @JoinTable (
            name = "review_likes",
            joinColumns = { @JoinColumn(name = "review_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> likes = new HashSet<>();

    public void addLike(User user) {
        likes.add(user);
    }

    public void addRater(User user) {raters.add(user);}

    public void removeLike(User user) {likes.remove(user);}

    public boolean isLiked(User user) {
        return likes.contains(user);
    }

    public boolean isRated(User user) {
        return raters.contains(user);
    }

    public boolean isAuthor(User user) {return user.getId().equals(getAuthor().getId());}
}
