// /js/upvote.js
document.addEventListener('DOMContentLoaded', () => {
    const upvoteIcons = document.querySelectorAll('.upvote-icon');

    upvoteIcons.forEach(icon => {
        icon.addEventListener('click', () => {
            const currentSrc = icon.getAttribute('src');
            const newSrc = currentSrc.includes('up-arrow.png') ? '/images/up-arrow-selected.png' : '/images/up-arrow.png';
            icon.setAttribute('src', newSrc);

            // Optionally update the like count or other UI elements here
            // Example: icon.nextElementSibling.textContent = parseInt(icon.nextElementSibling.textContent) + 1;
        });
    });
});
