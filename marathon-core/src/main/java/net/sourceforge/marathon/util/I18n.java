package net.sourceforge.marathon.util;

import java.util.ResourceBundle;

/**
 * Clase para cargar los valores de los strings personalizados.
 * 
 * @author glopez
 *
 */
public class I18n {

	public static final String SELECT_PROJECT_SHORT 										= "select.project.short";
	public static final String SELECT_PROJECT_LARGE 										= "select.project.large";
	public static final String SELECT_PROJECT_TABLE_NAME 									= "select.project.table.name";
	public static final String SELECT_PROJECT_TABLE_FRAMEWORK 								= "select.project.table.framework";
	public static final String SELECT_PROJECT_TABLE_DESTINATION 							= "select.project.table.destination";
	public static final String SELECT_PROJECT_BUTTON_NEW 									= "select.project.button.new";
	public static final String SELECT_PROJECT_BUTTON_NEW_TOOLTIP 							= "select.project.button.new.tooltip";
	public static final String SELECT_PROJECT_BUTTON_BROWSE 								= "select.project.button.browse";
	public static final String SELECT_PROJECT_BUTTON_BROWSE_TOOLTIP							= "select.project.button.browse.tooltip";
	public static final String SELECT_PROJECT_BUTTON_DELETE 								= "select.project.button.delete";
	public static final String SELECT_PROJECT_BUTTON_DELETE_TOOLTIP							= "select.project.button.delete.tooltip";
	public static final String SELECT_PROJECT_BUTTON_EDIT 									= "select.project.button.edit";
	public static final String SELECT_PROJECT_BUTTON_EDIT_TOOLTIP							= "select.project.button.edit.tooltip";
	public static final String SELECT_PROJECT_BUTTON_CANCEL 								= "select.project.button.cancel";
	public static final String SELECT_PROJECT_BUTTON_CANCEL_TOOLTIP							= "select.project.button.cancel.tooltip";
	public static final String SELECT_PROJECT_BUTTON_RUN 									= "select.project.button.run";
	public static final String SELECT_PROJECT_BUTTON_RUN_TOOLTIP							= "select.project.button.run.tooltip";
	public static final String EDIT_PROJECT_TITLE											= "edit-project.title";
	public static final String NEW_PROJECT_TITLE											= "new-project.title";
	public static final String PROJECT_LAYOUT_NAME											= "project-layout.name";
	public static final String PROJECT_LAYOUT_NAME_ERROR_MESSAGE							= "project-layout.name.error-message";
	public static final String PROJECT_LAYOUT_NAME_ERROR_MESSAGE_FIELD						= "project-layout.name.error-message.field";
	public static final String PROJECT_LAYOUT_DIRECTORY										= "project-layout.directory";
	public static final String PROJECT_LAYOUT_DIRECTORY_ERROR_MESSAGE						= "project-layout.directory.error-message";
	public static final String PROJECT_LAYOUT_DIRECTORY_ERROR_MESSAGE_FIELD					= "project-layout.directory.error-message.field";
	public static final String PROJECT_LAYOUT_DESCRIPTION									= "project-layout.description";
	public static final String PROJECT_LAYOUT_ISSUE_TRACKER_PATTERN							= "project-layout.issue-tracker-pattern";
	public static final String PROJECT_LAYOUT_TEST_MANAGEMENT_PATTERN						= "project-layout.test-management-pattern";
	public static final String PROJECT_LAYOUT_BUTTON_BROWSE									= "project-layout.button.browse";
	public static final String PROJECT_LAYOUT_BUTTON_BROWSE_TOOLTIP							= "project-layout.button.browse.tooltip";
	public static final String PROJECT_LAYOUT_TAB_LABEL										= "project-layout.project.tab.label";
	public static final String FILE_SELECTION_HANDLER_TITLE									= "file-selection-handler.title";
	public static final String APPLICATION_LAYOUT_APPLICATION_TAB_LABEL						= "application-layout.application.tab.label";
	
