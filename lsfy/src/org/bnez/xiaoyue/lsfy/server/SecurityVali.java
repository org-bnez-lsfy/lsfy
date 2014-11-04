package org.bnez.xiaoyue.lsfy.server;

import java.rmi.RemoteException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.bnez.xiaoyue.lsfy.common.Config;
import org.bnez.xiaoyue.lsfy.db.User;
import org.bnez.xiaoyue.lsfy.db.UserRepository;
import org.bnez.xiaoyue.lsfy.faced.inter.FaceDServer;
import org.bnez.xiaoyue.lsfy.faced.inter.RmiConnection;
import org.bnez.xiaoyue.lsfy.idcard.IdCardRecord;
import org.bnez.xiaoyue.lsfy.idcard.IdCardRepository;

public class SecurityVali
{
	private static final Logger _logger = Logger.getLogger(SecurityVali.class);
	private User _user;

	public boolean vali(String ip)
	{
		_user = UserRepository.getInstance().queryByIp(ip);
		if (_user == null)
		{
			_logger.debug("user from " + ip + " is NOT configed");
			return false;
		}

		boolean needIdCard = Config.getInstance().getBool("idcard.check");
		if (needIdCard)
		{
			IdCardRecord cr = IdCardRepository.getInstance().queryLastByDevice(_user.getDeviceId());
			if (cr == null)
			{
				_logger.warn("user id=" + _user.getId() + " is NOT found in idCard repository");
				return false;
			}
			
			String lastCardIdOnDevice = cr.getCardId();
			_user = UserRepository.getInstance().queryByCardId(lastCardIdOnDevice);
			if(_user == null)
			{
				_logger.warn("card id " + lastCardIdOnDevice + " is NOT configed");
				return false;
			}

			boolean needFaceD = Config.getInstance().getBool("faced.check");
			if (needFaceD)
			{
				// TODO 每次远程调用检测人脸识别的结果，应该缓存最近的结果，或者做个job不断更新到本地缓存
				return checkFaceDetect();
			}
		}

		return true;
	}

	private boolean checkFaceDetect()
	{
		Integer eid = getFaceDEmployeeId();
		if (eid == null)
			return false;

		Date last = getLastFaceD(eid);
		if (last == null)
			return false;

		Date now = new Date();
		int diff = Config.getInstance().getInt("faced.diff") * 60 * 1000;
		if (now.getTime() - last.getTime() < 0 || now.getTime() - last.getTime() > diff)
			return true;

		return false;
	}

	private Date getLastFaceD(int eid)
	{
		String ip = Config.getInstance().getString("faced.ip");
		int port = Config.getInstance().getInt("faced.port");
		Date c = null;
		try
		{
			String rmiUrl = "rmi://" + ip + ":" + port + "/FaceDServer";
			RmiConnection conn = new RmiConnection(rmiUrl);
			FaceDServer bs = (FaceDServer) conn.getConnection();
			c = bs.getLastCheck(eid);
		} catch (RemoteException e)
		{
			_logger.error(e.getMessage(), e);
		}
		return c;
	}

	private Integer getFaceDEmployeeId()
	{
		String[] idmap = Config.getInstance().getString("faced.idmap").split("\\|");
		for (String idid : idmap)
		{
			String[] twoId = idid.split(",");
			if (twoId == null || twoId.length != 2)
				return null;
			if (twoId[0].equals(String.valueOf(_user.getId())))
			{
				try
				{
					Integer eid = Integer.parseInt(twoId[1]);
					return eid;
				} catch (Exception e)
				{
				}
			}
		}
		return null;
	}

	public User getUser()
	{
		return _user;
	}

}
