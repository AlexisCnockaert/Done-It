import { useState, useEffect, useCallback } from 'react';
import aiService from '../services/aiService';

export const useAIPlan = (todoId) => {
    const [steps, setSteps] = useState([]);
    const [loading, setLoading] = useState(false);
    const [generating, setGenerating] = useState(false);
    const [error, setError] = useState(null);
    const [hasSteps, setHasSteps] = useState(false);

   const fetchSteps = useCallback(async () => {
    try {
        setLoading(true);
        setError(null);
        const data = await aiService.getTodoSteps(todoId);
        setSteps(data);
        setHasSteps(data.length > 0);
    } catch (err) {
        console.error('Error fetching steps:', err);
        setError('Failed to load steps.');
        setSteps([]);
        setHasSteps(false);
    } finally {
        setLoading(false);
    }
}, [todoId]);

useEffect(() => {
    if (todoId) {
        fetchSteps();
    }
}, [todoId, fetchSteps]);


    const generatePlan = async (todoTitle, userContext) => {
        try {
            setGenerating(true);
            setError(null);
            
            const response = await aiService.generatePlan(todoId, todoTitle, userContext);
            setSteps(response.steps);
            setHasSteps(response.steps.length > 0);
            
            return response;
        } catch (err) {
            console.error('Error generating plan:', err);
            
            if (err.response?.data?.message) {
                setError(err.response.data.message);
            } else {
                setError('Failed to generate plan. Please try again.');
            }
            
            throw err;
        } finally {
            setGenerating(false);
        }
    };

    const toggleStep = async (stepId) => {
        try {
            setError(null);
            const updatedStep = await aiService.toggleStep(stepId);
            
            // Mettre à jour la step dans l'état local
            setSteps(prevSteps =>
                prevSteps.map(step =>
                    step.id === stepId ? updatedStep : step
                )
            );
        } catch (err) {
            console.error('Error toggling step:', err);
            setError('Failed to update step.');
        }
    };

    const deleteStep = async (stepId) => {
        try {
            setError(null);
            await aiService.deleteStep(stepId);
            
            // Retirer la step de l'état local
            setSteps(prevSteps => prevSteps.filter(step => step.id !== stepId));
            
            if (steps.length === 1) {
                setHasSteps(false);
            }
        } catch (err) {
            console.error('Error deleting step:', err);
            setError('Failed to delete step.');
        }
    };

    const regeneratePlan = async (todoTitle, userContext) => {
        // Confirmer avant de régénérer
        if (steps.length > 0) {
            const confirmed = window.confirm(
                'This will replace your current plan. Are you sure?'
            );
            if (!confirmed) return;
        }
        
        return generatePlan(todoTitle, userContext);
    };

    return {
        steps,
        loading,
        generating,
        error,
        hasSteps,
        generatePlan,
        toggleStep,
        deleteStep,
        regeneratePlan,
        refreshSteps: fetchSteps
    };
};