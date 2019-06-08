package com.doolf101.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	@RequestMapping("page")
	public String getPage(Model model){
		model.addAttribute("modelPram","This is text from the server");
		return "page";
	}

	@RequestMapping("testtwo")
	public String testtwo(){
		return "testtwo";
	}

}
