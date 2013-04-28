package itmo.evaluator;

import com.google.protobuf.InvalidProtocolBufferException;
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
public class MWorker extends AbstractWorker{

    public static final int TAG = -2;

    public static void main(String[] args) throws MalformedURLException, InvalidProtocolBufferException, InterruptedException {
        System.out.println(args.length);
        MWorker worker = new MWorker();
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
        if (o == EvalMessage.Request.Operation.MUL){
            setResult(getArg1() * getArg2());
        } else if (o == EvalMessage.Request.Operation.DIV){
            setResult(getArg2() / getArg1());
        }
        if (o != EvalMessage.Request.Operation.ADD && o != EvalMessage.Request.Operation.SUB) {
            send();
        }
    }
}