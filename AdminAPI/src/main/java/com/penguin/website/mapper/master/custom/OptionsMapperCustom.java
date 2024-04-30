package com.penguin.website.mapper.master.custom;

import com.penguin.website.model.domain.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface OptionsMapperCustom {
	/**
	 * 保存
	 * 
	 * @param map
	 */
	void saveMap(@Param("map") Map<String, Object> map);

	/**
	 * 所有设置选项
	 * 
	 * @return map
	 */
	List<Options> selectMap();

}
