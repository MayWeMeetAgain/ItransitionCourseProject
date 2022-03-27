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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.RequestEntity.post;
import static org.springframework.http.RequestEntity.put;

@Service
@Transactional
public class ReviewService {

    @Autowired
    UserService userService;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    RoleRepository roleRepository;

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

    public ReviewDto readById(Long id) {
        ReviewDto dto = loadById(id);
        dto.setText(MarkdownService.markdownToHTML(dto.getText()));
        return dto;
    }

    public void saveReview(ReviewDto reviewDto) {
        Review review = reviewRepository.getById(reviewDto.getId());
        review.setText(reviewDto.getText());
        review.setTitle(reviewDto.getTitle());
        review.setCategory(reviewDto.getCategory());
        reviewRepository.save(review);
    }

    public void addReview(ReviewDto reviewDto, String authorName) {
        UserDto userDto = userService.getUserDtoByUsername(authorName);
        reviewDto.setAuthor(userDto);
        Review review = reviewMapper.toEntity(reviewDto);
        review.setAuthor(userService.getUserByUsername(authorName));
        reviewRepository.save(review);
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

    public Float rateReview(Integer rate, Long reviewId, String username) {
        User user = userService.getUserByUsername(username);
        Review review = reviewRepository.getById(reviewId);
        Set<User> raters = review.getRaters();
        Float userRating = review.getUsersRating();
        if (raters.contains(user))
            return userRating;
        review.setUsersRating((userRating * raters.size() + rate) / (raters.size() + 1));

        review.addRater(user);
        reviewRepository.save(review);
        return review.getUsersRating();
    }

    public List<ReviewDto> search(String text) {
        List<Review> reviews = reviewRepository.search(text);
        return reviews.stream().map(m -> reviewMapper.toDto(m)).collect(Collectors.toList());
    }

    public void deleteReviewById(Long reviewId, Authentication authentication) {
        Role admin = roleRepository.findByRole("ROLE_ADMIN");
        Review review = reviewRepository.getById(reviewId);
        if (authentication.getAuthorities().contains(admin) || authentication.getName().equals(review.getAuthor().getUsername()) )
            reviewRepository.deleteById(reviewId);
    }

    public List<ReviewDto> getReviewsByUsername(String username) {
        Long id = userService.getUserByUsername(username).getId();
        List<Review> reviews = reviewRepository.findAllByAuthorId(id);
        return reviews.stream().map(m -> reviewMapper.toDto(m)).collect(Collectors.toList());
    }
}
