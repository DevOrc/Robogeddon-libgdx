#version 130

uniform mat4 cam_combined;
attribute vec4 position;

void main() {
    gl_Position = cam_combined * position;
}