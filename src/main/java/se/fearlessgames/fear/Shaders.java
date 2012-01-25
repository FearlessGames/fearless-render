package se.fearlessgames.fear;

import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileReader;

public class Shaders {

    public int createProgram() {
        int shaderProgram = GL20.glCreateProgram();
        if (shaderProgram == 0) {
            throw new RuntimeException("Failed to create shader program");
        }
        return shaderProgram;
    }

    public int loadAndCompileVertexShader(String filename) {
        int vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);

        //if created, convert the vertex shaderProgram code to a String
        if (vertexShader == 0) {
            throw new RuntimeException("Failed to create vertex shader");
        }

        String vertexCode = getFileContent(filename);
        GL20.glShaderSource(vertexShader, vertexCode);
        GL20.glCompileShader(vertexShader);

        printLogInfo(vertexShader);

        return vertexShader;

    }

    public int loadAndCompileFragmentShader(String filename) {

        int fragShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        if (fragShader == 0) {
            throw new RuntimeException("Failed to create fragment shader");
        }

        String fragCode = getFileContent(filename);

        GL20.glShaderSource(fragShader, fragCode);
        GL20.glCompileShader(fragShader);

        printLogInfo(fragShader);

        return fragShader;
    }

    public void attachToProgram(int shaderProgram, int vertexShader, int fragmentShader) {
        if (vertexShader != 0) {
            GL20.glAttachShader(shaderProgram, vertexShader);
        }

        if (fragmentShader != 0) {
            GL20.glAttachShader(shaderProgram, fragmentShader);
        }

        GL20.glLinkProgram(shaderProgram);
        GL20.glValidateProgram(shaderProgram);
        printLogInfo(shaderProgram);
    }


    private String getFileContent(String filename) {
        String line;
        StringBuilder code = new StringBuilder();
        try {

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while ((line = reader.readLine()) != null) {
                code.append(line).append("\n");
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException("Fail reading vertex shading code");
        }

        return code.toString();
    }

    private void printLogInfo(int obj) {
        String out = GL20.glGetShaderInfoLog(obj, 40);
        System.out.println("Info log:\n" + out);
    }


}
