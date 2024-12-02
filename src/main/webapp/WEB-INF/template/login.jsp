<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Welcome!</title>
    <meta charset="utf-8"/>
</head>
<body>
<h1>Вход на страницу пользователя</h1>
<form action="/usercheck" method="get" >
    <div>
        <label for="name">Введите имя пользователя:</label>
        <input id="name" name="name" type="text">
    </div>

    <div>
        <label for="password">Введите пароль:</label>
        <input id="password" name="password" type="text">
    </div>
    <button type="submit">Ok</button>
</form>
</body>
</html>

