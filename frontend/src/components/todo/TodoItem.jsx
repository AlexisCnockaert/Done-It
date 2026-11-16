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
        <button
          type="button"
          className="gg-check-r"
          aria-label={todo.done ? `Mark ${todo.title} as not done` : `Mark ${todo.title} as done`}
          aria-pressed={todo.done}
          style={{ background: 'transparent', color: 'inherit', padding: 0 }}
          onClick={(e) => {
            e.stopPropagation();
            !loading && onToggle(todo.id);
          }}
        />
        <button
          type="button"
          className="gg-trash"
          aria-label={`Delete ${todo.title}`}
          style={{ background: 'transparent', color: 'inherit', padding: 0 }}
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
