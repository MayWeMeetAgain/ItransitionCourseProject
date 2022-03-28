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

    final ModelMapper modelMapper;

    final ReviewRepository reviewRepository;

    @Autowired
    public UserMapper(ModelMapper modelMapper, ReviewRepository reviewRepository) {
        this.modelMapper = modelMapper;
        this.reviewRepository = reviewRepository;
    }

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
        List<Review> reviews = reviewRepository.findAllByAuthorId(user.getId());
        return reviews.stream().map(x -> x.getLikes().size()).reduce(0, Integer::sum);
    }

    private void mapSpecificFields(UserDto source, User destination) {
    }
}
