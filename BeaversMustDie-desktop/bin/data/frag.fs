
#ifdef GL_ES
	precision mediump float; 
#endif

varying vec4 col; 

void main() 
{ 
	gl_FragColor = col;
}
