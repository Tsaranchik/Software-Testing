package chatbot;

public class ChatBot {
    public String handleCommand(String userMessage) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Вы ничего не сказали...\n";
        }

        String trimmed = userMessage.trim();

        if (trimmed.equalsIgnoreCase("/start") || trimmed.equalsIgnoreCase("привет")) {
            return getWelcomeMessage();
        } else if (trimmed.equalsIgnoreCase("/coin") || trimmed.equalsIgnoreCase("подбрось монетку"))  {
            return flipCoin();
        } else if (trimmed.equalsIgnoreCase("/dice") || trimmed.equalsIgnoreCase("подбрось кубик")) {
            return rollDice();
        } else if (userMessage.startsWith("/echo ")) {
            return doEcho(userMessage.substring(6));
        } else if (userMessage.startsWith("/caps ")) {
            return toUpperCase(userMessage.substring(6));
        } else if (trimmed.equalsIgnoreCase("/help") || trimmed.equalsIgnoreCase("помощь")) {
            return help();
        } else if (userMessage.startsWith("/calc ")) {
            return calc(userMessage.substring(6));
        }

        return "Простите, я ещё глупий, и знаю только эти команды: /start, /coin, /dice, /echo [текст], /caps [текст], /calc [операнд 1] [оператор] [операнд 2]\n";
    }

    private String calc(String substring) {
        String[] expr = substring.split("\\s+");

        if (expr.length != 3) return "Я могу посчитать выражения по типу: 2 + 2, со знаком и 2 операндами, никак иначе :(\n";

        int op1, op2;

        try {
            op1 = Integer.parseInt(expr[0]);
            op2 = Integer.parseInt(expr[2]);
        } catch (NumberFormatException e) {
            return "Я не могу оперировать с буковками, дайте мне пожалуйста чиселки :(\n";
        }

        String operation = expr[1];
        int res;

        switch (operation) {
            case "+":
                res = op1 + op2;
                break;

            case "-":
                res = op1 - op2;
                break;

            case "*":
                res = op1 * op2;
                break;

            case "/":
                if (op2 == 0) return "Делить на 0 нельзя!\n";

                res = op1 / op2;
                break;

            default:
                return "Я не знаю такой операции :(\n";
        }

        return "Ваш результат: " + res + "\n";
    }

    private String help() {
        return "Привет! Я самый простой чат бот на планете и пока ничего толком не умею! Поэтому не судите меня строго!\n" +
                "Вот списочек того, что я умею:\n" +
                "/help - команда для вывода сообщения, которое вы сейчас читаете;\n" +
                "/start - команда для вывода приветственного сообщения;\n" +
                "/coin - команда для поброса монетки, в ответ напишет либо Орёл, либо Решка;\n" +
                "/dice - команда для поброса кубика, в ответ напишет число от 1 до 6;\n" +
                "/echo [текст] - команда для повторения [текст];\n" +
                "/caps [текст] - команда, которая выведет [текст] капсом;\n" +
                "/calc [операнд 1] [оператор] [операнд 2] - команда, которая выполняет операцию над двумя операндами." +
                "Операнды обязательно должны быть целыми! Доступные операции: +, -, *, /.\n";
    }

    private String toUpperCase(String substring) {
        if (substring.isEmpty()) return "А что мне капсить? Ты ничего не написал :(\n";

        return substring.toUpperCase() + "\n";
    }

    private String doEcho(String substring) {
        if (substring.isEmpty()) return "Ты ничего не сказал для эха.\n";

        return "Эхо: " + substring + "\n";
    }

    private String rollDice() {
        int num = (int)((Math.random() * (7 - 1)) + 1);

        return Integer.toString(num) + "\n";
    }

    private String flipCoin() {
        return Math.random() > 0.5 ? "Орёл\n" : "Решка\n";
    }

    private String getWelcomeMessage() {
        return "Привет! Я небольшой и глупенький ботик. Напиши /help, чтобы узнать обо мне немного больше!\n";
    }
}
