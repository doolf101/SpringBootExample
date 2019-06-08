package com.doolf101.app.repository;

import com.doolf101.app.domain.Main;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MainRepository extends SuperRepository{

	private static final String MAIN_ID    = "mainid";
	private static final String MAIN_NAME  = "main_name";
	private static final String TABLE_MAIN = "main";

	public Integer insertMain(Main main){
		Map<String, Object> prams = new HashMap<>();
		prams.put(MAIN_NAME,main.getMainName());
		return insert(TABLE_MAIN,prams,MAIN_ID);
	}

	public Main updateMain(int mainID,Main main){
		Map<String, Object> prams = new HashMap<>();
		prams.put(MAIN_NAME,main.getMainName());
		update(TABLE_MAIN,prams,primaryKey(mainID));
		return getMainById(mainID);
	}
	public void deleteMain(int mainID){
		delete(TABLE_MAIN,primaryKey(mainID));
	}

	public Main getMainById(Integer mainID) {
		return getObjectWhere("SELECT * FROM main",primaryKey(mainID),Main.class);
	}

	public List<Main> getMains() {
		return getWhere("SELECT * FROM main",null,Main.class);
	}

	private Map<String, Object> primaryKey(int mainID){
		Map<String, Object> keys = new HashMap<>();
		keys.put(MAIN_ID,mainID);
		return keys;
	}

}
