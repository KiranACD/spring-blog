package com.example.springblog.comments.dtos;

import org.springframework.data.domain.Pageable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetCommentsDTO {

    private String slug;
    private Pageable pageable;
    
}
