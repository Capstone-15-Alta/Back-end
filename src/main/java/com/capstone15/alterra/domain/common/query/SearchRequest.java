package com.capstone15.alterra.domain.common.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchRequest implements Serializable {

    private static final long serialVersionUID = 3574760897008802683L;
    private List<FilterRequest> filters;
    private List<SortRequest> sort;
    private Integer page;
    private Integer size;

    public List<FilterRequest> getFilters() {
        if(Objects.isNull(filters)) return new ArrayList<>();
        return filters;
    }

    public List<SortRequest> getSort() {
        if(Objects.isNull(sort))return new ArrayList<>();
        return sort;
    }
}
