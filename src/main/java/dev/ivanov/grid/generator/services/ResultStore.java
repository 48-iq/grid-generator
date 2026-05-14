package dev.ivanov.grid.generator.services;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import dev.ivanov.grid.generator.entities.TaskResultEntity;
import dev.ivanov.grid.generator.repositories.TaskResultRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResultStore {

    private final TaskResultRepository taskResultRepository;

    private final ConcurrentHashMap<String, DeferredResult<ResponseEntity<String>>> pending =
        new ConcurrentHashMap<>();

    public void saveResult(String taskId, String resultJson) {
        taskResultRepository.save(new TaskResultEntity(taskId, resultJson));
        DeferredResult<ResponseEntity<String>> dr = pending.remove(taskId);
        if (dr != null) {
            dr.setResult(jsonResponse(resultJson));
        }
    }

    public DeferredResult<ResponseEntity<String>> waitForResult(String taskId, long timeoutMs) {
        DeferredResult<ResponseEntity<String>> dr = new DeferredResult<>(
            timeoutMs,
            ResponseEntity.noContent().build()
        );

        taskResultRepository.findById(taskId).ifPresentOrElse(
            entity -> dr.setResult(jsonResponse(entity.getResultJson())),
            () -> {
                pending.put(taskId, dr);
                dr.onCompletion(() -> pending.remove(taskId));
                dr.onTimeout(() -> pending.remove(taskId));
            }
        );

        return dr;
    }

    private ResponseEntity<String> jsonResponse(String json) {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(json);
    }
}
