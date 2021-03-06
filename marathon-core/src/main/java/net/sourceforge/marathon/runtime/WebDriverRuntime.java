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
package net.sourceforge.marathon.runtime;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Logger;

import org.java_websocket.WebSocket;
import org.json.JSONObject;

import net.sourceforge.marathon.fx.display.FXContextMenuTriggers;
import net.sourceforge.marathon.runtime.api.Constants.MarathonMode;
import net.sourceforge.marathon.runtime.api.IConsole;
import net.sourceforge.marathon.runtime.api.IMarathonRuntime;
import net.sourceforge.marathon.runtime.api.IRecorder;
import net.sourceforge.marathon.runtime.api.IScript;
import net.sourceforge.marathon.runtime.api.IScriptModel;
import net.sourceforge.marathon.runtime.api.MPFUtils;
import net.sourceforge.marathon.runtime.api.Module;
import net.sourceforge.marathon.runtime.api.ScriptModel;
import net.sourceforge.marathon.runtime.api.WindowId;
import net.sourceforge.marathon.runtime.ws.WSRecordingServer;

public class WebDriverRuntime implements IMarathonRuntime {

    public static final Logger LOGGER = Logger.getLogger(WebDriverRuntime.class.getName());

    private static boolean mergeOutput = Boolean.getBoolean("marathon.merge.output");

    private static class ScriptOutput extends ConsoleWriter {
        public ScriptOutput(final IConsole console) {
            super(new Writer() {
                @Override
                public void write(char cbuf[], int off, int len) throws IOException {
                    if (mergeOutput) {
                        System.out.print(new String(cbuf, off, len));
                    }
                    console.writeScriptOut(cbuf, off, len);
                }

                @Override
                public void flush() throws IOException {
                }

                @Override
                public void close() throws IOException {
                }
            });
        }
    }

    private static class ScriptError extends ConsoleWriter {
        public ScriptError(final IConsole console) {
            super(new Writer() {
                @Override
                public void write(char cbuf[], int off, int len) throws IOException {
                    if (mergeOutput) {
                        System.err.print(new String(cbuf, off, len));
                    }
                    console.writeScriptErr(cbuf, off, len);
                }

                @Override
                public void flush() throws IOException {
                }

                @Override
                public void close() throws IOException {
                }
            });
        }
    }

    private static class CommandOutput extends ConsoleWriter {
        public CommandOutput(final IConsole console) {
            super(new Writer() {
                @Override
                public void write(char cbuf[], int off, int len) throws IOException {
                    if (mergeOutput) {
                        System.out.print(new String(cbuf, off, len));
                    }
                    console.writeStdOut(cbuf, off, len);
                }

                @Override
                public void flush() throws IOException {
                }

                @Override
                public void close() throws IOException {
                }
            });
        }

    }

    @SuppressWarnings("unused")
    private static class CommandError extends ConsoleWriter {
        public CommandError(final IConsole console) {
            super(new Writer() {
                @Override
                public void write(char cbuf[], int off, int len) throws IOException {
                    if (mergeOutput) {
                        System.err.print(new String(cbuf, off, len));
                    }
                    console.writeStdErr(cbuf, off, len);
                }

                @Override
                public void flush() throws IOException {
                }

                @Override
                public void close() throws IOException {
                }
            });
        }

    }

    private IScriptModel scriptModel;
    private String driverURL;
    private IRecordingServer recordingServer;
    private IWebDriverRuntimeLauncherModel launcherModel;
    private IScript script;
    private IWebdriverProxy webDriverProxy;
    private int recordingPort = -1;
    protected boolean reloadingScript;
    private int recordingServerPort;
    protected Exception reloadPosition;

    public WebDriverRuntime(IWebDriverRuntimeLauncherModel launcherModel) {
        this.launcherModel = launcherModel;
    }

    public String createDriver(Map<String, Object> props, MarathonMode mode, IConsole console) {
        WriterOutputStream outputStream = new WriterOutputStream(new CommandOutput(console));
        webDriverProxy = launcherModel.createDriver(props, recordingPort, outputStream);
        return webDriverProxy.getURL();
    }

