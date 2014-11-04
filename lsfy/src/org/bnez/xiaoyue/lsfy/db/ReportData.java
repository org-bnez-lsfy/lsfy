package org.bnez.xiaoyue.lsfy.db;

import java.util.Date;

public class ReportData
{
	private Long id;
	private String zhibiao;
	private String koujing1;
	private String koujing2;
	private String koujing3;
	private String koujing4;
	private String from;
	private String to;
	private String value;
	private String tongbi;
	private String huanbi;
	private Integer paimingQuansheng;
	private Integer paimingZhongyuan;
	private Integer paimingJichen;
	private String condition;
	private Date insertAt;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getZhibiao()
	{
		return zhibiao;
	}

	public void setZhibiao(String zhibiao)
	{
		this.zhibiao = zhibiao;
	}

	public String getKoujing1()
	{
		return koujing1;
	}

	public void setKoujing1(String koujing1)
	{
		this.koujing1 = koujing1;
	}

	public String getKoujing2()
	{
		return koujing2;
	}

	public void setKoujing2(String koujing2)
	{
		this.koujing2 = koujing2;
	}

	public String getKoujing3()
	{
		return koujing3;
	}

	public void setKoujing3(String koujing3)
	{
		this.koujing3 = koujing3;
	}

	public String getKoujing4()
	{
		return koujing4;
	}

	public void setKoujing4(String koujing4)
	{
		this.koujing4 = koujing4;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getTongbi()
	{
		return tongbi;
	}

	public void setTongbi(String tongbi)
	{
		this.tongbi = tongbi;
	}

	public String getHuanbi()
	{
		return huanbi;
	}

	public void setHuanbi(String huanbi)
	{
		this.huanbi = huanbi;
	}

	public Integer getPaimingQuansheng()
	{
		return paimingQuansheng;
	}

	public void setPaimingQuansheng(Integer paimingQuansheng)
	{
		this.paimingQuansheng = paimingQuansheng;
	}

	public Integer getPaimingZhongyuan()
	{
		return paimingZhongyuan;
	}

	public void setPaimingZhongyuan(Integer paimingZhongyuan)
	{
		this.paimingZhongyuan = paimingZhongyuan;
	}

	public String getCondition()
	{
		return condition;
	}

	public void setCondition(String condition)
	{
		this.condition = condition;
	}

	public Date getInsertAt()
	{
		return insertAt;
	}

	public void setInsertAt(Date insertAt)
	{
		this.insertAt = insertAt;
	}

	public String getFrom()
	{
		return from;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}

	public String getTo()
	{
		return to;
	}

	public void setTo(String to)
	{
		this.to = to;
	}

	public Integer getPaimingJichen()
	{
		return paimingJichen;
	}

	public void setPaimingJichen(Integer paimingJichen)
	{
		this.paimingJichen = paimingJichen;
	}
}
