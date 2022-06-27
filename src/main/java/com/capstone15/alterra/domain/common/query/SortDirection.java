package com.capstone15.alterra.domain.common.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

public enum SortDirection {
    ASC {
        public <T> Order build(Root<T> root, CriteriaBuilder criteriaBuilder, SortRequest request) {
            return criteriaBuilder.asc(root.get(request.getKey()));
        }


    },
    DSC {
        public <T> Order build(Root<T> root, CriteriaBuilder criteriaBuilder, SortRequest request) {
            return criteriaBuilder.desc(root.get(request.getKey()));
        }
    };

    public abstract  <T> Order build(Root<T> root, CriteriaBuilder criteriaBuilder, SortRequest request);
}
