package dev.ivanov.grid.generator.services;

import org.springframework.stereotype.Service;
import com.google.protobuf.Empty;
import dev.ivanov.grid.proto.ResultServiceGrpc.ResultServiceImplBase;
import dev.ivanov.grid.proto.TaskResult;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultService extends ResultServiceImplBase {

  private final ResultStore resultStore;

  @Override
  public void postTaskResult(
      TaskResult request,
      StreamObserver<Empty> responseObserver) {

    log.info("postTaskResult: taskId={}, result={}", request.getTaskId(), request.getResult());

    resultStore.saveResult(request.getTaskId(), request.getResult());

    responseObserver.onNext(Empty.newBuilder().build());
    responseObserver.onCompleted();
  }
}
