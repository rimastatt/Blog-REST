package lt.codeacademy.controller;

import lt.codeacademy.dto.ArticleDTO;
import lt.codeacademy.entity.Article;
import lt.codeacademy.entity.Theme;
import lt.codeacademy.service.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<ArticleDTO> getArticlesByTheme(@RequestParam(defaultValue = "0") int pageNumber, @PathVariable(required = false) Long themeId) {
        Page<Article> articlesEntityPage = articleService.getAllArticlesByTheme(pageNumber, themeId);
        List<Article> articlesEntity = articlesEntityPage.getContent();
        return ArticleDTO.fromArticleEntityListToDTO(articlesEntity);

    }

    @GetMapping("/article/{id}")
    public ArticleDTO getArticle(@PathVariable Long id) {
        Article article = articleService.getArticleById(id);
        return ArticleDTO.fromArticleEntityToDTO(article);
    }

    @PostMapping("/article")
    public Article createArticle(
            @RequestParam(name = "title") String title,
            @RequestParam(name = "theme") Theme theme,
            @RequestParam(name = "picture", required = false) MultipartFile picture,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "text") String text,
            @RequestParam(name = "tag") String tag,
            @RequestParam(name = "date", required = false) Date date) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setTag(tag);
        articleDTO.setDescription(description);
        articleDTO.setText(text);
        articleDTO.setDate(date);
        articleDTO.setTitle(title);
        articleDTO.setTheme(theme);
        return articleService.createArticle(articleDTO, picture);
    }

    @GetMapping("{tag}")
    public Page<Article> getAllArticlesByTag(@PathVariable String tag, @RequestParam(defaultValue = "0") int pageNumber) {
        return articleService.getAllArticlesByTag(pageNumber, tag);
    }
}
