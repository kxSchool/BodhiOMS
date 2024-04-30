package com.penguin.website.mapper.slave;

import com.penguin.website.model.domain.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleMapper1 {


	@Select({"select * from penguin_article where article_post='page' order by article_url"  })
	/*@Results({
			@Result(property = "userId", column = "user_id"),
			@Result(property = "articleContent", column = "article_content")
	})*/
	List<Article> findAd();
}