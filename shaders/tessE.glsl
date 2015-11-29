#version 430

//IN VARIABLES
layout (quads, equal_spacing,ccw) in;
in vec2 tcs_out[];
//END IN VARIABLES

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
out vec3 vVertPos;
out vec3 vLightDir;
out vec2 tes_out;
//END OUT VARIABLES



void main (void)
{	vec2 tc1 = mix(tcs_out[0], tcs_out[1], gl_TessCoord.x);
	vec2 tc2 = mix(tcs_out[2], tcs_out[3], gl_TessCoord.x);
	vec2 tc = mix(tc2, tc1, gl_TessCoord.y);

	// map the tessellated grid onto the texture rectangle:
	vec4 p1 = mix(gl_in[0].gl_Position, gl_in[1].gl_Position, gl_TessCoord.x);
	vec4 p2 = mix(gl_in[2].gl_Position, gl_in[3].gl_Position, gl_TessCoord.x);
	vec4 p = mix(p2, p1, gl_TessCoord.y);
	
	// add the height from the height map to the vertex:
	p.y = p.y + (texture2D(tex_height, tc).r)/2.0;
	
	gl_Position = mvp * p;
	tes_out = tc;
	
	/*--- light stuff----*/
	//vVertPos = (mv_matrix * p).xyz;
	//vLightDir = light.position - vVertPos;
}