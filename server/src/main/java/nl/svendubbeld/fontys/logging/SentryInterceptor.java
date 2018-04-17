package nl.svendubbeld.fontys.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@SentryLogged
@Interceptor
public class SentryInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SentryInterceptor.class);

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        try {
            return context.proceed();
        } catch (Exception e) {
            logger.error("An uncaught exception had been thrown.", e);
            throw e;
        }
    }
}
