package Domain;

public class Msg {
    int id;
    String sentTo;
    String sentBy;
    String content;

    public Msg(String sentTo, String sentBy, String content) {
        this.sentTo = sentTo;
        this.sentBy = sentBy;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSentTo() {
        return sentTo;
    }

    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
