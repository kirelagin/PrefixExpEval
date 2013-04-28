package itmo.evaluator;

import java.util.List;

/**
 * @author Vladislav Kononov vincent@yandex-team.ru
 */
public class MWorker extends AbstractWorker {

    public MWorker() {
        super(-2);
    }

    @Override
    boolean canPerform(EvalMessage.Request.Operation op) {
        return op == EvalMessage.Request.Operation.MUL ||
               op == EvalMessage.Request.Operation.DIV;
    }

    @Override
    double perform(EvalMessage.Request.Operation op, List<Double> args) {
        if (op == EvalMessage.Request.Operation.MUL) {
            return args.get(0) * args.get(1);
        } else if (op == EvalMessage.Request.Operation.DIV) {
            return args.get(0) / args.get(1);
        }
        throw new IllegalArgumentException("Can't perform this");
    }

}
