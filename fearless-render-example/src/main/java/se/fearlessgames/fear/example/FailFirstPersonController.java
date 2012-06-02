package se.fearlessgames.fear.example;

import com.google.common.base.Predicate;
import se.fearlessgames.fear.camera.RotationCamera;
import se.fearlessgames.fear.input.*;
import se.fearlessgames.fear.math.Quaternion;
import se.fearlessgames.fear.math.Vector3;

public class FailFirstPersonController {
	private final InputHandler inputHandler;
	private final RotationCamera camera;


	private double yaw = 0;
	private double pitch = 0;
	private float distance = 1;

	public FailFirstPersonController(InputHandler inputHandler, RotationCamera camera) {
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

	private void rotateCamera(int dx, int dy) {
		double mouseRotateSpeed = .005;

		if (dx != 0) {
			yaw += mouseRotateSpeed * dx;
		}

		if (dy != 0) {
			pitch += mouseRotateSpeed * dy;
		}

		if (dx != 0 || dy != 0) {
			camera.setRotation(Quaternion.fromEulerAngles(yaw, 0, pitch).normalize());
		}
	}


	private void moveCamera(KeyboardState keyboardState) {
		double x = camera.getLocation().getX();
		double y = camera.getLocation().getY();
		double z = camera.getLocation().getZ();

		if (keyboardState.isDown(Key.W)) {
			x -= distance * (float) Math.sin(Math.toRadians(yaw));
			z += distance * (float) Math.cos(Math.toRadians(yaw));
		}

		if (keyboardState.isDown(Key.S)) {
			x += distance * (float) Math.sin(Math.toRadians(yaw));
			z -= distance * (float) Math.cos(Math.toRadians(yaw));
		}

		if (keyboardState.isDown(Key.A)) {
			x -= distance * (float) Math.sin(Math.toRadians(yaw - 90));
			z += distance * (float) Math.cos(Math.toRadians(yaw - 90));
		}

		if (keyboardState.isDown(Key.D)) {
			x -= distance * (float) Math.sin(Math.toRadians(yaw + 90));
			z += distance * (float) Math.cos(Math.toRadians(yaw + 90));
		}


		if (keyboardState.isDown(Key.UP)) {
			z++;

		}

		if (keyboardState.isDown(Key.DOWN)) {
			z--;
		}


		camera.setLocation(new Vector3(x, y, z));

	}
}
