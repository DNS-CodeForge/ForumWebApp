document.getElementById('posts').addEventListener('click', function(event) {
    if (event.target.classList.contains('upvote-icon')) {
        const icon = event.target;
        const postId = icon.getAttribute('data-post-id');
        const currentSrc = icon.getAttribute('src');

        // Retrieve CSRF token and header name
        const csrfToken = document.getElementById('csrfToken').value;
        const csrfHeader = document.getElementById('csrfHeader').value;

        // Make AJAX request to like/unlike the post
        fetch(`/like/post/${postId}`, {
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken, // Include CSRF token in the headers
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest' // Indicate this is an AJAX request
            },
        })
        .then(response => {
            console.log(response.status);

            if (response.status === 401 || response.status === 403) {
                console.log('Redirecting to /login');
                window.location.href = '/login'; // Redirection to login page
                return;
            }

            if (!response.ok) {
                throw new Error('Failed to like the post');
            }
            return response.text();
        })
        .then(data => {
            // Update the UI based on the response
            const newSrc = currentSrc.includes('up-arrow.png') ? '/images/up-arrow-selected.png' : '/images/up-arrow.png';
            icon.setAttribute('src', newSrc);

            // Update the like count
            const likeCountElement = document.getElementById(`like-count-${postId}`);
            if (likeCountElement) {
                console.log(likeCountElement);
                let likeCount = parseInt(likeCountElement.textContent, 10);

                if (newSrc.includes('up-arrow-selected.png')) {
                    likeCountElement.innerText = likeCount + 1;
                } else {
                    likeCountElement.innerText = likeCount - 1;
                }
            } else {
                console.error(`No element found with id 'like-count-${postId}'`);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
});

