package fr.celestialdreams.api.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.logging.Logger;

@Aspect
@Component
public class LogEndPoints {

    private final Logger logger = Logger.getLogger(LogEndPoints.class.getName());

    @Pointcut("execution(* fr.celestialdreams.api.controller..*(..))")
    public void endpointMethods() {}

    @Before("endpointMethods()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Endpoint called: " + joinPoint.getSignature());
        logger.info("Args: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "endpointMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        logger.info("Endpoint completed: " + joinPoint.getSignature());
        logger.info("Returned: " + result);
    }

    @AfterThrowing(pointcut = "endpointMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
    	System.out.println("toto");
        logger.warning("Exception in: " + joinPoint.getSignature());
        logger.warning("Error: " + ex.getMessage());
    }
}
