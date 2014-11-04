package org.bnez.xiaoyue.lsfy.db;

import java.util.List;

import org.bnez.xiaoyue.lsfy.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;

public class UserRepository
{
	private static UserRepository _instance;
	private List<User> _cache;
	
	public static UserRepository getInstance()
	{
		if(_instance == null)
			_instance = new UserRepository();
		return _instance;
	}
	
	private UserRepository()
	{
		_cache = queryAll();
	}
	
	private List<User> queryAll()
	{
		Session s = HibernateUtil.currentSession();
		Criteria c = s.createCriteria(User.class);
		@SuppressWarnings("unchecked")
		List<User> list = c.list();
		HibernateUtil.closeSession();
		return list;
	}
	
	public User queryByIp(String ip)
	{
		for(User u : _cache)
			if(u.getIp().equals(ip))
				return u;
		return null;
	}

	public User queryByCardId(String cardId)
	{
		for(User u : _cache)
			if(u.getCardId() != null && u.getCardId().equals(cardId))
				return u;
		return null;
	}

	public User queryByDeviceId(String deviceId)
	{
		for(User u : _cache)
			if(u.getDeviceId() != null && u.getDeviceId().equals(deviceId))
				return u;
		return null;
	}
	
	public static void main(String[] args)
	{
		for(User u : UserRepository.getInstance().queryAll())
			System.out.println(u.getName());
	}
}
