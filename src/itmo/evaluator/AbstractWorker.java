package itmo.evaluator;

import com.google.protobuf.InvalidProtocolBufferException;
import itmo.dreamq.MessageQueue;
import itmo.mq.Envelope;
import itmo.mq.Message;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author Vladislav Kononov vincent@yandex-team.ru
 */
public abstract class AbstractWorker {

    private int tag;

    public AbstractWorker(int tag) {
        this.tag = tag;
    }

    public void run() throws MalformedURLException, InvalidProtocolBufferException {
        URL url = new URL("http://localhost:9999/mq?wsdl");
        QName qname = new QName("http://dreamq.itmo/", "DreamQueueService");

        Service service = Service.create(url, qname);

        MessageQueue messageQueue = service.getPort(MessageQueue.class);

        while (true) {
            Envelope envelope = messageQueue.getBlocking(tag);
            Message msg = envelope.getMsg();
            EvalMessage.Request e = EvalMessage.Request.parseFrom(msg.getMsg());
            EvalMessage.Request.Operation o = e.getOp();
            if (canPerform(o)) {
                int evaluator = e.getEvaluatorId();
                double res = perform(o, e.getArgList());
                EvalMessage.Reply r = EvalMessage.Reply.newBuilder()
                        .setRes(res)
                        .setSeq(e.getSeq())
                        .build();
                Message m = new Message();
                m.setMsg(r.toByteArray());
                messageQueue.put(evaluator, m);
                messageQueue.ack(envelope.getTicketId());
                System.out.println(o.toString() + " " + e.getArgList().toString() + " = " + res);
            }
        }
    }

    abstract boolean canPerform(EvalMessage.Request.Operation op);

    abstract double perform(EvalMessage.Request.Operation op, List<Double> args);

}
