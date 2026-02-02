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
        TodoDTO task1 = createDemoTodo(
            "Préparer mon entretien technique chez Google",
            userId
        );
        createDemoSteps(task1.id(), Arrays.asList(
            "Réviser les algorithmes de tri et de recherche (Big O notation)",
            "Faire 5 exercices par jour sur LeetCode (niveau Medium)",
            "Préparer des exemples concrets de mes projets (STAR method)",
            "Simuler un entretien technique avec un ami développeur",
            "Préparer mes questions à poser au recruteur"
        ));

        TodoDTO task2 = createDemoTodo(
            "Maîtriser Docker pour mon portfolio",
            userId
        );
        createDemoSteps(task2.id(), Arrays.asList(
            "Suivre le tutoriel officiel Docker (2h)",
            "Containeriser mon projet Spring Boot existant",
            "Créer un docker-compose.yml avec frontend + backend + DB",
            "Optimiser les Dockerfiles avec multi-stage builds",
            "Écrire un README avec les commandes Docker essentielles"
        ));

        TodoDTO task3 = createDemoTodo(
            "Apprendre à jouer du piano",
            userId
        );
        createDemoSteps(task3.id(), Arrays.asList(
            "Trouver un professeur de piano dans ma ville ou en ligne",
            "Acheter un clavier MIDI 61 touches (budget 300€)",
            "Installer Flowkey ou Simply Piano pour pratiquer",
            "Pratiquer 20 minutes par jour les gammes de base",
            "Apprendre mon premier morceau simple (ex: La Lettre à Élise)"
        ));

        TodoDTO task4 = createDemoTodo(
            "Organiser mon déménagement à Paris",
            userId
        );
        createDemoTodo(
            "Refaire mon CV et mon portfolio",
            userId
        );
    }

    private TodoDTO createDemoTodo(String title, String userId) {
        TodoRequest request = new TodoRequest(title);
        return todoService.createTodo(request, userId);
    }

    private void createDemoSteps(String todoId, List<String> stepDescriptions) {
        for (int i = 0; i < stepDescriptions.size(); i++) {
            TodoStep step = new TodoStep(todoId, stepDescriptions.get(i), i + 1);
            todoStepRepository.save(step);
        }
    }
}
