#version 430

in vec2 tc;

out vec4 fragColor;

layout (binding=0) uniform sampler3D s;
uniform mat4 mv_matrix;
uniform mat4 proj_matrix;
uniform float d;

void main(void)
{
    //vec3 r = reflect(normalize(-vVertPos), normalize(vNormal));
	//fragColor = texture(t,r);


	vec3 tc3 = vec3(tc.x, tc.y, d);
	fragColor = texture(s,tc3);
	//if (fragColor == 0) discard;
}
