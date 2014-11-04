package org.bnez.xiaoyue.lsfy.report;

import java.util.ArrayList;
import java.util.List;

import org.bnez.lsfy.service.BizServiceClient;
import org.bnez.lsfy.service.FayuanBean;

public class RecordOrder
{
	private List<ReportData> _datas;

	public RecordOrder(List<ReportData> datas)
	{
		_datas = datas;
	}

	public void order()
	{
		List<ReportData> jcList = new ArrayList<ReportData>();
		List<ReportData> zyList = new ArrayList<ReportData>();
		BizServiceClient biz = BizServiceClient.getInstance();
		for (ReportData data : _datas)
		{
			String fayuan = data.getKoujing4();
			FayuanBean fy = biz.queryFayuanByRptName(fayuan);
			if (fy.isJichen())
			{
				data.setPaimingJichen(data.getPaimingQuansheng());
				jcList.add(data);
			}
			if (fy.isZhongyuan())
			{
				data.setPaimingZhongyuan(data.getPaimingQuansheng());
				zyList.add(data);
			}
		}

		makeOrder(jcList, new SetOrder()
		{
			@Override
			public void set(ReportData data, Integer order)
			{
				data.setPaimingJichen(order);
			}
		}, new GetOrder()
		{
			@Override
			public Integer get(ReportData data)
			{
				if(data == null)
					return null;
				return data.getPaimingJichen();
			}
		});
		
		makeOrder(zyList, new SetOrder()
		{
			@Override
			public void set(ReportData data, Integer order)
			{
				data.setPaimingZhongyuan(order);
			}
		}, new GetOrder()
		{
			@Override
			public Integer get(ReportData data)
			{
				if(data == null)
					return null;
				return data.getPaimingZhongyuan();
			}
		});
	}

	private void makeOrder(List<ReportData> list, SetOrder setOrder, GetOrder getOrder)
	{
		if (list.size() == 0)
			return;
//		if (getOrder.get(list.get(0)) != null)
//			setOrder.set(list.get(0), 1); // FIXME left data should change too

//		if (list.size() < 2)
//		{
//			if (getOrder.get(list.get(0)) != null)
//				setOrder.set(list.get(0), 1);
//			return;
//		}

		for (int i = 0; i < list.size(); i++)
		{
			ReportData data = list.get(i);
			Integer order = getOrder.get(data);
			if (order == null)
				continue;
			
			Integer preOrder = null;
			if(i == 0)
			{
				if(order != null)
					preOrder = 0;
			}
			else
			{
				preOrder = getOrder.get(list.get(i-1));
			}

			if (preOrder == null)
			{
				setOrder.set(data, 1);
			} else
			{
				int diff = order.intValue() - preOrder.intValue();
				if (diff > 1)
				{
					// 1,4,4,7,8 -> 1,2,2,5,6
					setOrder.set(data, preOrder + 1);
					if (i < list.size() - 1)
					{
						for(int left = i+1; left< list.size(); left++)
						{
							ReportData leftData = list.get(left);
							setOrder.set(leftData, getOrder.get(leftData) - diff + 1);
						}
					}
				}
			}
		}
	}

}

interface SetOrder
{
	void set(ReportData data, Integer order);
}

interface GetOrder
{
	Integer get(ReportData data);
}