	public static final String APPLICATION_LAYOUT_APPLICATION_LAUNCHER_LABEL				= "application-layout.application-launcher.label";
	public static final String APPLICATION_LAYOUT_APPLICATION_LAUNCHER_ERROR_MESSAGE		= "application-layout.application-launcher.error-message";
	public static final String APPLICATION_LAYOUT_APPLICATION_LAUNCHER_ERROR_MESSAGE_FIELD	= "application-layout.application-launcher.error-message.field";
	
	public static final String MPF_CONFIGURATION_STAGE_BUTTON_SAVE							= "mpf-configuration-stage.button.save";
	public static final String MPF_CONFIGURATION_STAGE_BUTTON_SAVE_TOOLTIP					= "mpf-configuration-stage.button.save.tooltip";
	public static final String MPF_CONFIGURATION_STAGE_BUTTON_CANCEL						= "mpf-configuration-stage.button.cancel";
	public static final String MPF_CONFIGURATION_STAGE_BUTTON_CANCEL_TOOLTIP				= "mpf-configuration-stage.button.cancel.tooltip";
	public static final String MPF_CONFIGURATION_STAGE_BUTTON_TEST							= "mpf-configuration-stage.button.test";
	public static final String MPF_CONFIGURATION_STAGE_BUTTON_TEST_TOOLTIP					= "mpf-configuration-stage.button.test.tooltip";
	
	public static final String LAUNCHER_WEBSTART_TAB_MAIN_LABEL								= "launcher.webstart.tab.main.label";
	public static final String LAUNCHER_WEBSTART_BUTTON_BROWSE_JNLP 						= "launcher.webstart.button.browse.jnlp";
	public static final String LAUNCHER_WEBSTART_BUTTON_BROWSE_JNLP_TOOLTIP 				= "launcher.webstart.button.browse.jnlp.tooltip";
	public static final String LAUNCHER_WEBSTART_BUTTON_BROWSE_JAVA 						= "launcher.webstart.button.browse.java";
	public static final String LAUNCHER_WEBSTART_BUTTON_BROWSE_JAVA_TOOLTIP 				= "launcher.webstart.button.browse.java.tooltip";
	public static final String LAUNCHER_WEBSTART_URL 										= "launcher.webstart.url";
	public static final String LAUNCHER_WEBSTART_URL_ERROR_MESSAGE 							= "launcher.webstart.url.error-message";
	public static final String LAUNCHER_WEBSTART_URL_ERROR_MESSAGE_FIELD 					= "launcher.webstart.url.error-message.field";
	public static final String LAUNCHER_WEBSTART_WINDOW_TITLE 								= "launcher.webstart.window-title";
	public static final String LAUNCHER_WEBSTART_VM_ARGUMENTS 								= "launcher.webstart.vm-arguments";
	public static final String LAUNCHER_WEBSTART_VM_ARGUMENTS_ERROR_MESSAGE 				= "launcher.webstart.vm-arguments.error-message";
	public static final String LAUNCHER_WEBSTART_VM_ARGUMENTS_ERROR_MESSAGE_FIELD 			= "launcher.webstart.vm-arguments.error-message.field";
	public static final String LAUNCHER_WEBSTART_WEBSTART_OPTIONS 							= "launcher.webstart.webstart-options"; 
	public static final String LAUNCHER_WEBSTART_WEBSTART_OPTIONS_ERROR_MESSAGE 			= "launcher.webstart.webstart-options.error-message";
	public static final String LAUNCHER_WEBSTART_WEBSTART_OPTIONS_ERROR_MESSAGE_FIELD 		= "launcher.webstart.webstart-options.error-message.field";
	public static final String LAUNCHER_WEBSTART_NO_SPLASH 									= "launcher.webstart.no-splash";
	public static final String LAUNCHER_WEBSTART_OFFLINE									= "launcher.webstart.offline";
	public static final String LAUNCHER_WEBSTART_JAVA_HOME									= "launcher.webstart.java-home";
	public static final String LAUNCHER_WEBSTART_URL_DIR_HANDLER_TITLE						= "launcher.webstart.url-dir-handler.title";
	public static final String LAUNCHER_WEBSTART_JAVA_HOME_HANDLER_TITLE					= "launcher.webstart.java-home-handler.title";

