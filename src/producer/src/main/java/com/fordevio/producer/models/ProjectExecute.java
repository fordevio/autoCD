package com.fordevio.producer.models;

import java.util.Date;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectExecute {
    
    @NotNull
    private Long id;

    @NotNull
    private Long projectId;

    @NotNull
    private String projectName;

    
    private Date createdDate;
}
