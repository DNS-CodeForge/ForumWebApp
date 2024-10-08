document.getElementById('addTagButton').addEventListener('click', function() {
    const tagInput = document.getElementById('tagInput');
    const tagContainer = document.getElementById('tagContainer');

    if (tagInput.value.trim() !== "") {

        const newTag = document.createElement('span');
        newTag.className = 'tag-bubble';
        newTag.innerText = tagInput.value.trim();


        const hiddenTagInput = document.createElement('input');
        hiddenTagInput.type = 'hidden';
        hiddenTagInput.name = 'tagNames';
        hiddenTagInput.value = tagInput.value.trim();


        tagContainer.appendChild(newTag);
        tagContainer.appendChild(hiddenTagInput);


        tagInput.value = '';


        newTag.addEventListener('click', function() {
            tagContainer.removeChild(newTag);
            tagContainer.removeChild(hiddenTagInput);
        });
    }
})

