package org.bnez.xiaoyue.lsfy.tdh;

import java.util.List;

import org.apache.log4j.Logger;
import org.bnez.xiaoyue.lsfy.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class XiaciRepository {

	private static final Logger _logger = Logger.getLogger(Xiaci.class);

	public void add(List<Xiaci> list) {
		Session s = HibernateUtil.currentSession();
		s.beginTransaction();
		try {
			for (Xiaci t : list)
				s.save(t);
			s.getTransaction().commit();
		} catch (Exception e) {
			s.getTransaction().rollback();
			_logger.error(e.getMessage(), e);
		}
		HibernateUtil.closeSession();
	}

	public List<Xiaci> queryAll() {
		Session s = HibernateUtil.currentSession();
		Criteria c = s.createCriteria(Xiaci.class);
		@SuppressWarnings("unchecked")
		List<Xiaci> list = c.list();
		HibernateUtil.closeSession();
		return list;
	}

	public List<Xiaci>  queryNotHandledByFayuan(String tdhFayuanName)
	{
		Session s = HibernateUtil.currentSession();
		Criteria c = s.createCriteria(Xiaci.class);
		c.add(Restrictions.eq("status", 0));
		c.add(Restrictions.eq("ysfy", tdhFayuanName));
		c.addOrder(Order.asc("createAt"));
		@SuppressWarnings("unchecked")
		List<Xiaci> list = c.list();
		HibernateUtil.closeSession();
		return list;
	}

	public List<Xiaci> queryYiyi()
	{
		Session s = HibernateUtil.currentSession();
		Criteria c = s.createCriteria(Xiaci.class);
		c.add(Restrictions.eq("status", 2));
		c.addOrder(Order.asc("createAt"));
		@SuppressWarnings("unchecked")
		List<Xiaci> list = c.list();
		HibernateUtil.closeSession();
		return list;
	}

	public static void main(String[] args)
	{
		System.out.println(new XiaciRepository().queryAll().size());
		for( Xiaci xc : new XiaciRepository().queryNotHandledByFayuan("丽水市莲都区人民法院"))
		{
			System.out.println(xc.getYsah());
		}
	}
}
