package com.github.namuan;

import javaslang.control.Try;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
class CommandRunnerService {

  private final Logger logger = LoggerFactory.getLogger(CommandRunnerService.class);

  public String runCommand(String command) {
    logger.info(String.format("Running command:%s", command));

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    final DefaultExecutor defaultExecutor = new DefaultExecutor();
    defaultExecutor.setStreamHandler(new PumpStreamHandler(outputStream, System.err));

    final CommandLine commandLine = CommandLine.parse(command);

    Try.of(() -> defaultExecutor.execute(commandLine)).getOrElseThrow(e -> {
      throw new RuntimeException(e);
    });

    return outputStream.toString();
  }
}
