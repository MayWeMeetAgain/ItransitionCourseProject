package com.annieryannel.recommendationsapp.repositories.impl;

import com.annieryannel.recommendationsapp.models.Review;
import com.annieryannel.recommendationsapp.repositories.SearchRepository;
import org.hibernate.search.mapper.orm.Search;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class SearchRepositoryImpl implements SearchRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Review> search(String terms) {
        return Search.session(em).search(Review.class)
                .where(f -> f.match()
                        .fields("title", "text")
                        .matching(terms)).fetchAllHits();
    }
}
