document.addEventListener('DOMContentLoaded', function() {
    let page = 0;
    let loading = false;
    let endOfComments = false;
    let lastScrollTop = 0;

    const commentsContainer = document.getElementById('comments');
    const loadingIndicator = document.getElementById('loading-comments');
    const isCommentPage = window.location.pathname.includes('/profile/info/comments');

    function loadMoreComments() {
        if (!isCommentPage) return;
        if (loading || endOfComments) return;

        loading = true;
        let url = `/profile/info/comments/loadMore?page=${page}&size=5`;

        fetch(url, {
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
        .then(response => {
            if (response.ok) {
                return response.text();
            }
            throw new Error('Network response was not ok.');
        })
        .then(html => {
            if (html.trim() === '') {
                endOfComments = true;
                loadingIndicator.textContent = 'No more comments to load';
                return;
            }

            commentsContainer.insertAdjacentHTML('beforeend', html);
            page++;
            loading = false;
        })
        .catch(error => {
            console.error('Error loading comments:', error);
            loading = false;
        });
    }

    function handleScroll() {
        const scrollTop = window.scrollY;
        const scrollPosition = scrollTop + window.innerHeight;
        const threshold = document.documentElement.scrollHeight - 100;

        if (scrollPosition >= threshold && scrollTop > lastScrollTop) {
            loadMoreComments();
        }

        lastScrollTop = scrollTop;
    }

    if (commentsContainer) {
        loadMoreComments();
        window.addEventListener('scroll', handleScroll);
    }
});
