package com.fordevio.producer.services.fileIO;

import java.io.IOException;

public interface FileHandlerSvc {
    
    public void createDirIfNot(String dirPath) throws Exception;
    public void createScriptsIfNot(String name) throws Exception;
    public void createLogsIfNot(String name) throws Exception;
    public void renameLogFiles(String oldName, String newName) throws Exception;
    public void renameScriptFiles(String oldName, String newName) throws Exception;
    public void removeLogFiles(String name) throws Exception;
    public void removeScriptFiles(String name) throws Exception;
    public void editProjectScript(String name, String script) throws Exception;
    public String getProjectScript(String name) throws Exception;
    public String getProjectLogs(String name) throws Exception;
    public String getProjectLogPath(String name) throws Exception;
    public void executeShellScript(String scriptFilePath, String logFilePath) throws IOException, InterruptedException;
} 