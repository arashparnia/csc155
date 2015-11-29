#version 430
//IN VARIABLES
layout (vertices = 4) out;
in vec2 tc[];

//LOCAL VARIABLES
struct PositionalLight
{	vec4 ambient, diffuse, specular;
	vec3 position;
};
struct Material
{	vec4 ambient, diffuse, specular;
	float shininess;
};

//UNIFORMS
uniform mat4 mvp;
uniform vec4 globalAmbient;
uniform PositionalLight light;
uniform Material material;
uniform mat4 mv_matrix;
uniform mat4 proj_matrix;
uniform mat4 normalMat;
uniform mat4 shadowMVP;
layout (binding=0) uniform sampler2DShadow shadowTex;
layout (binding=1)  uniform sampler2D s;
layout (binding=2) uniform sampler2D tex_height;
layout (binding=3) uniform sampler2D tex_normal;
//END UNIFORMS

//OUT VARIABLES
out vec2 tcs_out[];

void main(void)
{	int TL = 28;
	if (gl_InvocationID == 0)
	{ gl_TessLevelOuter[0] = TL;
	  gl_TessLevelOuter[2] = TL;
	  gl_TessLevelOuter[1] = TL;
	  gl_TessLevelOuter[3] = TL;
	  gl_TessLevelInner[0] = TL;
	  gl_TessLevelInner[1] = TL;
	}
	
	tcs_out[gl_InvocationID] = tc[gl_InvocationID];
	gl_out[gl_InvocationID].gl_Position = gl_in[gl_InvocationID].gl_Position;
}