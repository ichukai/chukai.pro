package com.wenchukai.blog.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.wenchukai.blog.annotation.Authority;
import com.wenchukai.blog.dto.PageIn;
import com.wenchukai.blog.enumerate.AuthorityEnum;
import com.wenchukai.blog.model.ArticleDraft;
import com.wenchukai.blog.model.User;
import com.wenchukai.blog.service.ArticleService;
import com.wenchukai.blog.service.UserService;
import com.wenchukai.blog.session.WebSessionSupport;
import com.wenchukai.common.base.BaseController;

/**
 * admin后台
 * 
 * @author ChuKai
 *
 */
@RestController
@RequestMapping("/admin")
@Authority
public class AdminController extends BaseController {
	@Autowired
	private UserService userService;

	@Autowired
	private ArticleService articleService;
	@Autowired
	private WebSessionSupport webSessionSupport;

	/**
	 * 首页
	 * 
	 * @return
	 */
	@RequestMapping(value = { "", "/" })
	public ModelAndView index() {
		return modelAndView("/admin/index");
	}

	/**
	 * 登录页,已登录则跳转首页
	 * 
	 * @return
	 */
	@Authority(authority = AuthorityEnum.VISITOR)
	@RequestMapping(value = { "/signIn" }, method = RequestMethod.GET)
	public ModelAndView signIn() {
		if (webSessionSupport.isSignIn()) {
			return new ModelAndView("redirect:/admin");
		}
		return modelAndView("/admin/signIn");
	}

	/**
	 * 登录操作
	 * 
	 * @param user
	 * @param response
	 * @return
	 */
	@Authority(authority = AuthorityEnum.VISITOR)
	@RequestMapping(value = { "/signIn" }, method = RequestMethod.POST)
	public ModelAndView signIn(@ModelAttribute User user, HttpServletResponse response) {
		User admin = userService.signInAdmin(user);
		if (admin == null) {
			return modelAndView("/admin/signIn").addObject("user", user);
		}
		// 回写sessionId cookie
		Cookie cookie = new Cookie(webSessionSupport.SESSION_ID, admin.getSessionId());
		cookie.setPath("/");// cookie 必须设置为根路径,否则会导致其他子路径无法拿到cookie
		response.addCookie(cookie);
		return new ModelAndView("redirect:/admin");
	}

	/**
	 * 后台文章编辑器页
	 * 
	 * @param article
	 * @return
	 */
	@RequestMapping("/editors")
	public ModelAndView editors(@ModelAttribute ArticleDraft articleDraft) {
		return modelAndView("/admin/editors").addObject("articleTypes", articleService.findAllArticleTypes())
				.addObject("articleDraftId", articleDraft.getId() == null
						? articleService.findArticleDraftIdByArticleId(articleDraft) : articleDraft.getId());
	}

	/**
	 * 草稿列表
	 * 
	 * @return
	 */
	@RequestMapping("/articleDrafts")
	public ModelAndView articlesDrafts() {
		return modelAndView("/admin/articleDrafts");
	}

	/**
	 * 根据id查询草稿
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/articleDraft/{id}", method = RequestMethod.GET)
	public @ResponseBody ArticleDraft getArticleDraft(@PathVariable Integer id) {
		return this.articleService.findArticleDraft(id);
	}

	/**
	 * 保存修改草稿
	 * 
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
	 * 
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
	 * 
	 * @param articleDraft
	 * @return
	 */
	@RequestMapping(value = "/articleDraft/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Map<String, Object> deleteArticleDraft(@PathVariable Integer id) {
		this.articleService.deleteArticleDraft(id);
		return successMap();
	}

	/**
	 * 分页查询草稿
	 * 
	 * @param pageIn
	 * @return
	 */
	@RequestMapping(value = "/articleDrafts", method = RequestMethod.GET)
	public @ResponseBody List<ArticleDraft> getArticleDrafts(@ModelAttribute PageIn<ArticleDraft> pageIn) {
		return articleService.findArticleDraftsListByAjax(pageIn);
	}

	/**
	 * 注销
	 * 
	 * @return
	 */
	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletResponse response) {
		webSessionSupport.logout();
		Cookie cookie = new Cookie(webSessionSupport.SESSION_ID, "");
		cookie.setPath("/");// cookie 必须设置为根路径,否则会导致其他子路径无法拿到cookie
		response.addCookie(cookie);
		return modelAndView("/admin/signIn");
	}
}
