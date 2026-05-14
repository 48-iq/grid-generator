package dev.ivanov.grid.generator.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.protobuf.ByteString;

import dev.ivanov.grid.proto.NewTaskRequest;
import dev.ivanov.grid.proto.NewTaskResponse;
import dev.ivanov.grid.proto.TaskServiceGrpc.TaskServiceBlockingStub;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskSendService {

    private final TaskServiceBlockingStub taskServiceBlockingStub;

    private final ObjectMapper objectMapper;

    public String send(List<List<Integer>> data) {
        ClassPathResource resource = new ClassPathResource("task.jar");
        if (!resource.exists()) {
            throw new RuntimeException("JAR file not found in resources: " + resource.getPath());

        }
        try (InputStream jarStream = resource.getInputStream()) {

            NewTaskRequest request = NewTaskRequest.newBuilder()
                    .setJar(ByteString.readFrom(jarStream))
                    .setData(objectMapper.writeValueAsString(data))
                    .build();
            NewTaskResponse response = taskServiceBlockingStub.postNewTask(request);
            return response.getTaskId();

        } catch (IOException e) {
            log.error(e.getStackTrace().toString(), e.getMessage());
            throw new RuntimeException("Failed to read JAR file", e);
        }

    }
}
