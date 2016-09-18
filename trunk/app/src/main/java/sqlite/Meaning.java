package sqlite;

/**
 * Created by User on 2016/2/22.
 */
public class Meaning {
    int sub_id;
    int id;
    String word;
    String part_of_speech;
    String EngChiTra;
    String EngEng;
    String synonym;
    String antonym;
    String sentence;
    String EngChiSpl;
    String EngJp;
    String EngKorea;

    public Meaning(){


    }

    public Meaning(int sub_id, int id, String word, String part_of_speech, String engChiTra, String engEng, String synonym, String antonym, String sentence, String engChiSpl, String engJp, String engKorea) {
        this.sub_id = sub_id;
        this.id = id;
        this.word = word;
        this.part_of_speech = part_of_speech;
        this.EngChiTra = engChiTra;
        this.EngEng = engEng;
        this.synonym = synonym;
        this.antonym = antonym;
        this.sentence = sentence;
        this.EngChiSpl = engChiSpl;
        this.EngJp = engJp;
        this.EngKorea = engKorea;
    }

    public int getSub_id() { return sub_id; }

    public int getId() { return id; }

    public String getWord() {
        return word;
    }

    public String getPart_of_speech() {
        return part_of_speech;
    }

    public String getEngChiTra() {
        return EngChiTra;
    }

    public String getEngEng() {
        return EngEng;
    }

    public String getSynonym() {
        return synonym;
    }

    public String getAntonym() {
        return antonym;
    }

    public String getSentence() { return sentence; }

    public String getEngChiSpl() { return EngChiSpl; }

    public String getEngJp() {
        return EngJp;
    }

    public String getEngKorea() {
        return EngKorea;
    }

    public void setSub_id(int sub_id) {
        this.sub_id = sub_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setPart_of_speech(String part_of_speech) {
        this.part_of_speech = part_of_speech;
    }

    public void setEngChiTra(String engChiTra) {
        EngChiTra = engChiTra;
    }

    public void setEngEng(String engEng) {
        EngEng = engEng;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }

    public void setAntonym(String antonym) {
        this.antonym = antonym;
    }

    public void setSentence(String sentence) { this.sentence = sentence; }

    public void setEngChiSpl(String engChiSpl) {
        EngChiSpl = engChiSpl;
    }

    public void setEngJp(String engJp) {
        EngJp = engJp;
    }

    public void setEngKorea(String engKorea) {
        EngKorea = engKorea;
    }
}
