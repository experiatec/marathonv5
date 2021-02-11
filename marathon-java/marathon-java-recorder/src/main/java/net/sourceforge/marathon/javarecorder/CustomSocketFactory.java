package net.sourceforge.marathon.javarecorder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

public class CustomSocketFactory extends SocketFactory {
	
	private InetAddress address;
	
	private int port;
	
	private InetAddress localAddress;
	
	private int localPort;
	
	/**
	 * 
	 * @param address
	 * @param port
	 * @param localAddress
	 * @param localPort
	 */
	public CustomSocketFactory(InetAddress address, int port, InetAddress localAddress, int localPort) {
		super();
		this.address = address;
		this.port = port;
		this.localAddress = localAddress;
		this.localPort = localPort;
	}

	@Override
	public Socket createSocket() throws IOException {
		return this.createSocket(address, port, localAddress, localPort);
	}

	@Override
	public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
			throws IOException {
		return new Socket(address, port, localAddress, localPort);
	}

	@Override
	public Socket createSocket(InetAddress host, int port) throws IOException {
		return new Socket(host, port);
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
			throws IOException, UnknownHostException {
		return new Socket(host, port, localHost, localPort);
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
		return new Socket(host, port);
	}

}
