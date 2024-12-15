package com.fordevio.producer.payloads.response;

import java.util.Set;

import com.fordevio.producer.models.enums.Permission;
import com.fordevio.producer.models.enums.Role;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CurrentUserResponse {
    private Long id;

   private String username;
   


   @ElementCollection(fetch = FetchType.EAGER)
   @Enumerated(EnumType.STRING)
   private Set<Role> roles;

   @ElementCollection(fetch = FetchType.EAGER)
   @Enumerated(EnumType.STRING)
   private Set<Permission> permissions;
}
