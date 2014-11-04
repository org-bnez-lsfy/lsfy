package org.bnez.xiaoyue.lsfy.server;

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
import org.bnez.xiaoyue.lsfy.XiaoyueResponse;
import org.bnez.xiaoyue.lsfy.rsp.ResponseBuilder;

public class PushToClient implements IoHandler
{
	private static final Logger _logger = Logger.getLogger(PushToClient.class);
	private String _clientIp;
	private int _clientPort;
	private XiaoyueResponse _msg;
	
	public PushToClient(String clientIp)
	{
		_clientIp = clientIp;
		_clientPort = 19999;
	}

	public void push(String content, String url)
	{
		_msg = ResponseBuilder.build(content, url);
		send();
	}

	private void send()
	{
		try
		{
			_logger.debug("try connect to " + _clientIp + ":" + _clientPort);
			NioSocketConnector connector = new NioSocketConnector();
			connector.getFilterChain().addLast("logger", new LoggingFilter());
			connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));

			connector.setHandler(this);
			connector.setConnectTimeoutCheckInterval(3);
			connector.getSessionConfig().setBothIdleTime(1);
			ConnectFuture cf = connector.connect(new InetSocketAddress(_clientIp, _clientPort));
//			ConnectFuture cf = connector.connect(new InetSocketAddress("localhost", 20000));

			cf.awaitUninterruptibly();
			cf.getSession().getCloseFuture().awaitUninterruptibly();
			connector.dispose();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1) throws Exception
	{
	}

	@Override
	public void messageReceived(IoSession arg0, Object arg1) throws Exception
	{
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
		_logger.debug("try push to " + _clientIp + ":" + _clientPort + " with msg " + _msg);
		arg0.write(_msg);
		arg0.close(true);
	}

}
