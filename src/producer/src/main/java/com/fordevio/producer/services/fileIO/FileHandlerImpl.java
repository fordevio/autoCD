package com.fordevio.producer.services.fileIO;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fordevio.producer.models.User;

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
        name = "/var/autocd/scripts/" + name+".sh";
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
        name = "/var/autocd/logs/" + name+".log";
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
        Path source = Paths.get("/var/autocd/logs/" + oldName+".log");
        Path target = Paths.get("/var/autocd/logs/" + newName+".log");
        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        log.info("Logs File renamed from {} to {}", oldName, newName);
    }

    @Override 
    public void renameScriptFiles(String oldName, String newName) throws Exception {
        Path source = Paths.get("/var/autocd/scripts/" + oldName+".sh");
        Path target = Paths.get("/var/autocd/scripts/" + newName+".sh");
        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        log.info("Scripts File renamed from {} to {}", oldName, newName);
    }

    @Override 
    public void removeLogFiles(String name) throws Exception {
        Path path = Paths.get("/var/autocd/logs/" + name+  ".log");
        Files.deleteIfExists(path);
        log.info("Logs File removed: " + name);
    }

    @Override 
    public void removeScriptFiles(String name) throws Exception {
        Path path = Paths.get("/var/autocd/scripts/" + name+  ".sh");
        Files.deleteIfExists(path);
        log.info("Scripts File removed: " + name);
    }

    @Override
    public void editProjectScript(String name, String script) throws Exception {
        Path path = Paths.get("/var/autocd/scripts/" + name+  ".sh");
        Files.write(path, script.getBytes());
        log.info("Scripts File edited: " + name);
    }
    
    @Override
    public String getProjectScript(String name) throws Exception {
        Path path = Paths.get("/var/autocd/scripts/" + name+  ".sh");
        String content = Files.readString(path);
        return content;
    }

    @Override
    public String getProjectLogs(String name) throws Exception {
        Path path = Paths.get("/var/autocd/logs/" + name+  ".log");
        String content = Files.readString(path);
        return content;
    }

    @Override
    public String getProjectLogPath(String name) throws Exception {
        return "/var/autocd/logs/" + name+  ".log";
    }

    @Override
    public void executeShellScript(String scriptFilePath, String logFilePath) throws IOException, InterruptedException {
        // Define the command to execute the shell script
        File file = new File(scriptFilePath);
        file.setExecutable(true);

        ProcessBuilder processBuilder = new ProcessBuilder("bash", scriptFilePath);

        // Redirect output (stdout and stderr) to the log file
        File logFile = new File(logFilePath);
        processBuilder.redirectOutput(logFile);
        processBuilder.redirectError(logFile);
        Process process = processBuilder.start();

        // Wait for the process to finish
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            log.info(scriptFilePath + " executed successfully");
        } else {
            log.error(scriptFilePath + " failed with exit code " + exitCode);
        }
    }

    @Override
    public void updateCredentialFile(User admin) throws Exception {
        String path = "/var/autocd/admin-credential.json";
        File credentialFile = new File(path);

        ObjectMapper objectMapper = new ObjectMapper();
        if (credentialFile.exists()) {
            Map<String, Object> jsonData = objectMapper.readValue(credentialFile, new TypeReference<Map<String, Object>>() {});
            jsonData.put("username", admin.getUsername());
            jsonData.put("password", admin.getPassword());
            objectMapper.writeValue(credentialFile, jsonData);
            log.info("Credentials file updated");
        }
        Map<String, Object> defaultData = new HashMap<>();
        defaultData.put("username", admin.getUsername());
        defaultData.put("password", admin.getPassword());
        objectMapper.writeValue(credentialFile, defaultData);
        log.info("Credentials file created");
    }
}
