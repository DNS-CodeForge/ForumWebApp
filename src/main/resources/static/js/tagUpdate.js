document.getElementById('addTagButton').addEventListener('click', function() {
    const tagInput = document.getElementById('tagInput');
    const tagContainer = document.getElementById('tagContainer');

    if (tagInput.value.trim() !== "") {

        const newTag = document.createElement('span');
        newTag.className = 'tag-bubble';
        newTag.innerText = tagInput.value.trim();


        const hiddenTagInput = document.createElement('input');
        hiddenTagInput.type = 'hidden';
        hiddenTagInput.name = 'tags';
        hiddenTagInput.value = tagInput.value.trim();


        tagContainer.appendChild(newTag);
        tagContainer.appendChild(hiddenTagInput);


        tagInput.value = '';


        newTag.addEventListener('click', function() {
            tagContainer.removeChild(newTag);
            tagContainer.removeChild(hiddenTagInput);
        });
    }
});
document.addEventListener('DOMContentLoaded', function() {
    const tags = document.querySelectorAll('#tagContainer .tag-bubble'); // Select all tags
    tags.forEach(function(tagElement) {
        const hiddenInput = tagElement.nextElementSibling;
        attachTagRemoveListener(tagElement, hiddenInput);
    });
});
function attachTagRemoveListener(tagElement, hiddenInput) {
    tagElement.addEventListener('click', function() {
        tagElement.remove();
        hiddenInput.remove();
    });
}
