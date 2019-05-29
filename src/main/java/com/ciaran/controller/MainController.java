package com.ciaran.controller;


import com.ciaran.domain.Main;
import com.ciaran.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/main")
public class MainController {

	@Autowired
	private MainService mainService;

//	@RequestMapping(method = RequestMethod.GET)
	@GetMapping
	public @ResponseBody Main getMain(){
		return mainService.getMain();
	}


	@GetMapping(value="/id/{id}")
	public @ResponseBody Main getMain(@PathVariable("id") Integer id){
		return mainService.getMain(id);
	}

	@RequestMapping(method = RequestMethod.POST)
	public  @ResponseBody Main postMain(@RequestBody Main main){
		main.setMainID(main.getMainID()+1);
		return main;
	}

}
