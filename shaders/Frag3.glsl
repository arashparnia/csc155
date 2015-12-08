#version 430

in vec2 tes_out;
in vec3 coord;
in vec4 shadow_coord;


out vec4 color;
uniform mat4 mvp;
uniform vec3 camera_loc;
layout (binding = 0) uniform sampler2DShadow shadowTex;
layout (binding = 1) uniform sampler2D tex_color;
layout (binding = 2) uniform sampler2D tex_height;
layout (binding = 3) uniform sampler2D tex_normal;

/* ---- for lighting ---- */
in vec3 varyingVertPos;
in vec3 varyingLightDir;
struct PositionalLight
{	vec4 ambient; vec4 diffuse; vec4 specular; vec3 position; };
struct Material
{	vec4 ambient; vec4 diffuse; vec4 specular; float shininess; };
uniform vec4 globalAmbient;
uniform PositionalLight light;
uniform Material material;
uniform mat4 mv_matrix;
uniform mat4 proj_matrix;
uniform mat4 normalMat;
uniform mat4 shadowMVP;

/* ---------------------- */

void main(void){
    vec4 fog = vec4(0.7, 0.8, 0.9, 1.0);
       float fogStart = 5.0;
       float fogEnd = 20000.0;
       float fogDensity = 0.0015;
       float dist = length(coord.xyz - camera_loc);
       float fogFactor= 1.0 -exp(-pow(dist*fogDensity,3));
   	//float fogFactor = clamp(((fogEnd-dist)/(fogEnd-fogStart)), 0.0, 1.0);


	vec3 L = normalize(varyingLightDir);
	// get the normal from the normal map
	vec3 N = texture2D(tex_normal,tes_out).rgb * 2.0 - 1.0;
	vec3 V = normalize(-varyingVertPos);
	vec3 R = normalize(reflect(-L, N));
	float cosTheta = dot(L,N);
	float cosPhi = dot(V,R);

    float inShadow = textureProj(shadowTex, shadow_coord);
    //inShadow = 1;
    if (inShadow != 0.0){
	color = ( globalAmbient * material.ambient  +  light.ambient * material.ambient
				+ light.diffuse * material.diffuse * max(cosTheta,0.0)
				+ light.specular * material.specular * pow(max(cosPhi,0.0), material.shininess));
	}

    color = color * 0.5 + ( mix( texture2D(tex_color, tes_out),fog,fogFactor)) * 0.5 ;
   //color = color * 0.9 +  texture(tex_color, tes_out) * 0.1 ;


}