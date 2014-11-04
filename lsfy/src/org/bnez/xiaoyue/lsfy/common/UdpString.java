package org.bnez.xiaoyue.lsfy.common;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

public class UdpString extends IoHandlerAdapter
{
	private static Logger _logger = Logger.getLogger(UdpString.class);
	
	private String _ip;
	private int _port;

	public UdpString(String ip, int port)
	{
		_ip = ip;
		_port = port;
	}

	public void send(final String msg)
	{
		_logger.debug("try send " + msg + " to " + _ip + " " + _port);
		
		NioDatagramConnector connector = new NioDatagramConnector();
		connector.setHandler(this);
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		chain.addLast("logger", new LoggingFilter());
		chain.addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"), LineDelimiter.UNIX, LineDelimiter.UNIX)));
		IoFuture connFuture = connector.connect(new InetSocketAddress(_ip, _port));
		
		connFuture.addListener(new IoFutureListener<IoFuture>()
		{
			public void operationComplete(IoFuture future)
			{
				ConnectFuture connFuture = (ConnectFuture) future;
				if (connFuture.isConnected())
				{
					IoSession session = future.getSession();
					try
					{
						session.write(msg);
						session.close(false);
					} catch (Exception e)
					{
						_logger.error("send to " + _ip + " " + _port);
						e.printStackTrace();
					}
				} else
				{
					_logger.error("not connected " + _ip + " " + _port);
				}
			}
		});
		connFuture.awaitUninterruptibly();
		connFuture.getSession().getCloseFuture().awaitUninterruptibly();
		connector.dispose();
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception
	{
		cause.printStackTrace();
		_logger.error(cause.getMessage() + " at " + _ip + " " + _port);
	}

	public static void main(String[] args)
	{
		UdpString udp = new UdpString("localhost", 10000);
		udp.send("open light");
		
		System.out.println("done");
	}
}
