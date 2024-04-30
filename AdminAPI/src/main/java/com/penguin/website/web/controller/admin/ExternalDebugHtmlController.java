package com.penguin.website.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ExternalDebugHtmlController {

    @RequestMapping(value = "/admin/externalDebugUser")
    @GetMapping
    public String externalDebug(Model model) {
        return "admin/debug_user_data";
    }

    @RequestMapping(value = "/admin/externalDebugMes")
    @GetMapping
    public String links(Model model) {
        return "admin/debug_user_Mes";
    }

}