package com.ciaran.test.service;


import com.doolf101.domain.Main;
import org.junit.jupiter.api.Test;


public class MainServiceTest {


	@Test
	public void getMainTest(){
		Main m = new Main();
		System.out.println(m.getMainID());
	}

}
