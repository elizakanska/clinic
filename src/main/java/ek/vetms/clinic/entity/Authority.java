package ek.vetms.clinic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authorities")
public class Authority {

    @EmbeddedId
    private AuthorityId id;

    @NotBlank
    @Column(name = "username", insertable = false, updatable = false)
    private String username;

    @NotBlank
    @Column(name = "authority", insertable = false, updatable = false)
    private String authority;
}
