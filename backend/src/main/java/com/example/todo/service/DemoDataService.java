package com.example.todo.service;

import com.example.todo.dto.TodoRequest;
import com.example.todo.dto.TodoDTO;
import com.example.todo.model.TodoStep;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.TodoStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class DemoDataService {

    private final TodoService todoService;
    private final TodoStepRepository todoStepRepository;
    private final TodoRepository todoRepository;
    private final AuthService authService;

    @Autowired
    public DemoDataService(
            TodoService todoService,
            TodoStepRepository todoStepRepository,
            TodoRepository todoRepository,
            AuthService authService) {
        this.todoService = todoService;
        this.todoStepRepository = todoStepRepository;
        this.todoRepository = todoRepository;
        this.authService = authService;
    }

    public void resetDemoData(String demoEmail) {

        String userId = authService.getUserIdByEmail(demoEmail);
        todoRepository.findByUserId(userId).forEach(todo -> {
            todoStepRepository.deleteByTodoId(todo.getId());
        });
        todoRepository.deleteByUserId(userId);

        createDemoTasks(userId);
    }

    private void createDemoTasks(String userId) {
        // Task 1: Career-focused
        TodoDTO task1 = createDemoTodo(
            "Préparer mon entretien technique chez Google",
            "Je veux être prêt pour l'entretien dans 2 semaines",
            userId
        );
        createDemoSteps(task1.id(), Arrays.asList(
            "Réviser les algorithmes de tri et de recherche (Big O notation)",
            "Faire 5 exercices par jour sur LeetCode (niveau Medium)",
            "Préparer des exemples concrets de mes projets (STAR method)",
            "Simuler un entretien technique avec un ami développeur",
            "Préparer mes questions à poser au recruteur"
        ));

        // Task 2: Technical learning
        TodoDTO task2 = createDemoTodo(
            "Maîtriser Docker pour mon portfolio",
            "Tous mes projets doivent être containerisés",
            userId
        );
        createDemoSteps(task2.id(), Arrays.asList(
            "Suivre le tutoriel officiel Docker (2h)",
            "Containeriser mon projet Spring Boot existant",
            "Créer un docker-compose.yml avec frontend + backend + DB",
            "Optimiser les Dockerfiles avec multi-stage builds",
            "Écrire un README avec les commandes Docker essentielles"
        ));

        // Task 3: Personal development
        TodoDTO task3 = createDemoTodo(
            "Apprendre à jouer du piano",
            "Je veux pouvoir jouer mes morceaux préférés d'ici 6 mois",
            userId
        );
        createDemoSteps(task3.id(), Arrays.asList(
            "Trouver un professeur de piano dans ma ville ou en ligne",
            "Acheter un clavier MIDI 61 touches (budget 300€)",
            "Installer Flowkey ou Simply Piano pour pratiquer",
            "Pratiquer 20 minutes par jour les gammes de base",
            "Apprendre mon premier morceau simple (ex: La Lettre à Élise)"
        ));

        // Task 4: Personal project (with pre-generated steps)
        TodoDTO task4 = createDemoTodo(
            "Organiser mon déménagement à Paris",
            "Je déménage dans 1 mois pour mon nouveau job",
            userId
        );
        createDemoSteps(task4.id(), Arrays.asList(
            "Résilier bail actuel (préavis 1 mois)",
            "Chercher appartement sur Leboncoin/PAP (budget 800€ max)",
            "Réserver camion de déménagement chez Rent a Car",
            "Faire cartons progressivement (1 pièce par weekend)",
            "Changer adresse (CAF, impôts, banque, mutuelle)"
        ));

        // Task 5: Without steps (to showcase AI generation feature)
        createDemoTodo(
            "Refaire mon CV et mon portfolio",
            "Mon CV a 2 ans, je dois le moderniser avant de postuler",
            userId
        );
    }

    private TodoDTO createDemoTodo(String title, String description, String userId) {
        TodoRequest request = new TodoRequest(title, description);
        return todoService.createTodo(request, userId);
    }

    private void createDemoSteps(String todoId, List<String> stepDescriptions) {
        for (int i = 0; i < stepDescriptions.size(); i++) {
            TodoStep step = new TodoStep(todoId, stepDescriptions.get(i), i + 1);
            todoStepRepository.save(step);
        }
    }
}
