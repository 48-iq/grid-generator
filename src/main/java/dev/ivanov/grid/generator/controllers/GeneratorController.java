package dev.ivanov.grid.generator.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import dev.ivanov.grid.generator.dto.NewTaskDto;
import dev.ivanov.grid.generator.services.TaskSendService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GeneratorController {

  private final TaskSendService taskSendService;
  
  @PostMapping("/new-task")
  public void generate(NewTaskDto newTaskDto) {
    taskSendService.send(newTaskDto.getMatrix());
  }
}
