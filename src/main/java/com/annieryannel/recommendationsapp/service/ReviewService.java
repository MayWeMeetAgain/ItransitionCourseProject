package com.annieryannel.recommendationsapp.service;

import com.annieryannel.recommendationsapp.DTO.ReviewDto;
import com.annieryannel.recommendationsapp.DTO.UserDto;
import com.annieryannel.recommendationsapp.mappers.ReviewMapper;
import com.annieryannel.recommendationsapp.models.Review;
import com.annieryannel.recommendationsapp.models.Role;
import com.annieryannel.recommendationsapp.models.User;
import com.annieryannel.recommendationsapp.repositories.ReviewRepository;
import com.annieryannel.recommendationsapp.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.MethodNotAllowedException;
import org.webjars.NotFoundException;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.RequestEntity.post;
import static org.springframework.http.RequestEntity.put;

@Service
@Transactional
public class ReviewService {

    final UserService userService;

    final ReviewRepository reviewRepository;

    final RoleRepository roleRepository;

    final ReviewMapper reviewMapper;

    @Autowired
    public ReviewService(UserService userService, ReviewRepository reviewRepository, RoleRepository roleRepository, ReviewMapper reviewMapper) {
        this.userService = userService;
        this.reviewRepository = reviewRepository;
        this.roleRepository = roleRepository;
        this.reviewMapper = reviewMapper;
    }

    public List<ReviewDto> loadAll() {
        return allReviewsToDto(reviewRepository.findAll());
    }

    public ReviewDto loadById(Long id, Authentication authentication) throws MethodNotAllowedException {
            Review review = reviewRepository.findById(id).get();
            if (!isPermit(review, authentication)) throw new MethodNotAllowedException(HttpMethod.GET, null);
            return reviewMapper.toDto(review);
    }

    public ReviewDto readById(Long id) throws NullPointerException {
        ReviewDto dto = reviewMapper.toDto(reviewRepository.findById(id).get());
        dto.setText(MarkdownService.markdownToHTML(dto.getText()));
        return dto;
    }

    public void saveReview(ReviewDto reviewDto, Authentication authentication) throws MethodNotAllowedException {
        Review review = reviewRepository.getById(reviewDto.getId());
        reviewRepository.save(reviewMapper.setDtoToEntity(reviewDto, review));
        if (!isPermit(review, authentication)) throw new MethodNotAllowedException(HttpMethod.GET, null);
    }

    public void addReview(ReviewDto reviewDto, String authorName) {
        Review review = reviewMapper.toEntity(reviewDto);
        review.setAuthor(userService.getUserByUsername(authorName));
        reviewRepository.save(review);
    }

    public Integer likeReview(Long reviewId, String username) {
        Handler func = Review::addLike;
        return likeHandler(reviewId, username, func);
    }

    public Integer unlikeReview(Long reviewId, String username) {
        Handler func = Review::removeLike;
        return likeHandler(reviewId, username, func);
    }

    public Integer likeHandler(Long reviewId, String username, Handler handler) {
        User user = userService.getUserByUsername(username);
        Review review = reviewRepository.getById(reviewId);
        handler.handle(review, user);
        reviewRepository.save(review);
        return review.getLikes().size();
    }

    public Float rateReview(Integer rate, Long reviewId, String username) {
        User user = userService.getUserByUsername(username);
        Review review = reviewRepository.getById(reviewId);
        Set<User> raters = review.getRaters();
        if (raters.contains(user))
            return review.getUsersRating();
        return setNewRate(review, user, raters.size(), rate);
    }

    public Float setNewRate(Review review, User user, Integer ratersAmount, Integer newRate) {
        Float ratingResult = calculateRating(review.getUsersRating(), ratersAmount, newRate);
        review.setUsersRating(ratingResult);
        review.addRater(user);
        reviewRepository.save(review);
        return ratingResult;
    }

    public Float calculateRating (Float usersRating, Integer ratersAmount, Integer newRate) {
        return (usersRating * ratersAmount + newRate) / (ratersAmount + 1);
    }

    public List<ReviewDto> search(String text) {
        List<Review> reviews = reviewRepository.search(text);
        return allReviewsToDto(reviews);
    }

    public void deleteReviewById(Long reviewId, Authentication authentication) {
        Review review = reviewRepository.getById(reviewId);
        if (isPermit(review, authentication))
            reviewRepository.deleteById(reviewId);
    }

    public boolean isPermit(Review review, Authentication authentication) {
        Role admin = roleRepository.findByRole("ROLE_ADMIN");
        boolean isAdmin = authentication.getAuthorities().contains(admin);
        boolean isAuthor = authentication.getName().equals(review.getAuthor().getUsername());
        return isAdmin || isAuthor;
    }

    public List<ReviewDto> getReviewsByUsername(String username) {
        Long id = userService.getUserByUsername(username).getId();
        List<Review> reviews = reviewRepository.findAllByAuthorId(id);
        return allReviewsToDto(reviews);
    }

    public List<ReviewDto> allReviewsToDto(List<Review> reviews) {
        return reviews.stream().map(reviewMapper::toDto).collect(Collectors.toList());
    }
}

interface Handler {
    void handle(Review x, User y);
}
