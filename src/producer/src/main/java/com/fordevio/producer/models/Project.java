package com.fordevio.producer.models;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "projects", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 50, min = 3)
  private String name;
  
  @Size(max = 255)
  private String description;

}
