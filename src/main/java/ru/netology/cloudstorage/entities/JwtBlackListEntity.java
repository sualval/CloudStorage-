package ru.netology.cloudstorage.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Objects;

/* @Data annotation isn't used because for jpa entities it can cause
severe performance and memory consumption issues
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "black_list_jwt")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtBlackListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String jwt;

    @Column(nullable = false)
    Long exp;

    public JwtBlackListEntity(String jwt, Long exp) {
        this.jwt = jwt;
        this.exp = exp;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtBlackListEntity jwtBlackListEntity = (JwtBlackListEntity) o;
        return jwt.equals(jwtBlackListEntity.jwt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jwt);
    }


}
