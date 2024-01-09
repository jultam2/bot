package Bot.service;
import Bot.model.BotList;
import Bot.model.BotStatus;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@WebServlet("/BotStatusServlet")
public class BotStatusServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        BotList botList = BotList.getInstance();
        Map<Integer, BotStatus> botStatusMap = botList.getBotStatusMap();
        request.setAttribute("botStatusMap", botStatusMap);

        Properties commonBotProperties = loadCommonBotProperties();
        request.setAttribute("commonBotProperties", commonBotProperties);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/status.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");

        if ("stop".equals(action)) {
            // Логика остановки бота

        } else if ("delete".equals(action)) {
            // Логика удаления бота
            // ... (Ваш код удаления бота)
        }

        response.sendRedirect("/Classss_war/BotStatusServlet");
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