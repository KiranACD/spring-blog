package com.example.springblog.comments;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.example.springblog.commons.BaseEntity;
import com.example.springblog.users.UserEntity;
import com.example.springblog.articles.ArticleEntity;

@Entity(name="comments")
public class CommentEntity extends BaseEntity {
    
    @Column(nullable=false, length=100)
    String title;
    @Column(length=1000)
    String body;

    @ManyToOne
    UserEntity author;

    @ManyToOne
    ArticleEntity article;
}
