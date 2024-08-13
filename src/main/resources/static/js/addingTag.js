document.getElementById('addTagButton').addEventListener('click', function() {
    const tagInput = document.getElementById('tagInput');
    const tagContainer = document.getElementById('tagContainer');

    if (tagInput.value.trim() !== "") {
        // Create a new tag bubble
        const newTag = document.createElement('span');
        newTag.className = 'tag-bubble';
        newTag.innerText = tagInput.value.trim();

        // Create a hidden input to store the tag
        const hiddenTagInput = document.createElement('input');
        hiddenTagInput.type = 'hidden';
        hiddenTagInput.name = 'tagNames';
        hiddenTagInput.value = tagInput.value.trim();

        // Append the new tag and hidden input to the tag container
        tagContainer.appendChild(newTag);
        tagContainer.appendChild(hiddenTagInput);

        // Clear the input field
        tagInput.value = '';

        // Add a click event to remove the tag and its hidden input when clicked
        newTag.addEventListener('click', function() {
            tagContainer.removeChild(newTag);
            tagContainer.removeChild(hiddenTagInput);
        });
    }
});

