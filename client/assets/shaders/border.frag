#version 130

uniform vec2 border_pos;
uniform vec2 border_size;
uniform vec4 team_color;

in vec2 world_cord;
in vec2 frag_Cord;
out vec4 frag_color;

void main()
{
    //Enable Blending



    vec2 pos = world_cord - border_pos;
    vec2 dist = vec2(0.0, 0.0);

    if(border_size.x / 2.0 < pos.x){
        dist.x = border_size.x - pos.x;
    }else{
        dist.x = pos.x;
    }

    if(border_size.y / 2.0 < pos.y){
        dist.y = border_size.y - pos.y;
    }else{
        dist.y = pos.y;
    }

    dist = dist / border_size * 1.0;

    //Horizontal
    if(border_size.x > border_size.y){
        frag_color = vec4(team_color.x, team_color.y, team_color.z, dist.y);
    }else{
        frag_color = vec4(team_color.x, team_color.y, team_color.z, dist.x);
    }

}