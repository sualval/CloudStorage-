package ru.netology.cloudstorage.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/* @Data annotation isn't used because for jpa entities it can cause
severe performance and memory consumption issues
 */

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
@Entity
public class CloudFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "created", nullable = false, updatable = false)
    LocalDateTime created;


    @Column(name = "file_name")
    @NotBlank
    String name;


    @Column(name = "file_type", nullable = false)
    String fileType;

    @Column(nullable = false)
    long size;


    @Lob
    @Column(nullable = false)
    byte[] bytes;

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "username")
    User owner;

    @PrePersist
    protected void create() {
        this.created = LocalDateTime.now();
    }

}
