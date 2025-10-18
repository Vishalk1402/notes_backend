package com.notesapp.notesplatform.controller;

import com.notesapp.notesplatform.entity.Folder;
import com.notesapp.notesplatform.entity.User;
import com.notesapp.notesplatform.repository.UserRepository;
import com.notesapp.notesplatform.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/folders")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Folder> getFolders() {
        return folderService.getAllFolders();
    }

    @PostMapping
    public ResponseEntity<?> createFolder(@RequestBody Folder folder, Authentication auth) {
        // ✅ Use username/email from JWT principal
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if ("ADMIN".equals(user.getRole().name())) {
            return ResponseEntity.ok(folderService.createFolder(folder));
        } else {
            return ResponseEntity.status(403).body("Only admin can create folders");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFolder(@PathVariable Long id, Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if ("ADMIN".equals(user.getRole().name())) {
            folderService.deleteFolder(id);
            return ResponseEntity.ok("Folder deleted successfully");
        } else {
            return ResponseEntity.status(403).body("Only admin can delete folders");
        }
    }
}
