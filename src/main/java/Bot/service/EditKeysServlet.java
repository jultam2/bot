package Bot.service;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.Properties;

@WebServlet("/EditKeysServlet")
public class EditKeysServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Properties commonBotProperties = loadCommonBotProperties();
        request.setAttribute("commonBotProperties", commonBotProperties);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/editkeys.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String newApiKey = request.getParameter("newApiKey");
        String newApiSecret = request.getParameter("newApiSecret");

        updatePropertiesFile(newApiKey, newApiSecret);
        response.sendRedirect("/Classss_war/BotStatusServlet");

    }

    private void updatePropertiesFile(String newApiKey, String newApiSecret) {
        // Загружаем данные из файла
        Properties properties = loadCommonBotProperties();

        // Обновляем значения
        properties.setProperty("apiKey", newApiKey);
        properties.setProperty("apiSecret", newApiSecret);

        // Сохраняем обновленные данные в файл
        String fileName = "common-bot.properties";

        try (OutputStream outputStream = new FileOutputStream(fileName)) {
            properties.store(outputStream, "Common API Key and API Secret");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Properties loadCommonBotProperties() {
        Properties properties = new Properties();
        String fileName = "common-bot.properties";

        try (InputStream inputStream = new FileInputStream(fileName)) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace(); // Обработайте исключение по вашему усмотрению
        }

        return properties;
    }
}