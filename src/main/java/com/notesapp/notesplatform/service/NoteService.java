package com.notesapp.notesplatform.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.notesapp.notesplatform.entity.Folder;
import com.notesapp.notesplatform.entity.Note;
import com.notesapp.notesplatform.repository.FolderRepository;
import com.notesapp.notesplatform.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class NoteService {

    @Autowired private NoteRepository noteRepository;
    @Autowired private FolderRepository folderRepository;
    @Autowired private Cloudinary cloudinary;

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public List<Note> getNotesByFolder(Long folderId) {
        return noteRepository.findAllByFolderId(folderId);
    }

    public Note uploadNote(MultipartFile file, String title, Long folderId) throws IOException {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                    "resource_type", "auto", // use auto for preview
                    "folder", "notes_platform/" + folder.getName()
                )
        );

        String fileUrl = uploadResult.get("secure_url").toString();
        String publicId = uploadResult.get("public_id").toString();
        String resourceType = uploadResult.get("resource_type").toString(); // <- store this

        Note note = new Note();
        note.setTitle(title);
        note.setFileUrl(fileUrl);
        note.setPublicId(publicId);
        note.setResourceType(resourceType); // <- add this field in Note entity
        note.setFolder(folder);

        return noteRepository.save(note);

    }

    public void deleteNote(Long id) {
    	Note note = noteRepository.findById(id)
    	        .orElseThrow(() -> new RuntimeException("Note not found"));

    	try {
    	    cloudinary.uploader().destroy(note.getPublicId(),
    	            ObjectUtils.asMap("resource_type", note.getResourceType())); // use stored type
    	    System.out.println("Deleted from Cloudinary: " + note.getPublicId());
    	} catch (Exception e) {
    	    System.err.println("Cloudinary deletion failed: " + e.getMessage());
    	}

    	noteRepository.deleteById(id);

    }
 
}
