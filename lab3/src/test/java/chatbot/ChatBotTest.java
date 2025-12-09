package chatbot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChatBotTest {
    private final ChatBot bot = new ChatBot();

    @Test
    @DisplayName("Тест на пустую команду")
    public void emptyCommandTest() {
        String input = "";
        String expected = "Вы ничего не сказали...\n";
        String actual = bot.handleCommand(input);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Тест на неизвестную команду")
    public void unknownCommandTest() {
        String input = "/test";
        String expected = "Простите, я ещё глупий, и знаю только эти команды: /start, /coin, /dice, /echo [текст], /caps [текст], /calc [операнд 1] [оператор] [операнд 2]\n";
        String actual = bot.handleCommand(input);
        assertEquals(expected, actual);
    }

    @ParameterizedTest(name = "Возведение в верхний регистр: {0}")
    @DisplayName("Тест возведения в верхний регистр")
    @ValueSource(strings = {
            "естовая строка для верхнего регистра",
            "ааАААааа",
            "test string for upper case",
            "AAAAAAAAAAAAA",
    })
    public void upperCaseTest(String input) {
        String expected = input.toUpperCase() + "\n";
        String actual = bot.handleCommand(String.format("/caps %s", input));
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Тест возведения пустой строки в верхний регистр")
    public void emptyStrToUpperCaseTest() {
        String input = "";
        String expected = "А что мне капсить? Ты ничего не написал :(\n";
        String actual = bot.handleCommand(String.format("/caps %s", input));
        assertEquals(expected, actual);
    }

    @ParameterizedTest(name = "Эхо: {0}")
    @DisplayName("Тест эха")
    @ValueSource(strings = {
            "Тестовая строка для эха",
            "привет",
            "hello",
            "test",
            "aaaaaaaaaaaaaaa",
            "A A A A A A A A         AAAAAAA"
    })
    public void echoTest(String input) {
        String expected = String.format("Эхо: %s\n", input);
        String actual = bot.handleCommand(String.format("/echo %s", input));
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Тест эха с пустой строкой")
    public void emptyStrEchoTest() {
        String input = "";
        String expected = "Ты ничего не сказал для эха.\n";
        String actual = bot.handleCommand(String.format("/echo %s", input));
        assertEquals(expected, actual);
    }

    @ParameterizedTest(name = "Тест {index}: {0} {1} {2} = {3}")
    @DisplayName("Тест калькулятора на корректные вычисления и корректный парсинг")
    @CsvSource({
            "5, +, 3, 8",
            "10, -, 4, 6",
            "6, *, 7, 42",
            "20, /, 5, 4",
            "0, +, 10, 10",
            "-5, +, 8, 3",
            "10, -, -3, 13",
            "-4, *, -3, 12",
            "15, /, 3, 5",
            "0, *, 100, 0",
            "100, -, 100, 0"
    })
    public void calcSuccessfulOperationTest(String op1, String operator, String op2, String res) {
        String input = String.format("/calc %s %s %s", op1, operator, op2);
        String expected = String.format("Ваш результат: %s\n", res);
        String actual = bot.handleCommand(input);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Тест деления на 0")
    public void calcDivisionByZeroTest() {
        String input = "/calc 100 / 0";
        String expected = "Делить на 0 нельзя!\n";
        String actual = bot.handleCommand(input);
        assertEquals(expected, actual);
    }

    @ParameterizedTest(name = "Неизвестная операция {0} {1} {2}")
    @DisplayName("Тест на неккоректные операции")
    @CsvSource({
            "5, %, 2",
            "10, ^, 3",
            "3, и, 4",
            "7, ., 2",
            "1, ++, 1"
    })
    public void calcUnknownOperationTest(String op1, String operation, String op2) {
        String input = String.format("/calc %s %s %s", op1, operation, op2);
        String expected = "Я не знаю такой операции :(\n";
        String actual = bot.handleCommand(input);
        assertEquals(expected, actual);
    }

    @ParameterizedTest(name = "Некорректный формат: {0}")
    @DisplayName("Тест на некорретный формат выражения")
    @ValueSource(strings = {
            "/calc 5 +",
            "/calc 5",
            "/calc + 3",
            "/calc 1 + 2 + 3",
            "/calc 1+2",
            "/calc ",
            "/calc 1 2 3 4"
    })
    public void calcInvalidFormatTest(String input) {
        String expected = "Я могу посчитать выражения по типу: 2 + 2, со знаком и 2 операндами, никак иначе :(\n";
        String actual = bot.handleCommand(input);
        assertEquals(expected, actual);
    }

    @ParameterizedTest(name = "Некорректный числа: {0}")
    @DisplayName("Тест на некорректный формат чисел")
    @ValueSource(strings = {
            "/calc abc + 2",
            "/calc 5 + def",
            "/calc 3.14 + 2",
            "/calc 2 + 3.5",
            "/calc 1,5 * 2",
            "/calc 1e3 + 2"
    })
    public void calcInvalidNumbers(String input) {
        String expected = "Я не могу оперировать с буковками, дайте мне пожалуйста чиселки :(\n";
        String actual = bot.handleCommand(input);
        assertEquals(expected, actual);
    }

    @ParameterizedTest(name = "Целочисленное деление {index}: {0} / {2} = {3}")
    @DisplayName("Тест на целочисленное деление")
    @CsvSource({
            "7, /, 2, 3",
            "9, /, 4, 2",
            "1, /, 2, 0",
            "10, /, 3, 3"
    })
    public void calcIntegerDivisionTest(String op1, String operation, String op2, String res) {
        String input = String.format("/calc %s %s %s", op1, operation, op2);
        String expected = String.format("Ваш результат: %s\n", res);
        String actual = bot.handleCommand(input);
        assertEquals(expected, actual);
    }

    @RepeatedTest(value = 100, name = "Повторение {currentRepetition}/{totalRepetitions}")
    @DisplayName("Тест на подброс 100 монеток")
    public void flipCointRepeatedTest() {
        String result = bot.handleCommand("/coin").trim();
        assertTrue(result.equals("Орёл") || result.equals("Решка"));
    }

    @RepeatedTest(value = 100, name = "Повторение {currentRepetition}/{totalRepetitions}")
    @DisplayName("Тест на подброс 100 кубиков")
    public void rollDiceRepeatedTest() {
        String result = bot.handleCommand("/dice").trim();
        int diceValue = Integer.parseInt(result);
        assertTrue(diceValue >= 1 && diceValue <= 6);
    }
}
