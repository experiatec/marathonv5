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
package net.sourceforge.marathon.runtime.ws;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;

import net.sourceforge.marathon.api.INamingStrategy;
import net.sourceforge.marathon.objectmap.ObjectMapConfiguration;
import net.sourceforge.marathon.objectmap.ObjectMapException;
import net.sourceforge.marathon.runtime.IRecordingServer;
import net.sourceforge.marathon.runtime.JSONScriptElement;
import net.sourceforge.marathon.runtime.api.IRecorder;
import net.sourceforge.marathon.runtime.api.IScriptElement;
import net.sourceforge.marathon.runtime.api.Indent;
import net.sourceforge.marathon.runtime.api.RecordingScriptModel;
import net.sourceforge.marathon.runtime.api.WindowId;

public abstract class WSRecordingServer extends WebSocketServer implements IRecordingServer {

    public static final Logger LOGGER = Logger.getLogger(WSRecordingServer.class.getName());
    public static String COMMENT_PREFIX = "# ";
	List<String> propertiesToUse = null;
	
    private static class MenuItemScriptElement implements IScriptElement {
        private static final long serialVersionUID = 1L;
        private String type;
        private String value;
        private WindowId windowId;

        public MenuItemScriptElement(JSONObject o, WindowId windowId) {
            this.windowId = windowId;
            type = o.getString("menu_type");
            value = o.getString("value");
        }

        @Override
        public String toScriptCode() {
            return Indent.getIndent()
                    + RecordingScriptModel.getModel().getScriptCodeForGenericAction("select_fx_menu", "", type, value);
        }

        @Override
        public WindowId getWindowId() {
            return windowId;
        }

        @Override
        public IScriptElement getUndoElement() {
            return null;
        }

        @Override
        public boolean isUndo() {
            return false;
        }

    }

    private static final class CommentScriptElement implements IScriptElement {
        private static final long serialVersionUID = 1L;
        private String comment;

        public CommentScriptElement(String comment) {
            this.comment = comment;
        }

        @Override
        public String toScriptCode() {
            return Indent.getIndent() + COMMENT_PREFIX + comment + "\n";
        }

        @Override
        public WindowId getWindowId() {
            return null;
        }

        @Override
        public IScriptElement getUndoElement() {
            return null;
        }

        @Override
        public boolean isUndo() {
            return false;
        }

    }

    private static final class ChooserScriptElement implements IScriptElement {
        private static final long serialVersionUID = 1L;
        private String type;
        private String value;

        public ChooserScriptElement(JSONObject o) {
            type = o.getString("type").equals("select_file_chooser") ? "#filechooser" : "#folderchooser";
            value = o.getString("value");
        }

        @Override
        public String toScriptCode() {
            return Indent.getIndent()
                    + RecordingScriptModel.getModel().getScriptCodeForGenericAction("select_file_chooser", "", type, value);
        }

        @Override
        public WindowId getWindowId() {
            return null;
        }

        @Override
        public IScriptElement getUndoElement() {
            return null;
        }

        @Override
        public boolean isUndo() {
            return false;
        }

    }

    private static final class FileDialogScriptElement implements IScriptElement {
        private static final long serialVersionUID = 1L;
        private String type;
        private String value;

        public FileDialogScriptElement(JSONObject o) {
            type = "#fileDialog";
            value = o.getString("value");
        }

        @Override
        public String toScriptCode() {
            return Indent.getIndent()
                    + RecordingScriptModel.getModel().getScriptCodeForGenericAction("select_file_dialog", "", type, value);
        }

        @Override
        public WindowId getWindowId() {
            return null;
        }

        @Override
        public IScriptElement getUndoElement() {
            return null;
        }

        @Override
        public boolean isUndo() {
            return false;
        }

    }

    private static final class WindowClosingScriptElement implements IScriptElement {
        private static final long serialVersionUID = 1L;
        private String title;

        public WindowClosingScriptElement(JSONObject o) {
            title = o.getString("value");
        }

        @Override
        public String toScriptCode() {
            return Indent.getIndent() + RecordingScriptModel.getModel().getScriptCodeForGenericAction("window_closed", "", title);
        }

