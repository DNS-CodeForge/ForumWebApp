document.addEventListener('DOMContentLoaded', () => {
    const profileImages = document.querySelectorAll('.profile-image');
    
    profileImages.forEach(profileImage => {
        const dropdownMenu = profileImage.nextElementSibling;

        profileImage.addEventListener('click', (event) => {
            event.stopPropagation();
            const isVisible = dropdownMenu.style.display === 'flex';
            dropdownMenu.style.display = isVisible ? 'none' : 'flex';
        });
    });

    document.addEventListener('click', (event) => {
        document.querySelectorAll('.dropdown-menu').forEach(dropdownMenu => {
            if (!event.target.closest('.profile-container')) {
                dropdownMenu.style.display = 'none';
            }
        });
    });
});

