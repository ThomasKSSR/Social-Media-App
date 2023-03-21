package Domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FriendshipDto {

    private String friendName;
    private String friendsfrom;
    private String status;

    public FriendshipDto(String friendName, LocalDateTime friendsfrom, String status) {
        this.friendName = friendName;
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.friendsfrom = friendsfrom.format(formatter);
        this.status = status;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendsfrom() {
        return friendsfrom;
    }

    public void setFriendsfrom(LocalDateTime friendsfrom) {
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.friendsfrom = friendsfrom.format(formatter);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
