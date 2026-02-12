import { useNavigate } from 'react-router-dom';

const HomeButton = () => {
  const navigate = useNavigate();

  return (
   <div className="home-wrapper" onClick={() => navigate('/')}>
  <div className="gg-home" />
</div>

  );
};

export default HomeButton;
