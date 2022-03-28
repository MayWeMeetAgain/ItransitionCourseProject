package com.annieryannel.recommendationsapp.repositories;

import com.annieryannel.recommendationsapp.models.Review;

import java.util.List;

public interface SearchRepository {

    List<Review> search(String terms);

}