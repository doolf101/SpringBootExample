package com.doolf101.service;

import com.doolf101.domain.Main;
import com.doolf101.repository.MainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainService {

	@Autowired
	private MainRepository mainRepository;

	public List<Main> getMains(){
		return mainRepository.getMains();
	}

	public Main getMain(Integer id){
		return mainRepository.getMainById(id);
	}

	public Main insertMain(Main main) {
		Integer id = mainRepository.insertMain(main);
		return mainRepository.getMainById(id);
	}

	public Main updateMain(Integer id,Main main){
		return mainRepository.updateMain(id,main);
	}

	public void deleteMain(Integer mainID) {
		mainRepository.deleteMain(mainID);
	}
}
