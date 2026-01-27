import React, { useState } from 'react';

const TodoForm = ({ onAdd, loading }) => {
    const [newTodo, setNewTodo] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        await onAdd(newTodo);
        setNewTodo('');
    };

    const handleKeyPress = (e) => {
        if (e.key === 'Enter' && !loading) {
            handleSubmit(e);
        }
    };

    return (
        <div className="input-container">
            <input
                type="text"
                value={newTodo}
                onChange={(e) => setNewTodo(e.target.value)}
                onKeyPress={handleKeyPress}
                placeholder="New todo"
                className="neon-input"
                disabled={loading}
            />
            <button
                onClick={handleSubmit}
                className="neon-button"
                disabled={loading}
            >
                {loading ? 'Adding...' : 'Add Todo'}
            </button>
        </div>
    );
};

export default TodoForm;