package com.example.springblog.comments.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateCommentDTO {

    Long userId;
    String slug;
    String title;
    String body;
}
