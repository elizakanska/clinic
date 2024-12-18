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
@Table(name = "pet")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id", nullable = false)
    private Long id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "species")
    private String species;

    @NotBlank
    @Column(name = "type")
    private String breed;

    @NotNull
    @Column(name = "age")
    private Integer age;

    @Column(name = "notes")
    private String notes;

    @NotBlank
    @Column(name = "owner_username")
    private String ownerUsername;
}
