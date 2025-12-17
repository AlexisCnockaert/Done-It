import { useNavigate } from 'react-router-dom';

const HomeButton = () => {
  const navigate = useNavigate();

  return (
    <div
      className="gg-home"
      onClick={() => navigate('/')}
    />
  );
};

export default HomeButton;
