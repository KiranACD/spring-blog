package com.example.springblog.comments;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import com.example.springblog.articles.ArticlesService;
import com.example.springblog.articles.ArticlesRepository;
import com.example.springblog.users.UsersRepository;
import com.example.springblog.users.UsersService;
import com.example.springblog.comments.dtos.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Service
public class CommentsService {
    private final CommentsRepository commentsRepository;
    private final ArticlesRepository articlesRepository;
    private final UsersRepository usersRepository;
    // private final ArticlesService articlesService;
    private ModelMapper modelMapper;

    public CommentsService(
            CommentsRepository commentsRepository,
            ArticlesRepository articlesRepository,
            UsersRepository usersRepository,
            ModelMapper modelMapper) {
        this.commentsRepository = commentsRepository;
        this.articlesRepository = articlesRepository;
        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
    }

    public List<CommentResponseDTO> getAllCommentsOfArticle(GetCommentsDTO getCommentsDTO) {

        String slug = getCommentsDTO.getSlug();
        var article = articlesRepository.findBySlug(slug);
        if (article == null) {
            throw new ArticlesService.ArticleDoesNotExistException(slug);
        }
        List<CommentEntity> pagedComments = commentsRepository.findAllByArticleId(article.getId(), getCommentsDTO.getPageable());
        if (pagedComments == null) {
            throw new CommentDoesNotExistException(slug);
        }

        //List<CommentEntity> comments = pagedComments.getContent();
        List<CommentResponseDTO> commentResponseDTOs = new ArrayList<>();
        for (CommentEntity comment: pagedComments) {
            CommentResponseDTO commentResponseDTO = modelMapper.map(comment, CommentResponseDTO.class);
            commentResponseDTOs.add(commentResponseDTO);
        }
        return commentResponseDTOs;
    }

    public CommentResponseDTO createComment(CreateCommentDTO createCommentDTO) {
        var article = articlesRepository.findBySlug(createCommentDTO.getSlug());
        var userOptional = usersRepository.findById(createCommentDTO.getUserId());
        if (userOptional.isEmpty()) throw new UsersService.UserNotFoundException(createCommentDTO.getUserId());
        if (article == null) {
            throw new ArticlesService.ArticleDoesNotExistException(createCommentDTO.getSlug());
        }
        CommentEntity commentEntity = modelMapper.map(createCommentDTO, CommentEntity.class);
        commentEntity.setArticle(article);
        commentEntity.setAuthor(userOptional.get());
        commentEntity.setCreatedAt(new Date());
        var savedComment = commentsRepository.save(commentEntity);
        CommentResponseDTO commentResponseDTO = modelMapper.map(savedComment, CommentResponseDTO.class);
        commentResponseDTO.setArticleSlug(savedComment.getArticle().getSlug());
        return commentResponseDTO;
    }

    public CommentResponseDTO deleteComment(DeleteCommentDTO deleteCommentDTO) {
        String slug = deleteCommentDTO.getArticleSlug();
        var article = articlesRepository.findBySlug(slug);
        if (article == null) {
            throw new ArticlesService.ArticleDoesNotExistException(slug);
        }
        Long commentId = deleteCommentDTO.getId();
        Long userId = deleteCommentDTO.getUserId();
        var commentOptional = commentsRepository.findById(commentId);
        if (!commentOptional.isPresent()) {
            throw new CommentDoesNotExistException(commentId);
        }
        var comment = commentOptional.get();
        Long authorId = comment.getAuthor().getId();
        if (authorId != userId) {
            throw new CommentAuthorNotUserException(userId, authorId);
        }
        commentsRepository.deleteById(commentId);
        CommentResponseDTO commentResponseDTO = modelMapper.map(comment, CommentResponseDTO.class);
        commentResponseDTO.setUserId(userId);
        return commentResponseDTO;

    }

    public static class CommentDoesNotExistException extends IllegalArgumentException {
        public CommentDoesNotExistException(Long id) {
            super("Comment with Id: " + id + " does not exist");
        }

        public CommentDoesNotExistException(String slug) {
            super("Comments for article with slug: " + slug + " does not exist");
        }
    }

    public static class CommentAuthorNotUserException extends IllegalStateException {
        public CommentAuthorNotUserException(Long userId, Long authorId) {
            super("User with user id: " + userId + " is not the author of comment with id " + authorId);
        }
    }
}   

