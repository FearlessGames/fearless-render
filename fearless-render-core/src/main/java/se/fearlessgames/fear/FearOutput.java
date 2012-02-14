package se.fearlessgames.fear;

public interface FearOutput {
	void flush();

	void add(GlCommand command);
}
