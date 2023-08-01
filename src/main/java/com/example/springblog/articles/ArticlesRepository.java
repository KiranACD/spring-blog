package com.example.springblog.articles;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticlesRepository extends JpaRepository<ArticleEntity, Long> {
    
    ArticleEntity findByAuthorId(Long authorId);
    ArticleEntity findBySlug(String slug);
    ArticleEntity getReferenceBySlug(String slug);
}
