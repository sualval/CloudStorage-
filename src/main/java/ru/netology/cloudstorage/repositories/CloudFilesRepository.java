package ru.netology.cloudstorage.repositories;


import ru.netology.cloudstorage.entities.CloudFile;
import ru.netology.cloudstorage.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CloudFilesRepository extends JpaRepository<CloudFile, Long> {


    List<CloudFile> findAllByOwnerOrderByCreatedDesc(User user);


    Optional<CloudFile> findCloudFileByName(String name);


    Optional<CloudFile> findCloudFileByNameAndOwner(String filename, User user);

}
