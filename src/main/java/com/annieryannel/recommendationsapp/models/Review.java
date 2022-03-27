package com.annieryannel.recommendationsapp.models;

import com.annieryannel.recommendationsapp.utils.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;



import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
    private String title;

    @FullTextField
    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;

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
}
