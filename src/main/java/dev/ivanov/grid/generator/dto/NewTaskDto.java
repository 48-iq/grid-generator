package dev.ivanov.grid.generator.dto;

import java.util.List;

import lombok.Data;

@Data
public class NewTaskDto {
  private List<List<Integer>> matrix;
}
