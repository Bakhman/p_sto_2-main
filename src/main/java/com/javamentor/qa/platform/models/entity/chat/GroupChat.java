package com.javamentor.qa.platform.models.entity.chat;

import com.javamentor.qa.platform.exception.ApiRequestException;
import com.javamentor.qa.platform.models.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "group_chat")
public class GroupChat{
    @Id
    private Long id;

    @Column(name = "image_link")
    private String imageLink;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @MapsId
    private Chat chat = new Chat(ChatType.GROUP);

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinTable(name = "user_entity",
//            joinColumns = @JoinColumn(name = "id"))
    private User author;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "groupchat_has_users",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;

    public GroupChat(Set<User> users, String title){
        this.users = users;
        chat.setTitle(title);
    }

    @PrePersist
    private void prePersistFunction() {
        checkConstraints();
    }

    @PreUpdate
    private void preUpdateFunction() {
        checkConstraints();
    }

    private void checkConstraints() {
        if (this.chat.getChatType() != ChatType.GROUP) {
            throw new ApiRequestException("У экземпляра Chat, связанного с GroupChat, " +
                    "поле chatType должно принимать значение ChatType.GROUP");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupChat groupChat = (GroupChat) o;
        return Objects.equals(id, groupChat.id) &&
                Objects.equals(chat, groupChat.chat) &&
                Objects.equals(users, groupChat.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chat, users);
    }

    @Override
    public String toString() {
        return "GroupChat{" +
                "id=" + id +
                ", chat=" + chat +
                ", users=" + users +
                '}';
    }
}