package com.gomez.docutrack.bundle.controller;

import com.gomez.docutrack.bundle.entities.Document;
import com.gomez.docutrack.bundle.entities.User;
import com.gomez.docutrack.bundle.repository.UserRepository;
import com.gomez.docutrack.bundle.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class WebController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) {
        addUserInfoToModel(model);
        model.addAttribute("title", "Home Page");
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        addUserInfoToModel(model);
        model.addAttribute("title", "Login Page");
        return "login";
    }


    @GetMapping("/header")
public ModelAndView getHeader(Model model) {
    addUserInfoToModel(model);
    return new ModelAndView("header");
}

    @GetMapping("/main")
    public String main(Model model) {
        addUserInfoToModel(model);
        List<Document> documents = documentService.getAllDocuments();
        model.addAttribute("documents", documents);
        return "main";
    }

    // @GetMapping("/document")
    // public String showDocumentDetails(@RequestParam("id") Long documentId, Model model) {
    //     Optional<Document> optionalDocument = documentService.getDocumentById(documentId);
        
    //     if (optionalDocument.isPresent()) {
    //         model.addAttribute("document", optionalDocument.get());
    //         return "documentDetails";
    //     } else {
    //         model.addAttribute("error", "Document not found.");
    //         return "error";
    //     }
    // }

    
    
    @GetMapping("/document/{id}")
    public String showDocument(@PathVariable("id") Long id, Model model) {
        Optional<Document> document = documentService.getDocumentById(id);
        model.addAttribute("document", document);
        logDocumentHistory(id, "Viewed");
        return "document_details"; // The Thymeleaf template name
    }

    private void addUserInfoToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user = userRepository.findByUsername(username);
            
            if (user != null) {
                model.addAttribute("user", user);
            } else {
                model.addAttribute("user", new User("Welcome, User", "Please log in.", null));
            }
        } else {
            model.addAttribute("user", new User("Welcome, User", "Please log in.", null));
        }
    }
    
    
    private void logDocumentHistory(Long documentId, String action) {
        // Retrieve logged-in user details from SecurityContext
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
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
