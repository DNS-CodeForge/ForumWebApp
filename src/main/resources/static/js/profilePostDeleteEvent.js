document.addEventListener('DOMContentLoaded', function() {
    const deleteButtons = document.querySelectorAll('.delete-post');

    deleteButtons.forEach(button => {
        button.addEventListener('click', function() {
            const postId = this.getAttribute('data-post-id');
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
                            window.location.href = '/profile/info';
                        } else {
                            alert('Failed to delete the post.');
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert('An error occurred while deleting the post.');
                    });
            }
        });
    });
});
