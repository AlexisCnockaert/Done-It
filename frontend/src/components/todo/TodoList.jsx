import React from "react";
import TodoItem from './TodoItem';

const TodoList = ({ todos, onToggle, loading, onDelete }) => {
    if (loading && todos.length === 0) {
        return <p className="loading-text">Loading todos...</p>;
    }

    return (
        <ul className="todo-list">
            {todos.map((todo) => (
                <TodoItem
                    key={todo.id}
                    todo={todo}
                    onToggle={onToggle}
                    loading={loading}
                    onDelete={onDelete}
                />
            ))}
        </ul>

    );
};

export default TodoList;