package com.penguin.website.web.controller.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.penguin.website.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.penguin.website.model.domain.Article;
import com.penguin.website.model.domain.ArticleCustom;
import com.penguin.website.model.domain.Category;
import com.penguin.website.model.domain.Link;
import com.penguin.website.model.domain.Tag;
import com.penguin.website.model.dto.ArchiveBo;
import com.penguin.website.model.dto.PenguinConst;
import com.penguin.website.model.enums.ArticleStatus;
import com.penguin.website.model.enums.PageNumber;
import com.penguin.website.model.enums.PostType;
import com.penguin.website.util.PenguinUtil;
import com.penguin.website.web.controller.admin.BaseController;
import com.sun.syndication.io.FeedException;
import com.penguin.website.model.dto.JsonResult;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;

@Controller
public class IndexController extends BaseController {
	@Autowired
	private ArticleService articleService;
	@Autowired
	private LinksService linksService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private TagService tagService;
	@Autowired
	private AdService adService;

	/**
	 * 请求首页
	 *
	 * @param model
	 * @return 模板路径
	 */
	@GetMapping(value = { "/", "index" })
	//@ResponseBody
	public String index(Model model, HttpServletRequest request) {
		// 调用方法渲染首页 JsonResult
		log.info(model.toString());
		return this.post(model, "aboutus",request);
	}

	@GetMapping("/ListUser")
	@ResponseBody
	public JsonResult ListUser(){
		List<Article> adServices=adService.findAd();
		log.info("成功");
		return new JsonResult(true,"ok",adServices);
	}

	@GetMapping("/ListUser127")
	@ResponseBody
	public JsonResult ListUser127(){
		List<Article> adServices=adService.findAd127();
		log.info("成功");
		return new JsonResult(true,"ok",adServices);
	}

	@GetMapping("/president")
	public String President(){
		log.info("成功");
		return this.render("president");
	}

	@GetMapping("/departments")
	public String Departments(){
		log.info("成功");
		return this.render("departments");
	}

	@GetMapping("/contact")
	public String Contact(){
		log.info("成功");
		return this.render("contact");
	}

	/**
	 * 首页分页
	 *
	 * @return 模板路径/themes/{theme}/index
	 */
	@GetMapping(value = "page/{page}")
	//@ResponseBody
	public String index(Model model, @PathVariable(value = "page") Integer page) {
		page = page < 0 || page > PenguinConst.MAX_PAGE ? 1 : page;
		// 默认显示条数
		Integer limit = StrUtil.isEmpty(PenguinConst.OPTIONS.get("index_article")) ? PageNumber.POST_INDEX_lIMIT.getLimit()
				: Integer.parseInt(PenguinConst.OPTIONS.get("index_article"));
		ArticleCustom articleCustom = new ArticleCustom();
		articleCustom.setArticleStatus(ArticleStatus.PUBLISH.getStatus());
		articleCustom.setArticlePost(PostType.POST_TYPE_POST.getValue());
		PageInfo<ArticleCustom> pageInfo = articleService.findPageArticle(page, limit, articleCustom);
		List<Article> adServices=adService.findAd();
		model.addAttribute("articles", pageInfo);
		model.addAttribute("ads", adServices);
		log.info(String.valueOf(adServices));
		return this.render("index");
		//return new JsonResult(true,"ok",pageInfo);
	}

	/**
	 * 归档 BY LHY
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "archives")
	public String archives(Model model) throws Exception {
		List<ArchiveBo> archiveBos = articleService.archives();
		model.addAttribute("articleList", archiveBos);
		return this.render("archives");
	}

	/**
	 * 文章页
	 * 
	 * @param model
	 * @param articleUrl
	 *            文章url
	 * @return
	 */
	@GetMapping(value = { "article/{articleUrl}", "article/{articleUrl}.html" })
	//@ResponseBody
	public String post(Model model, @PathVariable(value = "articleUrl") String articleUrl, HttpServletRequest request) {
		ArticleCustom articleCustom = articleService.findByArticleUrl(articleUrl);
		if (articleCustom == null) {
			//return this.render_404();
		}
		log.info(articleUrl);
		System.out.println(articleCustom.toString());
		if (!checkRepeatIp(request, articleCustom.getId())) {
			updateArticleViews(articleCustom.getId(), articleCustom.getArticleViews());
		}
		model.addAttribute("article", articleCustom);
		//return new JsonResult(true,"ok",articleCustom);
		return this.render("post");
	}

	/**
	 * 检测同一IP十分钟以内重复访问同一篇文章
	 * 
	 * @param request
	 * @param id
	 *            文章id
	 * @return
	 */
	public boolean checkRepeatIp(HttpServletRequest request, int id) {

		String value = ServletUtil.getClientIP(request) + ":" + id;
		Integer count = cache.hget("hits:frequency", value);
		if (count != null && count > 0) {
			return true;
		}
		cache.hset("hits:frequency", value, 1, PenguinConst.IP_REPEAT_TIME);
		return false;
	}

