function updateStyleBasedOnRoute(elementId) {
    const element = document.getElementById(elementId);
    const path = window.location.pathname;

    // Ensure the element exists
    if (!element) {
        console.error(`Element with id "${elementId}" not found.`);
        return;
    }
    console.log(elementId)

    if(element.className == "menu-item" || element.className == "navbar-item" || element.className == "profile-sections-field") {
        if (path === '/home' && elementId == "home-item") {
            element.classList.add('active');
        } else if(path === '/post'  && elementId == "create-button") {
            element.classList.add('active');
        } else if(path === '/register'  && elementId == "register-item") {
            element.classList.add('active');
        } else if(path === '/login'  && elementId == "login-item") {
            element.classList.add('active');
        } else if(path === '/about'  && elementId == "about-item") {
            element.classList.add('active');
        } else if (path === '/profile/info/liked'  && elementId == "profile-sections-liked") {
            element.classList.add('active');
        } else if (path === '/profile/info/posts'  && elementId == "profile-sections-posts") {
            element.classList.add('active');
        } else if (path === '/profile/info/comments'  && elementId == "profile-sections-comments") {
            element.classList.add('active');
        } else if (path === '/manage-users'  && elementId == "admin-item") {
            element.classList.add('active');
        } else if (path === '/search'  && elementId == "search-item") {
            element.classList.add('active');
        } else {
            element.classList.remove('active');
        }
    }
}


document.addEventListener('DOMContentLoaded', () => {
    updateStyleBasedOnRoute('profile-sections-comments');
    updateStyleBasedOnRoute('search-item');
    updateStyleBasedOnRoute('admin-item');
    updateStyleBasedOnRoute('profile-sections-posts');
    updateStyleBasedOnRoute('profile-sections-liked');
    updateStyleBasedOnRoute('home-item');
    updateStyleBasedOnRoute('register-item');
    updateStyleBasedOnRoute('login-item');
    updateStyleBasedOnRoute('create-button');
    updateStyleBasedOnRoute('about-item');
});

