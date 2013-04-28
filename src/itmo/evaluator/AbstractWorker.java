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
public abstract class AbstractWorker {

    private MessageQueue messageQueue;
    private double arg1;
    private double arg2;
    private int evaluatorTag;
    private EvalMessage.Request.Operation operation;
    private double result;
    private int seq;
    private long ticket;

    public void init(String url, String serviceHost, String serviceName) throws MalformedURLException {
        URL u = new URL(url);
        QName qName = new QName(serviceHost, serviceName);
        Service service = Service.create(u, qName);
        messageQueue = service.getPort(MessageQueue.class);
    }

    public void receiveEnvelope(int queueTag) throws InvalidProtocolBufferException {
        Envelope e = messageQueue.getBlocking(queueTag);
        EvalMessage.Request request = EvalMessage.Request.parseFrom(e.getMsg().getMsg());
        evaluatorTag = request.getEvaluatorId();
        arg1 = request.getArg(0);
        arg2 = request.getArg(1);
        operation = request.getOp();
        seq = request.getSeq();
        ticket = e.getTicketId();
    }

    public void send() {
        EvalMessage.Reply reply = EvalMessage.Reply.newBuilder()
                .setRes(result)
                .setSeq(seq)
                .build();
        Message message = new Message();
        message.setMsg(reply.toByteArray());
        messageQueue.put(evaluatorTag, message);
        messageQueue.ack(ticket);
        System.out.println(result);
    }

    public abstract void proceed();

    public double getArg1() {
        return arg1;
    }

    public double getArg2() {
        return arg2;
    }

    public EvalMessage.Request.Operation getOperation() {
        return operation;
    }

    public void setResult(double r) {
        result = r;
    }

}
