package com.fordevio.consumer.service.kafka;

import java.io.File;
import java.io.IOException;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaMessageListener {

    @KafkaListener(topics = "autocd", groupId = "autocd-consumer")
    public void listen(String message) {
        log.info("message recieved: {}", message);
        processMsg(message);
    }

    private void processMsg(String projectName) {
       String scriptFilePath = "/var/autocd/scripts/"+projectName+".sh";
       String logFilePath = "/var/autocd/logs/"+projectName+".log";
       try {
          executeShellScript(scriptFilePath, logFilePath);
        } catch (IOException | InterruptedException e) {
          log.error(scriptFilePath, e);
       }
    }

        private void executeShellScript(String scriptFilePath, String logFilePath) throws IOException, InterruptedException {
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
}
