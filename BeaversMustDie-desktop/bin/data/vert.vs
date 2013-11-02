
#ifdef GL_ES
	precision mediump float; 
#endif

uniform float size;
uniform float time;
attribute vec4 a_position; 
uniform mat4 u_worldView; 
varying vec4 col; 

#define PI 3.141592653590
#define PI2 6.28318530718

void main() 
{
	vec4 realPos = vec4(a_position); //vec4(a_position.x + 0.1, a_position.y + 0.1, a_position.zw);
	float l = length(realPos);
	
	if (l < 1.1) { //mid point -> dark blue
	
		col = COLOR_INNER; // vec4(0.0, 0.0, 1.0, 1.0);
		
	} else { //else, move around, color cyan
		float ang = atan(a_position.y, a_position.x) + PI;
		
		float mv = -0.2 + cos(25.0*ang+time)*0.0025;
		mv *= size;
		realPos.x += cos(ang-PI)*mv;
		realPos.y += sin(ang-PI)*mv;
		col = COLOR_OUTER; // vec4(0.0, 1.0, 1.0, 1.0);
	}
	
	
	gl_Position =  u_worldView * realPos; 
}      