package com.penguin.website.web.controller.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.penguin.website.model.domain.Options;
import com.penguin.website.model.dto.JsonResult;
import com.penguin.website.model.dto.PenguinConst;
import com.penguin.website.model.enums.PenguinEnums;
import com.penguin.website.service.OptionsService;



@Controller
@RequestMapping("/admin/option")
public class OptionsController extends BaseController {
	@Autowired
	private OptionsService optionsService;

	/**
	 * 所有设置选项
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping
	public String option(Model model) {
		return "admin/admin_options";
	}

	/**
	 * 保存设置
	 * 
	 * @param map
	 * @return
	 */
	@PostMapping(value = "/save")
	@ResponseBody
	public JsonResult save(@RequestParam Map<String, String> map) {
		try {
			optionsService.save(map);
			PenguinConst.OPTIONS.clear();
			List<Options> listMap = optionsService.selectMap();
			for (Options options : listMap) {
				PenguinConst.OPTIONS.put(options.getOptionName(), options.getOptionValue());
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return new JsonResult(PenguinEnums.PRESERVE_ERROR.isFlag(), PenguinEnums.PRESERVE_ERROR.getMessage());
		}
		return new JsonResult(PenguinEnums.PRESERVE_SUCCESS.isFlag(), PenguinEnums.PRESERVE_SUCCESS.getMessage());
	}
}
