package se.fearlessgames.fear.collada;

public class ColladaException extends RuntimeException {
	public ColladaException(final String message, final Object source) {
		super(ColladaException.createMessage(message, source));
	}

	public ColladaException(final String msg, final Object source, final Throwable cause) {
		super(ColladaException.createMessage(msg, source), cause);
	}

	private static String createMessage(final String message, final Object source) {
		return "Collada problem for source: " + (source != null ? source.toString() : "null" + ": " + message);
	}
}