    private int startRecordingServer() {
        recordingServerPort = findPort();
        LOGGER.info(String.format("Starting RecordingServer on port [%s]...", recordingServerPort));
        recordingServer = new WSRecordingServer(recordingServerPort, NamingStrategyFactory.get()) {
            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                super.onClose(conn, code, reason, remote);
                //scriptReloadScript(recordingServerPort);
            }

            @Override
            public void reloadScript(WebSocket conn, JSONObject query) {
                //scriptReloadScript(recordingServerPort);
            }

            public JSONObject getContextMenuTriggers() {
                return new JSONObject().put("contextMenuKeyModifiers", FXContextMenuTriggers.getContextMenuKeyModifiers())
                        .put("contextMenuKey", FXContextMenuTriggers.getContextMenuKeyCode())
                        .put("menuModifiers", FXContextMenuTriggers.getContextMenuModifiers());
            }

        };
        recordingServer.start();
        LOGGER.info(String.format("RecordingServer started on port [%s]...", recordingServerPort));
        return recordingServerPort;
    }

    private void scriptReloadScript(final int port) {
    	LOGGER.fine(String.format("Reloading script with port[%s]...", port));
        if (!script.isDriverAvailable()) {
            releaseInterpreters();
            destroy();
            return;
        }
        if (recordingServer.isRecording()) {
            WebDriverRuntime.this.reloadPosition = new Exception("Reload going on from here...");
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (WebDriverRuntime.this) {
                        Logger.getLogger(WebDriverRuntime.class.getName()).info("About to reload script...");
                        WebDriverRuntime.this.reloadingScript = true;
                    }
                    script.onWSConnectionClose(port);
                    synchronized (WebDriverRuntime.this) {
                        WebDriverRuntime.this.reloadingScript = false;
                        Logger.getLogger(WebDriverRuntime.class.getName()).info("Script reloaded.");
                    }
                }
            });
            thread.start();
        }
    }

    private int findPort() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(Integer.parseInt(System.getProperty("recordingserver.port", "0")));
            return socket.getLocalPort();
        } catch (IOException e1) {
            throw new RuntimeException("Could not allocate a port: " + e1.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private void replaceEnviron(Map<String, Object> props) {
        Iterator<Entry<String, Object>> iterator = props.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            if (entry.getValue() instanceof String) {
                props.put(entry.getKey(), MPFUtils.getUpdatedValue((String) entry.getValue()));
            }
        }
    }

    @Override
    public void startRecording(IRecorder recorder) {
        recordingServer.startRecording(recorder);
    }

    @Override
    public void stopRecording() {
        System.setProperty("marathon.recording.port", "");
        recordingServer.insertingScriptStart();
        script.runFixtureTeardown();
        recordingServer.stopRecording();
    }

    @Override
    public void startApplication() {
        recordingServer.insertingScriptStart();
        script.runFixtureSetup();
        recordingServer.insertingScriptFinish();
    }

    @Override
    public void stopApplication() {
        if (recordingServer != null) {
            recordingServer.stopRecording();
        }
    }

    @Override
    public void destroy() {
        if (script != null) {
            script.quit();
        } else if (webDriverProxy != null) {
            webDriverProxy.quit(false);
        }
    }

    @Override
    public void releaseInterpreters() {
        if (script != null)
            script.releaseInterpreters();
    }

    @Override
    public Module getModuleFunctions() {
        return script.getModuleFunctions();
    }

    @Override
    public void setRawRecording(boolean selected) {
        if (recordingServer != null) {
            try {
                recordingServer.setRawRecording(selected);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String evaluate(String code) {
        return script.evaluate(code);
    }

    @Override
    public WindowId getTopWindowId() {
        return recordingServer.getFocusedWindowId();
    }

    @Override
    public File getScreenCapture() {
        return script.getScreenCapture();
    }

    @Override
    public void insertScript(String function) {
        try {
            recordingServer.insertingScriptStart();
            script.exec(function);
        } finally {
            recordingServer.insertingScriptFinish();
        }
    }

    @Override
    public IScript createScript(MarathonMode mode, IConsole console, String scriptText, String filePath, boolean isRecording,
            boolean isDebugging, Properties dataVariables) {
        if (mode == MarathonMode.RECORDING) {
            recordingPort = startRecordingServer();
            System.setProperty("marathon.recording.port", "" + recordingPort);
            Logger.getLogger(WebDriverRuntime.class.getName())
                    .info(String.format("RecordingServer started on port [%s]...: ", System.getProperty("marathon.recording.port")));
        }
        scriptModel = ScriptModel.getModel();
        script = scriptModel.createScript(new ScriptOutput(console), new ScriptError(console), scriptText, filePath, isRecording,
                isDebugging, dataVariables, launcherModel.getFramework());
        if (driverURL == null) {
            Map<String, Object> fixtureProperties = scriptModel.getFixtureProperties(scriptText);
            if (launcherModel.needReplaceEnviron()) {
                replaceEnviron(fixtureProperties);
            }
            driverURL = createDriver(fixtureProperties, mode, console);
        }
        script.setDriverURL(driverURL);
        return script;
    }

    @Override
    public void pauseRecording() {
        recordingServer.pauseRecording();
    }

    @Override
    public void resumeRecording() {
        recordingServer.resumeRecording();
    }

}
