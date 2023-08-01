package com.example.springblog.comments.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDTO {

    Long id;
    Long userId;
    String articleSlug;
    String title;
    String body;
    String createdAt;
}
