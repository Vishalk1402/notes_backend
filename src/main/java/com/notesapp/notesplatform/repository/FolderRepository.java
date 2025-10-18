package com.notesapp.notesplatform.repository;


import com.notesapp.notesplatform.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    // No extra methods needed for now
}
