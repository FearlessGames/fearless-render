package se.fearlessgames.fear;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;

import java.util.List;

public class FearScene {
    private final FearNode root;

    public FearScene(FearNode root) {
        this.root = root;
    }

    public FearNode getRoot() {
        return root;
    }

	public void render(FearOutput output) {
		clear(output);
		renderSkybox(output);
		renderOpaqueObjects(output);
		renderTransparentObjects(output);
	}

	private void renderTransparentObjects(FearOutput output) {
	}

	private void renderOpaqueObjects(FearOutput output) {
        render(output, root);
	}

    private void render(FearOutput output, FearNode node) {
        if (!node.isVisible()) {
            return;
        }
        Vector3D position = node.getPosition();
        Rotation rotation = node.getRotation();

        applyTransform(position, rotation);
        List<FearMesh> meshes = node.getMeshes();
        for (FearMesh mesh : meshes) {
            renderMesh(mesh);
        }
        for (FearNode child : node.getChildNodes()) {
            render(output, child);
        }
        reverseTransform(position, rotation);
    }

    private void renderMesh(FearMesh mesh) {
        Vector3D position = mesh.getPosition();
        Rotation rotation = mesh.getRotation();
        applyTransform(position, rotation);
        // TODO: do GL render stuff here
        reverseTransform(position, rotation);
    }

    private void reverseTransform(Vector3D position, Rotation rotation) {
        // TODO: Do GL stuff here
    }

    private void applyTransform(Vector3D position, Rotation rotation) {
        // TODO: Do GL stuff here
    }

    private void renderSkybox(FearOutput output) {
	}

	private void clear(FearOutput output) {
        // TODO: glClear here
	}
}
