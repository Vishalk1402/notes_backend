package com.notesapp.notesplatform.controller;

import com.notesapp.notesplatform.entity.Note;
import com.notesapp.notesplatform.entity.User;
import com.notesapp.notesplatform.repository.UserRepository;
import com.notesapp.notesplatform.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping("/folder/{folderId}")
    public List<Note> getNotesByFolder(@PathVariable Long folderId) {
        return noteService.getNotesByFolder(folderId);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> uploadNote(@RequestParam("file") MultipartFile file,
                                        @RequestParam("title") String title,
                                        @RequestParam("folderId") Long folderId,
                                        Authentication auth) throws IOException {

        // ✅ Use username (email) from JWT principal
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"ADMIN".equals(user.getRole().name())) {
            return ResponseEntity.status(403).body("Only admin can upload notes");
        }

        Note note = noteService.uploadNote(file, title, folderId);
        return ResponseEntity.ok(note);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id, Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"ADMIN".equals(user.getRole().name())) {
            return ResponseEntity.status(403).body("Only admin can delete notes");
        }

        noteService.deleteNote(id);
        return ResponseEntity.ok("Note deleted successfully");
    }
}
