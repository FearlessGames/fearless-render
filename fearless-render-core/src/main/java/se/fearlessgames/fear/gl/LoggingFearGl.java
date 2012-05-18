package se.fearlessgames.fear.gl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class LoggingFearGl implements InvocationHandler {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final FearGl delegate;

	private LoggingFearGl(FearGl delegate) {
		this.delegate = delegate;
	}


	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		StringBuilder builder = new StringBuilder(method.getName()).append("(");
		if (args != null) {
			for (int i = 0, argsLength = args.length; i < argsLength; i++) {
				builder.append(args[i]);
				if (i < argsLength - 1) {
					builder.append(", ");
				}
			}
		}
		builder.append(")");
		log.debug(builder.toString());
		return method.invoke(delegate, args);
	}

	public static FearGl create(FearGl fearGl) {
		return (FearGl) Proxy.newProxyInstance(fearGl.getClass().getClassLoader(), new Class<?>[]{FearGl.class}, new LoggingFearGl(fearGl));
	}
}
