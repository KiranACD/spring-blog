package com.example.springblog.articles;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import com.example.springblog.users.UsersRepository;
import com.example.springblog.articles.dtos.CreateArticleDTO;
import com.example.springblog.users.UserEntity;
import com.example.springblog.users.UsersService;
import com.example.springblog.articles.dtos.*;
@Service
public class ArticlesService {
    private ArticlesRepository articlesRepository;
    private UsersRepository usersRepository;
    private UsersService usersService;
    ModelMapper modelMapper;

    public ArticlesService(ArticlesRepository articlesRepository,
                           UsersRepository usersRepository,
                           UsersService usersService,
                           ModelMapper modelMapper) {

        this.articlesRepository = articlesRepository;
        this.usersRepository = usersRepository;
        this.usersService = usersService;
        this.modelMapper = modelMapper;
    }

    public ArticleResponseDTO createArticle(CreateArticleDTO createArticleDTO) {
        Long userId = createArticleDTO.getUserId();
        var authorOptional = this.usersRepository.findById(userId);
        if (authorOptional.isPresent()) {
            UserEntity author = authorOptional.get();
            ArticleEntity newArticle = modelMapper.map(createArticleDTO, ArticleEntity.class);
            newArticle.setAuthor(author);
            newArticle.setSlug(newArticle.getTitle().toLowerCase().replaceAll("\\s+", "-") + '-' + author.getUsername());
            var savedArticle = this.articlesRepository.save(newArticle);
            ArticleResponseDTO articleResponseDTO = modelMapper.map(savedArticle, ArticleResponseDTO.class);
            return articleResponseDTO;
        } else throw new UsersService.UserNotFoundException(userId);
        
    }

    public ArticleResponseDTO updateArticle(UpdateArticleDTO updateArticleDTO) {

        Long userId = updateArticleDTO.getUserId();
        String slug = updateArticleDTO.getSlug();
        var articleEntity = this.articlesRepository.getReferenceBySlug(slug);

        if(articleEntity == null) {
            throw new ArticleDoesNotExistException(slug);
        }
        Long authorId = articleEntity.getAuthor().getId();
        Long articleId = articleEntity.getId();
        if (authorId != userId) {
            throw new UserNotAuthorException(userId, articleId);
        }
        if (updateArticleDTO.getTitle() != null) {
            articleEntity.setTitle(updateArticleDTO.getTitle());
            articleEntity.setSlug(articleEntity.getTitle().toLowerCase().replaceAll("\\s+", "-") + "-" + articleEntity.getAuthor().getUsername());
        }
        if(updateArticleDTO.getSubtitle() != null) articleEntity.setSubtitle(updateArticleDTO.getSubtitle());
        if (updateArticleDTO.getBody() != null) articleEntity.setBody(updateArticleDTO.getBody());
        var updatedArticle = articlesRepository.save(articleEntity);
        ArticleResponseDTO articleResponseDTO = modelMapper.map(updatedArticle, ArticleResponseDTO.class);
        return articleResponseDTO;
    }

    public ArticleResponseDTO getArticleBySlug(String slug) {
        var article = this.articlesRepository.findBySlug(slug);
        if (article == null) throw new ArticleDoesNotExistException(slug);
        ArticleResponseDTO articleResponseDTO = modelMapper.map(article, ArticleResponseDTO.class);
        return articleResponseDTO;
    }
    

    public static class UserNotAuthorException extends IllegalStateException {
        public UserNotAuthorException(Long userId, Long articleId) {
            super("User with user id: " + userId + " is not the author of article with id " + articleId);
        }
    }

    public static class ArticleDoesNotExistException extends IllegalArgumentException {
        public ArticleDoesNotExistException(Long id) {
            super("Article with articleId: " + id + " does not exist");
        }
        public ArticleDoesNotExistException(String slug) {
            super("Article with slug: " + slug + " does not exist");
        }
    }


}
