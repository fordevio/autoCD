package com.fordevio.producer.models;

import java.io.Serializable;
import java.util.Set;

import com.fordevio.producer.models.enums.Permission;
import com.fordevio.producer.models.enums.Role;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "users", uniqueConstraints = {
      @UniqueConstraint(columnNames = "username")
})

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User implements Serializable{
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

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
