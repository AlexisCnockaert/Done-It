import React, { useState } from 'react';
import { useAIPlan } from '../../hooks/useAIPlan';
import AIPromptInput from './AIPromptInput';
import TodoStepsList from './TodoStepsList';
import ErrorMessage from '../ErrorMessage';


const AIPlanGenerator = ({ todoId, todoTitle }) => {
    const [showPrompt, setShowPrompt] = useState(false);
    const {
        steps,
        loading,
        generating,
        error,
        hasSteps,
        generatePlan,
        toggleStep,
        deleteStep,
        regeneratePlan
    } = useAIPlan(todoId);

    const handleGenerate = async (context) => {
        try {
            await generatePlan(todoTitle, context);
            setShowPrompt(false);
        } catch (err) {
            // Error is handled by the hook
        }
    };

    const handleRegenerate = () => {
        setShowPrompt(true);
    };

    return (
        <div className="ai-plan-section">
            <div className="ai-plan-header">
                <h3>🤖 AI Generated Plan</h3>
                {hasSteps && (
                    <button
                        className="ai-button-regenerate"
                        onClick={handleRegenerate}
                        disabled={generating}
                    >
                        🔄 Regenerate
                    </button>
                )}
            </div>

            {error && <ErrorMessage message={error} />}

            {!hasSteps && !showPrompt && (
                <div className="neon-button no-hover">
                    <p>Let AI help you break down this task into actionable steps!</p>
                    <button
                        className="ai-button-primary"
                        onClick={() => setShowPrompt(true)}
                        disabled={generating}
                    >
                     <p> Generate AI Plan</p>  
                    </button>
                </div>
            )}

            {showPrompt && (
                <AIPromptInput
                    onGenerate={handleGenerate}
                    onCancel={() => setShowPrompt(false)}
                    generating={generating}
                />
            )}

            {hasSteps && !showPrompt && (
                <>
                    <div className="steps-header">
                        <h4>Steps ({steps.filter(s => s.completed).length}/{steps.length} completed)</h4>
                    </div>
                    <TodoStepsList
                        steps={steps}
                        onToggle={toggleStep}
                        onDelete={deleteStep}
                        loading={loading}
                    />
                </>
            )}

            {loading && !generating && (
                <div className="steps-loading">
                    <span className="spinner"></span>
                    <p>Loading steps...</p>
                </div>
            )}
        </div>
    );
};

export default AIPlanGenerator;