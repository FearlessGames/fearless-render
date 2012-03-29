package se.fearlessgames.fear.gl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DebuggingFearLwjgl implements InvocationHandler {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final FearGl fearGl;
	private int lastError;
	private final boolean throwException;

	private DebuggingFearLwjgl(boolean throwException) {
		this.throwException = throwException;
		fearGl = new FearLwjgl();
	}


	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (method.getName().equals("glGetError")) {
			int error = lastError;
			lastError = 0;
			return error;
		}

		Object returnValue = method.invoke(fearGl, args);

		int error = fearGl.glGetError();

		if (error != 0) {
			String message = "OpenGL error " + error;
			RuntimeException runtimeException = new RuntimeException(message);
			logger.error(message, runtimeException);
			if (throwException) {
				throw runtimeException;
			}
		}

		lastError = error;
		return returnValue;
	}

	public static FearGl create(boolean throwException) {
		return (FearGl) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class<?>[]{FearGl.class}, new DebuggingFearLwjgl(throwException));
	}
}
