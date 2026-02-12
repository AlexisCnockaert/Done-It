import { useState, useEffect, useCallback } from 'react';
import todoService from '../services/todoService';

export const useTodos = () => {
    const [todos, setTodos] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchTodos = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);
            const data = await todoService.getAllTodos();
            setTodos(data);
        } catch (err) {
            setError('Failed to load todos. Please try again.');
            console.error('Error fetching todos:', err);
        } finally {
            setLoading(false);
        }
    }, []);

    const addTodo = async (title) => {
        try {
            setLoading(true);
            setError(null);
            await todoService.createTodo(title);
            await fetchTodos();
        } catch (err) {
            setError('Failed to add todo, please try again.');
            console.error('Error adding todo:', err);
        } finally {
            setLoading(false);
        }
    };

    const toggleTodo = async (id) => {
        try {
            setError(null);
            await todoService.toggleTodo(id);
            await fetchTodos();
        } catch (err) {
            setError('Failed to update todo, please try again.');
            console.error('Error updating todo:', err);
        }
    };

    const deleteTodo = async (id) => {
        try {
            setError(null);
            await todoService.deleteTodo(id);
            await fetchTodos();
        } catch (err) {
            setError('Failed to delete todo, please try again.');
            console.error('Error deleting todo:', err);
        }
    };

    useEffect(() => {
        fetchTodos();
    }, [fetchTodos]);

    return {
        todos,
        loading,
        error,
        addTodo,
        toggleTodo,
        deleteTodo,
        refreshTodos: fetchTodos,
    };
};