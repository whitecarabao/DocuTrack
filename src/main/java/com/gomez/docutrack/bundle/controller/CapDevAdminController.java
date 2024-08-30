package com.gomez.docutrack.bundle.controller;

import com.gomez.docutrack.bundle.entities.Question;
import com.gomez.docutrack.bundle.entities.TrainingScenario;
import com.gomez.docutrack.bundle.repository.QuestionRepository;
import com.gomez.docutrack.bundle.repository.TrainingScenarioRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/capdev-admin")
public class CapDevAdminController {

    @Autowired
    private TrainingScenarioRepository scenarioRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping
    public String showAdminPanel(Model model) {
        model.addAttribute("scenario", new TrainingScenario()); 
        model.addAttribute("question", new Question()); 
        model.addAttribute("scenarios", scenarioRepository.findAll()); 
        return "capdev-admin";
    }

    @GetMapping("/scenarios/{id}")
@ResponseBody
public ResponseEntity<TrainingScenario> getScenario(@PathVariable Long id) {
    TrainingScenario selectedScenario = scenarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid scenario Id:" + id));
    return ResponseEntity.ok(selectedScenario);
}


    @PostMapping("/scenarios")
    public String createScenario(@ModelAttribute TrainingScenario scenario) {
        scenarioRepository.save(scenario);
        return "redirect:/capdev-admin";  // Redirect to refresh the scenarios list
    }

    @GetMapping("/scenarios/{id}/edit")
    public String editScenario(@PathVariable Long id, Model model) {
        TrainingScenario selectedScenario = scenarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid scenario Id:" + id));
        model.addAttribute("selectedScenario", selectedScenario);
        model.addAttribute("scenarios", scenarioRepository.findAll());
        return "capdev-admin";
    }

// @PostMapping("/scenarios/{id}")
// public ResponseEntity<String> updateScenario(@PathVariable Long id, @ModelAttribute TrainingScenario scenario) {
//     // Debug logging
//     System.out.println("Updating scenario with ID: " + id);
//     if (!id.equals(scenario.getId())) {
//         return ResponseEntity.badRequest().body("Scenario ID mismatch");
//     }
//     try {
//         scenarioRepository.save(scenario);
//         return ResponseEntity.ok("Scenario updated successfully");
//     } catch (Exception e) {
//         e.printStackTrace();  // Print the stack trace for debugging
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating scenario");
//     }
// }

// @PostMapping("/scenarios/{id}")
// public ResponseEntity<String> updateScenario(@PathVariable Long id, @RequestBody TrainingScenario scenario) {
//     if (!id.equals(scenario.getId())) {
//         return ResponseEntity.badRequest().body("Scenario ID mismatch");
//     }
//     scenarioRepository.save(scenario);
//     return ResponseEntity.ok("Scenario updated successfully");
// }
// @PostMapping("/scenarios/{id}")
// public ResponseEntity<String> updateScenario(@PathVariable Long id, @RequestBody TrainingScenario scenario) {
//     if (!id.equals(scenario.getId())) {
//         return ResponseEntity.badRequest().body("Scenario ID mismatch");
//     }

//     Optional<TrainingScenario> existingScenarioOpt = scenarioRepository.findById(id);
//     if (!existingScenarioOpt.isPresent()) {
//         return ResponseEntity.notFound().build();
//     }

//     TrainingScenario existingScenario = existingScenarioOpt.get();
//     existingScenario.setScript(scenario.getScript());

//     // Handle updating questions
//     List<Question> updatedQuestions = scenario.getQuestions();
//     Set<Question> updatedQuestionsSet = new HashSet<>(updatedQuestions);

//     // Remove questions that are no longer present
//     existingScenario.getQuestions().removeIf(q -> !updatedQuestionsSet.contains(q));
    
//     // Update or add new questions
//     for (Question updatedQuestion : updatedQuestions) {
//         if (updatedQuestion.getId() != null) {
//             // Update existing question
//             Optional<Question> existingQuestionOpt = questionRepository.findById(updatedQuestion.getId());
//             if (existingQuestionOpt.isPresent()) {
//                 Question existingQuestion = existingQuestionOpt.get();
//                 existingQuestion.setQuestionText(updatedQuestion.getQuestionText());
//                 existingQuestion.setCorrectAnswer(updatedQuestion.getCorrectAnswer());
//                 existingQuestion.setWrongAnswer1(updatedQuestion.getWrongAnswer1());
//                 existingQuestion.setWrongAnswer2(updatedQuestion.getWrongAnswer2());
//                 existingQuestion.setWrongAnswer3(updatedQuestion.getWrongAnswer3());
//                 questionRepository.save(existingQuestion);
//             }
//         } else {
//             // Add new question
//             updatedQuestion.setScenario(existingScenario);
//             questionRepository.save(updatedQuestion);
//         }
//     }

//     scenarioRepository.save(existingScenario);
//     return ResponseEntity.ok("Scenario updated successfully");
// }

@PostMapping("/scenarios/{id}")
public ResponseEntity<String> updateScenario(@PathVariable Long id, @RequestBody TrainingScenario scenario) {
    if (!id.equals(scenario.getId())) {
        return ResponseEntity.badRequest().body("Scenario ID mismatch");
    }

    Optional<TrainingScenario> existingScenarioOpt = scenarioRepository.findById(id);
    if (!existingScenarioOpt.isPresent()) {
        return ResponseEntity.notFound().build();
    }

    TrainingScenario existingScenario = existingScenarioOpt.get();
    existingScenario.setScript(scenario.getScript());

    // Handle updating questions
    System.out.println("Scenario Script: " + scenario.getScript().toString());
    System.out.println("Get Questions Results: " + scenario.getQuestions().toString());

    List<Question> updatedQuestions = scenario.getQuestions();
    if (updatedQuestions == null) {
        updatedQuestions = new ArrayList<>(); // Initialize to avoid NullPointerException
    }
    Set<Question> updatedQuestionsSet = new HashSet<>(updatedQuestions);

    // Remove questions that are no longer present
    existingScenario.getQuestions().removeIf(q -> !updatedQuestionsSet.contains(q));
    
    // Update or add new questions
    for (Question updatedQuestion : updatedQuestions) {
        if (updatedQuestion.getId() != null) {
            // Update existing question
            Optional<Question> existingQuestionOpt = questionRepository.findById(updatedQuestion.getId());
            if (existingQuestionOpt.isPresent()) {
                Question existingQuestion = existingQuestionOpt.get();
                existingQuestion.setQuestionText(updatedQuestion.getQuestionText());
                existingQuestion.setCorrectAnswer(updatedQuestion.getCorrectAnswer());
                existingQuestion.setWrongAnswer1(updatedQuestion.getWrongAnswer1());
                existingQuestion.setWrongAnswer2(updatedQuestion.getWrongAnswer2());
                existingQuestion.setWrongAnswer3(updatedQuestion.getWrongAnswer3());
                questionRepository.save(existingQuestion);
            }
        } else {
            // Add new question
            updatedQuestion.setScenario(existingScenario);
            questionRepository.save(updatedQuestion);
        }
    }

    scenarioRepository.save(existingScenario);
    return ResponseEntity.ok("Scenario updated successfully");
}




