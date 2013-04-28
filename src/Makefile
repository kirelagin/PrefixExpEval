PROTOJAR := /usr/share/protobuf/lib/protobuf.jar

FILES := $(shell find . -name '*.java')
CLASSES := $(FILES:%.java=%.class)

WS_CLASSES := itmo/mq/Message.class itmo/mq/ObjectFactory.class itmo/mq/package-info.class itmo/mq/Envelope.class itmo/dreamq/MessageQueue.class itmo/dreamq/DreamQueueService.class


.PHONY : build clean

build : $(CLASSES)

%.class : %.java
	javac -cp $(PROTOJAR):. $<

$(CLASSES) : $(FILES) $(WS_CLASSES) itmo/evaluator/EvalMessage.java

$(WS_CLASSES) :
	wsimport 'http://localhost:9999/mq?wsdl'

itmo/evaluator/EvalMessage.java : EvalMessage.proto
	protoc --java_out=. $<


clean :
	find . -name '*.class' -exec rm -rf {} \;
	rm -f itmo/evaluator/EvalMessage.java
