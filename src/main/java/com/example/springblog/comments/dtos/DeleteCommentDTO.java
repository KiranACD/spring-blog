package com.example.springblog.comments.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeleteCommentDTO {
    
    private Long id;
    private Long userId;
    private String articleSlug;
}
