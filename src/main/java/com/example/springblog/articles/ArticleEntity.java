package com.example.springblog.articles;

import com.example.springblog.commons.BaseEntity;
import com.example.springblog.users.UserEntity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;

import java.util.List;

@Entity(name="articles")
@Getter
@Setter
public class ArticleEntity extends BaseEntity {
    
    @Column(unique=true, nullable=false, length=150)
    String slug;
    @Column(nullable=false, length=200)
    String title;
    String subtitle;
    @Column(nullable=false, length=8000)
    String body;

    @ManyToOne
    UserEntity author;

    @ManyToMany
    @JoinTable(
        name="article_likes",
        joinColumns=@JoinColumn(name="article_id"),
        inverseJoinColumns=@JoinColumn(name="user_id")
    )
    List<UserEntity> likedBy;
}
