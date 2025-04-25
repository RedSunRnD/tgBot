package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "notification_task")
public class NotificationTask {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "chat_id", nullable = false)
    private long chatId;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "is_done", nullable = false)
    private boolean isDone;

    @Column(name = "text_message", nullable = false)
    private String textMessage;

    public NotificationTask() {

    }

    public NotificationTask(long id, long chatId, LocalDateTime dateTime, boolean isDone, String textMessage) {
        this.chatId = chatId;
        this.dateTime = dateTime;
        this.isDone = isDone;
        this.textMessage = textMessage;
    }

    public long getId() {
        return id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return id == that.id && chatId == that.chatId && isDone == that.isDone && Objects.equals(dateTime, that.dateTime) && Objects.equals(textMessage, that.textMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, dateTime, isDone, textMessage);
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", dateTime=" + dateTime +
                ", isDone=" + isDone +
                ", textMessage='" + textMessage + '\'' +
                '}';
    }
}