	public static final String LAUNCHER_CMDLINE_TAB_MAIN_LABEL								= "launcher.cmdline.tab.main.label";
	public static final String LAUNCHER_CMDLINE_BUTTON_BROWSE_SCRIPT						= "launcher.cmdline.button.browse.script";
	public static final String LAUNCHER_CMDLINE_BUTTON_BROWSE_SCRIPT_TOOLTIP				= "launcher.cmdline.button.browse.script.tooltip";
	public static final String LAUNCHER_CMDLINE_BUTTON_BROWSE_WORKING_DIR					= "launcher.cmdline.button.browse.working-dir";
	public static final String LAUNCHER_CMDLINE_BUTTON_BROWSE_WORKING_DIR_TOOLTIP			= "launcher.cmdline.button.browse.working-dir.tooltip";
	public static final String LAUNCHER_CMDLINE_BUTTON_BROWSE_JAVA_HOME						= "launcher.cmdline.button.browse.java-home";
	public static final String LAUNCHER_CMDLINE_BUTTON_BROWSE_JAVA_HOME_TOOLTIP				= "launcher.cmdline.button.browse.java-home.tooltip";
	public static final String LAUNCHER_CMDLINE_SCRIPT										= "launcher.cmdline.script";
	public static final String LAUNCHER_CMDLINE_SCRIPT_ERROR_MESSAGE						= "launcher.cmdline.script.error-message";
	public static final String LAUNCHER_CMDLINE_SCRIPT_ERROR_MESSAGE_FIELD					= "launcher.cmdline.script.error-message.field";
	public static final String LAUNCHER_CMDLINE_WORKING_DIR									= "launcher.cmdline.working-dir"; 
	public static final String LAUNCHER_CMDLINE_WINDOW_TITLE								= "launcher.cmdline.window-title"; 
	public static final String LAUNCHER_CMDLINE_PROGRAM_ARGS								= "launcher.cmdline.program-args";
	public static final String LAUNCHER_CMDLINE_PROGRAM_ARGS_ERROR_MESSAGE					= "launcher.cmdline.program-args.error-message";
	public static final String LAUNCHER_CMDLINE_PROGRAM_ARGS_ERROR_MESSAGE_FIELD			= "launcher.cmdline.program-args.error-message";
	public static final String LAUNCHER_CMDLINE_VM_ARGS										= "launcher.cmdline.vm-args";
	public static final String LAUNCHER_CMDLINE_VM_ARGS_ERROR_MESSAGE						= "launcher.cmdline.vm-args.error-message";
	public static final String LAUNCHER_CMDLINE_VM_ARGS_ERROR_MESSAGE_FIELD					= "launcher.cmdline.vm-args.error-message.field";
	public static final String LAUNCHER_CMDLINE_JAVA_HOME									= "launcher.cmdline.java-home";
	public static final String LAUNCHER_CMDLINE_JARFILE_DIR_HANDLER_TITLE					= "launcher.cmdline.jarfile-dir-handler.title";
	public static final String LAUNCHER_CMDLINE_WOWRKING_DIR_HANDLER_TITLE					= "launcher.cmdline.working-dir-handler.title";
	public static final String LAUNCHER_CMDLINE_JAVA_HOME_DIR_HANDLER_TITLE					= "launcher.cmdline.java-home-dir-handler.title";
	
