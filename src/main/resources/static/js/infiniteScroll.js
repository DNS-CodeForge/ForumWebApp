document.addEventListener('DOMContentLoaded', function() {
    let page = 0; // Initial page index
    let loading = false; // Prevent multiple simultaneous fetches
    let endOfPosts = false; // Flag to determine if all posts are loaded
    let lastScrollTop = 0; // Track the last scroll position

    function loadMorePosts() {
        if (loading || endOfPosts) return; // Prevents loading if already in process or no more posts

        loading = true; // Set loading flag to true

        fetch(`/api/post?page=${page}&size=5`)
            .then(response => {
                if (response.status === 401) {
                    window.location.href = "/login"; // Redirect to login if unauthorized
                    throw new Error('Unauthorized');
                }
                return response.json();
            })
            .then(data => {
                const posts = data.data.posts;

                if (posts.length === 0) {
                    endOfPosts = true; // No more posts available
                    document.getElementById('loading').textContent = 'No more posts to load';
                    return;
                }

                posts.forEach(post => {
                    const postHtml = `
                        <div class="post-item">
                            <div class="post-title">
                                <a href="/posts/${post.id}">${post.title}</a>
                            </div>
                            <div class="post-description">
                                <p>${post.description.length > 120 ? post.description.substring(0, 120) + '...' : post.description}</p>
                            </div>
                            <div class="post-tags">
                                <p>${post.tags.map(tag => `<span class="post-tag">${tag.name}</span>`).join(' ')}</p>
                            </div>
                            <div class="post-actions">
                                <div class="post-action">
                                    <img src="/images/up-arrow.png" alt="Upvote" class="icon upvote-icon">
                                    <p>${post.likeCount}</p>
                                </div>
                                <div class="post-action">
                                    <img src="/images/comment.png" alt="Comment" class="icon comment-icon">
                                    <p>${post.commentCount}</p>
                                </div>
                            </div>
                        </div>
                    `;
                    document.getElementById('posts').insertAdjacentHTML('beforeend', postHtml);
                });

                if (page === 0) {
                    window.scrollTo(0, 0); // Scroll to top after initial load
                }

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

        // Trigger loadMorePosts only if scrolling down
        if (scrollPosition >= threshold && scrollTop > lastScrollTop) {
            loadMorePosts();
        }

        lastScrollTop = scrollTop; // Update the last scroll position
    }

    loadMorePosts();

    window.addEventListener('scroll', handleScroll);

    // Use event delegation for the upvote icons

});
