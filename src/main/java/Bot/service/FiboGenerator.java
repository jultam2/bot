package Bot.service;

import java.util.ArrayList;
import java.util.List;

public class FiboGenerator {

    public List<Double> generateFibonacciSequence(int numberOfElements) {
        List<Double> fibonacciNumbers = new ArrayList<>();
        fibonacciNumbers.add(1.0); // Сначала добавляем 1
        double a = 1, b = 1;

        for (int i = 1; i < numberOfElements; i++) { // Начинаем с 2, так как 1 уже добавлено
            fibonacciNumbers.add(a);
            double temp = a;
            a = b;
            b = temp + b;
        }
        return fibonacciNumbers;
    }

    public int findNumberOfElements(double totalValue, double coefficient) {
        List<Double> fibonacciNumbers = new ArrayList<>();
        fibonacciNumbers.add(1.0); // Сначала добавляем 1

        double a = 1, b = 1;
        int numberOfElements = 1;

        while (true) {
            double fibValue = a;
            totalValue -= (int) (fibValue * coefficient);
            if (totalValue <= 0) {
                break; // Прерываем цикл, если достигли или превысили общее значение
            }
            // Генерируем следующее число Фибоначчи
            fibonacciNumbers.add(fibValue);
            double temp = a;
            a = b;
            b = temp + b;
            numberOfElements++;
        }
        return numberOfElements;
    }
}
