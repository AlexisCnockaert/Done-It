import React from 'react';

const ErrorMessage = ({ message, onDismiss }) => {
    if (!message) return null;

    return (
        <div className="error-message">
            {message}
            {onDismiss && (
                <button
                    className="error-dismiss"
                    onClick={onDismiss}
                    aria-label="Dismiss error"
                >
                    ×
                </button>
            )}
        </div>
    );
};

export default ErrorMessage;