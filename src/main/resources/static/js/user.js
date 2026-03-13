$(document).ready(function () {
    showUserInfo();
});

async function showUserInfo() {
    try {
        // Запрос к вашему RestController
        const response = await fetch("/api/users/current");

        if (!response.ok) {
            throw new Error("Ошибка при получении данных пользователя");
        }

        const user = await response.json();

        // Преобразуем роли в строку (убираем ROLE_)
        const roles = user.roles.map(role => role.name.replace('ROLE_', '')).join(' ');

        // 1. Заполняем данные в шапке (Navbar)
        $('#header-email').text(user.email);
        $('#header-roles').text(roles);

        // 2. Заполняем строку в таблице
        const userRow = `
            <tr>
                <td>${user.id}</td>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${roles}</td>
            </tr>`;

        $('#user-info-table').html(userRow);

    } catch (error) {
        console.error("Error loading user info:", error);
    }
}
