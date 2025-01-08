document.getElementById('filters-toggle').addEventListener('click', function () {
    const arrow = this.querySelector('.arrow');
    const filterContent = this.nextElementSibling;

    arrow.classList.toggle('collapsed');
    filterContent.classList.toggle('collapsed');
});