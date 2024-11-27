<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title_page}</title>
</head>
<body>
<h2>Список пользователей:</h2>
<p>Количество пользователей: ${users?size}</p>
<ul>
    <#list users as user>
        <li>${user.name} (ID: ${user.id})</li>
    </#list>
</ul>
</body>
</html>


