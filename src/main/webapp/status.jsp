<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        body {
            background-color: #333;
            color: white;
            text-align: center;
            padding: 15px;
        }

        h1, h2 {
            color: #ff5733;
        }

        th, td {
            text-align: left; /* Добавлено для центрирования текста в ячейках таблицы */
        }

        .btn-danger, .btn-warning, .btn-primary {
            color: white;
        }

        .button-container {
            display: flex;
            justify-content: flex-start;  /* Изменено на space-between для добавления пространства между кнопками */
            margin-bottom: 10px;
        }

        .button-container button {
            margin-right: 10px; /* Увеличен отступ между кнопками */
        }

        .button1-container {
            display: flex;
            align-items: center;
        }

        .button1-container button {
            margin-right: 10px; /* Увеличен отступ между кнопками */
        }
    </style>
    <title>Bot Status</title>
</head>
<body class="container mt-5">

<%
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String updatedTime = sdf.format(new Date());
%>

<h1>Launched Bot Data</h1>

<!-- Блок кнопок -->
<div class="button-container">
    <button class="btn btn-primary" onclick="redirect('http://localhost:8080/Classss_war/')">Back</button>
    <button class="btn btn-primary" onclick="redirect('http://localhost:8080/Classss_war/EditKeysServlet')">Edit API Keys</button>
    <button class="btn btn-primary" onclick="redirect('http://localhost:8080/Classss_war/EditConfigServlet')">Add Another Config</button>
</div>

<script>
    function redirect(url) {
        window.location.href = url;
    }
</script>

<table class="table table-dark table-striped">
    <tr>
        <th>Public Key</th>
        <th>Secret API Key</th>
        <th>Updated</th>
    </tr>
    <tr>
        <td>${commonBotProperties.getProperty("apiKey")}</td>
        <td>Hidden</td>
        <td><%= updatedTime %>
        </td>
    </tr>
</table>

<!-- Таблица с параметрами бота и кнопками -->
<table class="table table-dark table-striped">
    <tr>
        <th>Price Step</th>
        <th>Coefficient</th>
        <th>Number of Orders</th>
        <th>Status</th>
        <th>Action</th>
    </tr>

    <%-- Цикл для отображения информации о каждом BotStatus --%>
    <c:forEach var="entry" items="${botStatusMap}">
        <c:set var="index" value="${entry.key}" />
        <c:set var="botStatus" value="${entry.value}" />

        <c:if test="${not botStatus.deleted}">
        <tr>
            <td>${botStatus.getPriceStep()}</td>
            <td>${botStatus.getCoefficient()}</td>
            <td>${botStatus.getNumberOfOrders()}</td>
            <td>${botStatus.isStatus() ? 'Working' : 'Stopped'}</td>
            <td style="text-align: center;">

                <!-- Измененный блок для кнопок "Save" и "Delete" -->
                <div class="button1-container">
                    <form method="post" action="/Classss_war/BotStatusServlet">
                        <input type="hidden" name="action" value="stop">
                        <input type="hidden" name="index" value="${index}">
                        <button class="btn btn-danger">Stop</button>
                    </form>
                    <form method="post" action="/Classss_war/BotStatusServlet">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="index" value="${index}">
                        <button class="btn btn-warning" ${!botStatus.isStatus() ? '' : 'disabled'}>Delete</button>
                    </form>

                </div>

            </td></tr>
        </c:if>
    </c:forEach>

</table>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>