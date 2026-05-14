package dev.ivanov.grid.generator.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.ivanov.grid.proto.TaskServiceGrpc;
import dev.ivanov.grid.proto.TaskServiceGrpc.TaskServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

@Configuration
public class ClientConfig {
  
  @Bean
  public TaskServiceBlockingStub blockingStub(
    @Value("${app.separator.host}") String host,
    @Value("${app.separator.port}") int port
  ) {
    ManagedChannel channel = ManagedChannelBuilder
        .forAddress(host, port)
        .usePlaintext()
        .build();
    
    TaskServiceBlockingStub taskServiceBlockingStub = TaskServiceGrpc
        .newBlockingStub(channel);
    
    return taskServiceBlockingStub;
  }
}
