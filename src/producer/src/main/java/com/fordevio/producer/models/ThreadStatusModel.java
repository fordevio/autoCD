package com.fordevio.producer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ThreadStatusModel {
    private Thread thread;
    private Boolean running;
}
