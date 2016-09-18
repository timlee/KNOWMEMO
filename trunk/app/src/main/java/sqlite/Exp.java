package sqlite;

/**
 * Created by User on 2016/3/11.
 */
public class Exp {
    String user_id;
    int word_id;
    int level;
    int position;
    int learned;
    String Last_Learnt_Time;

    public Exp(String user_id, int word_id, int level, int position, int learned, String last_Learnt_Time) {
        this.user_id = user_id;
        this.word_id = word_id;
        this.level = level;
        this.position = position;
        this.learned = learned;
        this.Last_Learnt_Time = last_Learnt_Time;
    }

    public Exp() {

    }

    public String getUser_id() {
        return user_id;
    }

    public int getWord_id() {
        return word_id;
    }

    public int getLevel() {
        return level;
    }

    public int getPosition() {
        return position;
    }

    public int getLearned() {
        return learned;
    }

    public String getLast_Learnt_Time() {
        return Last_Learnt_Time;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setWord_id(int word_id) {
        this.word_id = word_id;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setLearned(int learned) {
        this.learned = learned;
    }

    public void setLast_Learnt_Time(String last_Learnt_Time) {
        Last_Learnt_Time = last_Learnt_Time;
    }
}
