package com.capstone15.alterra.domain.common.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;

public enum Operator {

    EQUAL {
        public <T> Predicate build(Root<T> root, CriteriaBuilder criteriaBuilder, FilterRequest request, Predicate predicate) {
            Object value = request.getFieldType().parse(request.getValue().toString());
            Expression<?> key = root.get(request.getKey());
            return criteriaBuilder.and(criteriaBuilder.equal(key, value), predicate);

        }
    },
    NOT_EQUAL {
        public <T> Predicate build(Root<T> root, CriteriaBuilder criteriaBuilder, FilterRequest request, Predicate predicate) {
            Object value = request.getFieldType().parse(request.getValue().toString());
            Expression<?> key = root.get(request.getKey());
            return criteriaBuilder.and(criteriaBuilder.notEqual(key, value), predicate);

        }

    },
    LIKE {
        public <T> Predicate build(Root<T> root, CriteriaBuilder criteriaBuilder, FilterRequest request, Predicate predicate) {

            Expression<String> key = root.get(request.getKey());
            return criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.upper(key), "%" + request.getValue().toString().toUpperCase() + "%"), predicate);

        }
    },
    BETWEEN {
        public <T> Predicate build(Root<T> root, CriteriaBuilder criteriaBuilder, FilterRequest request, Predicate predicate) {
            Object value = request.getFieldType().parse(request.getValue().toString());
            Object value2 = request.getFieldType().parse(request.getValueTo().toString());
            if (request.getFieldType() == FieldType.DATE) {
                LocalDateTime startDate = (LocalDateTime) value;
                LocalDateTime endDate = (LocalDateTime) value2;
                Expression<LocalDateTime> key = root.get(request.getKey());
                return criteriaBuilder.and(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(key, startDate), criteriaBuilder.lessThanOrEqualTo(key, endDate)), predicate);
            }
            if (request.getFieldType() != FieldType.BOOLEAN && request.getFieldType() != FieldType.STRING) {
                Number startNum = (Number) value;
                Number endNum = (Number) value2;
                Expression<Number> key = root.get(request.getKey());
                return criteriaBuilder.and(criteriaBuilder.and(criteriaBuilder.ge(key, startNum), criteriaBuilder.le(key, endNum)), predicate);
            }
            return predicate;

        }
    };

    public abstract <T> Predicate build(Root<T> root, CriteriaBuilder criteriaBuilder, FilterRequest request, Predicate predicate);
}
