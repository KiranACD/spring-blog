package com.example.springblog.articles.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateArticleDTO {

    Long userId;
    String title;
    String subtitle;
    String body;
}
