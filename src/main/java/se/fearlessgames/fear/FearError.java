package se.fearlessgames.fear;

public class FearError extends Error {
	public FearError(Throwable cause) {
		super(cause);
	}

	public FearError(String s) {
		super(s);
	}
}
