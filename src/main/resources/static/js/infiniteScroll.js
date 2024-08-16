document.addEventListener('DOMContentLoaded', function() {
    let page = 0;
    let loading = false;
    let endOfPosts = false;
    let lastScrollTop = 0;

    // Check if we are on the profile page
    const isProfilePage = window.location.pathname.includes('/profile/info');
    const username = isProfilePage ? document.body.getAttribute('data-username') : null;
    const postsContainer = document.getElementById('posts');
    const loadingIndicator = document.getElementById('loading');

    function loadMorePosts() {
        if (loading || endOfPosts) return;

        loading = true;

        let url = `/posts/loadMore?page=${page}&size=5`;
        if (username) {
            url += `&username=${username}`;
        }

        fetch(url, {
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
            if (html.trim() === '') {
                endOfPosts = true; // No more posts available
                loadingIndicator.textContent = 'No more posts to load';
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

    if (postsContainer) {
        loadMorePosts();
        window.addEventListener('scroll', handleScroll);
    }
});

