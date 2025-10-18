package com.notesapp.notesplatform.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.notesapp.notesplatform.entity.Folder;
import com.notesapp.notesplatform.entity.Note;
import com.notesapp.notesplatform.repository.FolderRepository;
import com.notesapp.notesplatform.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FolderService {

    @Autowired private FolderRepository folderRepository;
    @Autowired private NoteRepository noteRepository;
    @Autowired private Cloudinary cloudinary;

    public List<Folder> getAllFolders() {
        return folderRepository.findAll();
    }

    public Folder createFolder(Folder folder) {
        return folderRepository.save(folder);
    }

    public void deleteFolder(Long id) {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        List<Note> notes = noteRepository.findAllByFolderId(id);
        for (Note note : notes) {
            try {
                System.out.println("Deleting from Cloudinary: " + note.getPublicId());
                Map result = cloudinary.uploader().destroy(
                    note.getPublicId(),
                    ObjectUtils.asMap("resource_type", note.getResourceType()) // use image instead of raw
                );
                System.out.println("Delete result: " + result);
            } catch (Exception e) {
                System.err.println("Failed to delete file: " + e.getMessage());
            }
        }


        noteRepository.deleteAll(notes);
        folderRepository.delete(folder);
    }
}
