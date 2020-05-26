#version 130

 uniform mat4 cam_combined;
 attribute vec4 position;

 out vec2 world_cord;

 void main() {
     world_cord = vec2(position.x, position.y);
     gl_Position = cam_combined * position;
 }