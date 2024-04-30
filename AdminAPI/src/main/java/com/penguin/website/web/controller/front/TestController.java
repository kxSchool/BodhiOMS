package com.penguin.website.web.controller.front;

import cn.hutool.extra.servlet.ServletUtil;
import com.penguin.website.model.domain.*;
import com.penguin.website.model.dto.PenguinConst;
import com.penguin.website.model.dto.JsonResult;
import com.penguin.website.service.*;
import com.penguin.website.web.controller.admin.BaseController;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @ClassName : TestCOntroller  //类名
 * @Description : 测试controller  //描述
 * @Author : Penguin  //作者
 * @Date: 2020/8/25 14:29  //时间
 */
@Controller
public class TestController extends BaseController {

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
    @Autowired
    private DepartmentService departmentService;

    /**
     * 首页数据
     * @return
     */
    @GetMapping(value = { "/index2/{articleUrl}"})
    @ResponseBody
    public JsonResult index(@PathVariable(name = "articleUrl") String articleUrl,HttpServletRequest request) {
        // 调用方法渲染首页 JsonResult
        ArticleCustom articleCustom = articleService.findByArticleUrl(articleUrl);
        if (articleCustom == null) {
            return new JsonResult(false,"服务器异常");
        }
        log.info(articleCustom.toString());
        System.out.println(articleCustom.toString());
        if (!checkRepeatIp(request, articleCustom.getId())) {
            updateArticleViews(articleCustom.getId(), articleCustom.getArticleViews());
        }
        return new JsonResult(true,"ok",articleCustom);
    }

    /**
     * 查询所有部门
     * @return
     */
    @GetMapping("/getDepartmentAll")
    @ResponseBody
    public JsonResult getDepartmentAll() {
        List<Departments> all = departmentService.getAll();
        return new JsonResult(true,"ok",all);
    }

    @PostMapping("/getPresidentAll")
    @ResponseBody
    public JsonResult getPresidentAll(@RequestBody Map<String, Object> param) {
        long startTime=System.currentTimeMillis();
        log.info("查询条件："+param);
        Integer pageNum = (Integer)param.get("pageNum");
        Integer pageSize = (Integer)param.get("pageSize");
        if(pageNum!=null && pageSize!=null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<President> presidentAll = departmentService.getPresidentAll(param);
        PageInfo<President> presidentPageInfo = new PageInfo<>(presidentAll);
        long endTime=System.currentTimeMillis();
        log.info("执行耗时："+(float)(endTime-startTime)/1000+"s");
        return new JsonResult(true,"ok",presidentPageInfo);
    }

    @GetMapping("/getPresidentById")
    @ResponseBody
    public JsonResult getPresidentAll(Integer presidentId) {
        long startTime=System.currentTimeMillis();
        log.info("查询条件："+presidentId);
        President president = departmentService.getPresidentById(presidentId);

        long endTime=System.currentTimeMillis();
        log.info("执行耗时："+(float)(endTime-startTime)/1000+"s");
        return new JsonResult(true,"ok",president);
    }

    /**
     * 根据部门id查询
     * @param departmentId
     * @return
     */
    @GetMapping("/getDepartment/{departmentId}")
    @ResponseBody
    public JsonResult getDepartment(@PathVariable(name = "departmentId") Integer departmentId) {
        DepartmetsDesc departmentIds = departmentService.getDepartmentId(departmentId);
        return new JsonResult(true,"ok",departmentIds);
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
}
