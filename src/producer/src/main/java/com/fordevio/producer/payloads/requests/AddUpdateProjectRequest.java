package com.fordevio.producer.payloads.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddUpdateProjectRequest {
  @NotBlank
  @Size(max = 50, min = 3)
  private String name;
  
  @Size(max = 255)
  private String description;
}
