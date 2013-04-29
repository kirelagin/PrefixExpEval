package itmo.evaluator;

import com.google.protobuf.InvalidProtocolBufferException;
import itmo.dreamq.MessageQueue;
import itmo.mq.Envelope;
import itmo.mq.Message;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import javax.xml.ws.WebServiceException;
import java.net.URL;
import java.util.List;

/**
 * @author Vladislav Kononov vincent@yandex-team.ru
 */
public abstract class AbstractWorker implements Runnable {

    private int tag;
    private MessageQueue messageQueue;

    public AbstractWorker(int tag, String url) throws MalformedURLException {
        this.tag = tag;

        QName qName = new QName("http://dreamq.itmo/", "DreamQueueService");
        Service service = Service.create(new URL(url), qName);
        messageQueue = service.getPort(MessageQueue.class);
    }

    @Override
    public void run() {
        try {
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
                    System.err.println(o.toString() + " " + e.getArgList().toString() + " = " + res + " // " + Thread.currentThread().getId());
                }
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    abstract boolean canPerform(EvalMessage.Request.Operation op);

    abstract double perform(EvalMessage.Request.Operation op, List<Double> args);

}
