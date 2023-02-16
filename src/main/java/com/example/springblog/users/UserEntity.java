package com.example.springblog.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import java.util.List;

import com.example.springblog.commons.BaseEntity;

import lombok.Getter;
import lombok.Setter;

import com.example.springblog.articles.ArticleEntity;

@Entity(name="users")
@Getter
@Setter
public class UserEntity extends BaseEntity {
    
    @Column(unique=true, nullable=false, length=50)
    String username;
    String password;
    String email;
    String bio;
    String image;

    @ManyToMany(mappedBy="likedBy")
    List<ArticleEntity> likeArticles;

    @ManyToMany
    @JoinTable(
        name = "user_follows",
        joinColumns = @JoinColumn(name="follower_id"),
        inverseJoinColumns = @JoinColumn(name="following_id")
    )
    List<UserEntity> following;

    @ManyToMany(mappedBy="following")
    List<UserEntity> followers;
}
