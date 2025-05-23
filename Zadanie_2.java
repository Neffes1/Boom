import java.util.*;

class CalculatorModel {
    public double evaluate(String expression) throws Exception {
        // Проверка скобок
        checkParentheses(expression);
        
        return new Object() {
            int pos = -1, ch;
            
            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }
            
            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }
            
            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }
            
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }
            
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*') && eat('*')) x = Math.pow(x, parseFactor()); // ** - возведение в степень
                    else if (eat('*')) x *= parseFactor();
                    else if (eat('/') && eat('/')) x = Math.floor(x / parseFactor()); // // - деление без остатка
                    else if (eat('/')) x /= parseFactor();
                    else if (eat('^')) x = Math.pow(x, parseFactor()); // ^ - возведение в степень
                    else return x;
                }
            }
            
            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();
                
                double x;
                int startPos = this.pos;
                
                // Обработка функций
                if (eat('l') && eat('o') && eat('g') && eat('(')) {
                    x = parseExpression();
                    eat(')');
                    x = Math.log(x) / Math.log(2); // log по основанию 2
                } 
                else if (eat('e') && eat('x') && eat('p') && eat('(')) {
                    x = parseExpression();
                    eat(')');
                    x = Math.exp(x);
                }
                else if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                }
                else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } 
                else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }
                
                // Обработка факториала
                if (eat('!')) {
                    x = factorial((int) x);
                }
                
                return x;
            }
            
            // Метод для вычисления факториала
            private double factorial(int n) {
                if (n < 0) throw new RuntimeException("Negative factorial");
                double result = 1;
                for (int i = 2; i <= n; i++) {
                    result *= i;
                }
                return result;
            }
        }.parse();
    }
    
    // Проверка правильности расстановки скобок
    private void checkParentheses(String expression) throws Exception {
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                if (stack.isEmpty() || stack.pop() != '(') {
                    throw new Exception("Несбалансированные скобки");
                }
            }
        }
        if (!stack.isEmpty()) {
            throw new Exception("Несбалансированные скобки");
        }
    }
}

class CalculatorView {
    private Scanner scanner = new Scanner(System.in);
    
    public String getInput() {
        System.out.print("Введите математическое выражение: ");
        return scanner.nextLine();
    }
    
    public void displayResult(double result) {
        System.out.println("Результат: " + result);
    }
    
    public void displayError(String message) {
        System.out.println("Ошибка: " + message);
    }
}

class CalculatorController {
    private CalculatorModel model;
    private CalculatorView view;
    
    public CalculatorController(CalculatorModel model, CalculatorView view) {
        this.model = model;
        this.view = view;
    }
    
    public void calculate() {
        String expression = view.getInput();
        try {
            double result = model.evaluate(expression);
            view.displayResult(result);
        } catch (Exception e) {
            view.displayError(e.getMessage());
        }
    }
    
    public void run() {
        while (true) {
            System.out.println("\n1. Вычислить выражение");
            System.out.println("2. Выход");
            System.out.print("Выберите действие: ");
            
            String choice = new Scanner(System.in).nextLine();
            switch (choice) {
                case "1":
                    calculate();
                    break;
                case "2":
                    return;
                default:
                    System.out.println("Неверный выбор!");
            }
        }
    }
}

public class MathCalculatorApp {
    public static void main(String[] args) {
        CalculatorModel model = new CalculatorModel();
        CalculatorView view = new CalculatorView();
        CalculatorController controller = new CalculatorController(model, view);
        
        System.out.println("Программа для вычисления математических выражений");
        System.out.println("Поддерживаемые операции: +, -, *, /, // (целочисленное деление), ** или ^ (степень)");
        System.out.println("Поддерживаемые функции: log() (по основанию 2), exp(), ! (факториал)");
        System.out.println("Пример: -3234+((exp(2)*843/log(3234)-4232123)/(34+123+32+5))*3234");
        
        controller.run();
    }
}
