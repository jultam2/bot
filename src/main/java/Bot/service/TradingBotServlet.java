package Bot.service;

import Bot.model.BotList;
import Bot.model.BotStatus;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

@WebServlet("/TradingBotServlet")
public class TradingBotServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String apiKey = request.getParameter("apiKey");
        String apiSecret = request.getParameter("apiSecret");
        double priceStep = Double.parseDouble(request.getParameter("priceStep"));
        double coefficient = Double.parseDouble(request.getParameter("coefficient"));
        int numberOfOrders = Integer.parseInt(request.getParameter("numberOfOrders"));

        BotList botList = BotList.getInstance();
        Map<Integer, BotStatus> botStatusMap = botList.getBotStatusMap();

        // Создание нового объекта BotStatus и добавление в botStatusMap с приставкой индекса
        int newIndex = botStatusMap.size() + 1;
        BotStatus botStatus = new BotStatus();
        botStatus.updateStatus(apiKey, priceStep, coefficient, numberOfOrders, "Working");
        botStatusMap.put(newIndex, botStatus);

        saveApiKeySecretToFile(apiKey, apiSecret);

        BotExecutor botExecutor = new BotExecutor(apiKey, apiSecret, priceStep, coefficient, numberOfOrders);
        new Thread(botExecutor::execute).start();

        response.sendRedirect("/Classss_war/BotStatusServlet");
    }

    private void saveApiKeySecretToFile(String apiKey, String apiSecret) {
        Properties properties = new Properties();
        properties.setProperty("apiKey", apiKey);
        properties.setProperty("apiSecret", apiSecret);

        String fileName = "common-bot.properties";

        try (OutputStream outputStream = new FileOutputStream(fileName)) {
            properties.store(outputStream, "Common API Key and API Secret");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
