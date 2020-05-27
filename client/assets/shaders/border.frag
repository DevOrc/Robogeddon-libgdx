#version 130

uniform vec2 border_pos;
uniform vec2 border_size;
uniform vec4 team_color;

in vec2 world_cord;
in vec2 frag_Cord;
out vec4 frag_color;

void main()
{
    vec2 pos = world_cord - border_pos;
    float factor = 1.0;
    float dist = 0.0;

    if(border_size.x > border_size.y){ //Horizontal
        if(border_size.y / 2.0 < pos.y){
            dist = border_size.y - pos.y;
        }else{
            dist = pos.y;
        }

        frag_color = vec4(team_color.x, team_color.y, team_color.z, dist / border_size.y * factor);
    }else if(border_size.y > border_size.x){ //Vertical
        if(border_size.x / 2.0 < pos.x){
            dist = border_size.x - pos.x;
        }else{
            dist = pos.x;
        }

        frag_color = vec4(team_color.x, team_color.y, team_color.z, dist / border_size.x * factor);
    }else{ //Square (I.e. chunk debug color)
        frag_color = vec4(team_color.x, team_color.y, team_color.z, 0.8);
    }
}