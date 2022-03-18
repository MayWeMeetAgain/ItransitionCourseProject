package com.annieryannel.recommendationsapp.repositories;

import com.annieryannel.recommendationsapp.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByAuthorId(Long authorId);

}
