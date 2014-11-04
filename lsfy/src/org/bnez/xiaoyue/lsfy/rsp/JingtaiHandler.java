package org.bnez.xiaoyue.lsfy.rsp;

import org.bnez.xiaoyue.lsfy.XiaoyueResponse;
import org.bnez.xiaoyue.lsfy.common.ErrorMessage;
import org.bnez.xiaoyue.lsfy.db.Jingtai;
import org.bnez.xiaoyue.lsfy.db.JingtaiRepository;

public class JingtaiHandler extends Handler
{

	public JingtaiHandler(String combName, String combString)
	{
		_combName = combName;
		_combString = combString;
	}

	public XiaoyueResponse response()
	{
		Jingtai jt = new JingtaiRepository().queryByQuestion(_combName);
		if(jt == null)
			return ResponseBuilder.build(ErrorMessage.getErrorDefine("jt.notFound"));
		
		XiaoyueResponse rsp = ResponseBuilder.build(jt.getAnswer(), jt.getUrl());
		return rsp;
	}
}