        @Override
        public WindowId getWindowId() {
            return null;
        }

        @Override
        public IScriptElement getUndoElement() {
            return null;
        }

        @Override
        public boolean isUndo() {
            return false;
        }

    }

    private static final class WindowStateScriptElement implements IScriptElement {
        private static final long serialVersionUID = 1L;
        private String title;
        private int x;
        private int y;
        private int width;
        private int height;

        public WindowStateScriptElement(JSONObject o) {
            JSONObject value = o.getJSONObject("value");
            title = value.getString("title");
            x = value.getInt("x");
            y = value.getInt("y");
            width = value.getInt("width");
            height = value.getInt("height");
        }

        @Override
        public String toScriptCode() {
            String bounds = "" + x + ":" + y + ":" + width + ":" + height;
            return Indent.getIndent() + RecordingScriptModel.getModel().getScriptCodeForGenericAction("window_changed", "", bounds);
        }

        @Override
        public WindowId getWindowId() {
            return new WindowId(title, null, "window");
        }

        @Override
        public IScriptElement getUndoElement() {
            return null;
        }

        @Override
        public boolean isUndo() {
            return false;
        }

    }

    private final class JavaVersionScriptElement implements IScriptElement {
        private static final long serialVersionUID = 1L;

        @Override
        public String toScriptCode() {
            if (java_version != null) {
                return Indent.getIndent() + "java_recorded_version = '" + java_version + "'\n";
            }
            java_version = null;
            return "";
        }

        @Override
        public boolean isUndo() {
            return false;
        }

        @Override
        public WindowId getWindowId() {
            return null;
        }

        @Override
        public IScriptElement getUndoElement() {
            return null;
        }
    }

    private IRecorder recorder;
    INamingStrategy ns;
    private String java_version;
    private boolean javaVersionRecorded = false;
    private WindowId focusedWindowId;

    public WSRecordingServer(int port, INamingStrategy ns) {
        super(new InetSocketAddress(port));
        this.ns = ns;
    }

    @Override
    public void startRecording(IRecorder recorder) {
        this.recorder = recorder;
        if (recorder != null && !javaVersionRecorded) {
            recorder.record(new JavaVersionScriptElement());
            javaVersionRecorded = true;
        }
    }

