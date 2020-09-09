package net.themorningcompany.survivalutil.util.random;

import net.themorningcompany.survivalutil.math.MathOperator;
import net.themorningcompany.survivalutil.math.MathProblemType;
import net.themorningcompany.survivalutil.math.parsing.ExpressionNode;
import net.themorningcompany.survivalutil.math.parsing.InvalidExpressionException;

import static net.themorningcompany.survivalutil.math.MathOperator.randomOperator;
import static net.themorningcompany.survivalutil.util.random.RandomUtil.randomRange;

public class RandomMath {

    public static MathProblemType randomProblemType() {
        return MathProblemType.values()[randomRange(0, MathProblemType.values().length - 1)];
    }

    public static Tuple<String, Double> randomProblem() {

        MathProblemType problemType = MathProblemType.ARITHMETIC;

        boolean isValidProblemType = false;
        while (!isValidProblemType) {
            problemType = randomProblemType();
            isValidProblemType = problemType != MathProblemType.SYMBOLS;
        }
        MathOperator operator = randomOperator(problemType);

        int n1 = randomRange(0, 100);
        int n2 = randomRange(0, 100);

        String problem = n1 + " " + MathOperator.toString(operator) + " " + n2;

        try {
            double solution = ExpressionNode.evaluateExpression(problem);

            return new Tuple<String, Double>(problem, solution);
        } catch (InvalidExpressionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class Tuple<X, Y> {
        public X first;
        public Y second;

        public Tuple() {
            super();
        }

        public Tuple(X first, Y second) {
            this.first = first;
            this.second = second;
        }

        public X getFirst() {
            return first;
        }

        public void setFirst(X first) {
            this.first = first;
        }

        public Y getSecond() {
            return second;
        }

        public void setSecond(Y second) {
            this.second = second;
        }
    }
}
