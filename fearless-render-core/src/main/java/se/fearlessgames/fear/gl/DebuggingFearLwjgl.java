package se.fearlessgames.fear.gl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DebuggingFearLwjgl implements InvocationHandler {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final FearGl fearGl;

	private DebuggingFearLwjgl() {
		fearGl = new FearLwjgl();
	}


	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		Object returnValue = method.invoke(fearGl, args);

		int error = fearGl.glGetError();

		if (error != 0) {
			String message = "OpenGL error " + error;
			throw new RuntimeException(message);

		}

		return returnValue;
	}

	public static FearGl create() {
		return (FearGl) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class<?>[]{FearGl.class}, new DebuggingFearLwjgl());
	}
}
