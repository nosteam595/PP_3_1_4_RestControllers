const URL = "/api/users";

$(document).ready(function () {
    // 1. Загружаем данные текущего пользователя в шапку
    fillHeader();
    // 2. Загружаем таблицу пользователей
    fillTable();
    // 3. Предзагружаем роли в формы создания и редактирования
    loadRoles();

    // Слушатель для формы СОЗДАНИЯ нового пользователя
    $('#add-user-form').on('submit', async (e) => {
        e.preventDefault();
        await createUser();
    });

    // Слушатель для формы РЕДАКТИРОВАНИЯ
    $('#edit-user-form').on('submit', async (e) => {
        e.preventDefault();
        await updateUser();
    });

    // Слушатель для формы УДАЛЕНИЯ
    $('#delete-user-form').on('submit', async (e) => {
        e.preventDefault();
        await deleteUser();
    });
});

// --- ФУНКЦИИ ---

async function fillHeader() {
    const response = await fetch(URL + "/current");
    const user = await response.json();
    $('#header-email').text(user.email);
    $('#header-roles').text(user.roles.map(r => r.name.replace('ROLE_', '')).join(' '));
}

async function fillTable() {
    const response = await fetch(URL);
    const users = await response.json();
    const tableBody = $('#all-users-table');
    tableBody.empty();

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
        tableBody.append(row);
    });
}

// Загрузка ролей из БД для выпадающих списков
async function loadRoles() {
    const response = await fetch(URL + "/roles");
    const roles = await response.json();
    const selectors = $('#edit-roles, #add-roles, #delete-roles');
    selectors.empty();
    roles.forEach(role => {
        selectors.append(`<option value="${role.id}">${role.name.replace('ROLE_', '')}</option>`);
    });
}

// Показ модалки EDIT
async function showEditModal(id) {
    const response = await fetch(`${URL}/${id}`);
    const user = await response.json();

    $('#edit-id').val(user.id);
    $('#edit-firstname').val(user.firstName);
    $('#edit-lastname').val(user.lastName);
    $('#edit-age').val(user.age);
    $('#edit-email').val(user.email);
    $('#edit-password').val(''); // Пароль оставляем пустым

    // Отмечаем роли пользователя
    $('#edit-roles option').prop('selected', false);
    user.roles.forEach(role => {
        $(`#edit-roles option[value="${role.id}"]`).prop('selected', true);
    });

    $('#editModal').modal('show');
}

// Показ модалки DELETE (аналогично, но поля readonly)
async function showDeleteModal(id) {
    const response = await fetch(`${URL}/${id}`);
    const user = await response.json();

    $('#delete-id').val(user.id);
    $('#delete-firstname').val(user.firstName);
    $('#delete-lastname').val(user.lastName);
    $('#delete-age').val(user.age);
    $('#delete-email').val(user.email);

    $('#delete-roles option').prop('selected', false);
    user.roles.forEach(role => {
        $(`#delete-roles option[value="${role.id}"]`).prop('selected', true);
    });

    $('#deleteModal').modal('show');
}

// ОТПРАВКА ДАННЫХ НА СЕРВЕР

async function createUser() {
    const newUser = {
        firstName: $('#add-firstname').val(),
        lastName: $('#add-lastname').val(),
        age: $('#add-age').val(),
        email: $('#add-email').val(),
        password: $('#add-password').val(),
        roles: getSelectedRoles('#add-roles')
    };

    await fetch(URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newUser)
    });

    $('#add-user-form')[0].reset(); // Очистка формы
    fillTable(); // Обновить таблицу
    $('[data-bs-target="#usersTablePane"]').click(); // Вернуться на вкладку таблицы
}

async function updateUser() {
    const updatedUser = {
        id: $('#edit-id').val(),
        firstName: $('#edit-firstname').val(),
        lastName: $('#edit-lastname').val(),
        age: $('#edit-age').val(),
        email: $('#edit-email').val(),
        password: $('#edit-password').val(),
        roles: getSelectedRoles('#edit-roles')
    };

    await fetch(URL, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedUser)
    });

    $('#editModal').modal('hide');
    fillTable();
}

async function deleteUser() {
    const id = $('#delete-id').val();
    await fetch(`${URL}/${id}`, { method: 'DELETE' });
    $('#deleteModal').modal('hide');
    fillTable();
}

// Хелпер для сбора объектов Ролей из селекта
function getSelectedRoles(selector) {
    let roles = [];
    $(selector + " option:selected").each(function () {
        roles.push({
            id: $(this).val(),
            name: "ROLE_" + $(this).text()
        });
    });
    return roles;
}
