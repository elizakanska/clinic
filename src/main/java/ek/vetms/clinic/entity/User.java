package ek.vetms.clinic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "username", nullable = false, length = 50)
    @NotBlank
    private String username;

    @Column(name = "password", nullable = false, length = 68)
    @NotBlank
    private String password;

    @Column(name = "email", nullable = false, length = 100)
    @NotBlank
    private String email;

    @Column(name = "enabled", nullable = false)
    @NotNull
    private Boolean enabled;

    @Column(name = "specialty", length = 100)
    private String specialty;
}
