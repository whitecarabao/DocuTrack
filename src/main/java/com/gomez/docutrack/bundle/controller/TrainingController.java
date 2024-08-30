package com.gomez.docutrack.bundle.controller;

import com.gomez.docutrack.bundle.entities.Question;
import com.gomez.docutrack.bundle.entities.TrainingScenario;
import com.gomez.docutrack.bundle.repository.TrainingScenarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class TrainingController {

    @Autowired
    private TrainingScenarioRepository scenarioRepository;

    @GetMapping("/training")
    public String showTraining(Model model) {
        // Fetch a random scenario or implement your own logic for scenario selection
        TrainingScenario scenario = scenarioRepository.findRandomScenario();
        model.addAttribute("scenario", scenario);
        return "training";
    }

    @PostMapping("/submitAnswers")
    public ResponseEntity<Integer> submitAnswers(@RequestParam Map<String, String> answers) {
        int score = evaluateAnswers(answers); // Implement your evaluation logic here
        return ResponseEntity.ok(score); // Return the score directly in the response
    }

    @GetMapping("/getNewScenario")
    public ResponseEntity<TrainingScenario> getNewScenario() {
        TrainingScenario newScenario = scenarioRepository.findRandomScenario();
        return ResponseEntity.ok(newScenario);
    }

    // Helper method to evaluate answers (replace with your actual logic)
    private int evaluateAnswers(Map<String, String> answers) {
        int score = 0;
        List<Question> questions = scenarioRepository.findQuestionsByScenarioId(1L); // Placeholder, get questions for the current scenario

        for (Question question : questions) {
            String selectedAnswer = answers.get("answer" + question.getId()); // Adjust based on your form structure
            if (selectedAnswer != null && selectedAnswer.equals(question.getCorrectAnswer())) {
                score++;
            }
        }

        int totalQuestions = questions.size();
        return (int) Math.round((score / (double) totalQuestions) * 100); // Calculate percentage
    }
}