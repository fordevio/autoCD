package com.fordevio.producer.services;

public interface FileHandlerSvc {
    
    public void createDirIfNot(String dirPath) throws Exception;
    public void createScriptsIfNot(String name) throws Exception;
    public void createLogsIfNot(String name) throws Exception;
    public void renameLogFiles(String oldName, String newName) throws Exception;
    public void renameScriptFiles(String oldName, String newName) throws Exception;
    public void removeLogFiles(String name) throws Exception;
    public void removeScriptFiles(String name) throws Exception;
} 