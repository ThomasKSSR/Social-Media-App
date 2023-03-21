package Domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FriendReqDto {
    private String friendName;

    private String reqSentDate;

    public String getReqSentDate() {
        return reqSentDate;
    }

    public void setReqSentDate(LocalDateTime reqSentDate) {
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.reqSentDate = reqSentDate.format(formatter);
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {

        this.friendName = friendName;
    }

    public FriendReqDto(String friendName,LocalDateTime reqSendLocalDate) {
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.reqSentDate = reqSendLocalDate.format(formatter);
        this.friendName = friendName;
    }
}
