package org.example.instr.bytecode.core;

import com.thoughtworks.xstream.XStream;
import org.glowroot.agent.plugin.api.*;
import org.glowroot.agent.plugin.api.weaving.*;


public class DogAspect {
    @Pointcut(className = "org.example.instr.bytecode.core.Dog",
            methodName = "getDog",
            methodParameterTypes = {"java.lang.String"},
            timerName = "Timer - name")
    public static class TargetMethodAdvice {
        private static final TimerName timer = Agent.getTimerName(TargetMethodAdvice.class);
        private static final String transactionType = "Target";
        private static final XStream xStream = new XStream();

        public static void logInvocation(Object someObjectInProfile) {
            System.out.println(xStream.toXML(someObjectInProfile));
        }

        @OnBefore
        public static TraceEntry onBefore(OptionalThreadContext context,
                                          @BindReceiver Object receivingObject,
                                          @BindParameterArray Object parameterObjects,
                                          @BindMethodName String methodName) {
            logInvocation(receivingObject);
            logInvocation(parameterObjects);

            MessageSupplier messageSupplier = MessageSupplier.create(
                    "className: {}, methodName: {}",
                    TargetMethodAdvice.class.getAnnotation(Pointcut.class).className(),
                    methodName
            );
            return context.startTransaction(transactionType, methodName, messageSupplier, timer, OptionalThreadContext.AlreadyInTransactionBehavior.CAPTURE_NEW_TRANSACTION);
        }

        @OnReturn
        public static void onReturn(@BindReturn Object returnedObject,
                                    @BindTraveler TraceEntry traceEntry) throws Exception {
            logInvocation(returnedObject);
            traceEntry.end();
        }

    }
}