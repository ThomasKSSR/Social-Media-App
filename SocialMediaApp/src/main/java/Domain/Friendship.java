package Domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Friendship implements HasID<Set<Integer>> {
    private User user1;
    private User user2;

    private LocalDateTime friendsfrom;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(user1, that.user1) && Objects.equals(user2, that.user2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1, user2);
    }

    public Friendship(User user1, User user2,LocalDateTime friendsfrom,String status) {
        this.user1 = user1;
        this.user2 = user2;
        this.friendsfrom = friendsfrom;
        this.status = status;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }


    @Override
    public HashSet<Integer> getID() {
        HashSet<Integer> ids = new HashSet<Integer>();
        ids.add(user1.getID());
        ids.add(user2.getID());
        return ids;
    }

    @Override
    public void setID(Set<Integer> ints) {

    }


    public LocalDateTime getFriendsfrom() {
        return friendsfrom;
    }

    public void setFriendsfrom(LocalDateTime friendsfrom) {
        this.friendsfrom = friendsfrom;
    }
}
