import React from 'react';


const TodoStepsList = ({ steps, onToggle, onDelete, loading }) => {
    if (steps.length === 0) {
        return (
            <div className="steps-empty">
                <p>No steps yet. Generate an AI plan to get started!</p>
            </div>
        );
    }

    return (
        <div className="steps-list">
            {steps.map((step) => (
                <div
                    key={step.id}
                    className={`step-item ${step.completed ? 'completed' : ''}`}
                >
                    <div className="step-content">
                        <div
                            className="step-checkbox"
                            onClick={() => !loading && onToggle(step.id)}
                        >
                            {step.completed ? (
                                <span className="checkbox-checked">✓</span>
                            ) : (
                                <span className="checkbox-unchecked"></span>
                            )}
                        </div>
                        
                        <div className="step-text">
                            <span className="step-number">{step.stepOrder}.</span>
                            <span className="step-description">{step.description}</span>
                        </div>
                    </div>

                    <button
                        className="step-delete"
                        onClick={() => !loading && onDelete(step.id)}
                        disabled={loading}
                        title="Delete step"
                    >
                        ×
                    </button>
                </div>
            ))}
        </div>
    );
};

export default TodoStepsList;