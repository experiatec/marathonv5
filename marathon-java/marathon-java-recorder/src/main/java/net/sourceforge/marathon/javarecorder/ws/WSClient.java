/*******************************************************************************
 * Copyright 2016 Jalian Systems Pvt. Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package net.sourceforge.marathon.javarecorder.ws;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import net.sourceforge.marathon.javarecorder.CustomSocketFactory;

public class WSClient extends WebSocketClient {

    public static final Logger LOGGER = Logger.getLogger(WSClient.class.getName());

    private WSRecorder recorder;

    public WSClient(int port, WSRecorder recorder) throws URISyntaxException {
        super(new URI("http://localhost:" + port));
        try {
        	InetAddress loopbackInetAddress = InetAddress.getByName("localhost");
			this.setSocketFactory(
					new CustomSocketFactory(
							loopbackInetAddress, 
							port, 
							loopbackInetAddress, 
							getLocalPort() 
					)
			);
			LOGGER.info(
					String.format("Configuring WSClient to connect to [%s] on port [%s] from address [%s] and client port [%s]...", 
							loopbackInetAddress, 
							port, 
							loopbackInetAddress, 
							getLocalPort()));
			this.recorder = recorder;
			connect();
		} catch (UnknownHostException e) {
			String errMsg = "'localhost' could not be resolved! Please check the network configuration and make sure "
					+ "'localhost' is properly resolved as the loopback address of this PC!";
			LOGGER.log(Level.SEVERE, errMsg, e);
			throw new RuntimeException(errMsg, e);
		}
    }

    /**
     * 
     * @return The configured local port (client port) or the default one (8002).
     */
    private int getLocalPort() {
    	int localPortNumber = 8002;
    	String configuredPort = System.getProperty("java.recorder.port", Integer.toString(localPortNumber));
    	if (null != configuredPort && !"".equals(configuredPort.trim())) {
    		try {
				localPortNumber = Integer.parseInt(configuredPort);
			} catch (NumberFormatException e) {
				String errMsg = "A numerical port number is required for java.recorder.port!";
				LOGGER.severe(errMsg);
				throw new RuntimeException(errMsg, e);
			} 
    	}
    	LOGGER.fine(String.format("Configured port number [%s] for java WSRecorder...", localPortNumber));
    	return localPortNumber;
	}

	@Override
    public void onOpen(ServerHandshake handshakedata) {
        recorder.onOpen();
    }

    @Override
    public void onMessage(String message) {
        recorder.onMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
    }

    @Override
    public void onError(Exception ex) {
    }

    public void post(String method, String data) {
        JSONObject o = new JSONObject();
        o.put("method", method);
        if (data != null) {
            o.put("data", data);
        }
        send(o.toString());
    }

}