    @Override
    public void stopRecording() {
        this.recorder = null;
        try {
            stop();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        ns.save();
    }

    @Override
    public void setRawRecording(boolean rawRecording) throws IOException {
        JSONObject o = new JSONObject();
        o.put("value", Boolean.toString(rawRecording));
        Collection<WebSocket> cs = connections();
        for (WebSocket webSocket : cs) {
            post(webSocket, "setRawRecording", o.toString());
        }
    }

    @Override
    public void pauseRecording() {
        setRecordingPause(true);
    }

    @Override
    public void resumeRecording() {
        setRecordingPause(false);
    }

    private void setRecordingPause(boolean paused) {
        JSONObject o = new JSONObject();
        o.put("value", Boolean.toString(paused));
        Collection<WebSocket> cs = connections();
        for (WebSocket webSocket : cs) {
            post(webSocket, "setRecordingPause", o.toString());
        }
    }

    public JSONObject getObjectMapConfiguration() {
        ObjectMapConfiguration omc = ns.getObjectMapConfiguration();
        BeanToJsonConverter btjc = new BeanToJsonConverter();
        String s = btjc.convert(omc);
        return new JSONObject(s);
    }

    public abstract JSONObject getContextMenuTriggers();

    public JSONObject record(WebSocket conn, JSONObject query) throws IOException {
        LOGGER.info("WSRecordingServer.record(" + query.toString(2) + ")");
        
        JSONObject queryAttributes = query.getJSONObject("attributes");
        JSONObject attributes = new JSONObject();
        
        getJSONPropertiesToUse().stream().filter(p -> queryAttributes.has(p)).forEach(p -> attributes.put(p, queryAttributes.get(p)));

        StringSelection stringSelection = new StringSelection(attributes.toString(2));
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);

        if (recorder == null) {
            return new JSONObject();
        }
        try {
//            JSONObject eventObject = query.getJSONObject("event");
//            String type = eventObject.getString("type");
//            if (type.equals("log")) {
//                System.out.println(eventObject.get("message"));
//                return new JSONObject();
//            }
//            if (type.equals("comment")) {
//                recorder.record(new CommentScriptElement(eventObject.getString("comment")));
//                return new JSONObject();
//            }
//            if (type.equals("select_file_dialog")) {
//                recorder.record(new FileDialogScriptElement(query.getJSONObject("event")));
//                return new JSONObject();
//            }
//            if (type.equals("select_file_chooser") || type.equals("select_folder_chooser")) {
//                recorder.record(new ChooserScriptElement(query.getJSONObject("event")));
//                return new JSONObject();
//            } else if (type.equals("select_fx_menu")) {
//                WindowId windowId = createWindowId(query.getJSONObject("container"));
//                recorder.record(new MenuItemScriptElement(query.getJSONObject("event"), windowId));
//                return new JSONObject();
//            }
//            if (type.equals("window_closing_with_title")) {
//                recorder.record(new WindowClosingScriptElement(query.getJSONObject("event")));
//                return new JSONObject();
//            }
//            if (type.equals("window_state_with_title")) {
//                recorder.record(new WindowStateScriptElement(query.getJSONObject("event")));
//                return new JSONObject();
//            }
            String name = null;
            try {
                name = query.getJSONObject("attributes").getString("suggestedName");
            } catch (JSONException e) {
            }
            String cName = getName(query, name);
            WindowId windowId = createWindowId(query.getJSONObject("container"));
            recorder.record(new JSONScriptElement(windowId, cName, attributes));
        } catch (JSONException je) {
            je.printStackTrace();
            System.err.println(query.toString(2));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return new JSONObject();
    }

    protected String getName(JSONObject query, String name) throws ObjectMapException {
        return ns.getName(query, name);
    }

    public JSONObject focusedWindow(WebSocket conn, JSONObject query) throws IOException, JSONException, ObjectMapException {
        if (query.has("container") && query.get("container") != JSONObject.NULL)
            focusedWindowId = createWindowId(query.getJSONObject("container"));
        return new JSONObject();
    }

    private WindowId createWindowId(final JSONObject container) throws JSONException, ObjectMapException {
        WindowId parent = container.has("container") ? createWindowId(container.getJSONObject("container")) : null;
        return new WindowId(createTitle(container), parent, container.getString("container_type"));
    }

    private String createTitle(JSONObject container) throws JSONException, ObjectMapException {
        if (!"window".equals(container.getString("container_type"))) {
            return ns.getContainerName(container);
        }
        JSONObject urp = container.getJSONObject("attributes");
        if (urp.has("title")) {
            return urp.getString("title");
        }
        urp = container.getJSONObject("urp");
        LOGGER.warning("Window attributes doesn't contain title. Using URP as title");
        return urp.toString();
    }

    @Override
    public void abortApplication() {
        Collection<WebSocket> cs = connections();
        for (WebSocket webSocket : cs) {
            post(webSocket, "abortApplication", null);
        }
    }

    @Override
    public WindowId getFocusedWindowId() {
        return focusedWindowId;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        post(conn, "setContextMenuTriggers", getContextMenuTriggers().toString());
        post(conn, "setObjectMapConfig", getObjectMapConfiguration().toString());
    }

    public void reopened(WebSocket conn, JSONObject query) {
    	System.out.println("ON reOPEN!!!!");
        post(conn, "setContextMenuTriggers", getContextMenuTriggers().toString());
        post(conn, "setObjectMapConfig", getObjectMapConfiguration().toString());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        JSONObject o = new JSONObject(message);
        String method = o.getString("method");
        String data = o.has("data") ? o.getString("data") : null;
        serve(method, conn, data);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
    }

    public void serve(String method, WebSocket conn, String data) {
        JSONObject jsonQuery = null;
        if (data != null) {
            try {
                jsonQuery = new JSONObject(data);
            } catch (JSONException e) {
                e.printStackTrace();
                LOGGER.warning("Could not convert `" + data + "` to JSON");
                return;
            }
        }
        serve_internal(method, conn, jsonQuery);
    }

    public void serve_internal(String methodName, WebSocket conn, JSONObject jsonQuery) {
        java.lang.reflect.Method method = getMethod(methodName);
        if (method != null) {
            handleRoute(method, conn, jsonQuery);
        } else if (method == null) {
            LOGGER.warning("Unknown method `" + methodName + "` called with " + jsonQuery);
        }
    }

    private void handleRoute(java.lang.reflect.Method method, WebSocket conn, JSONObject query) {
        try {
            invoke(method, conn, query);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warning("Unable to execute method `" + method.getName() + "` with data " + query);
        }
    }

    public void invoke(java.lang.reflect.Method method, WebSocket conn, JSONObject query) {
        try {
            method.invoke(this, conn, query);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            String message = method.getName() + "(" + query.toString() + ")";
            throw new RuntimeException(message, cause);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static java.lang.reflect.Method getMethod(String name) {
        java.lang.reflect.Method[] methods = WSRecordingServer.class.getMethods();
        for (java.lang.reflect.Method method : methods) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        return null;
    }

    public void postAll(String method, String data) {
        Collection<WebSocket> cs = connections();
        for (WebSocket webSocket : cs) {
            post(webSocket, method, data);
        }
    }

    private void post(WebSocket conn, String method, String data) {
        JSONObject o = new JSONObject();
        o.put("method", method);
        if (data != null)
            o.put("data", data);
        conn.send(o.toString());
    }

    @Override
    public boolean isRecording() {
        return recorder != null;
    }

    public void reloadScript(WebSocket conn, JSONObject query) {
    }

    public void alert(WebSocket conn, JSONObject query) {
        WindowId windowId;
        try {
            windowId = createWindowId(query.getJSONObject("container"));
        } catch (JSONException | ObjectMapException e) {
            e.printStackTrace();
            return;
        }
        recorder.record(new IScriptElement() {
            private static final long serialVersionUID = 1L;

            @Override
            public String toScriptCode() {
                String method = query.getString("method");
                if (method.equals("prompt") || method.equals("confirm")) {
                    String result = query.getString("result");
                    if (result == null)
                        return Indent.getIndent() + RecordingScriptModel.getModel().getScriptCodeForGenericAction(method, "",
                                query.optString("text", ""));
                    else
                        return Indent.getIndent() + RecordingScriptModel.getModel().getScriptCodeForGenericAction(method, "",
                                query.optString("text", ""), result);
                } else {
                    return Indent.getIndent() + RecordingScriptModel.getModel().getScriptCodeForGenericAction(method, "",
                            query.optString("text", ""));
                }
            }

            @Override
            public WindowId getWindowId() {
                return windowId;
            }

            @Override
            public IScriptElement getUndoElement() {
                return null;
            }

            @Override
            public boolean isUndo() {
                return false;
            }

        });
    }

    @Override
    public void insertingScriptStart() {
        setInsertingScript(true);
    }

    @Override
    public void insertingScriptFinish() {
        setInsertingScript(false);
    }

    private void setInsertingScript(boolean inserting) {
        JSONObject o = new JSONObject();
        o.put("value", Boolean.toString(inserting));
        Collection<WebSocket> cs = connections();
        for (WebSocket webSocket : cs) {
            post(webSocket, "setInsertingScript", o.toString());
        }
    }

    /**
     * 
     * @return The list of properties to copy to the clipboard.
     */
	private List<String> getJSONPropertiesToUse() {
		if (this.propertiesToUse == null) {
			try (InputStream input = WSRecordingServer.class.getClassLoader()
					.getResourceAsStream("stb-config.properties")) {
				this.propertiesToUse = new ArrayList<>();
				Properties prop = new Properties();

				if (input == null) {
					LOGGER.warning("Sorry, unable to find stb-config.properties");
					return null;
				}

				// load a properties file from class path, inside static method
				prop.load(input);
				this.propertiesToUse.addAll(Arrays.asList(prop.getProperty("obj.properties").split(",")));
			} catch (IOException ex) {
				LOGGER.warning("An errro has occurred while reading stb-config.properties");
				throw new RuntimeException(ex);
			}
		}
		return this.propertiesToUse;
	}
}
