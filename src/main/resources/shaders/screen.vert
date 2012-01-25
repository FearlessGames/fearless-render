uniform vec3 pos;
uniform vec3 rot;
varying vec4 vertColor;

void main(){
	 mat4x4 position=mat4x4(1.0);
	 position[3].x=pos.x;
	 position[3].y=pos.y;
	 position[3].z=pos.z;
	 mat4x4 heading=mat4x4(1.0);
	 heading[0][0]=cos(rot.y);
	 heading[0][2]=-(sin(rot.y));
	 heading[2][0]=sin(rot.y);
	 heading[2][2]=cos(rot.y);
	 mat4x4 pitch=mat4x4(1.0);
	 pitch[1][1]=cos(rot.x);
	 pitch[1][2]=sin(rot.x);
	 pitch[2][1]=-(sin(rot.x));
	 pitch[2][2]=cos(rot.x);
	 mat4x4 roll=mat4x4(1.0);
	 roll[0][0]=cos(rot.z);
	 roll[0][1]=sin(rot.z);
	 roll[1][0]=-(sin(rot.z));
	 roll[1][1]=cos(rot.z);

    gl_Position= gl_ModelViewProjectionMatrix*position*heading*pitch*roll*gl_Vertex;
    vertColor = vec4(0.6 , 0.3 , (gl_Position.z - 10) / 10.0, 1.0);
}