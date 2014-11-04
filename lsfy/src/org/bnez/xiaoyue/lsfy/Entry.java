package org.bnez.xiaoyue.lsfy;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import org.apache.log4j.Logger;
import org.bnez.nlp.seg.rmi.SegConfig;
import org.bnez.xiaoyue.lsfy.common.Config;
import org.bnez.xiaoyue.lsfy.human.LsfyHumanAnswerServer;
import org.bnez.xiaoyue.lsfy.human.inter.HumanAnswerServer;
import org.bnez.xiaoyue.lsfy.job.JobScheduler;
import org.bnez.xiaoyue.lsfy.server.TcpServer;

public class Entry
{
	private static final Logger _logger = Logger.getLogger(Entry.class);
	
	public static void main(String[] args)
	{
		initConfig();
		startJobScheduler();
		startTcpLsnr();
		startHumanAnswerServer();
	}

	private static void initConfig()
	{
		String segip = Config.getInstance().getString("seg.ip", "localhost");
		SegConfig conf = SegConfig.getInstance();
		//conf.setIp("203.150.144.89");
		conf.setIp(segip);
	}

	private static void startHumanAnswerServer()
	{
		int port = Config.getInstance().getInt("human.port");
		try
		{
			HumanAnswerServer server = new LsfyHumanAnswerServer();
			LocateRegistry.createRegistry(port);

			Naming.bind("rmi://localhost:" + port + "/HumanAnswerServer", server);
			_logger.info("HumanAnswerServer bind succed at " + port);
		} catch (Exception e)
		{
			_logger.info("HumanAnswerServer bind FAILED at " + port);
			_logger.error(e.getMessage(), e);
		}
	}

	private static void startJobScheduler()
	{
		new JobScheduler().start();
	}

	private static void startTcpLsnr()
	{
		int port = Config.getInstance().getInt("lsner.port");
		try
		{
			new TcpServer(port).start();
		} catch (IOException e)
		{
			_logger.error(e.getMessage(), e);
		}
	}
}
