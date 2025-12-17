import { useTodos } from '../hooks/useTodos';
import { useParams, useNavigate } from 'react-router-dom';

const TodoPage = () => {
  const { todos, loading} = useTodos();
  const { id } = useParams();
  const navigate = useNavigate();

  if (loading) {
    return <p>Loading...</p>;
  }
  console.log (id);
  const todo = todos.find(t => t.id ===id);


  if (!todo) {
    return <p>Todo not found</p>;
  }

  return (
    <div className="page">
      <div className="App">
        <div className='todos'>
        <h1>{todo.title}</h1>
        <p>Status: {todo.done ? 'Done' : 'Pending'}</p>
      </div>
    </div>
    </div>
  );
};

export default TodoPage;
