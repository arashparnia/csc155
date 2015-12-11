#version 430

//  based on a similar shader in "Graphics Shaders"
//  by Bailey & Cunningham.

layout (lines_adjacency) in;
layout (line_strip, max_vertices=256) out;

void main (void)
{	int numSteps = 20;
	float dt = 1.0 / float(numSteps);
	float t = 0.0;
	for (int i=0; i<=numSteps; i++)
    {	float B0 = (1.0 - t) * (1.0 - t) * (1.0 - t);
		float B1 = 3.0 * t * (1.0 - t) * (1.0 - t);
		float B2 = 3.0 * t * t * (1.0 - t);
		float B3 = t * t * t;
		vec4 pt =  B0 * gl_in[0].gl_Position
				+ B1 * gl_in[1].gl_Position
				+ B2 * gl_in[2].gl_Position
				+ B3 * gl_in[3].gl_Position;
		gl_Position = pt;
		t = t + dt;
		EmitVertex();
	}
}