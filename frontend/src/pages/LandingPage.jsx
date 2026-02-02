import React from 'react';
import { useNavigate } from 'react-router-dom';

const LandingPage = () => {
  const navigate = useNavigate();

  const handleDemoLogin = async () => {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL || 'http://localhost:8080/api'}/demo/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      console.log('Demo login response status:', response.status);
      
      if (!response.ok) {
        console.error('Demo login failed with status:', response.status);
        alert(`Login failed: ${response.statusText}`);
        return;
      }

      const data = await response.json();
      console.log('Demo login response data:', data);
      localStorage.setItem('token', data.token);
      localStorage.setItem('user', JSON.stringify({
        id: data.userId,
        username: data.username,
        email: data.email
      }));
      localStorage.setItem('isDemo', 'true');
      
      console.log('Demo login successful, navigating...');
      window.location.href = '/';
    } catch (error) {
      console.error('Error during demo login:', error);
      alert('Can\'t connect to demo account. Please try again later.');
    }
  };

  return (
    <div className="landing-container">
      <div className="landing-content">
        {}
        <section className="hero">
  <h1 className="hero-title">DoneIt</h1>
  <p className="hero-subtitle">
    Turn vague goals into concrete action plans
  </p>
  <p className="hero-description">
    An intelligent todo list that uses AI to break down your tasks
    into clear, actionable steps.
  </p>
  
  <div className="cta-buttons">
    <button className="btn-demo" onClick={handleDemoLogin}>
      Try demo now
    </button>
    <button className="btn-signup" onClick={() => navigate('/register')}>
      Create an account
    </button>
  </div>
  
  <p className="demo-note">
    No sign-up required to try it out
  </p>
</section>

{}
<section className="use-case">
  <h2>The problem</h2>
  <p>
    "Learn Docker", "Prepare for my interview", "Organize my move"...
    <p></p>
    Important goals that stay vague and keep getting postponed.
  </p>
  
  <h2>The solution</h2>
  <p>
    DoneIt uses AI to turn these goals into concrete action lists.
    No more wondering where to start.
  </p>
  
  <div className="example">
    <div className="example-before">
      <h3>Before</h3>
      <p className="vague-task">"Learn Docker"</p>
      <span className="status-badge overwhelmed">Too vague, I procrastinate</span>
    </div>
    
    <div className="arrow">→</div>
    
    <div className="example-after">
      <h3>After (AI-generated)</h3>
      <ul className="concrete-steps">
        <li>✓ Follow the official Docker tutorial (2h)</li>
        <li>✓ Containerize my Spring Boot project</li>
        <li>✓ Create a docker-compose.yml</li>
        <li>✓ Optimize with multi-stage builds</li>
        <li>✓ Document everything in the README</li>
      </ul>
      <span className="status-badge actionable">Clear and actionable</span>
    </div>
  </div>
</section>

{}
<section className="tech-stack">
  <h2>Tech stack</h2>
  <p className="tech-intro">
    A full-stack application deployed to production, from code to deployment.
  </p>

          
          <div className="tech-grid">
            <div className="tech-card">
              <h3>Backend</h3>
              <ul>
                <li>Spring Boot 3.x (Java 21)</li>
                <li>Spring Security + JWT</li>
                <li>MongoDB Atlas</li>
                <li>OpenRouter AI API</li>
              </ul>
            </div>
            
            <div className="tech-card">
              <h3>Frontend</h3>
              <ul>
                <li>React 18</li>
                <li>React Router</li>
                <li>Custom CSS (neon theme)</li>
                <li>Responsive design</li>
              </ul>
            </div>
            
            <div className="tech-card">
              <h3>DevOps</h3>
              <ul>
                <li>Docker (multi-stage builds)</li>
                <li>GitHub Actions (CI)</li>
                <li>Render (déploiement)</li>
                <li>MongoDB Atlas (database)</li>
              </ul>
            </div>
          </div>
        </section>

      </div>
      
      <footer className="landing-footer">
        <p>Projet réalisé par Alexis Cnockaert</p>
        <div className="footer-links">
          <a href="https://github.com/AlexisCnockaert/react-springboot-todolist" target="_blank" rel="noopener noreferrer">
            GitHub
          </a>
          <a href="https://linkedin.com/in/alexis-cnockaert-ba228323a" target="_blank" rel="noopener noreferrer">
            LinkedIn
          </a>
        </div>
      </footer>
    </div>
  );
};

export default LandingPage;