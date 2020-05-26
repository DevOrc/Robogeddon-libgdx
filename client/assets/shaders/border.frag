#version 130

uniform vec4 team_color;
out vec4 frag_color;

void main()
{
    frag_color = team_color;
}