	/**
	 * 修改文章点击率
	 * 
	 * @param id
	 * @param views
	 */
	public void updateArticleViews(Integer id, Long views) {
		if (views == null) {
			views = 0L;
		}
		// 获取缓存数据
		Long cacheViews = cache.hget("article" + id, "cacheViews");
		// 如果缓存数据为null赋值1，反之加1
		cacheViews = cacheViews == null ? 1 : cacheViews + 1;
		// 如果缓存只大于等于设置的次数则修改到数据库
		if (cacheViews >= PenguinConst.CLICK_EXCEED) {
			Article article = new Article();
			article.setId(id);
			article.setArticleViews(views + cacheViews);
			articleService.updateArticleViews(article);
			cache.hset("article" + id, "cacheViews", null);
		} else {
			cache.hset("article" + id, "cacheViews", cacheViews);
		}
	}

	/**
	 * 友链
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping(value = "links")
	public String links(Model model) {
		List<Link> links = linksService.findLinks();
		model.addAttribute("links", links);
		return this.render("links");
	}

	/**
	 * 自建页面
	 * 
	 * @param pagename
	 * @param model
	 * @return
	 */
	@GetMapping(value = "url/{articleUrl}")
	public String page(@PathVariable String articleUrl, Model model, HttpServletRequest request) {
		ArticleCustom articleCustom = articleService.findByArticleUrl(articleUrl);
		if (articleCustom == null) {
			return this.render_404();
		}
		if (!checkRepeatIp(request, articleCustom.getId())) {
			updateArticleViews(articleCustom.getId(), articleCustom.getArticleViews());
		}
		model.addAttribute("article", articleCustom);
		return this.render("page");
	}

	/**
	 * 分类页
	 *
	 * @return
	 */
	@GetMapping(value = "category/{cateUrl}")
	public String categories(Model model, @PathVariable String cateUrl) {
		return this.categories(model, cateUrl, 1);
	}

	@GetMapping(value = "category/{cateUrl}/{page}")
	public String categories(Model model, @PathVariable String cateUrl, @PathVariable Integer page) {
		// 验证是否存在该路径
		Category category = categoryService.findByCateUrl(cateUrl);
		if (category == null) {
			this.render_404();
		}
		Integer limit = PageNumber.POST_INDEX_lIMIT.getLimit();
		page = page < 0 || page > PenguinConst.MAX_PAGE ? 1 : page;
		PageInfo<ArticleCustom> info = articleService.findArtileByCategory(page, limit, category,
				ArticleStatus.PUBLISH.getStatus());
		model.addAttribute("articles", info);
		model.addAttribute("type", "分类");
		model.addAttribute("url", "category/" + category.getCategoryUrl());
		model.addAttribute("keywords", category.getCategoryName());
		return this.render("page-category");
	}

	/**
	 * 标签页
	 *
	 * @return
	 */
	@GetMapping(value = "tags/{tagUrl}")
	public String tags(Model model, @PathVariable String tagUrl) {
		return this.tags(model, tagUrl, 1);
	}

	@GetMapping(value = "tags/{tagUrl}/{page}")
	public String tags(Model model, @PathVariable String tagUrl, @PathVariable Integer page) {
		// 验证是否存在该路径
		Tag tag = tagService.findByTagUrl(tagUrl);
		if (tag == null) {
			this.render_404();
		}
		Integer limit = PageNumber.POST_INDEX_lIMIT.getLimit();
		page = page < 0 || page > PenguinConst.MAX_PAGE ? 1 : page;
		PageInfo<ArticleCustom> info = articleService.findArtileByTag(page, limit, tag,
				ArticleStatus.PUBLISH.getStatus());
		model.addAttribute("articles", info);
		model.addAttribute("type", "标签");
		model.addAttribute("url", "tags/" + tag.getTagUrl());
		model.addAttribute("keywords", tag.getTagName());
		return this.render("page-category");
	}

	/**
	 * 标题搜索
	 *
	 * @return
	 */
	@GetMapping(value = "search/{keywords}")
	public String search(Model model, @PathVariable String keywords) {
		return this.search(model, keywords, 1);
	}

	@GetMapping(value = "search/{keywords}/{page}")
	public String search(Model model, @PathVariable String keywords, @PathVariable Integer page) {
		Integer limit = PageNumber.POST_INDEX_lIMIT.getLimit();
		page = page < 0 || page > PenguinConst.MAX_PAGE ? 1 : page;
		PageInfo<Article> info = articleService.findArticleByKeywords(keywords, page, limit);
		model.addAttribute("articles", info);
		model.addAttribute("type", "搜索");
		model.addAttribute("keywords", keywords);
		model.addAttribute("url", "search/" + keywords);
		return this.render("page-category");
	}

	/**
	 * 文章rss
	 * 
	 * @return
	 * @throws FeedException 
	 */
	@GetMapping(value = { "feed", "feed.xml", "index.xml", "atom", "atom.xml" }, produces = "application/xml;charset=UTF-8")
	@ResponseBody
	public String rss() throws FeedException {
		String rssNumber = PenguinConst.OPTIONS.get("rss_number");
		if (StrUtil.isBlank(rssNumber)) {
			rssNumber = "20";
		}
		ArticleCustom articleCustom = new ArticleCustom();
		articleCustom.setArticleStatus(ArticleStatus.PUBLISH.getStatus());
		articleCustom.setArticlePost(PostType.POST_TYPE_POST.getValue());
		PageInfo<ArticleCustom> pageInfo = articleService.findPageArticle(0, Integer.parseInt(rssNumber), articleCustom);
		List<ArticleCustom> articles=pageInfo.getList();
		return PenguinUtil.buildRss(articles);
	}

}
