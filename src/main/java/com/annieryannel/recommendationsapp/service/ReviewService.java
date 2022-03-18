package com.annieryannel.recommendationsapp.service;

import com.annieryannel.recommendationsapp.DTO.CardDTO;
import com.annieryannel.recommendationsapp.models.Review;
import com.annieryannel.recommendationsapp.models.User;
import com.annieryannel.recommendationsapp.repositories.ReviewRepository;
import com.annieryannel.recommendationsapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    UserService userService;

    @Autowired
    ReviewRepository reviewRepository;


    public List<Review> loadAllReviews() {
        return reviewRepository.findAll();
    }

    public Review loadReviewById(Long id) {
        return reviewRepository.findById(id).get();
    }

    public List<Review> loadAllByUserId(Long userId) {
        return reviewRepository.findAllByAuthorId(userId);
    }

    public List<CardDTO> loadAllCardsByUserId(Long userId) {
        List<Review> reviews = loadAllByUserId(userId);
        List<CardDTO> cards = new ArrayList<>();
        for (Review review : reviews)
            cards.add(makeDto(review));
        return cards;
    }

    public CardDTO makeDto(Review review) {
        CardDTO card = new CardDTO();
        card.setReviewId(review.getId());
        card.setTitle(review.getTitle());
        card.setAuthorName(userService.getUsernameById(review.getAuthorId()));
        card.setLikes((long) review.getLikes().size());
        try {
            card.setLiked(review.getLikes().contains(userService.getCurrentUser()));
        } catch (Exception e) {
            card.setLiked(false);
        }
        card.setText(review.getText());
        //card.setReadOnlyMode(userService.getCurrentUser().getId() == review.getAuthorId());
        return card;
    }


    public List<CardDTO> loadAllCards() {
        List<Review> reviews = loadAllReviews();
        return reviews.stream().map(this::makeDto).collect(Collectors.toList());
    }

    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    public Review createReviewFromDto(CardDTO dto) {
        Review review = new Review();
        review.setAuthorId(userService.getUserByUsername(dto.getAuthorName()).getId());
        review.setTitle(dto.getTitle());
        review.setText(dto.getText());
        return review;
    }

    public void saveCard(CardDTO dto) {
        saveReview(createReviewFromDto(dto));
    }

    public Integer likeReview(Long reviewId, String username) {
        User user = userService.getUserByUsername(username);
        Review review = reviewRepository.getById(reviewId);
        review.addLike(user);
        saveReview(review);
        return review.getLikes().size();
    }

    public Integer unlikeReview(Long reviewId, String username) {
        User user = userService.getUserByUsername(username);
        Review review = reviewRepository.getById(reviewId);
        review.removeLike(user);
        saveReview(review);
        return review.getLikes().size();
    }
}
