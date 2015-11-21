#version 430

layout (location=0) in vec3 vertPos;

uniform mat4 shadowMVP;

void main(void){
	 if (gl_InstanceID > 0){
         int x = (gl_InstanceID & 7)*4;
         int y = 0;
         int z = (gl_InstanceID >> 3)*4;
        vec3 pos = vertPos + vec3(x,y,z);
	gl_Position = shadowMVP * vec4(pos,1.0);
	}else{
	gl_Position = shadowMVP * vec4(vertPos,1.0);
	}
}
