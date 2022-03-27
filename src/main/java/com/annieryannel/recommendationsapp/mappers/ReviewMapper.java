package com.annieryannel.recommendationsapp.mappers;


import com.annieryannel.recommendationsapp.DTO.ReviewDto;
import com.annieryannel.recommendationsapp.models.Review;
import com.annieryannel.recommendationsapp.models.User;
import com.annieryannel.recommendationsapp.repositories.UserRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class ReviewMapper {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRepository userRepository;

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Review.class, ReviewDto.class)
                .addMappings(m -> m.skip(ReviewDto::setLikes))
                .addMappings(m -> m.skip(ReviewDto::setLiked))
                .addMappings(m-> m.skip(ReviewDto::setAuthor))
                .addMappings(m -> m.skip(ReviewDto::setReadOnlyMode))
                .addMappings(m->m.skip(ReviewDto::setRated))
                .setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(ReviewDto.class, Review.class)
                .addMappings(m -> m.skip(Review::setLikes))
                .addMappings(m -> m.skip(Review::setAuthor))
                .setPostConverter(toEntityConverter());
    }

    public ReviewDto toDto(Review review) {
        return modelMapper.map(review, ReviewDto.class);
    }

    public Review toEntity(ReviewDto dto) {
        return modelMapper.map(dto, Review.class);
    }

    public Converter<Review, ReviewDto> toDtoConverter() {
        return context -> {
            Review source = context.getSource();
            ReviewDto destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    public Converter<ReviewDto, Review> toEntityConverter() {
        return context -> {
            ReviewDto source = context.getSource();
            Review destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    private void mapSpecificFields(Review source, ReviewDto destination) {
        destination.setLikes(source.getLikes().size());
        destination.setAuthor(userMapper.toDto(source.getAuthor()));
        mapCurrentUserFields(source, destination);
    }

    private void mapCurrentUserFields(Review source, ReviewDto destination) {
        try {
        User currentUser = userRepository.findByUsername(((UserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal()).getUsername());
            destination.setLiked(source.getLikes().contains(currentUser));
            destination.setReadOnlyMode(!source.getAuthor().equals(currentUser));
            destination.setRated(source.getRaters().contains(currentUser));
        } catch (Exception e) {
            destination.setLiked(false);
            destination.setReadOnlyMode(true);
            destination.setRated(false);
        }
    }

    private void mapSpecificFields(ReviewDto source, Review destination) {
        destination.setAuthor(userMapper.toEntity(source.getAuthor()));
    }
}
