package com.fordevio.producer.services;

import java.nio.file.*;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileHandlerImpl implements FileHandlerSvc{
    
    @Override 
    public void createDirIfNot(String dirPath) throws Exception {
        dirPath = "/var/autocd/" + dirPath;
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            log.info("Directory created: " + dirPath);
        } else {
            log.info("Directory already exists: " + dirPath);
        }
    }

    @Override 
    public void createScriptsIfNot(String name) throws Exception {
        name = "/var/autocd/scripts/" + name;
        Path path = Paths.get(name);
        if (!Files.exists(path)) {
            Files.createFile(path);
            log.info("File created: " + name);
        } else {
            log.info("File already exists: " + name);
        }
    }

    @Override 
    public void createLogsIfNot(String name) throws Exception {
        name = "/var/autocd/logs/" + name;
        Path path = Paths.get(name);
        if (!Files.exists(path)) {
            Files.createFile(path);
            log.info("File created: " + name);
        } else {
            log.info("File already exists: " + name);
        }
    }

    @Override 
    public void renameLogFiles(String oldName, String newName) throws Exception {
        Path source = Paths.get("/var/autocd/logs/" + oldName);
        Path target = Paths.get("/var/autocd/logs/" + newName);
        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        log.info("Logs File renamed from {} to {}", oldName, newName);
    }

    @Override 
    public void renameScriptFiles(String oldName, String newName) throws Exception {
        Path source = Paths.get("/var/autocd/scripts/" + oldName);
        Path target = Paths.get("/var/autocd/scripts/" + newName);
        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        log.info("Scripts File renamed from {} to {}", oldName, newName);
    }

    @Override 
    public void removeLogFiles(String name) throws Exception {
        Path path = Paths.get("/var/autocd/logs/" + name);
        Files.deleteIfExists(path);
        log.info("Logs File removed: " + name);
    }

    @Override 
    public void removeScriptFiles(String name) throws Exception {
        Path path = Paths.get("/var/autocd/scripts/" + name);
        Files.deleteIfExists(path);
        log.info("Scripts File removed: " + name);
    }

}
