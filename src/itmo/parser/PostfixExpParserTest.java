package itmo.parser;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import itmo.dreamq.DreamQueueService;
import itmo.dreamq.MessageQueue;
import itmo.evaluator.EvalMessage;
import itmo.mq.Envelope;
import itmo.mq.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.Random;


public class PostfixExpParserTest {

	public static void main(String[] args) throws IOException, ParseException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        DreamQueueService service = new DreamQueueService();
        MessageQueue messageQueue = service.getDreamQueuePort();
        int submitterTag = -1;
        Random rng = new Random();
        while (true){
            submitterTag = rng.nextInt();
            if (submitterTag < -2 || submitterTag >= 0){
                if (messageQueue.createQueue(submitterTag)){
                    break;
                }
            }
        }
        int time = 0;
		while (true) {
			String exp = br.readLine();
			PostfixExpParser pep = new PostfixExpParser(exp);
			while (pep.hasNextExps()) {
				PostfixExp pe = pep.getPostfixExp();
				
				EvalMessage.Request request = EvalMessage.Request.newBuilder()
						.setEvaluatorId(submitterTag)
						.setOp(pe.getOperation())
						.addArg(pe.getArg1())
						.addArg(pe.getArg2())
                        .setSeq(time)
						.build();
				
                Message m = new Message();
                m.setMsg(request.toByteArray());
				messageQueue.put(-1 - pe.getOperation().getNumber() / 2, m);

                double res = 0;
                boolean got = false;
                while (!got) {
                    Envelope e = messageQueue.getBlocking(submitterTag);
                    EvalMessage.Reply reply = EvalMessage.Reply.parseFrom(e.getMsg().getMsg());
                    if (reply.getSeq() != time){
                        continue;
                    }

                    res = reply.getRes();
                    got = true;
                }

				if (pep.hasNextExps())
					pep.putResult(res);
				else
					System.out.println(res);
                ++time;
			}
		}
	}

}
