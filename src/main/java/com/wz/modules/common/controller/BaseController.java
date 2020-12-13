package com.wz.modules.common.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.wz.modules.common.utils.ShiroUtils;
import com.wz.modules.common.utils.UserUtils;
import com.wz.modules.projectinfo.entity.ProjectInfoEntity;
import com.wz.modules.projectinfo.service.ProjectInfoService;
import com.wz.modules.sys.entity.UserEntity;
import com.wz.modules.sys.service.UserService;

/**
 * 类的功能描述. 公共的控件器，可在类中实现一些共同的方法和属性 持续更新中
 * 
 * @Auther hxy
 * @Date 2017/4/28
 */
public class BaseController {

	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	private UserService userService;
	@Autowired
	private ProjectInfoService projectInfoService;

	/**
	 * 获取当前登陆用户
	 * 
	 * @return
	 */
	public UserEntity getUser() {
		return UserUtils.getCurrentUser();
	}

	/**
	 * 获取当前登陆用户Id
	 * 
	 * @return
	 */
	public String getUserId() {
		UserEntity user = getUser();
		if (null != user && null != user.getId()) {
			return user.getId();
		}
		return "";
	}

	public ProjectInfoEntity getCurrentProject() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		ProjectInfoEntity currentProject = (ProjectInfoEntity) request.getSession().getAttribute("currentProject");
		if(currentProject == null){			
			setCurrProjectSession();
			currentProject = (ProjectInfoEntity) request.getSession().getAttribute("currentProject");
		}
		projectInfoService.setProjectPicture(currentProject);
		return currentProject;
	}

	public String getCurrentProjectId() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		ProjectInfoEntity currentProject = (ProjectInfoEntity) request.getSession().getAttribute("currentProject");
		if (null != currentProject) {
			return currentProject.getId();
		} else {
			setCurrProjectSession();
			currentProject = (ProjectInfoEntity) request.getSession().getAttribute("currentProject");
			return currentProject.getId();
		}
	}
	
	private void setCurrProjectSession(){
		UserEntity user = userService.queryObject(ShiroUtils.getUserId());
		String projectIds = user.getProjectIds();
		List<ProjectInfoEntity> projectList = new ArrayList<ProjectInfoEntity>();
		if (StringUtils.isNotBlank(projectIds)) {
			for (String projectId : projectIds.split(",")) {
				ProjectInfoEntity project = projectInfoService.queryObject(projectId);
				projectList.add(project);
			}
		}
		ProjectInfoEntity currentProject = null;
		if ( projectList.size() > 0) {
			currentProject = projectList.get(0);
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			request.getSession().setAttribute("currentProject", currentProject);
		}
	}
}