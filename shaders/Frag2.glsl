#version 430
//IN VARIABLES
in vec3 vNormal, vLightDir, vVertPos, vHalfVec;
in vec4 shadow_coord;
in vec2 tc;
in vec3 coord;

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
uniform float d;
uniform vec3 camera_loc;

layout (binding=0) uniform sampler2DShadow shadowTex;
layout (binding=1)  uniform sampler2D s;
//END UNIFORMS

//OUT VARIABLES
out vec4 fragColor;
//END OUR VARIABLES

void main(void){
    vec4 fog = vec4(0.7, 0.8, 0.9, 1.0);
    float fogStart = 0.0;
    float fogEnd = 20000.0;
    float fogDensity = 0.0025;
    float dist = length(coord.xyz - camera_loc);
    float fogFactor= 1.0 -exp(-pow(dist*fogDensity,3));
	//float fogFactor = clamp(((fogEnd-dist)/(fogEnd-fogStart)), 0.0, 1.0);



	vec3 L = normalize(vLightDir);
	vec3 N = normalize(vNormal);
	vec3 V = normalize(-vVertPos);
	vec3 H = normalize(vHalfVec);

	float inShadow = textureProj(shadowTex, shadow_coord);

	fragColor = globalAmbient * material.ambient
				+ light.ambient * material.ambient;

	if (inShadow != 0.0)
	{	fragColor += light.diffuse * material.diffuse * max(dot(L,N),0.0)
				+ light.specular * material.specular
				* pow(max(dot(H,N),0.0),material.shininess*3.0);
	}

    fragColor = fragColor * 0.5 + (  mix( texture(s,tc),fog,fogFactor)) * 0.5 ;

}
