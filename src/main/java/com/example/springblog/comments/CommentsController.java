package com.example.springblog.comments;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.example.springblog.comments.dtos.*;
import com.example.springblog.users.UsersService;
import com.example.springblog.articles.ArticlesService;

import java.util.List;

@RestController
public class CommentsController {
    private final CommentsService commentsService;

    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @GetMapping("/article/{article-slug}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getAllCommentsOfArticle(
                                @PathVariable("article-slug") String slug,
                                @RequestParam(name="page", defaultValue="0") Integer pageNo,
                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                @RequestParam(name="sort", defaultValue="id") String sortBy) {
        
        
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        GetCommentsDTO getCommentsDTO = new GetCommentsDTO();
        getCommentsDTO.setSlug(slug);
        getCommentsDTO.setPageable(pageable);
        var commentResponseDTOs = commentsService.getAllCommentsOfArticle(getCommentsDTO);
        return ResponseEntity.ok(commentResponseDTOs);
    }

    @PostMapping("article/{article-slug}/comments")
    public ResponseEntity<CommentResponseDTO> createCommentForArticle(@PathVariable("article-slug") String slug, @RequestBody CreateCommentDTO createCommentDTO, @AuthenticationPrincipal Long userId) {
        createCommentDTO.setSlug(slug);
        createCommentDTO.setUserId(userId);
        var commentResponseDTO = commentsService.createComment(createCommentDTO);
        commentResponseDTO.setUserId(userId);
        return ResponseEntity.created(URI.create("/article/"+commentResponseDTO.getArticleSlug()+"/"+commentResponseDTO.getId())).body(commentResponseDTO);
    }

    @DeleteMapping("article/{article-slug}/comments/{comment-id}")
    public ResponseEntity<CommentResponseDTO> deleteComment(@PathVariable("article-slug") String slug, @PathVariable("comment-id") Long commentId, @AuthenticationPrincipal Long userId) {
        DeleteCommentDTO deleteCommentDTO = new DeleteCommentDTO();
        deleteCommentDTO.setId(commentId);
        deleteCommentDTO.setUserId(userId);
        deleteCommentDTO.setArticleSlug(slug);
        var commentResponseDTO = commentsService.deleteComment(deleteCommentDTO);
        return ResponseEntity.accepted().body(commentResponseDTO);
    }

    @ExceptionHandler(UsersService.UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UsersService.UserNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(CommentsService.CommentAuthorNotUserException.class)
    public ResponseEntity<String> handleUserNotAuthorException(CommentsService.CommentAuthorNotUserException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(CommentsService.CommentDoesNotExistException.class)
    public ResponseEntity<String> handleArticleDoesNotExistException(CommentsService.CommentDoesNotExistException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ArticlesService.ArticleDoesNotExistException.class)
    public ResponseEntity<String> handleArticleDoesNotExistException(ArticlesService.ArticleDoesNotExistException e) {
        return ResponseEntity.notFound().build();
    }
}