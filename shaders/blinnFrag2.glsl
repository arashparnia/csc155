#version 430
//IN VARIABLES
in vec3 vNormal, vLightDir, vVertPos, vHalfVec;
in vec4 shadow_coord;
in vec2 tc;
in vec2 tes_out;

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
out vec4 fragColor;
//END OUR VARIABLES

void main(void){

vec3 L = normalize(varyingLightDir);

	// get the normal from the normal map
	vec3 N = texture2D(tex_normal,tes_out).rgb * 2.0 - 1.0;

	vec3 V = normalize(-varyingVertPos);
	vec3 R = normalize(reflect(-L, N));
	float cosTheta = dot(L,N);
	float cosPhi = dot(V,R);

	color = 0.5 *
				( globalAmbient * material.ambient  +  light.ambient * material.ambient
				+ light.diffuse * material.diffuse * max(cosTheta,0.0)
				+ light.specular * material.specular * pow(max(cosPhi,0.0), material.shininess)
				) +
			0.5 *
				( texture2D(s, tes_out)
				);



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

	 fragColor = fragColor * 0.8  +  texture2D(s,tc) * 0.2 ;
}
