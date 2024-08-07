
document.addEventListener('DOMContentLoaded', function() {
    const textarea = document.getElementById('comment-textarea');
    const cancelButton = document.getElementById('cancel-button');
    const submitButton = document.querySelector('button[type="submit"]');
    const buttonsDiv = document.getElementById('buttons');
    const commentingSection = document.querySelector('.commenting-section');

    const resizeTextarea = () => {
        textarea.style.height = `${textarea.scrollHeight}px`; 
        if(textarea.focus()) {
        commentingSection.style.height = `${textarea.scrollHeight + 45}px`;
        }
    };

    textarea.addEventListener('input', resizeTextarea);

  if (commentingSection) {
        commentingSection.addEventListener('click', () => {
            if (textarea) {
                textarea.focus();
            }
            if (buttonsDiv) {
                buttonsDiv.style.display = 'flex';
            }
        });
    }

    if (textarea) {
        textarea.addEventListener('click', () => {
            if (buttonsDiv) {
                buttonsDiv.style.display = 'flex';
            }
        });
    }

    if (cancelButton) {
        cancelButton.addEventListener('click', () => {
            if (textarea) {
                textarea.value = ''; 
                setTimeout(() => {
                    textarea.blur();
                }, 100);

                resizeTextarea();
            }
            if (buttonsDiv) {
                 setTimeout(() => {
                    buttonsDiv.style.display = 'none'; // Hide buttons after a slight delay
                }, 100);
            }

        });
    }

    resizeTextarea(); 
});