	public static final String LAUNCHER_APPLET_TAB_MAIN_LABEL								= "launcher.applet.tab.main.label";
	public static final String LAUNCHER_APPLET_BUTTON_BROWSE_URL							= "launcher.applet.button.browse.url";
	public static final String LAUNCHER_APPLET_BUTTON_BROWSE_URL_TOOLTIP 					= "launcher.applet.button.browse.url.tooltip";
	public static final String LAUNCHER_APPLET_BUTTON_BROWSE_JAVA_HOME						= "launcher.applet.button.browse.java-home";
	public static final String LAUNCHER_APPLET_BUTTON_BROWSE_JAVA_HOME_TOOLTIP				= "launcher.applet.button.browse.java-home.tooltip";
	public static final String LAUNCHER_APPLET_URL											= "launcher.applet.url";
	public static final String LAUNCHER_APPLET_URL_ERROR_MESSAGE							= "launcher.applet.url.error-message";
	public static final String LAUNCHER_APPLET_URL_ERROR_MESSAGE_FIELD						= "launcher.applet.url.error-message.field";
	public static final String LAUNCHER_APPLET_WINDOW_TITLE									= "launcher.applet.window-title";
	public static final String LAUNCHER_APPLET_VM_ARGS										= "launcher.applet.vm-args";
	public static final String LAUNCHER_APPLET_VM_ARGS_ERROR_MESSAGE						= "launcher.applet.vm-args.error-message";
	public static final String LAUNCHER_APPLET_VM_ARGS_ERROR_MESSAGE_FIELD					= "launcher.applet.vm-args.error-message.field";
	public static final String LAUNCHER_APPLET_JAVA_HOME									= "launcher.applet.java-home";
	public static final String LAUNCHER_APPLET_URL_DIR_HANDLER_TITLE						= "launcher.applet.url-dir-handler.title";
	public static final String LAUNCHER_APPLET_JAVA_HOME_DIR_HANDLER_TITLE					= "launcher.applet.java-home-dir-handler.title";
	
	public static final String CLASSPATH_BUTTON_UP 											= "classpath.button.up";
	public static final String CLASSPATH_BUTTON_UP_TOOLTIP									= "classpath.button.up.tooltip";
	public static final String CLASSPATH_BUTTON_DOWN										= "classpath.button.down";
	public static final String CLASSPATH_BUTTON_DOWN_TOOLTIP								= "classpath.button.down.tooltip";
	public static final String CLASSPATH_BUTTON_ADD_ARCHIVES								= "classpath.button.add-archives";
	public static final String CLASSPATH_BUTTON_ADD_ARCHIVES_TOOLTIP						= "classpath.button.add-archives.tooltip";
	public static final String CLASSPATH_BUTTON_ADD_FOLDERS									= "classpath.button.add-folders";
	public static final String CLASSPATH_BUTTON_ADD_FOLDERS_TOOLTIP							= "classpath.button.add-folders.tooltip";
	public static final String CLASSPATH_BUTTON_REMOVE										= "classpath.button.remove";
	public static final String CLASSPATH_BUTTON_REMOVE_TOOLTIP								= "classpath.button.remove.tooltip";
	public static final String CLASSPATH_POPUP_FILE_SELECTION_TITLE 						= "classpath.popup.file-selection.title";
	public static final String CLASSPATH_POPUP_FILE_SELECTION_FILE_TYPE						= "classpath.popup.file-selection.file-type";
	public static final String CLASSPATH_POPUP_FILE_SELECTION_SUB_TITLE 					= "classpath.popup.file-selection.sub-title";
	public static final String CLASSPATH_POPUP_FOLDER_SELECTION_TITLE 						= "classpath.popup.folder-selection.title";
	public static final String CLASSPATH_POPUP_FOLDER_SELECTION_SUB_TITLE 					= "classpath.popup.folder-selection.sub-title";

	public static final String FILE_SELECTION_STAGE_BUTTON_BROWSE 							= "file-selection-stage.button.browse";
	public static final String FILE_SELECTION_STAGE_BUTTON_BROWSE_TOOLTIP					= "file-selection-stage.button.browse.tooltip";
	public static final String FILE_SELECTION_STAGE_BUTTON_OK								= "file-selection-stage.button.ok";
	public static final String FILE_SELECTION_STAGE_BUTTON_OK_TOOLTIP 						= "file-selection-stage.button.ok.tooltip";
	public static final String FILE_SELECTION_STAGE_BUTTON_CANCEL							= "file-selection-stage.button.cancel";
	public static final String FILE_SELECTION_STAGE_BUTTON_CANCEL_TOOLTIP					= "file-selection-stage.button.cancel.tooltip";
	public static final String FILE_SELECTION_STAGE_NAME	 								= "file-selection-stage.name";
	
