package se.fearlessgames.fear.example;

import com.google.common.base.Predicate;
import se.fearlessgames.fear.input.*;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;

public class FirstPersonController {
	private final InputHandler inputHandler;
	private final CameraTest camera;

	private float distance = 0.01f;

	public FirstPersonController(InputHandler inputHandler, CameraTest camera) {
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
						rotateCamera(-mouse.getDx(), -mouse.getDy());
					} else {
						firstPing = false;
					}
				}
			}
		};

		inputHandler.addTrigger(new InputTrigger(moveAction, MousePredicates.MOUSE_MOVED_CONDITION));

	}

	private void rotateCamera(int dx, int dy) {
		double mouseRotateSpeed = .05;
		Quaternion quaternion = Quaternion.fromAngleAxis(new Vector3(dx * mouseRotateSpeed, dy * mouseRotateSpeed, 0));
		camera.setOrientation(quaternion);
	}


	private void moveCamera(KeyboardState keyboardState) {
		double x = 0;
		double z = 0;

		if (keyboardState.isDown(Key.W)) {
			z = -0.1;
		}

		if (keyboardState.isDown(Key.S)) {
			z = +0.1;
		}

		if (keyboardState.isDown(Key.A)) {
			x = -0.1;
		}

		if (keyboardState.isDown(Key.D)) {
			x = +0.1;
		}


		if (keyboardState.isDown(Key.UP)) {
			z++;

		}

		if (keyboardState.isDown(Key.DOWN)) {
			z--;
		}

		Vector3 move = camera.getOrientation().applyTo(new Vector3(x, 0, z));
		camera.translate(move);
	}
}
