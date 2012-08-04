package se.fearlessgames.fear.example;

import com.google.common.base.Predicate;
import se.fearlessgames.fear.camera.MatrixBasedCamera;
import se.fearlessgames.fear.input.*;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;

public class FirstPersonController {
	private final InputHandler inputHandler;
	private final MatrixBasedCamera camera;

	private float distance = 0.01f;
	public static final double MOUSE_ROTATE_SPEED = .0005;

	public FirstPersonController(InputHandler inputHandler, MatrixBasedCamera camera) {
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
				if (mouse.getDx() != 0 || mouse.getDy() != 0) {
					if (!firstPing) {
						rotateCamera(mouse.getDx(), mouse.getDy());
					} else {
						firstPing = false;
					}
				}
			}
		};

		inputHandler.addTrigger(new InputTrigger(moveAction, MousePredicates.MOUSE_MOVED_CONDITION));

	}

	private void rotateCamera(int mouseDeltaX, int mouseDeltaY) {
		camera.setOrientation(camera.getOrientation().multiply(Quaternion.fromAngleAxis(new Vector3(mouseDeltaY * MOUSE_ROTATE_SPEED, -mouseDeltaX * MOUSE_ROTATE_SPEED, 0))));
	}


	private void moveCamera(KeyboardState keyboardState) {
		double x = 0;
		double z = 0;

		if (keyboardState.isDown(Key.W)) {
			z = +0.1;
		}

		if (keyboardState.isDown(Key.S)) {
			z = -0.1;
		}

		if (keyboardState.isDown(Key.A)) {
			x = -0.1;
		}

		if (keyboardState.isDown(Key.D)) {
			x = +0.1;
		}


		if (keyboardState.isDown(Key.UP)) {
			rotateCamera(0, 10);
		}

		if (keyboardState.isDown(Key.DOWN)) {
			rotateCamera(0, -10);
		}

		if (keyboardState.isDown(Key.LEFT)) {
			rotateCamera(-10, 0);
		}

		if (keyboardState.isDown(Key.RIGHT)) {
			rotateCamera(10, 0);
		}

		Vector3 move = camera.getOrientation().invert().applyTo(new Vector3(x, 0, z));
		camera.translate(move);
	}
}
