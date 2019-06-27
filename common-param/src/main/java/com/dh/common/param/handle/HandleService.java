package com.dh.common.param.handle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dh.common.param.dao.ParamDao;
import com.dh.common.restful.entity.DHRequest;
import com.dh.common.restful.entity.DHResponse;
import com.dh.qf.model.TParam;

@Service("common-param")
public class HandleService{

	@Autowired
	private ParamDao paramDao;
	
	@Transactional(propagation=Propagation.NEVER)
	public DHResponse handle(DHRequest request) {
		DHResponse response = new DHResponse("000000", "success");
		TParam param = paramDao.getParam(request.getJsonParam());
		response.setRespJson(param.getpVal());
		return response;
	}
}