	public static final String LAUNCHER_JCMDLINE_TAB_MAIN_LABEL								= "launcher.jcmdline.tab.main.label";
	public static final String LAUNCHER_JCMDLINE_BUTTON_BROWSE_WORKING_DIR 					= "launcher.jcmdline.button.browse.working-dir";
	public static final String LAUNCHER_JCMDLINE_BUTTON_BROWSE_WORKING_DIR_TOOLTIP 			= "launcher.jcmdline.button.browse.working-dir.tooltip";
	public static final String LAUNCHER_JCMDLINE_BUTTON_BROWSE_JAVA_HOME					= "launcher.jcmdline.button.browse.java-home";
	public static final String LAUNCHER_JCMDLINE_BUTTON_BROWSE_JAVA_HOME_TOOLTIP			= "launcher.jcmdline.button.browse.java-home.tooltip";
	public static final String LAUNCHER_JCMDLINE_CLASS_NAME									= "launcher.jcmdline.class-name";
	public static final String LAUNCHER_JCMDLINE_CLASS_NAME_ERROR_MESSAGE					= "launcher.jcmdline.class-name.error-message";
	public static final String LAUNCHER_JCMDLINE_CLASS_NAME_ERROR_MESSAGE_FIELD				= "launcher.jcmdline.class-name.error-message.field";
	public static final String LAUNCHER_JCMDLINE_CLASS_NAME_ERROR_MESSAGE2					= "launcher.jcmdline.class-name.error-message2";
	public static final String LAUNCHER_JCMDLINE_CLASS_NAME_ERROR_MESSAGE2_FIELD			= "launcher.jcmdline.class-name.error-message2.field";
	public static final String LAUNCHER_JCMDLINE_CLASS_NAME_ERROR_MESSAGE3					= "launcher.jcmdline.class-name.error-message3";
	public static final String LAUNCHER_JCMDLINE_CLASS_NAME_ERROR_MESSAGE3_FIELD			= "launcher.jcmdline.class-name.error-message3.field";
	public static final String LAUNCHER_JCMDLINE_PROGRAM_ARGS								= "launcher.jcmdline.program-args";
	public static final String LAUNCHER_JCMDLINE_PROGRAM_ARGS_ERROR_MESSAGE					= "launcher.jcmdline.program-args.error-message";
	public static final String LAUNCHER_JCMDLINE_PROGRAM_ARGS_ERROR_MESSAGE_FIELD			= "launcher.jcmdline.program-args.error-message.field";
	public static final String LAUNCHER_JCMDLINE_VM_ARGS									= "launcher.jcmdline.vm-args";
	public static final String LAUNCHER_JCMDLINE_VM_ARGS_ERROR_MESSAGE						= "launcher.jcmdline.vm-args.error-message";
	public static final String LAUNCHER_JCMDLINE_VM_ARGS_ERROR_MESSAGE_FIELD				= "launcher.jcmdline.vm-args.error-message.field";
	public static final String LAUNCHER_JCMDLINE_WORKING_DIR								= "launcher.jcmdline.working-dir";
	public static final String LAUNCHER_JCMDLINE_JAVA_HOME									= "launcher.jcmdline.java-home";
	public static final String LAUNCHER_JCMDLINE_WORKING_DIR_HANDLER_TITLE					= "launcher.jcmdline.working-dir-handler.title";
	public static final String LAUNCHER_JCMDLINE_JAVA_HOME_DIR_HANDLER_TITLE				= "launcher.jcmdline.java-home-dir-handler.title";
	
