<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
            padding: 20px;
        }

        h1, th, td {
            color: #ff5733;
        }

        .btn-primary {
            color: white;
        }
    </style>
    <title>Edit your API Keys</title>
</head>
<body class="container mt-5">

<h1>Edit your API Keys</h1>

<form id="editForm" method="post" action="/Classss_war//EditKeysServlet">
    <div class="mb-3">
        <label for="currentApiKey" class="form-label">Current API Key</label>
        <input type="text" class="form-control" id="currentApiKey" name="currentApiKey" required>
    </div>

    <button type="button" class="btn btn-primary" onclick="checkCurrentApiKey()">Check</button>

    <div id="newKeysSection" style="display: none;">
        <div class="mb-3">
            <label for="newApiKey" class="form-label">New API Key</label>
            <input type="text" class="form-control" id="newApiKey" name="newApiKey" disabled>
        </div>

        <div class="mb-3">
            <label for="newApiSecret" class="form-label">New Secret API Key</label>
            <input type="text" class="form-control" id="newApiSecret" name="newApiSecret" disabled>
        </div>

        <button type="submit" class="btn btn-primary" id="confirmButton" style="display: none;">Confirm</button>
    </div>
</form>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    var apiKeyFromJava = "${commonBotProperties.getProperty("apiKey")}";
    var newApiKeyField = document.getElementById('newApiKey');
    var newApiSecretField = document.getElementById('newApiSecret');
    var confirmButton = document.getElementById('confirmButton');

    function checkCurrentApiKey() {
        var currentApiKey = document.getElementById('currentApiKey').value;

        if (currentApiKey === apiKeyFromJava) {
            alert('Check passed. Please, enter new API keys');
            newApiKeyField.removeAttribute('disabled');
            newApiSecretField.removeAttribute('disabled');
            document.getElementById('newKeysSection').style.display = 'block';
            confirmButton.style.display = 'block';
        } else {
            alert('API Key is invalid');
        }
    }
</script>

</body>
</html>
