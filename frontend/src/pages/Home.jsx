import React, { useState } from 'react';
import { useTodos } from '../hooks/useTodos';
import TodoForm from '../components/todo/TodoForm';
import TodoList from '../components/todo/TodoList';
import ErrorMessage from '../components/layout/ErrorMessage';
import Navbar from '../components/layout/Navbar';


const Home = () => {
    const { todos, loading, error, addTodo, toggleTodo, deleteTodo } = useTodos();
    const [showHelp, setShowHelp] = useState(false); // state to toggle help text
    const toggleHelp = () => setShowHelp(prev => !prev);
    return (
        <>
        <Navbar />
        <div className="page page with-navbar">
 
            <link rel="preconnect" href="https://fonts.googleapis.com" />
            <link rel="preconnect" href="https://fonts.gstatic.com/" />
            <link href="https://fonts.googleapis.com/css2?family=BBH+Sans+Hegarty&display=swap" rel="stylesheet" />
            <h1 className="neon-title">Done It</h1>
            <p className="intro-desc">Be productive, efficient, organized</p>
            <p className="intro-text">
                Manage your tasks with a <span className="glow">touch of style </span>
                - all in a futuristic and optimized way 

            </p>
            
            <div style={{ alignSelf: 'flex-start', marginBottom: '60px' }}>
                <button onClick={toggleHelp} className="help-button">
                    How to use it?
                </button>
            </div>
            {showHelp && (
                <div className="help-box">
                    <h3>How to use "Done It"</h3>
                    <ul>
                        <li>Use the form above to add a new todo.<br />
                            Make sure to give it a clear name so AI can interpret it correctly and accurately.
                        </li>
                        <li>Click a todo to open it in your todo tab, where AI will generate step-by-step guidance to help you stay productive.</li>
                        <li>You can also mark a todo as done by clicking the little checkbox.</li>
                        <li>Stay organized and track your tasks efficiently!</li>
                      
                    </ul>
                </div>
            )}
            <div className="App">
                <p className="todos">Todo List</p>
                <ErrorMessage message={error} />

                <TodoForm onAdd={addTodo} loading={loading} />

                <TodoList
                    todos={todos}
                    onToggle={toggleTodo}
                    loading={loading}
                    onDelete={deleteTodo}
                />
            </div>
        </div>
         </>
    );
}

export default Home;