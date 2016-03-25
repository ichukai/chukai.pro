package com.wck.blog.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.wck.blog.bean.Article;
import com.wck.blog.bean.ArticleDraft;
import com.wck.blog.bean.BaseController;
import com.wck.blog.service.ArticleService;
import com.wck.durable.bean.PageIn;

/**
 * 基于rest风格的文章,草稿WS
 * @author ChuKai
 *
 */
@RestController
@RequestMapping("/")
public class ArticleController extends BaseController {
	@Autowired
	private ArticleService articleService;
	
	/**
	 * 根据id查询文章
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/article/{id}", method = RequestMethod.GET)
	public @ResponseBody ModelAndView getArticle(@PathVariable Integer id, HttpServletRequest request) {
		Article article = articleService.findArticleById(id);
		return modelAndView("article").addObject("article", article).addObject("typeId", article.getTypeId());
	}
	/**
	 * 查询文章list
	 * @param pageIn
	 * @return
	 */
	@RequestMapping(value = "/articles", method = RequestMethod.GET)
	public @ResponseBody List<Article> getArticles(@ModelAttribute PageIn pageIn) {
		return articleService.findArticlesListByAjax(pageIn);
	}
	
	/**
	 * 根据id查询草稿
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/articleDraft/{id}", method = RequestMethod.GET)
	public @ResponseBody ArticleDraft getArticleDraft(@PathVariable Integer id,@RequestParam(required=false)Integer version) {
		ArticleDraft articleDraft=new ArticleDraft();
		articleDraft.setId(id);
		articleDraft.setVersion(version);
		return this.articleService.findArticleDraft(articleDraft);
	}
	
	/**
	 * 保存修改草稿
	 * @param articleDraft
	 * @return
	 */
	@RequestMapping(value = "/articleDraft", method = RequestMethod.PUT)
	public @ResponseBody Map<String, Object> putArticleDraft(@ModelAttribute ArticleDraft articleDraft) {
		this.articleService.update(articleDraft);
		return successMap();
	}
	
	/**
	 * 保存新建草稿
	 * @param articleDraft
	 * @return
	 */
	@RequestMapping(value = "/articleDraft", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> postArticle(@ModelAttribute ArticleDraft articleDraft) {
		this.articleService.insert(articleDraft);
		return successMap();
	}
	/**
	 * 删除草稿
	 * @param articleDraft
	 * @return
	 */
	@RequestMapping(value = "/articleDraft/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Map<String, Object> postArticle(@PathVariable Integer id) {
		this.articleService.delete(id);
		return successMap();
	}
	/**
	 * 分页查询草稿
	 * @param pageIn
	 * @return
	 */
	@RequestMapping(value = "/articleDrafts",method=RequestMethod.GET)
	public @ResponseBody List<ArticleDraft> getArticleDrafts(@ModelAttribute PageIn pageIn){
		return articleService.findArticleDraftsListByAjax(pageIn);
	}
}
