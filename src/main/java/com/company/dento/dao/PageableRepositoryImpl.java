package com.company.dento.dao;

import com.company.dento.dao.PageableRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class PageableRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements PageableRepository<T, ID> {

    public PageableRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Override
    public List<T> findAll(Specification<T> spec, int offset, int limit, Sort sort) {
        final TypedQuery<T> query = getQuery(spec, sort);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }
}
