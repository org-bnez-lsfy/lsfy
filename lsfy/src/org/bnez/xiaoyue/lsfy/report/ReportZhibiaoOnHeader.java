package org.bnez.xiaoyue.lsfy.report;

import java.util.List;

/**
 * 
 * @author bnez
 *
 * 5.5等横表用不到,所以这个类写了一半
 * 
 */
@Deprecated
public class ReportZhibiaoOnHeader
{
	@SuppressWarnings("unused")
	private int _firstVolCol;
	private String[] _zhibiaoWithTongbi;
	@SuppressWarnings("unused")
	private String[] _zhibiaoWithoutTongbi;
	private Integer _kj4col;
	private Integer _kj3col;
	private Integer _kj2col;
	private Integer _kj1col;

	 void setFirstValueColumn(int col)
	{
		 _firstVolCol = col;
	}

	public void setZhibiaoWithTongbi(String... zhibiaoArray)
	{
		_zhibiaoWithTongbi = zhibiaoArray;
	}

	public void setZhibiao(String... zhibiaoArray)
	{
		_zhibiaoWithoutTongbi = zhibiaoArray;
	}

	public void setKoujing4Column(int col)
	{
		_kj4col = col;
	}

	public void setKoujing3Column(int col)
	{
		_kj3col = col;
	}

	public void setKoujing2Column(int col)
	{
		_kj2col = col;
	}

	public void setKoujing1Column(int col)
	{
		_kj1col = col;
	}

	@SuppressWarnings("unused")
	public List<ReportData> buildData(List<String> record)
	{
		String kj1 = _kj1col == null ? null : record.get(_kj1col);
		String kj2 = _kj2col == null ? null : record.get(_kj2col);
		String kj3 = _kj3col == null ? null : record.get(_kj3col);
		String kj4 = _kj4col == null ? null : record.get(_kj4col);
		
		for(int i=0; i<_zhibiaoWithTongbi.length; i++)
		{
			
		}
		return null;
	}

}
