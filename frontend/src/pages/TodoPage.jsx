import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import todoService from '../services/todoService';
import ErrorMessage from '../components/layout/ErrorMessage';
import AIPlanGenerator from '../components/ai/AIPlanGenerator';
import Navbar from '../components/layout/Navbar';


const TodoPage = () => {
    const { id } = useParams();
    const [todo, setTodo] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchTodo = useCallback(async () => {
    try {
        setLoading(true);
        setError(null);
        const data = await todoService.getTodoById(id);
        setTodo(data);
    } catch (err) {
        setError('Failed to load todo. It may have been deleted.');
        console.error('Error fetching todo:', err);
    } finally {
        setLoading(false);
    }
}, [id]);

useEffect(() => {
    fetchTodo();
}, [id, fetchTodo]);


    if (loading) {
        return (
              <>
                <Navbar />
            <div className="page page-with-navbar">
                <div className="todo-detail-container">
                    <p className="loading-text">Loading todo...</p>
                </div>
            </div>
             </>
        );
    }

    
    return (
          <>
                <Navbar />
        <div className="page page-with-navbar">
            <link rel="preconnect" href="https://fonts.googleapis.com" />
            <link rel="preconnect" href="https://fonts.gstatic.com/"  />
            <link href="https://fonts.googleapis.com/css2?family=BBH+Sans+Hegarty&display=swap" rel="stylesheet" />
            <div className="todo-detail-container">
                <h1 className="neon-title">{todo.title}</h1>
                <ErrorMessage message={error} />
                <div className="todo-detail-card">
                    <div className="todo-detail-info">
                        <p>Status:{todo.done ? ' Completed' : ' In Progress'}</p>
                    </div>

                    <AIPlanGenerator 
                        todoId={todo.id} 
                        todoTitle={todo.title}
                    />
                </div>
            </div>
        </div>
         </>
    );
};

export default TodoPage;