package se.fearlessgames.fear;

import org.lwjgl.LWJGLException;

public interface FearOutput {
	void render(FearScene scene) throws LWJGLException;
}
