package com.annieryannel.recommendationsapp.service;

import com.annieryannel.recommendationsapp.DTO.ReviewDto;
import com.annieryannel.recommendationsapp.mappers.ReviewMapper;
import com.annieryannel.recommendationsapp.models.Review;
import com.annieryannel.recommendationsapp.models.User;
import com.annieryannel.recommendationsapp.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewService {

    @Autowired
    UserService userService;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ReviewMapper reviewMapper;

    public List<ReviewDto> loadAll() {
        return reviewRepository.findAll().stream().map(review -> reviewMapper.toDto(review))
                .collect(Collectors.toList());
    }

    public ReviewDto loadById(Long id) {
        return reviewMapper.toDto(reviewRepository.findById(id).get());
    }

    public List<ReviewDto> loadAllByUserId(Long userId) {
        return reviewRepository.findAllByAuthorId(userId).stream().map(review -> reviewMapper.toDto(review))
                .collect(Collectors.toList());
    }

    public void saveReview(ReviewDto reviewDto) {
        reviewRepository.save(reviewMapper.toEntity(reviewDto));
    }

    public Integer likeReview(Long reviewId, String username) {
        User user = userService.getUserByUsername(username);
        Review review = reviewRepository.getById(reviewId);
        review.addLike(user);
        reviewRepository.save(review);
        return review.getLikes().size();
    }

    public Integer unlikeReview(Long reviewId, String username) {
        User user = userService.getUserByUsername(username);
        Review review = reviewRepository.getById(reviewId);
        review.removeLike(user);
        reviewRepository.save(review);
        return review.getLikes().size();
    }

    public List<ReviewDto> search(String text) {
        List<Review> reviews = reviewRepository.search(text);
        return reviews.stream().map(m -> reviewMapper.toDto(m)).collect(Collectors.toList());
    }
}
