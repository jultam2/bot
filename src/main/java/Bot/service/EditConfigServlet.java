package Bot.service;
import Bot.model.BotList;
import Bot.model.BotStatus;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.Map;
import java.util.Properties;

@WebServlet("/EditConfigServlet")
public class EditConfigServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/editconfig.html");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Properties commonBotProperties = loadCommonBotProperties();
        String apiKey = commonBotProperties.getProperty("apiKey");
        String apiSecret = commonBotProperties.getProperty("apiSecret");

        double priceStep = Double.parseDouble(request.getParameter("priceStep"));
        double coefficient = Double.parseDouble(request.getParameter("coefficient"));
        int numberOfOrders = Integer.parseInt(request.getParameter("numberOfOrders"));

        BotList botList = BotList.getInstance();
        Map<Integer, BotStatus> botStatusMap = botList.getBotStatusMap();

        int newIndex = botStatusMap.size() + 1;
        BotStatus botStatus = new BotStatus();
        botStatus.updateStatus(apiKey, priceStep, coefficient, numberOfOrders, true);
        botStatusMap.put(newIndex, botStatus);

        BotExecutor botExecutor = new BotExecutor(apiKey, apiSecret, priceStep, coefficient, numberOfOrders);
        new Thread(botExecutor::execute).start();

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