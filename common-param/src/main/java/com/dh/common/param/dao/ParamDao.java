package com.dh.common.param.dao;

import java.util.List;
import java.util.Map;

import com.dh.qf.model.TParam;

public interface ParamDao {
	
	/**
	 * 获取全局参数
	 * */
	TParam getParam(String pKey);
	
	/**
	 * 获取参数列表
	 * */
	List<TParam> getParams(Map<String, Object> map);
	
	/**
	 * 获得记录数
	 * */
	int getCount(Map<String, Object> map);
	
	/**
	 * 添加参数
	 * */
	int saveParam(TParam param);
	
	/**
	 * 删除参数
	 * */
	int deleteParam(String pId);
}
