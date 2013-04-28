package itmo.evaluator;

import com.google.protobuf.InvalidProtocolBufferException;
import itmo.dreamq.DreamQueueService;
import itmo.dreamq.MessageQueue;
import itmo.mq.Envelope;
import itmo.mq.Message;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Vladislav Kononov vincent@yandex-team.ru
 */
public class PWorker extends AbstractWorker{

    public static final int TAG = -1;

    public static void main(String[] args) throws MalformedURLException, InvalidProtocolBufferException, InterruptedException {
        System.out.println(args.length);
        PWorker worker = new PWorker();
        worker.init(args[0], args[1], args[2]);
        while (true) {
            worker.receiveEnvelope(TAG);
            worker.proceed();
            worker.send();
        }

    }

    @Override
    public void proceed() {
        EvalMessage.Request.Operation o = getOperation();
        if (o == EvalMessage.Request.Operation.ADD){
            setResult(getArg1() + getArg2());
        } else if (o == EvalMessage.Request.Operation.SUB){
            setResult(getArg2() - getArg1());
        }
        if (o != EvalMessage.Request.Operation.DIV && o != EvalMessage.Request.Operation.MUL) {
            send();
        }
    }
}
