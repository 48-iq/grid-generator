package dev.ivanov.grid.generator.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import dev.ivanov.grid.generator.services.ResultStore;
import dev.ivanov.grid.generator.services.TaskSendService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TaskController {

    private final TaskSendService taskSendService;
    private final ResultStore resultStore;

    @PostMapping("/task")
    public Map<String, String> createTask(@RequestBody List<List<Integer>> matrix) {
        String taskId = taskSendService.send(matrix);
        return Map.of("taskId", taskId);
    }

    @GetMapping("/result/{taskId}")
    public DeferredResult<ResponseEntity<String>> getResult(
            @PathVariable String taskId,
            @RequestParam(defaultValue = "30000") long timeout) {
        return resultStore.waitForResult(taskId, timeout);
    }
}
