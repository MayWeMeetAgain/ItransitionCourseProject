package com.annieryannel.recommendationsapp.mappers;

import com.annieryannel.recommendationsapp.DTO.ReviewDto;
import com.annieryannel.recommendationsapp.DTO.UserDto;
import com.annieryannel.recommendationsapp.models.Review;
import com.annieryannel.recommendationsapp.models.User;
import com.annieryannel.recommendationsapp.repositories.ReviewRepository;
import com.annieryannel.recommendationsapp.repositories.UserRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class UserMapper {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ReviewRepository reviewRepository;

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(User.class, UserDto.class)
                .addMappings(m -> m.skip(UserDto::setLikes))
                .setPostConverter(toDtoConverter());
    }

    public UserDto toDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public User toEntity(UserDto dto) {
        return modelMapper.map(dto, User.class);
    }

    public Converter<User, UserDto> toDtoConverter() {
        return context -> {
            User source = context.getSource();
            UserDto destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    public Converter<UserDto, User> toEntityConverter() {
        return context -> {
            UserDto source = context.getSource();
            User destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    private void mapSpecificFields(User source, UserDto destination) {
        destination.setLikes(countLikes(source));
    }


    public Integer countLikes(User user) {
        int count = 0;
        List<Review> reviews = reviewRepository.findAllByAuthorId(user.getId());
        for (Review review : reviews)
            count += review.getLikes().size();
        return count;
    }

    private void mapSpecificFields(UserDto source, User destination) {
    }
}
