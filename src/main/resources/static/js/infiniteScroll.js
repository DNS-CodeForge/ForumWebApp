document.addEventListener('DOMContentLoaded', function() {
    let page = 0;
    let loading = false;
    let endOfPosts = false;
    let lastScrollTop = 0;

    function loadMorePosts() {
        if (loading || endOfPosts) return;

        loading = true;

        fetch(`/posts/loadMore?page=${page}&size=5`, {
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
            .then(response => {
                if (response.ok) {
                    return response.text();
                }
                if (response.status === 401) {
                    window.location.href = "/login";
                    throw new Error('Unauthorized');
                }
                throw new Error('Network response was not ok.');
            })
            .then(html => {
                const postsContainer = document.getElementById('posts');

                if (html.trim() === '') {
                    endOfPosts = true; // No more posts available
                    document.getElementById('loading').textContent = 'No more posts to load';
                    return;
                }

                postsContainer.insertAdjacentHTML('beforeend', html);

                page++;
                loading = false;
            })
            .catch(error => {
                console.error('Error loading posts:', error);
                loading = false;
            });
    }

    function handleScroll() {
        const scrollTop = window.scrollY;
        const scrollPosition = scrollTop + window.innerHeight;
        const threshold = document.documentElement.scrollHeight - 100;


        if (scrollPosition >= threshold && scrollTop > lastScrollTop) {
            loadMorePosts();
        }

        lastScrollTop = scrollTop;
    }

    loadMorePosts();

    window.addEventListener('scroll', handleScroll);
});
