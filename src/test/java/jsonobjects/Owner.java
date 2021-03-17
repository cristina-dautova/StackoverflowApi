package jsonobjects;

public class Owner {

    public int reputation;
    public String user_id;
    public String user_type;
    public String profile_image;
    public String display_name;
    public String link;
    public int accept_rate;

    @Override
    public String toString() {
        return "Owner{" +
                "reputation=" + reputation +
                ", user_id=" + user_id +
                ", user_type='" + user_type + '\'' +
                ", accept_rate=" + accept_rate +
                ", profile_image='" + profile_image + '\'' +
                ", display_name='" + display_name + '\'' +
                ", link='" + link +
                '}';
    }
}
