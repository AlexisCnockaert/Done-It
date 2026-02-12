import React from 'react';
import { useNavigate } from 'react-router-dom';

const TodoItem = ({ todo, onToggle, loading, onDelete }) => {
  const navigate = useNavigate();

  const goToTodo = () => {
    navigate(`/todos/${todo.id}`);
  };

  return (
    <li className={`todo-item ${todo.done ? 'done' : ''}`}>
      <div
        className="todo-title"
        onClick={goToTodo}
        style={{ cursor: 'pointer' }}
      >
        {todo.title}
      </div>

      <div className="todo-icons">
        <div
          className="gg-check-r"
          onClick={(e) => {
            e.stopPropagation();
            !loading && onToggle(todo.id);
          }}
        />
        <div
          className="gg-trash"
          onClick={(e) => {
            e.stopPropagation();
            !loading && onDelete(todo.id);
          }}
        />
      </div>
    </li>
  );
};

export default TodoItem;
