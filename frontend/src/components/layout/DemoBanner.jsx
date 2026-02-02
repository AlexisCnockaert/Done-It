import React from 'react';

const DemoBanner = () => {
  const isDemo = localStorage.getItem('isDemo') === 'true';
  
  if (!isDemo) return null;
  
  return (
    <div className="demo-banner">
      <span className="demo-icon">🧪</span>
      <span>Demo mode</span>
    </div>
  );
};

export default DemoBanner;