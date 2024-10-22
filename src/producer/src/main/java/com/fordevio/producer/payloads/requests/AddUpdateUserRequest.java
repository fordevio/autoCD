package com.fordevio.producer.payloads.requests;

import java.util.Set;

import com.fordevio.producer.models.enums.Permission;
import com.fordevio.producer.models.enums.Role;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddUpdateUserRequest {
   @NotBlank
   @Size(max = 20, min = 3)
   private String username;
   
   @NotBlank
   @Size(max = 20, min = 4)
   private String password;

   @NotBlank
   @ElementCollection(fetch = FetchType.EAGER)
   @Enumerated(EnumType.STRING)
   private Set<Role> roles;

   @NotBlank
   @ElementCollection(fetch = FetchType.EAGER)
   @Enumerated(EnumType.STRING)
   private Set<Permission> permissions;
}
