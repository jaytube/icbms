package com.wz.modules.sys.service;

import java.util.List;
import java.util.Map;

import com.wz.modules.sys.entity.CodeEntity;

/**
 * 系统数据字典
 * 
 * @author admin
 * @email rui.sun.java@gmail.com
 * @date 2017-07-14 13:42:42
 */
public interface CodeService {
	
	CodeEntity queryObject(String id);
	
	List<CodeEntity> queryList(Map<String, Object> map);

	List<CodeEntity> queryListByBean(CodeEntity codeEntity);
	
	int queryTotal(Map<String, Object> map);
	
	String save(CodeEntity code);
	
	void update(CodeEntity code);
	
	void delete(String id);
	
	void deleteBatch(String[] ids);

	List<CodeEntity> queryListByCode(CodeEntity codeEntity);

	/**
	 * 查询所有的字典及子字典(用做字典缓存)
	 * @return
	 */
	List<CodeEntity> queryAllCode();

	/**
	 * 根据字典标识查询
	 * @param mark
	 * @return
	 */
	CodeEntity queryByMark(String mark);

	/**
	 * 查询所有的字典及子字典(用做字典缓存)
	 * @return
	 */
	List<CodeEntity> queryChildsByMark(String mark);

    public Map<String, String> queryChildsMapByMark(String mark);

}
