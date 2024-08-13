function updateStyleBasedOnRoute(elementId) {
    const element = document.getElementById(elementId);
    const path = window.location.pathname;

    // Ensure the element exists
    if (!element) {
        console.error(`Element with id "${elementId}" not found.`);
        return;
    }
    if(element.className == "menu-item" || element.className == "navbar-item") {
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
        } else {
            element.classList.remove('active');
        }
    }
}

updateStyleBasedOnRoute('home-item');
updateStyleBasedOnRoute('register-item');
updateStyleBasedOnRoute('login-item');
updateStyleBasedOnRoute('create-button');
updateStyleBasedOnRoute('about-item');

