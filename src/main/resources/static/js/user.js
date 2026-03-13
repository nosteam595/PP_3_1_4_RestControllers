// Чистый JS (Vanilla JS)
document.addEventListener("DOMContentLoaded", function () {
    showUserInfo();
});

async function showUserInfo() {
    try {
        const response = await fetch("/api/users/current");

        if (!response.ok) {
            throw new Error("Ошибка при получении данных пользователя");
        }

        const user = await response.json();

        // Преобразуем роли в строку (убираем ROLE_)
        const roles = user.roles.map(role => role.name.replace('ROLE_', '')).join(' ');

        // 1. Заполняем данные в шапке (вместо $('#id').text(...))
        const headerEmail = document.getElementById('header-email');
        const headerRoles = document.getElementById('header-roles');

        if (headerEmail) headerEmail.textContent = user.email;
        if (headerRoles) headerRoles.textContent = roles;

        // 2. Заполняем строку в таблице
        const tableBody = document.getElementById('user-info-table');
        if (tableBody) {
            const userRow = `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.firstName}</td>
                    <td>${user.lastName}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${roles}</td>
                </tr>`;
            tableBody.innerHTML = userRow;
        }

    } catch (error) {
        console.error("Error loading user info:", error);
    }
}
