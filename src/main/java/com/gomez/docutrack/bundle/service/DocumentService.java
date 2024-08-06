package com.gomez.docutrack.bundle.service;

import com.gomez.docutrack.bundle.entities.Document;
import com.gomez.docutrack.bundle.entities.DocumentHistory;
import com.gomez.docutrack.bundle.entities.User;
import com.gomez.docutrack.bundle.repository.DocumentHistoryRepository;
import com.gomez.docutrack.bundle.repository.DocumentRepository;
import com.gomez.docutrack.bundle.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;

import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentHistoryRepository documentHistoryRepository;

    @Autowired
    private UserRepository userRepository;


    public Optional<Document> getDocumentByIdWithHistory(Long id) {
        return documentRepository.findByIdWithHistory(id);
    }
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    public Document createDocument(Document document) {
        return documentRepository.save(document);
    }

    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }

    public void updateDocument(Document document) {
    documentRepository.save(document);
}



// public void createDocumentHistory(Long documentId, String action) {
//     // Document document = documentRepository.findById(documentId).orElseThrow(() -> new RuntimeException("Document not found"));
//     // DocumentHistory history = new DocumentHistory();
//     // history.setDocument(document);
//     // history.setAction(action);
//     // history.setDate(LocalDateTime.now());
//     // history.setLocation(document.getLocation());
//     // history.setPerson(document.getPerson());
//     // documentHistoryRepository.save(history);

    
// }

public void createDocumentHistory(Long documentId, String action, String userName, String userPosition, String userLocation) {
    // Fetch the document from the repository
    Optional<Document> documentOptional = documentRepository.findById(documentId);
    
    if (documentOptional.isPresent()) {
        Document document = documentOptional.get();
        
        System.out.println("Querying at createDocHistory for Username: " + userName);
        User contextUser = userRepository.findByName(userName);        // Create a new DocumentHistory entry
        DocumentHistory history = new DocumentHistory();
        history.setDocument(document);
        history.setAction(action);
        history.setUserName(userName); // Add userName
        history.setUserPosition(userPosition); // Add userPosition
        history.setDate(LocalDateTime.now());
        history.setLocation(userLocation);
        history.setPerson(contextUser.getName());

        
        // Save the DocumentHistory entry
        documentHistoryRepository.save(history);
    } else {
        throw new EntityNotFoundException("Document not found with ID: " + documentId);
    }
}

}
