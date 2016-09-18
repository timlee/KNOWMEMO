package sqlite;

/**
 * Created by User on 2016/2/3.
 */
public class Words {
    int id;
    String word;
    String phonetics;
    int GEPTlow;
    int GEPTmiddle;
    int GEPTmiddlehigh;
    int GEPThigh;
    int TOEFL;
    int TOEIC;
    int IELTS;

    public Words(int id, String word, String phonetics, int GEPTlow, int GEPTmiddle, int GEPTmiddlehigh, int GEPThigh, int TOEFL, int TOEIC, int IELTS) {
        this.id = id;
        this.word = word;
        this.phonetics = phonetics;
        this.GEPTlow = GEPTlow;
        this.GEPTmiddle = GEPTmiddle;
        this.GEPTmiddlehigh = GEPTmiddlehigh;
        this.GEPThigh = GEPThigh;
        this.TOEFL = TOEFL;
        this.TOEIC = TOEIC;
        this.IELTS = IELTS;
    }

    public Words() {

    }

    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getPhonetics() {
        return phonetics;
    }

    public int getGEPTlow() {
        return GEPTlow;
    }

    public int getGEPTmiddle() {
        return GEPTmiddle;
    }

    public int getGEPTmiddlehigh() {
        return GEPTmiddlehigh;
    }

    public int getGEPThigh() {
        return GEPThigh;
    }

    public int getTOEFL() {
        return TOEFL;
    }

    public int getTOEIC() {
        return TOEIC;
    }

    public int getIELTS() {
        return IELTS;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setPhonetics(String phonetics) {
        this.phonetics = phonetics;
    }

    public void setGEPTlow(int GEPTlow) { this.GEPTlow = GEPTlow; }

    public void setGEPTmiddle(int GEPTmiddle) {
        this.GEPTmiddle = GEPTmiddle;
    }

    public void setGEPTmiddlehigh(int GEPTmiddlehigh) {
        this.GEPTmiddlehigh = GEPTmiddlehigh;
    }

    public void setGEPThigh(int GEPThigh) {
        this.GEPThigh = GEPThigh;
    }

    public void setTOEFL(int TOEFL) {
        this.TOEFL = TOEFL;
    }

    public void setTOEIC(int TOEIC) {
        this.TOEIC = TOEIC;
    }

    public void setIELTS(int IELTS) {
        this.IELTS = IELTS;
    }
}
