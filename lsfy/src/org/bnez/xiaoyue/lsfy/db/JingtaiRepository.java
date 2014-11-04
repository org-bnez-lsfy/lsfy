package org.bnez.xiaoyue.lsfy.db;

import java.util.List;

import org.bnez.xiaoyue.lsfy.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class JingtaiRepository
{
	public Jingtai queryByQuestion(String question)
	{
		Session s = HibernateUtil.currentSession();
		Criteria c = s.createCriteria(Jingtai.class);
		c.add(Restrictions.eq("combName", question));
		@SuppressWarnings("unchecked")
		List<Jingtai> list = c.list();
		HibernateUtil.closeSession();
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	public static void main(String[] args)
	{
		System.out.println(new JingtaiRepository().queryByQuestion("ls_jt_bgszn").getAnswer());
		System.out.println(new JingtaiRepository().queryByQuestion("ls_jt_bgszn").getUrl());
	}
}
