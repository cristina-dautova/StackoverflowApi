package jsonobjects;

import java.util.List;

public class Item {

    public Owner owner;
    public boolean is_accepted;
    public int score;
    public int last_activity_date;
    public int creation_date;
    public int answer_id;
    public int question_id;
    public String content_license;
    public int last_edit_date;

        @Override
    public String toString() {
        return "Item{" +
                "owner=" + owner +
                ", is_accepted=" + is_accepted +
                ", score=" + score +
                ", last_activity_date='" + last_activity_date + '\'' +
                ", creation_date='" + creation_date + '\'' +
                ", answer_id=" + answer_id +
                ", question_id='" + question_id + '\'' +
                ", content_license='" + content_license + '\'' +
                ", last_edit_date='" + last_edit_date +
                '}';
    }
}
