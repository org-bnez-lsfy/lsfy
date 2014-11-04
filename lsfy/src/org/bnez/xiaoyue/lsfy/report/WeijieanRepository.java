package org.bnez.xiaoyue.lsfy.report;

import java.util.List;

import org.apache.log4j.Logger;
import org.bnez.xiaoyue.lsfy.util.HibernateUtil;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

public class WeijieanRepository {
	private static final Logger _logger = Logger.getLogger(Weijiean.class);
	
	public void save(List<Weijiean> jas)
	{
		Session s = HibernateUtil.currentSession();
		s.beginTransaction();
		try
		{
			for (Weijiean ja : jas)
				s.save(ja);
			s.getTransaction().commit();
			_logger.debug("data save succed, " + jas.size());
		} catch (Exception e)
		{
			s.getTransaction().rollback();
			_logger.error(e.getMessage(), e);
		}
	}
	
	public void delete(String from, String to, String fy)
	{
		Session s = HibernateUtil.currentSession();
		
		String tableName = "tbl_weijiean";// FIXME create table name from
		String sql = "delete from " + tableName + " where fy = ? and `from` = ? and `to` = ? ";
		SQLQuery q = s.createSQLQuery(sql);

		try
		{
			s.beginTransaction();
			int pos = 0;
			q.setString(pos++, fy);
			q.setString(pos++, from);
			q.setString(pos++, to);
			q.executeUpdate();
			s.getTransaction().commit();
		} catch (Exception e)
		{
			s.getTransaction().rollback();
			_logger.error(e.getMessage(), e);
		}
	}

}
