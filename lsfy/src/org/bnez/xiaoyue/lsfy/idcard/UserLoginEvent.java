package org.bnez.xiaoyue.lsfy.idcard;

import java.util.List;

import org.apache.log4j.Logger;
import org.bnez.lsfy.service.BizServiceClient;
import org.bnez.lsfy.service.FayuanBean;
import org.bnez.xiaoyue.lsfy.common.Config;
import org.bnez.xiaoyue.lsfy.db.User;
import org.bnez.xiaoyue.lsfy.db.UserRepository;
import org.bnez.xiaoyue.lsfy.server.PushToClient;
import org.bnez.xiaoyue.lsfy.tdh.Xiaci;
import org.bnez.xiaoyue.lsfy.tdh.XiaciRepository;

public class UserLoginEvent
{
	private static final Logger _logger = Logger.getLogger(UserLoginEvent.class);
	
	private User _user;
	private String _xiaoyueClientIp;
	private String _loginDeviceId;

	public UserLoginEvent(String cardId, String deviceId)
	{
		_user = UserRepository.getInstance().queryByCardId(cardId);
		_loginDeviceId = deviceId;
	}

	public void fire()
	{
		if (_user == null)
			return;
		
		_logger.debug(_user.getName() + " " + _user.getCardId() + " " + _user.getDeviceId() + " login");
		queryUserPosition();
		pushXiaciAlert();
		pushXiaciYiyi();
	}

	private void queryUserPosition()
	{
		if (_loginDeviceId == null)
		{
			_xiaoyueClientIp = _user.getIp();
			return;
		}

		User loginPos = UserRepository.getInstance().queryByDeviceId(_loginDeviceId);
		if (loginPos != null)
			_xiaoyueClientIp = loginPos.getIp();
		else
			_xiaoyueClientIp = _user.getIp();
	}

	private void pushXiaciAlert()
	{
		UserRuleChecker urc = new UserRuleChecker(_user);
		if(!urc.hasRule("receive_xiaci_alert"))
			return;
		
		FayuanBean fy = BizServiceClient.getInstance().queryFayuan(_user.getFayuanId());
		List<Xiaci> list = new XiaciRepository().queryNotHandledByFayuan(fy.getTdhName());
		if (list.size() == 0)
			return;

		String url = String.format(Config.getInstance().getString("url.xiaciJichen"), _user.getFayuanId());
		String content = "";
		Xiaci xc = list.get(0);
		if (list.size() == 1)
		{
			content = String.format("%s，你院%s案件经二审法院审理被判定为瑕疵案件，如果一审合议庭或承办人出现异议，可在收到卷宗后7日内向中院审判管理处提出瑕疵不当申请", xc.getYsfy(),
					xc.getYsah());
		}
		else
		{
			content = String.format("%s，你院%s等%d个案件经二审法院审理被判定为瑕疵案件，如果一审合议庭或承办人出现异议，可在收到卷宗后7日内向中院审判管理处提出瑕疵不当申请", xc.getYsfy(),
					xc.getYsah(), list.size());
		}
		
		new PushToClient(_xiaoyueClientIp).push(content, url);
	}

	private void pushXiaciYiyi()
	{
		UserRuleChecker urc = new UserRuleChecker(_user);
		if(!urc.hasRule("receive_xiaci_yiyi"))
			return;

		List<Xiaci> list = new XiaciRepository().queryYiyi();
		if (list.size() == 0)
			return;

		String url = Config.getInstance().getString("url.xiaciZhongyuan");
		String content = "";
		Xiaci xc = list.get(0);
		if (list.size() == 1)
		{
			content = String.format("%s案件已经提交了瑕疵案件的异议，请查阅", xc.getYsah());
		}
		else
		{
			content = String.format("%s等%d个案件已经提交了瑕疵案件的异议，请查阅", xc.getYsah(), list.size());
		}
		
		new PushToClient(_xiaoyueClientIp).push(content, url);
	}

	public static void main(String[] args)
	{
		UserLoginEvent ule = new UserLoginEvent("0003876096", null);
		ule.fire();
		System.out.println("done");
	}
}
