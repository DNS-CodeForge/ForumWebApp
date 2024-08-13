let currentExp = parseInt(document.getElementById('current-exp').textContent, 10);
let expToNextLevel = parseInt(document.getElementById('exp-to-next-level').textContent, 10);

if (expToNextLevel === 0) {
    expToNextLevel = 1;
}

let progressPercentage = (currentExp / expToNextLevel) * 100;
document.querySelector('.progress-bar').style.width = progressPercentage + '%';
document.getElementById('progress-text').innerText = progressPercentage.toFixed(1) + '% Complete';
