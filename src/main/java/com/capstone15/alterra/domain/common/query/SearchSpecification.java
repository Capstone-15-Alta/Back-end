package com.capstone15.alterra.domain.common.query;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class SearchSpecification<T> implements Specification<T> {


    private static final long serialVersionUID = -5668008640673968593L;

    private final transient SearchRequest request;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        Predicate predicate = criteriaBuilder.equal(criteriaBuilder.literal(Boolean.TRUE), Boolean.TRUE);

        for (FilterRequest filter : request.getFilters()) {
            predicate = filter.getOperator().build(root, criteriaBuilder, filter, predicate);
        }
        List<Order> orders = new ArrayList<>();
        for (SortRequest sort : request.getSort()) {
            orders.add(sort.getDirection().build(root, criteriaBuilder, sort));
        }
        query.orderBy(orders);
        return predicate;

    }

    public static Pageable getPageable(Integer page, Integer size) {

        return PageRequest.of(Objects.requireNonNullElse(page, 0), Objects.requireNonNullElse(size, 100));
    }
}
