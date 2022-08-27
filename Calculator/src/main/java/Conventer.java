import java.util.Arrays;
import java.util.Stack;


public class Conventer {
    private static String operators = "(+-*/^";
    public static double evaluate (String expression){
        String postfixExp = infixToPostfix(expression);
        Stack<Double> arr = new Stack<Double>();
        String[] operands = postfixExp.split(" ");
        double result = 0;
        for (int i = 0; i < operands.length; ++i){
            String curr =  operands[i];
            double digit;
            if(curr.equals("0")){
                ++i;
                arr.push(Double.parseDouble(operands[i]) * -1);
            } else if (curr.equals("_")){
                Stack<Double> tempArr = new Stack<Double>();
                do {
                    double temp = arr.pop();
                    if(temp != result) {
                        temp *= -1;
                        tempArr.push(temp);}
                    }while (arr.size() > 0);
                    arr = tempArr;
                    }
            else {
                try {
                    arr.push(Double.parseDouble(curr));
                }
                catch(NumberFormatException ex) {
                    double firstNum = arr.pop();
                    double secondNum = arr.pop();
                    switch(curr)
                    {
                        case "+":
                            result = secondNum + firstNum;
                            break;
                        case "-":
                            result = secondNum - firstNum;
                            break;
                        case "*":
                            result = secondNum * firstNum;
                            break;
                        case "/":
                            if (firstNum == 0)throw new ArithmeticException();
                            result = secondNum / firstNum;
                            break;
                        case "^":
                            result = Math.pow(secondNum, firstNum);
                            break;
                        default:
                            throw new ArithmeticException();
                    }
                    arr.push(result);
                }
            }
        }
  return  result;
    }
    public static String infixToPostfix(String exp) {
        exp = exp.replaceAll("\\s", "");
        StringBuilder result = new StringBuilder();
        char temp;
        Stack<Character> operator = new Stack<>();
        boolean isMinusBeforeBkt = false;
        char[] chars = new char[exp.length()];
        exp.getChars(0, exp.length(), chars, 0);

        for (int i = 0; i < chars.length; ++i){
            char symbol = chars[i];
            if(Character.isDigit(symbol)){
               String stemp = checkNumberDigits(chars, i);
               i += stemp.length()-1;
               result.append(stemp + " ");
            } else if (symbol == '('){
                operator.push(symbol);
            } else if (symbol == ')'){
                if (isMinusBeforeBkt){
                    result.append("_ ");
                }
                temp = operator.pop();
                while (temp != '('){
                    result.append(temp + " ");
                    temp = operator.pop();
                }
                if (!operator.contains('(')){
                    isMinusBeforeBkt = false;
                }}
            else{
                if (symbol == '-' && (i == 0 || (operators.indexOf(exp.charAt(i-1)) != -1 && (Character.isDigit(chars[i+1]) || chars[i+1] == '(')))){
                    if(chars[i+1]=='('){
                        isMinusBeforeBkt = true;
                    } else {
                        result.append("0 ");
                    }

                } else if (operator.size() != 0 && precedence(operator.peek(), symbol)) {
                    if (isMinusBeforeBkt)
                        result.append("_ ");
                    temp = operator.pop();
                    while (precedence(temp, symbol)) {
                        result.append(temp + " ");
                        if (operator.size() == 0) {
                            break;
                        }
                        temp = operator.pop();
                    }
                    operator.push(symbol);
                }
                else{
                    operator.push(symbol);
                }
            }
        }
        if(isMinusBeforeBkt)
            result.append("_ ");
        while (operator.size() > 0) {
            temp = operator.pop();
            result.append(temp + " ");
        }

        return result.toString();
        }

    private static boolean precedence (char firstOper, char secondOper){
        int [] precedence = {1, 2, 2, 3, 3, 3};
        return precedence[operators.indexOf(firstOper)] >= precedence[operators.indexOf(secondOper)];
    }
    private static String checkNumberDigits (char[] exp, int position){
        String result = "";
        for (; position < exp.length; ++position){
            char c = exp[position];
            if (c == '.' || Character.isDigit(c)){
                result += c;
            } else {
                --position;
                break;
            }
        }
        return result;
    }
}
