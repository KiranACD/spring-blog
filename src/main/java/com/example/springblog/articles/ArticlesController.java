package com.example.springblog.articles;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springblog.articles.dtos.*;
import com.example.springblog.users.UsersService;

@RestController
@RequestMapping("/articles")
public class ArticlesController {
    private final ArticlesService articlesService;

    public ArticlesController(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }

    @PostMapping("")
    public ResponseEntity<ArticleResponseDTO> createArticle(@RequestBody CreateArticleDTO createArticleDTO, @AuthenticationPrincipal Long userId) {
        ArticleResponseDTO articleResponseDTO = articlesService.createArticle(createArticleDTO);
        articleResponseDTO.setUserId(userId);
        return ResponseEntity.created(URI.create("/articles/"+articleResponseDTO.getSlug())).body(articleResponseDTO);
    } 

    @GetMapping("")
    public String getArticles()  {
        return "Articles";
    }

    @GetMapping("/private")
    public String getArticlesPrivate(@AuthenticationPrincipal Long userId)  {
        return "Articles";
    }

    @GetMapping("/{article-slug}")
    public ResponseEntity<ArticleResponseDTO> getArticleBySlug(@PathVariable("article-slug") String slug, @AuthenticationPrincipal Long userId) {
        var articleResponse = articlesService.getArticleBySlug(slug);
        articleResponse.setUserId(userId);
        return ResponseEntity.ok(articleResponse);
    }

    @PatchMapping("/{article-slug}")
    public ResponseEntity<ArticleResponseDTO> updateArticle(@PathVariable("article-slug") String slug, @RequestBody UpdateArticleDTO updateArticleDTO, @AuthenticationPrincipal Long userId) {
        updateArticleDTO.setSlug(slug);
        updateArticleDTO.setUserId(userId);
        ArticleResponseDTO articleResponseDTO = articlesService.updateArticle(updateArticleDTO);
        articleResponseDTO.setUserId(userId);
        return ResponseEntity.accepted().body(articleResponseDTO);
    }

    @ExceptionHandler(UsersService.UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UsersService.UserNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ArticlesService.UserNotAuthorException.class)
    public ResponseEntity<String> handleUserNotAuthorException(ArticlesService.UserNotAuthorException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ArticlesService.ArticleDoesNotExistException.class)
    public ResponseEntity<String> handleArticleDoesNotExistException(ArticlesService.ArticleDoesNotExistException e) {
        return ResponseEntity.notFound().build();
    }

}
