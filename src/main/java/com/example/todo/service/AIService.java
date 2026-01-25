package com.example.todo.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AIService {


    public List<String> generateSteps(String todoTitle, String userContext) {
        System.out.println("=== AI Service Called ===");
        System.out.println("Todo: " + todoTitle);
        System.out.println("Context: " + userContext);
        

        try {
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return generateMockSteps(todoTitle, userContext);
    }

    private List<String> generateMockSteps(String title, String context) {

        if (title.toLowerCase().contains("course")) {
            return Arrays.asList(
                "Faire une liste de courses détaillée",
                "Vérifier le frigo et les placards",
                "Préparer les sacs réutilisables",
                "Aller au supermarché",
                "Ranger les courses au retour"
            );
        } else if (title.toLowerCase().contains("voyage")) {
            return Arrays.asList(
                "Réserver les billets d'avion/train",
                "Réserver l'hébergement",
                "Faire une liste de bagages",
                "Vérifier les documents (passeport, visa)",
                "Préparer l'itinéraire",
                "Faire les valises"
            );
        } else if (title.toLowerCase().contains("projet")) {
            return Arrays.asList(
                "Définir les objectifs du projet",
                "Créer un planning détaillé",
                "Identifier les ressources nécessaires",
                "Diviser en sous-tâches",
                "Commencer par la première étape",
                "Suivre l'avancement régulièrement"
            );
        } else {

            return Arrays.asList(
                "Planifier et préparer " + title,
                "Rassembler les ressources nécessaires",
                "Commencer l'exécution pas à pas",
                "Vérifier la progression régulièrement",
                "Finaliser et valider le résultat"
            );
        }
    }
}