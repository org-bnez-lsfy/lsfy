package org.bnez.xiaoyue.lsfy.report;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.bnez.xiaoyue.lsfy.util.HibernateUtil;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

public class ReportDB
{
	private static final Logger _logger = Logger.getLogger(ReportDB.class);

	public ReportDB()
	{
	}

	public void save(List<ReportData> datas)
	{
		Session s = HibernateUtil.currentSession();
		s.beginTransaction();
		try
		{
			for (ReportData data : datas)
				save(data);
			s.getTransaction().commit();
			_logger.debug("data save succed, " + datas.size());
		} catch (Exception e)
		{
			s.getTransaction().rollback();
			_logger.error(e.getMessage(), e);
		}
	}

	private void save(final ReportData data)
	{
		Session s = HibernateUtil.currentSession();
		s.doWork(new Work()
		{
			@Override
			public void execute(Connection conn) throws SQLException
			{
				// FIXME insert之前，检查旧的数据是否存在、新数据是否为空或者是'-'
				String tableName = "tbl_zhibiao_2014_05";// FIXME create table name from
				String sql = "insert into " + tableName + " values ( null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ? )";
				PreparedStatement ps = conn.prepareStatement(sql);

				int pos = 1;
				ps.setString(pos++, data.getZhibiao());
				ps.setString(pos++, data.getKoujing1());
				ps.setString(pos++, data.getKoujing2());
				ps.setString(pos++, data.getKoujing3());
				ps.setString(pos++, data.getKoujing4());
				ps.setString(pos++, data.getFrom());
				ps.setString(pos++, data.getTo());
				ps.setString(pos++, data.getValue());
				ps.setString(pos++, data.getTongbi());
				ps.setString(pos++, data.getTongqi());
				ps.setString(pos++, data.getHuanbi());
				
				if(data.getPaimingQuansheng() != null)
					ps.setInt(pos++, data.getPaimingQuansheng());
				else
					ps.setBigDecimal(pos++, null);
				
				if(data.getPaimingZhongyuan() != null)
					ps.setInt(pos++, data.getPaimingZhongyuan());
				else
					ps.setBigDecimal(pos++, null);
				
				if(data.getPaimingJichen() != null)
					ps.setInt(pos++, data.getPaimingJichen());
				else
					ps.setBigDecimal(pos++, null);
				
				ps.setString(pos++, data.getCondition());
				ps.setDate(pos++, new Date(data.getInsertAt().getTime()));
				
				ps.execute();
			}
		});
	}

	public void delete(String from, String to, String zhibiao)
	{
		Session s = HibernateUtil.currentSession();
		
		String tableName = "tbl_zhibiao_2014_05";// FIXME create table name from
		String sql = "delete from " + tableName + " where zhibiao = ? and `from` = ? and `to` = ? ";
		SQLQuery q = s.createSQLQuery(sql);

		try
		{
			s.beginTransaction();
			int pos = 0;
			q.setString(pos++, zhibiao);
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

	public int queryCount(String from, String to, String zhibiao)
	{
		Session s = HibernateUtil.currentSession();
		
		String tableName = "tbl_zhibiao_2014_05";// FIXME create table name from
		String sql = "select count(1) from " + tableName + " where zhibiao = ? and `from` = ? and `to` = ? ";
		SQLQuery q = s.createSQLQuery(sql);
		int c = 0;
		try
		{
			int pos = 0;
			q.setString(pos++, zhibiao);
			q.setString(pos++, from);
			q.setString(pos++, to);
			BigInteger bi = (BigInteger)q.list().get(0);
			c = bi.intValue();
		} catch (Exception e)
		{
			_logger.error(e.getMessage(), e);
		}
		return c;
	}
	
	public static void main(String[] args)
	{
		HibernateUtil.currentSession();
		int c = new ReportDB().queryCount("2014-03-23", "2014-04-22", "结案均衡度");
		System.out.println(c);

		c = new ReportDB().queryCount("2014-03-23", "2014-04-22", "aaa");
		System.out.println(c);
		
		HibernateUtil.closeSession();
	}

}
