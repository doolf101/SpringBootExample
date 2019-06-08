package com.doolf101.app.controller;


import com.doolf101.app.domain.Main;
import com.doolf101.app.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/main")
public class MainController {

	@Autowired
	private MainService mainService;

	@GetMapping
	public @ResponseBody List<Main> getMains(){
		return mainService.getMains();
	}

	@GetMapping(value="/id/{id}")
	public @ResponseBody Main getMain(@PathVariable("id") Integer id){
		return mainService.getMain(id);
	}

	@PutMapping(value="/id/{id}")
	public  @ResponseBody Main putMain(@PathVariable("id") Integer id,@RequestBody Main main){
		return mainService.updateMain(id,main);
	}

	@DeleteMapping(value="/id/{id}")
	public @ResponseBody void deleteMain(@PathVariable("id") Integer id){
		mainService.deleteMain(id);
	}

	@PostMapping
	public  @ResponseBody Main postMain(@RequestBody Main main){
		return mainService.insertMain(main);
	}

}
