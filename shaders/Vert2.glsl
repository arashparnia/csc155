#version 430

//IN VARIABLES
layout (location=0) in vec3 vertPos;
layout (location=1) in vec3 vertNormal;
layout (location=2) in vec2 texPos;


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

uniform vec4 globalAmbient;
uniform PositionalLight light;
uniform Material material;
uniform mat4 mv_matrix;
uniform mat4 proj_matrix;
uniform mat4 normalMat;
uniform mat4 shadowMVP;
layout (binding=0) uniform sampler2DShadow shadowTex;
layout (binding=1) uniform sampler2D s;
//END UNIFORMS

//OUT VARIABLES
out vec3 vNormal, vLightDir, vVertPos, vHalfVec;
out vec4 shadow_coord;
out vec2 tc;

void main(void){
 tc = texPos;
	if (gl_InstanceID > 0){
            int x = (gl_InstanceID & 63)*4;
            int y = 0;
            int z = (gl_InstanceID >> 6)*4;
            vec3 pos = vertPos + vec3(x,y,z);
        //output the vertex position to the rasterizer for interpolation
        vVertPos = (mv_matrix * vec4(pos,1.0)).xyz;

        //get a vector from the vertex to the light and output it to the rasterizer for interpolation
        vLightDir = light.position - vVertPos;

        //get a vertex normal vector in eye space and output it to the rasterizer for interpolation
        vNormal = (normalMat * vec4(vertNormal,1.0)).xyz;

        // calculate the half vector (L+V)
        vHalfVec = (vLightDir-vVertPos).xyz;

        shadow_coord = shadowMVP * vec4(pos,1.0);

        gl_Position = proj_matrix * mv_matrix * vec4(pos,1.0);
	}else {
	    //output the vertex position to the rasterizer for interpolation
    	vVertPos = (mv_matrix * vec4(vertPos,1.0)).xyz;

    	//get a vector from the vertex to the light and output it to the rasterizer for interpolation
    	vLightDir = light.position - vVertPos;

    	//get a vertex normal vector in eye space and output it to the rasterizer for interpolation
    	vNormal = (normalMat * vec4(vertNormal,1.0)).xyz;

    	// calculate the half vector (L+V)
    	vHalfVec = (vLightDir-vVertPos).xyz;

    	shadow_coord = shadowMVP * vec4(vertPos,1.0);

    	gl_Position = proj_matrix * mv_matrix * vec4(vertPos,1.0);
	}
}
