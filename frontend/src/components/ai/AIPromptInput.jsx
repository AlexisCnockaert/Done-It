import React, { useState } from 'react';


const AIPromptInput = ({ onGenerate, onCancel, generating }) => {
    const [context, setContext] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        onGenerate(context);
    };

    return (
        <div className="neon-button no-hover">
            <div className="ai-prompt-header">
                <h3>Generate AI Plan</h3>
            </div>
            
            <p className="ai-prompt-description">
                Add context to help the AI generate better steps for your task.
            </p>

            <form onSubmit={handleSubmit}>
                <div className="ai-input-group">
                    <textarea
                        id="context"
                        value={context}
                        onChange={(e) => setContext(e.target.value)}
                        placeholder="E.g., I want to do organic shopping on a budget..."
                        maxLength={500}
                        rows={4}
                        disabled={generating}
                        className="ai-textarea"
                    />
                    <span className="char-count">{context.length}/500</span>
                </div>

                <div className="ai-prompt-actions">
                    <button
                        type="button"
                        onClick={onCancel}
                        className="ai-button ai-button-cancel"
                        disabled={generating}
                    >
                        Cancel
                    </button>
                    
                    <button
                        type="submit"
                        className="ai-button ai-button-generate"
                        disabled={generating}
                    >
                        {generating ? (
                            <>
                                <span className="spinner"></span>
                                Generating...
                            </>
                        ) : (
                            <p>
                                Generate Plan
                            </p>
                        )}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default AIPromptInput;