package org.bnez.xiaoyue.lsfy.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.bnez.xiaoyue.lsfy.XiaoyueResponse;

public class TcpServer implements IoHandler
{
	private static final Logger _logger = Logger.getLogger(TcpServer.class);
	
	private int _port;
	private LsfyHandler _hdler;

	public TcpServer(int port)
	{
		_port = port;
		_hdler = LsfyHandler.getInstance();
	}

	public void start() throws IOException
	{
		IoAcceptor acceptor = new NioSocketAcceptor();

		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		//acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"), LineDelimiter.UNIX, LineDelimiter.UNIX)));
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));

		acceptor.setHandler(this);

		acceptor.getSessionConfig().setReadBufferSize(2048);
		//acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 1);
		acceptor.bind(new InetSocketAddress(_port));
		
		_logger.info("start at " + _port);		
	}

	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1) throws Exception
	{
		_logger.error(arg0.getRemoteAddress() + " " + arg1.getMessage(), arg1);
	}

	@Override
	public void messageReceived(IoSession session, Object received) throws Exception
	{
		String ip = parseIp(session);
		_logger.info("received " + received + " from " + ip);
		
		// for chengl load runner test
		if(received != null && received.toString().equalsIgnoreCase("hello"))
		{
			//session.write("hello response " + new SimpleDateFormat("yyyyMMdd-HHmmss").format(start));
			session.write("hello response");
			_logger.info("message response - hello response");
			return;
		}
		
		XiaoyueResponse rsp = _hdler.handle(received, ip);
		session.write(rsp);
	}

	private String parseIp(IoSession session)
	{
		if(session == null)
			return null;

		InetSocketAddress addr = (InetSocketAddress)session.getRemoteAddress();
		if(addr == null)
			return null;
		
		try
		{
			String a = addr.toString();
			int right = a.indexOf(':');
			return a.substring(1, right);
		} catch (Exception e)
		{
			_logger.error("failed to parse ip from " + session, e);
			return null;
		}
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
		
	}

}
