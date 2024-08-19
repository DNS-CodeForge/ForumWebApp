document.addEventListener('DOMContentLoaded', function() {
    const postsContainer = document.getElementById('posts');

    function deletePost(postId) {
        if (confirm('Are you sure you want to delete this post?')) {
            const csrfToken = document.getElementById('csrfToken').value;
            const csrfHeader = document.getElementById('csrfHeader').value;

            fetch(`/posts/delete/${postId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                }
            })
                .then(response => {
                    if (response.status === 204) {
                        alert('Post deleted successfully!');
                        document.querySelector(`[data-post-id="${postId}"]`).closest('.post-item').remove();
                    } else {
                        alert('Failed to delete the post.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('An error occurred while deleting the post.');
                });
        }
    }

    // Event delegation for handling delete post clicks
    if (postsContainer) {
        postsContainer.addEventListener('click', function(event) {
            if (event.target.classList.contains('delete-post')) {
                const postId = event.target.getAttribute('data-post-id');
                deletePost(postId);
            }
        });
    }
});

