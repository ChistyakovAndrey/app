async function loginForm(event) {
    event.preventDefault();

    const data = {
        username: $('input[name="username"]').val(),
        password: $('input[name="password"]').val()
    };

    try {
        const response = await fetch('/login/isUserValid', {
            method: "POST",
            body: JSON.stringify(data),
            headers: {
                "Content-Type": "application/json"
            }
        });


        if (response.ok) {
            const text = await response.text();
            if (text === 'true') {
                // Создаем скрытую HTML-форму
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = '/login';

                // Добавляем в форму поля для ввода имени пользователя и пароля
                const usernameField = document.createElement('input');
                usernameField.type = 'hidden';
                usernameField.name = 'username';
                usernameField.value = data.username;
                form.appendChild(usernameField);

                const passwordField = document.createElement('input');
                passwordField.type = 'hidden';
                passwordField.name = 'password';
                passwordField.value = data.password;
                form.appendChild(passwordField);

                // Добавляем форму на страницу и автоматически отправляем
                document.body.appendChild(form);
                form.submit();
            } else {
                $('#username').css("border", "4px solid #F37E7E");
                $('#pass').css("border", "4px solid #F37E7E");
                $('.err_msg').text("Неверное имя пользователя или пароль");
                $('.err_msg').animate({"opacity": "1"}, 300);
            }
        } else {
            throw new Error('Network response was not ok');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}