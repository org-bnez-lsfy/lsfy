package org.bnez.xiaoyue.lsfy.test;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class TestTcp implements IoHandler
{
	private static final Logger _logger = Logger.getLogger(TestTcp.class);
	private String _received;
	private String _ip;
	private int _port;
	private String _toSend;

	public TestTcp(String ip, int port)
	{
		_ip = ip;
		_port = port;
	}

	public String send(String t)
	{
		_toSend = t;
		try
		{
			NioSocketConnector connector = new NioSocketConnector();
			connector.getFilterChain().addLast("logger", new LoggingFilter());
			connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));

			connector.setHandler(this);
			connector.setConnectTimeoutCheckInterval(3);
			connector.getSessionConfig().setBothIdleTime(1);
			ConnectFuture cf = connector.connect(new InetSocketAddress(_ip, _port));
//			ConnectFuture cf = connector.connect(new InetSocketAddress("localhost", 20000));

			cf.awaitUninterruptibly();
			cf.getSession().getCloseFuture().awaitUninterruptibly();
			connector.dispose();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return _received;
	}

	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1) throws Exception
	{
		arg1.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession arg0, Object arg1) throws Exception
	{
		_received = arg1.toString();
		arg0.close(true);
	}

	@Override
	public void messageSent(IoSession arg0, Object arg1) throws Exception
	{
	}

	@Override
	public void sessionClosed(IoSession arg0) throws Exception
	{
		
	}

	@Override
	public void sessionCreated(IoSession arg0) throws Exception
	{
		
	}

	@Override
	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception
	{
		
	}

	@Override
	public void sessionOpened(IoSession arg0) throws Exception
	{
		arg0.write(_toSend);
	}
	
	public String getReceived()
	{
		return _received;
	}

	private static void t(String s)
	{
		String ip = "203.150.144.89";
//		String ip = "localhost";
		int port = 20000;
		
		TestTcp tt = new TestTcp(ip, port);
		tt.send(s);
		_logger.debug(tt.getReceived());
	}

	public static void main(String[] args)
	{
		t("我院今年的结案率是多少");
		t("丽中今年的结案率是多少");
		t("丽水两级法院今年的结案率是多少");
		t("松阳呢");
		t("本院今年的结案率是多少");
	}
}
