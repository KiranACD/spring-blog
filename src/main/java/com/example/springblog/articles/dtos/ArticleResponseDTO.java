package com.example.springblog.articles.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleResponseDTO {
    String slug;
    String title;
    String subtitle;
    String body;
    String createdAt;
    Long userId;

}
