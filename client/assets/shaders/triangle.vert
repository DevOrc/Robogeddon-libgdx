#version 130

attribute vec3 position;

out vec3 color;

void main() {
    gl_Position = vec4(position, 1.0);
    color = vec3(position.x + 0.5, 0.0, position.y + .5);
}