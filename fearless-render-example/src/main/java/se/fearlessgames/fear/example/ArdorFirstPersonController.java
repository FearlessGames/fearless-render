package se.fearlessgames.fear.example;

import com.google.common.base.Predicate;
import se.fearlessgames.fear.camera.VectorCamera;
import se.fearlessgames.fear.input.*;
import se.fearlessgames.fear.math.Matrix4;
import se.fearlessgames.fear.math.Vector3;

public class ArdorFirstPersonController {
	private final InputHandler inputHandler;
	private final VectorCamera camera;

	protected final Vector3 upAxis = Vector3.UNIT_X;
	protected double mouseRotateSpeed = .005;
	protected double moveSpeed = 50;
	protected double keyRotateSpeed = 2.25;


	public ArdorFirstPersonController(InputHandler inputHandler, VectorCamera camera) {
		this.inputHandler = inputHandler;
		this.camera = camera;
	}

	public void setupKeyboard() {

		final Predicate<InputState> keysHeld = new Predicate<InputState>() {
			Key[] keys = new Key[]{Key.W, Key.A, Key.S, Key.D, Key.LEFT, Key.RIGHT, Key.UP, Key.DOWN};

			public boolean apply(final InputState states) {
				for (final Key k : keys) {
					if (states.getKeyboardState().isDown(k)) {
						return true;
					}
				}
				return false;
			}
		};

		TriggerAction triggerAction = new TriggerAction() {
			@Override
			public void perform(InputState inputState) {
				moveCamera(inputState.getKeyboardState());
			}
		};

		inputHandler.addTrigger(new InputTrigger(triggerAction, keysHeld));
	}

	public void setupMouseTriggers() {
		TriggerAction moveAction = new TriggerAction() {
			private boolean firstPing = true;

			@Override
			public void perform(InputState inputState) {
				final MouseState mouse = inputState.getMouseState();
				if (mouse.getDX() != 0 || mouse.getDY() != 0) {
					if (!firstPing) {
						rotateCamera(-mouse.getDX(), -mouse.getDY());
					} else {
						firstPing = false;
					}
				}
			}
		};

		inputHandler.addTrigger(new InputTrigger(moveAction, MousePredicates.MOUSE_MOVED_CONDITION));

	}

	protected void moveCamera(final KeyboardState kb) {
		double tpf = 1d;
		// MOVEMENT
		int moveFB = 0, strafeLR = 0;
		if (kb.isDown(Key.W)) {
			moveFB += 1;
		}
		if (kb.isDown(Key.S)) {
			moveFB -= 1;
		}
		if (kb.isDown(Key.A)) {
			strafeLR += 1;
		}
		if (kb.isDown(Key.D)) {
			strafeLR -= 1;
		}

		if (moveFB != 0 || strafeLR != 0) {
			Vector3 location = Vector3.ZERO;
			if (moveFB == 1) {
				location = location.add(camera.getDirection());
			} else if (moveFB == -1) {
				location = location.subtract(camera.getDirection());
			}
			if (strafeLR == 1) {
				location = location.add(camera.getLeft());
			} else if (strafeLR == -1) {
				location = location.subtract(camera.getLeft());
			}

			camera.setLocation(location.normalize().multiply(moveSpeed * tpf).add(camera.getLocation()));
		}

		// ROTATION
		int rotX = 0, rotY = 0;

		if (kb.isDown(Key.UP)) {
			rotY -= 1;
		}

		if (kb.isDown(Key.DOWN)) {
			rotY += 1;
		}

		if (kb.isDown(Key.LEFT)) {
			rotX += 1;
		}

		if (kb.isDown(Key.RIGHT)) {
			rotX -= 1;
		}

		if (rotX != 0 || rotY != 0) {
			rotateCamera(rotX * (keyRotateSpeed / mouseRotateSpeed) * tpf, rotY * (keyRotateSpeed / mouseRotateSpeed) * tpf);
		}
	}

	protected void rotateCamera(final double dx, final double dy) {
		if (dx != 0) {
			applyDx(dx);
		}

		if (dy != 0) {
			applyDY(dy);
		}

		if (dx != 0 || dy != 0) {
			camera.normalize();
		}
	}

	private void applyDx(final double dx) {
		Matrix4 workerMatrix = Matrix4.fromAngleAxis(mouseRotateSpeed * dx, upAxis);

		camera.setLeft(workerMatrix.multiply(camera.getLeft()));
		camera.setDirection(workerMatrix.multiply(camera.getDirection()));
		camera.setUp(workerMatrix.multiply(camera.getUp()));

	}

	private void applyDY(final double dy) {
		Matrix4 workerMatrix = Matrix4.fromAngleAxis(mouseRotateSpeed * dy, camera.getLeft());

		camera.setDirection(workerMatrix.multiply(camera.getDirection()));
		camera.setUp(workerMatrix.multiply(camera.getUp()));
	}


}
