package ru.netology.cloudstorage.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/* @Data annotation isn't used because for jpa entities it can cause
severe performance and memory consumption issues
 */

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Username shouldn't be empty")
    @Size(min = 2, max = 20, message = "Username should be between 2 and 20 characters")
    @Column(unique = true, name = "username")
    String login;

    @NotBlank
    @Size(min = 3, max = 60, message = "Password should be between 3 and 60 characters")
    @Column(nullable = false)
    String password;


    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    Role role;


    public User(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }


}
