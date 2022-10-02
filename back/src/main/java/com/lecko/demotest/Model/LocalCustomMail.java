package com.lecko.demotest.Model;

import com.mongodb.lang.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
public class LocalCustomMail {
    @Id
    String _id;
    @Nullable
    LocalDateTime receivedDate;
    @Nullable
    LocalDateTime sentDateTime;
    @Nullable
    String from;
    @Nullable
    List<String> toRecipients;
    @Nullable
    String subject;

    public LocalCustomMail(String _id, @Nullable LocalDateTime receivedDate, @Nullable LocalDateTime sentDateTime, @Nullable String from, @Nullable List<String> toRecipients, @Nullable String subject) {
        this._id = _id;
        this.receivedDate = receivedDate;
        this.sentDateTime = sentDateTime;
        this.from = from;
        this.toRecipients = toRecipients;
        this.subject = subject;
    }
}
