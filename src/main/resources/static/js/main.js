const URL = "/api/users";

document.addEventListener("DOMContentLoaded", () => {
    fillHeader();
    fillTable();
    loadRoles();

    // Слушатель для создания
    const addForm = document.getElementById('add-user-form');
    if (addForm) {
        addForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            await createUser();
        });
    }

    // Слушатель для редактирования
    const editForm = document.getElementById('edit-user-form');
    if (editForm) {
        editForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            await updateUser();
        });
    }

    // Слушатель для удаления
    const deleteForm = document.getElementById('delete-user-form');
    if (deleteForm) {
        deleteForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            await deleteUser();
        });
    }
});

// --- ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ ---

async function fillHeader() {
    const response = await fetch(URL + "/current");
    const user = await response.json();
    document.getElementById('header-email').textContent = user.email;
    document.getElementById('header-roles').textContent = user.roles.map(r => r.name.replace('ROLE_', '')).join(' ');
}

async function fillTable() {
    const response = await fetch(URL);
    const users = await response.json();
    const tableBody = document.getElementById('all-users-table');
    tableBody.innerHTML = '';

    users.forEach(user => {
        const roles = user.roles.map(r => r.name.replace('ROLE_', '')).join(' ');
        const row = `
            <tr>
                <td>${user.id}</td>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${roles}</td>
                <td>
                    <button class="btn btn-info btn-sm text-white" onclick="showEditModal(${user.id})">Edit</button>
                </td>
                <td>
                    <button class="btn btn-danger btn-sm" onclick="showDeleteModal(${user.id})">Delete</button>
                </td>
            </tr>`;
        tableBody.insertAdjacentHTML('beforeend', row);
    });
}

async function loadRoles() {
    const response = await fetch(URL + "/roles");
    const roles = await response.json();
    const selectors = ['edit-roles', 'add-roles', 'delete-roles'];

    selectors.forEach(id => {
        const select = document.getElementById(id);
        if (select) {
            select.innerHTML = '';
            roles.forEach(role => {
                let option = new Option(role.name.replace('ROLE_', ''), role.id);
                select.add(option);
            });
        }
    });
}

// --- МОДАЛЬНЫЕ ОКНА ---

async function showEditModal(id) {
    const response = await fetch(`${URL}/${id}`);
    const user = await response.json();

    document.getElementById('edit-id').value = user.id;
    document.getElementById('edit-firstname').value = user.firstName;
    document.getElementById('edit-lastname').value = user.lastName;
    document.getElementById('edit-age').value = user.age;
    document.getElementById('edit-email').value = user.email;
    document.getElementById('edit-password').value = '';

    const select = document.getElementById('edit-roles');
    Array.from(select.options).forEach(opt => {
        opt.selected = user.roles.some(r => r.id == opt.value);
    });

    const modal = new bootstrap.Modal(document.getElementById('editModal'));
    modal.show();
}

async function showDeleteModal(id) {
    const response = await fetch(`${URL}/${id}`);
    const user = await response.json();

    document.getElementById('delete-id').value = user.id;
    document.getElementById('delete-firstname').value = user.firstName;
    document.getElementById('delete-lastname').value = user.lastName;
    document.getElementById('delete-age').value = user.age;
    document.getElementById('delete-email').value = user.email;

    const select = document.getElementById('delete-roles');
    Array.from(select.options).forEach(opt => {
        opt.selected = user.roles.some(r => r.id == opt.value);
    });

    const modal = new bootstrap.Modal(document.getElementById('deleteModal'));
    modal.show();
}

// --- CRUD ОПЕРАЦИИ ---

async function createUser() {
    const newUser = {
        firstName: document.getElementById('add-firstname').value,
        lastName: document.getElementById('add-lastname').value,
        age: document.getElementById('add-age').value,
        email: document.getElementById('add-email').value,
        password: document.getElementById('add-password').value,
        roles: getSelectedRoles('add-roles')
    };

    await fetch(URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newUser)
    });

    document.getElementById('add-user-form').reset();
    fillTable();
    // Переключение на вкладку таблицы
    const tableTab = document.querySelector('#adminTabs button[data-bs-target="#usersTablePane"]');
    bootstrap.Tab.getInstance(tableTab).show();
}

async function updateUser() {
    const updatedUser = {
        id: document.getElementById('edit-id').value,
        firstName: document.getElementById('edit-firstname').value,
        lastName: document.getElementById('edit-lastname').value,
        age: document.getElementById('edit-age').value,
        email: document.getElementById('edit-email').value,
        password: document.getElementById('edit-password').value,
        roles: getSelectedRoles('edit-roles')
    };

    await fetch(URL, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedUser)
    });

    const modalElement = document.getElementById('editModal');
    const modal = bootstrap.Modal.getInstance(modalElement);
    modal.hide();
    fillTable();
}

async function deleteUser() {
    const id = document.getElementById('delete-id').value;
    await fetch(`${URL}/${id}`, { method: 'DELETE' });

    const modalElement = document.getElementById('deleteModal');
    const modal = bootstrap.Modal.getInstance(modalElement);
    modal.hide();
    fillTable();
}

function getSelectedRoles(selectId) {
    const select = document.getElementById(selectId);
    return Array.from(select.selectedOptions).map(opt => ({
        id: opt.value,
        name: "ROLE_" + opt.text
    }));
}
