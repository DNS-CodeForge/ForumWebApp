document.getElementById('posts').addEventListener('click', function(event) {
    if (event.target.classList.contains('upvote-icon')) {
        const icon = event.target;
        const currentSrc = icon.getAttribute('src');
        const newSrc = currentSrc.includes('up-arrow.png') ? '/images/up-arrow-selected.png' : '/images/up-arrow.png';
        icon.setAttribute('src', newSrc);
    }
});