package se.fearlessgames.fear.shader;

import se.fearlessgames.fear.gl.DataType;
import se.fearlessgames.fear.gl.FearGl;

class AttributePointer implements Attribute {
	private final FearGl fearGl;
	private final int pointer;

	public AttributePointer(FearGl fearGl, int pointer) {
		this.fearGl = fearGl;
		this.pointer = pointer;
	}

	@Override
	public void setAttribute(int size, int stride, int offset) {
		fearGl.glEnableVertexAttribArray(pointer);
		fearGl.glVertexAttribPointer(pointer, size, DataType.GL_FLOAT, true, stride, offset);
	}
}
