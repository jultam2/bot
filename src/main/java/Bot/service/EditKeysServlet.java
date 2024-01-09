package Bot.service;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

@WebServlet("Classss_war//EditKeysServlet")
public class EditKeysServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/editkeys.html");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String newApiKey = request.getParameter("newApiKey");
        String newApiSecret = request.getParameter("newApiSecret");

        // Проверяем, совпадает ли текущий API Key с фиксированным значением
        Properties commonBotProperties = loadCommonBotProperties();
        String storedApiKey = commonBotProperties.getProperty("apiKey");
        if ("your_fixed_secret_key".equals(storedApiKey)) {
            // Если совпадает, обновляем файл common-bot.properties
            updatePropertiesFile(newApiKey, newApiSecret);
            response.getWriter().println("Keys successfully changed");
        } else {
            // Если текущий API Key неверен, выводим сообщение об ошибке
            response.getWriter().println("API Key is invalid");
        }
    }

    private void updatePropertiesFile(String newApiKey, String newApiSecret) throws IOException {
        // Загружаем данные из файла
        Properties properties = loadCommonBotProperties();

        // Обновляем значения
        properties.setProperty("api.key", newApiKey);
        properties.setProperty("api.secret", newApiSecret);

        // Сохраняем обновленные данные в файл
        try (OutputStream output = new FileOutputStream("common-bot.properties")) {
            properties.store(output, "Common API Key and API Secret");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Properties loadCommonBotProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("common-bot.properties")) {
            properties.load(input);
        }
        return properties;
    }
}