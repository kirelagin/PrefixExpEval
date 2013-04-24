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
public class MWorker {

    public static final int TAG = -2;

    public static final int TIME_OUT = 1000;


    public static void main(String[] args) throws MalformedURLException, InvalidProtocolBufferException, InterruptedException {
        URL url = new URL("http://localhost:9999/mq?wsdl");
        QName qname = new QName("http://dreamq.itmo/", "DreamQueueService");

        Service service = Service.create(url, qname);

        MessageQueue messageQueue = service.getPort(MessageQueue.class);

        while(true){
            Envelope envelope = messageQueue.get(TAG);
            Message msg = envelope.getMsg();
            if (msg != null) {
                EvalMessage.Request e = EvalMessage.Request.parseFrom(msg.getMsg());
                EvalMessage.Request.Operation o = e.getOp();
                if (o == EvalMessage.Request.Operation.MUL || o == EvalMessage.Request.Operation.DIV) {
                    int tag = e.getEvaluatorId();
                    double arg1 = e.getArg(0);
                    double arg2 = e.getArg(1);
                    double res;
                    if (o == EvalMessage.Request.Operation.MUL) {
                        res = arg2 * arg1;
                    } else {
                        res = arg2 / arg1;
                    }
                    EvalMessage.Reply r = EvalMessage.Reply.newBuilder()
                            .setRes(res)
                            .setSeq(e.getSeq())
                            .build();
                    Message m = new Message();
                    m.setMsg(r.toByteArray());
                    messageQueue.put(tag, m);
                    messageQueue.ack(envelope.getTicketId());
                    System.out.println("Result " + arg1 + " after " + e.getOp().toString());
                }
            } else {
                Thread.sleep(TIME_OUT);
            }
        }
    }

}