	public static final String LAUNCHER_EXEC_JAR_TAB_MAIN_LABEL 							= "launcher.exec-jar.tab.main.label";
	public static final String LAUNCHER_EXEC_JAR_BUTTON_BROWSE_JAR_FILE						= "launcher.exec-jar.button.browse.jar-file";
	public static final String LAUNCHER_EXEC_JAR_BUTTON_BROWSE_JAR_FILE_TOOLTIP				= "launcher.exec-jar.button.browse.jar-file.tooltip";
	public static final String LAUNCHER_EXEC_JAR_BUTTON_BROWSE_WORKING_DIR					= "launcher.exec-jar.button.browse.working-dir";
	public static final String LAUNCHER_EXEC_JAR_BUTTON_BROWSE_WORKING_DIR_TOOLTIP			= "launcher.exec-jar.button.browse.working-dir.tooltip";
	public static final String LAUNCHER_EXEC_JAR_BUTTON_BROWSE_JAVA_HOME					= "launcher.exec-jar.button.browse.java-home";
	public static final String LAUNCHER_EXEC_JAR_BUTTON_BROWSE_JAVA_HOME_TOOLTIP 			= "launcher.exec-jar.button.browse.java-home.tooltip";
	public static final String LAUNCHER_EXEC_JAR_JAR_FILE									= "launcher.exec-jar.jar-file";
	public static final String LAUNCHER_EXEC_JAR_JAR_FILE_ERROR_MESSAGE						= "launcher.exec-jar.jar-file.error-message";
	public static final String LAUNCHER_EXEC_JAR_JAR_FILE_ERROR_MESSAGE_FIELD				= "launcher.exec-jar.jar-file.error-message.field";
	public static final String LAUNCHER_EXEC_JAR_WORKING_DIR								= "launcher.exec-jar.working-dir";
	public static final String LAUNCHER_EXEC_JAR_WINDOW_TITLE								= "launcher.exec-jar.window-title";
	public static final String LAUNCHER_EXEC_JAR_PROGRAM_ARGS								= "launcher.exec-jar.program-args";
	public static final String LAUNCHER_EXEC_JAR_PROGRAM_ARGS_ERROR_MESSAGE					= "launcher.exec-jar.program-args.error-message";
	public static final String LAUNCHER_EXEC_JAR_PROGRAM_ARGS_ERROR_MESSAGE_FIELD			= "launcher.exec-jar.program-args.error-message.field";
	public static final String LAUNCHER_EXEC_JAR_VM_ARGS									= "launcher.exec-jar.vm-args";
	public static final String LAUNCHER_EXEC_JAR_VM_ARGS_ERROR_MESSAGE						= "launcher.exec-jar.vm-args.error-message";
	public static final String LAUNCHER_EXEC_JAR_VM_ARGS_ERROR_MESSAGE_FIELD				= "launcher.exec-jar.vm-args.error-message.field";
	public static final String LAUNCHER_EXEC_JAR_JAVA_HOME									= "launcher.exec-jar.java-home";
	public static final String LAUNCHER_EXEC_JAR_JARFILE_DIR_HANDLER_TITLE					= "launcher.exec-jar.jarfile-dir-handler.title";
	public static final String LAUNCHER_EXEC_JAR_WORKING_DIR_HANDLER_TITLE					= "launcher.exec-jar.working-dir-handler.title";
	public static final String LAUNCHER_EXEC_JAR_JAVA_HOME_DIR_HANDLER_TITLE				= "launcher.exec-jar.java-home-dir-handler.title";
	
	private static final String FILE_NAME = "i18n";
	private static ResourceBundle labels;
	
	private I18n() {
		
	}
	
	/**
	 * 
	 * @return
	 */
	private static final ResourceBundle getLabels() {
		if (labels == null) {
			labels = ResourceBundle.getBundle(FILE_NAME);
		}
		return labels;
	}
	
	/**
	 * 
	 * @param key Clave de la cadena de texto indicada.
	 * @return El string internacionalizado.
	 */
	public static final String getI18nLabel(String key) {
		return getLabels().getString(key);
	}
}
