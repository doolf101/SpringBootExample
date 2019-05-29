package com.ciaran.service;

import com.ciaran.domain.Main;
import org.springframework.stereotype.Service;

@Service
public class MainService {

	public Main getMain(){
		return new Main(1,"This is a Test");
	}

	public Main getMain(Integer id){
		return new Main(id,"This is a Test");
	}

}
