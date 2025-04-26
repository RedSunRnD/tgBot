package pro.sky.telegrambot.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_task")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
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

    public NotificationTask(long chatId, LocalDateTime dateTime, String textMessage, boolean isDone) {
        this.chatId = chatId;
        this.dateTime = dateTime;
        this.textMessage = textMessage;
        this.isDone = isDone;
    }
}
