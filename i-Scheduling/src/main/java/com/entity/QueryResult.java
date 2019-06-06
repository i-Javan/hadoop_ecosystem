package com.entity;

import java.util.List;

public class QueryResult<T> {
	private List<T> resultList;
	private long totalrecord;
	/**
	 * @return the resultList
	 */
	public List<T> getResultList() {
		return resultList;
	}
	/**
	 * @param resultList the resultList to set
	 */
	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}
	/**
	 * @return the totalrecord
	 */
	public long getTotalrecord() {
		return totalrecord;
	}
	/**
	 * @param totalrecord the totalrecord to set
	 */
	public void setTotalrecord(long totalrecord) {
		this.totalrecord = totalrecord;
	}

}
