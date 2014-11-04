package org.bnez.xiaoyue.lsfy.idcard;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

public class IdCardRepository
{
	private static final Logger _logger = Logger.getLogger(IdCardRepository.class);

	private static IdCardRepository _instance = null;
	private Set<IdCardRecord> _records;

	public static IdCardRepository getInstance()
	{
		if (_instance == null)
			_instance = new IdCardRepository();
		return _instance;
	}

	private IdCardRepository()
	{
		_records = Collections.synchronizedSet(new HashSet<IdCardRecord>());
	}

	public void add(IdCardRecord r)
	{
		_records.add(r);
	}
//
//	public IdCardRecord queryByCard(String cardId)
//	{
//		_logger.debug("user card id is " + cardId);
//		
//		// remove out-of-time, TODO move to job
//		Set<IdCardRecord> outOfTimeSet = new HashSet<IdCardRecord>();
//		for (IdCardRecord r : _records)
//			if (r.isOutOfTime())
//				outOfTimeSet.add(r);
//		for (IdCardRecord r : outOfTimeSet)
//			_records.remove(r);
//
//		for (IdCardRecord r : _records)
//			if (r.getCardId().equals(cardId))
//				return r;
//		return null;
//	}

	public IdCardRecord queryLastByDevice(String deviceId)
	{
		_logger.debug("user device id is " + deviceId);
		
		// remove out-of-time
		Set<IdCardRecord> outOfTimeSet = new HashSet<IdCardRecord>();
		for (IdCardRecord r : _records)
			if (r.isOutOfTime())
				outOfTimeSet.add(r);
		for (IdCardRecord r : outOfTimeSet)
			_records.remove(r);

		IdCardRecord found = null;
		for (IdCardRecord r : _records)
			if (r.getDeviceId().equals(deviceId))
			{
				if(found == null)
					found = r;
				else
					if(r.getRecordTime().getTime() > found.getRecordTime().getTime())
						found = r;
			}
		return found;
	}

	public void print()
	{
		_logger.debug(_records.size());
		for (IdCardRecord r : _records)
			_logger.debug(r.toString());
	}

	public boolean contains(IdCardRecord r)
	{
		return _records.contains(r);
	}

	public void remove(IdCardRecord r)
	{
		_records.remove(r);
	}

}
