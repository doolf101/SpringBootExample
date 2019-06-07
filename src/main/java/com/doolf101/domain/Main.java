package com.doolf101.domain;

public class Main {

	private Integer mainID;
	private String mainName;

	public Main() {
	}

	public Main(Integer mainID, String mainName) {
		this.mainID = mainID;
		this.mainName = mainName;
	}

	public Integer getMainID() {
		return mainID;
	}

	public void setMainID(Integer mainID) {
		this.mainID = mainID;
	}

	public String getMainName() {
		return mainName;
	}

	public void setMainName(String mainName) {
		this.mainName = mainName;
	}

	@Override
	public String toString() {
		return "Main{" +
				"mainID=" + mainID +
				", mainName='" + mainName + '\'' +
				'}';
	}

}
