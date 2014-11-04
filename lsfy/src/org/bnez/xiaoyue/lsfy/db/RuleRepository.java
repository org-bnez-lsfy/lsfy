package org.bnez.xiaoyue.lsfy.db;

import java.util.List;

import org.bnez.xiaoyue.lsfy.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;

public class RuleRepository
{
	private static RuleRepository _instance;
	private List<Rule> _cache;
	
	public static RuleRepository getInstance()
	{
		if(_instance == null)
			_instance = new RuleRepository();
		return _instance;
	}
	
	private RuleRepository()
	{
		_cache = queryAll();
	}

	@SuppressWarnings("unchecked")
	private List<Rule> queryAll()
	{
		Session s = HibernateUtil.currentSession();
		Criteria c = s.createCriteria(Rule.class);
		List<Rule> list = c.list();
		HibernateUtil.closeSession();
		return list;
	}

	private List<Rule> getAll()
	{
		return _cache;
	}

	public Rule queryById(long id)
	{
		for(Rule r : _cache)
			if(r.getId().equals(id))
				return r;
		return null;
	}

	public Rule queryByCode(String ruleCode)
	{
		for(Rule r : _cache)
			if(r.getCode().equals(ruleCode))
				return r;
		return null;
	}
	
	public static void main(String[] args)
	{
		List<Rule> rs = RuleRepository.getInstance().getAll();
		for(Rule r : rs)
			System.out.println(r.getCode() + "/" + r.getName());
	}
}
