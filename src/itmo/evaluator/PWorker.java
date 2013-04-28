package itmo.evaluator;

import java.util.List;

/**
 * @author Vladislav Kononov vincent@yandex-team.ru
 */
public class PWorker extends AbstractWorker {

    public PWorker() {
        super(-1);
    }

    @Override
    boolean canPerform(EvalMessage.Request.Operation op) {
        return op == EvalMessage.Request.Operation.ADD ||
               op == EvalMessage.Request.Operation.SUB;
    }

    @Override
    double perform(EvalMessage.Request.Operation op, List<Double> args) {
        if (op == EvalMessage.Request.Operation.ADD) {
            return args.get(0) + args.get(1);
        } else if (op == EvalMessage.Request.Operation.SUB) {
            return args.get(0) - args.get(1);
        }
        throw new IllegalArgumentException("Can't perform this");
    }

}