    @GetMapping("/scenarios/{id}/delete")
    public String deleteScenario(@PathVariable Long id) {
        questionRepository.deleteByScenarioId(id); // Assuming a method to delete questions by scenario ID
        scenarioRepository.deleteById(id);
        return "redirect:/capdev-admin";  // Redirect after deletion
    }

    @PostMapping("/questions")
    public String createQuestion(@ModelAttribute Question question) {
        questionRepository.save(question);
        return "redirect:/capdev-admin";  // Redirect to refresh the questions list
    }

    @GetMapping("/questions/{id}/edit")
    public String editQuestion(@PathVariable Long id, Model model) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question Id:" + id));
        model.addAttribute("question", question);
        model.addAttribute("scenarioId", question.getScenario().getId());
        model.addAttribute("scenarios", scenarioRepository.findAll());
        return "capdev-admin";
    }

    @PostMapping("/questions/{id}")
    public String updateQuestion(@PathVariable Long id, @ModelAttribute Question question) {
        if (!id.equals(question.getId())) {
            throw new IllegalArgumentException("Question ID mismatch");
        }
        questionRepository.save(question);
        return "redirect:/capdev-admin";  // Redirect after update
    }

    @GetMapping("/questions/{id}/delete")
    public String deleteQuestion(@PathVariable Long id) {
        questionRepository.deleteById(id);
        return "redirect:/capdev-admin";  // Redirect after deletion
    }
}
