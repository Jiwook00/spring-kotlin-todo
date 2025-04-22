document.addEventListener('DOMContentLoaded', function() {
    // Flash message 자동 닫기
    const alert = document.querySelector('.alert');
    if (alert) {
        setTimeout(() => {
            alert.style.opacity = '0';
            setTimeout(() => {
                alert.style.display = 'none';
            }, 300);
        }, 3000);
    }

    // 새 Todo 폼 토글
    const addTodoForm = document.querySelector('.todo-form');
    const addTodoBtn = document.querySelector('.add-todo-toggle');

    if (addTodoBtn && addTodoForm) {
        addTodoBtn.addEventListener('click', function() {
            addTodoForm.classList.toggle('expanded');
            this.textContent = addTodoForm.classList.contains('expanded') ? 'Cancel' : 'Add New Todo';
        });
    }

    // 우선순위에 따른 색상 변경
    const prioritySelects = document.querySelectorAll('select[name="priority"]');

    prioritySelects.forEach(select => {
        updateSelectColor(select);

        select.addEventListener('change', function() {
            updateSelectColor(this);
        });
    });

    function updateSelectColor(select) {
        const value = select.value.toLowerCase();
        select.classList.remove('priority-high', 'priority-medium', 'priority-low');

        if (value) {
            select.classList.add(`priority-${value}`);
        }
    }
});