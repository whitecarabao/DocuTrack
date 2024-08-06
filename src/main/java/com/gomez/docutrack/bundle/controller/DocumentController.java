package com.gomez.docutrack.bundle.controller;

import com.gomez.docutrack.bundle.entities.Document;
import com.gomez.docutrack.bundle.entities.DocumentHistory;
import com.gomez.docutrack.bundle.entities.User;
import com.gomez.docutrack.bundle.repository.DocumentHistoryRepository;
import com.gomez.docutrack.bundle.repository.DocumentRepository;
import com.gomez.docutrack.bundle.repository.UserRepository;
import com.gomez.docutrack.bundle.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;
    private DocumentRepository documentRepository;
    private DocumentHistory documentHistory;
    private DocumentHistoryRepository documentHistoryRepository;
    // private UserRepository userRepository;

    @Autowired
    private UserRepository userRepository;
    
    private final Path rootLocation = Paths.get("C:\\Users\\PC\\Documents\\Dev\\bundle\\src\\main\\resources\\static\\uploads\\docs");

    @GetMapping
    public List<Document> getAllDocuments() {
        System.out.println("Getting all documents");
        return documentService.getAllDocuments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        System.out.println("Getting document with ID: " + id);
        Optional<Document> document = documentService.getDocumentById(id);
        if (document.isPresent()) {
            System.out.println("Document found: " + document.get());
            return ResponseEntity.ok(document.get());
        } else {
            System.out.println("Document not found with ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public RedirectView createDocument(@RequestParam("title") String title,
                                       @RequestParam("description") String description,
                                       @RequestParam("location") String location,
                                       @RequestParam("person") String person,
                                       @RequestParam("file") MultipartFile file) {
        System.out.println("Creating document with title: " + title);
        try {
            if (!Files.exists(rootLocation)) {
                System.out.println("Uploads directory does not exist. Creating...");
                Files.createDirectories(rootLocation);
            }

            String filename = file.getOriginalFilename();
            System.out.println("Original file name: " + filename);
            if (filename == null || filename.contains("..")) {
                System.out.println("Invalid file name: " + filename);
                return new RedirectView("/main?error=Invalid+file+name");
            }

            Path destinationFile = rootLocation.resolve(Paths.get(filename)).normalize().toAbsolutePath();
            System.out.println("Destination file path: " + destinationFile);

            // Ensure file path is within the intended directory
            if (!destinationFile.startsWith(rootLocation.toAbsolutePath())) {
                System.out.println("Invalid file path: " + destinationFile);
                return new RedirectView("/main?error=Invalid+file+path");
            }

            // Check if file already exists
            if (Files.exists(destinationFile)) {
                System.out.println("File already exists: " + filename);
                return new RedirectView("/main?error=File+already+exists");
            }

            file.transferTo(destinationFile);
            System.out.println("File transferred successfully");

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/static/uploads/docs/")
                    .path(filename)
                    .toUriString();
            System.out.println("File download URI: " + fileDownloadUri);

            Document document = new Document();
            document.setTitle(title);
            document.setDescription(description);
            document.setLocation(location);
            document.setPerson(person);
            document.setFilePath(fileDownloadUri);
            document.setCreatedAt(LocalDateTime.now());
            document.setUpdatedAt(LocalDateTime.now());

            Document savedDocument = documentService.createDocument(document);
            System.out.println("Document created: " + savedDocument);
            return new RedirectView("/main?success=Document+created+successfully");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error occurred while creating document: " + e.getMessage());
            return new RedirectView("/main?error=Error+occurred+while+creating+document");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred while creating document: " + e.getMessage());
            return new RedirectView("/main?error=Error+occurred+while+creating+document");
        }
    }

    // @PostMapping
    // public ResponseEntity<Document> createDocument(@RequestParam("title") String title,
    //                                                @RequestParam("description") String description,
    //                                                @RequestParam("location") String location,
    //                                                @RequestParam("person") String person,
    //                                                @RequestParam("file") MultipartFile file) {
    //     System.out.println("Creating document with title: " + title);
    //     try {
    //         if (!Files.exists(rootLocation)) {
    //             System.out.println("Uploads directory does not exist. Creating...");
    //             Files.createDirectories(rootLocation);
    //         }

    //         String filename = file.getOriginalFilename();
    //         System.out.println("Original file name: " + filename);
    //         if (filename == null || filename.contains("..")) {
    //             System.out.println("Invalid file name: " + filename);
    //             return ResponseEntity.badRequest().body(null);
    //         }

    //         Path destinationFile = rootLocation.resolve(Paths.get(filename)).normalize().toAbsolutePath();
    //         System.out.println("Destination file path: " + destinationFile);

    //         // Ensure file path is within the intended directory
    //         if (!destinationFile.startsWith(rootLocation.toAbsolutePath())) {
    //             System.out.println("Invalid file path: " + destinationFile);
    //             throw new IOException("Invalid file path");
    //         }

    //         // Check if file already exists
    //         if (Files.exists(destinationFile)) {
    //             System.out.println("File already exists: " + filename);
    //             return ResponseEntity.status(409).body(null);  // Conflict status
    //         }

    //         file.transferTo(destinationFile);
    //         System.out.println("File transferred successfully");

    //         String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
    //                 .path("/static/uploads/docs/")
    //                 .path(filename)
    //                 .toUriString();
    //         System.out.println("File download URI: " + fileDownloadUri);

    //         Document document = new Document();
    //         document.setTitle(title);
    //         document.setDescription(description);
    //         document.setLocation(location);
    //         document.setPerson(person);
    //         document.setFilePath(fileDownloadUri);
    //         document.setCreatedAt(LocalDateTime.now());
    //         document.setUpdatedAt(LocalDateTime.now());

    //         Document savedDocument = documentService.createDocument(document);
    //         System.out.println("Document created: " + savedDocument);
    //         // return new RedirectView("/main?success=Document+created+successfully");
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         System.out.println("Error occurred while creating document: " + e.getMessage());
    //         return ResponseEntity.status(500).body(null);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         System.out.println("Error occurred while creating document: " + e.getMessage());
    //         return ResponseEntity.status(500).body(null);
    //     }
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        System.out.println("Deleting document with ID: " + id);
        documentService.deleteDocument(id);
        System.out.println("Document deleted with ID: " + id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public RedirectView updateDocument(@PathVariable Long id,
                                       @RequestParam("title") String title,
                                       @RequestParam("description") String description,
                                       @RequestParam(value = "file", required = false) MultipartFile file) {
        System.out.println("Updating document with ID: " + id);
        try {
            Optional<Document> optionalDocument = documentService.getDocumentById(id);
            if (optionalDocument.isPresent()) {
                Document document = optionalDocument.get();
                document.setTitle(title);
                document.setDescription(description);
                document.setUpdatedAt(LocalDateTime.now());

                if (file != null && !file.isEmpty()) {
                    String filename = file.getOriginalFilename();
                    if (filename != null && !filename.contains("..")) {
                        Path destinationFile = rootLocation.resolve(Paths.get(filename)).normalize().toAbsolutePath();

                        if (destinationFile.startsWith(rootLocation.toAbsolutePath())) {
                            if (Files.exists(destinationFile)) {
                                Files.delete(destinationFile);
                            }
                            file.transferTo(destinationFile);

                            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                                    .path("/static/uploads/docs/")
                                    .path(filename)
                                    .toUriString();
                            document.setFilePath(fileDownloadUri);
                        }
                    }
                }

                documentService.updateDocument(document);
                logDocumentHistory(document.getId(), "Edit");
                return new RedirectView("/main?success=Document+updated+successfully");
            } else {
                return new RedirectView("/main?error=Document+not+found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new RedirectView("/main?error=Error+occurred+while+updating+document");
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("/main?error=Error+occurred+while+updating+document");
        }
    }

    // @PostMapping("/{id}/history")
    // public ResponseEntity<Void> createDocumentHistory(@PathVariable Long id, @RequestParam("action") String action) {
    //     System.out.println("Creating document history for document ID: " + id + " with action: " + action);
    //     documentService.createDocumentHistory(id, action);
    //     return ResponseEntity.ok().build();
    // }

    @PostMapping("/{id}/history")
public ResponseEntity<Void> createDocumentHistory(
        @PathVariable Long id, 
        @RequestParam("action") String action, 
        @RequestParam("userName") String userName, 
        @RequestParam("userPosition") String userPosition) {

            System.out.println("Querying User Repository using Username: " + userName);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            User loggedUser = userRepository.findByUsername(userDetails.getUsername());

    System.out.println("Creating document history for document ID: " + id + " with action: " + action);
    documentService.createDocumentHistory(id, action, loggedUser.getName(), loggedUser.getPosition(), loggedUser.getSectionName().toString());
    return ResponseEntity.ok().build();
}


private void logDocumentHistory(Long documentId, String action) {
        // Retrieve logged-in user details from SecurityContext
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        String userPosition;
        // UserRepository userRepository
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        System.out.println("Querying User Repository using Username: " + userDetails.getUsername());

        User loggedUser = userRepository.findByUsername(userDetails.getUsername());

        // Log the action
        documentService.createDocumentHistory(documentId, action, loggedUser.getName(), loggedUser.getPosition(), loggedUser.getSectionName().toString());
        System.out.println("Logged action: " + action + " by user: " + userName);
    }
    
}
