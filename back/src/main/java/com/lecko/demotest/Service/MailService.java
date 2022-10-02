package com.lecko.demotest.Service;

import com.lecko.demotest.Model.LocalCustomMail;
import com.lecko.demotest.Repository.MailsRepository;
import com.google.gson.Gson;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class MailService {
    @Autowired
    MailsRepository mailsRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public String countMailsPerMonth() {
        Aggregation agg = newAggregation(
                project().andExpression("month(receivedDate)").as("month")
                        .andExpression("year(receivedDate)").as("year"),
                group("month", "year").count().as("sum"),
                        sort(Sort.Direction.ASC, "month"));
        AggregationResults<Document> results = mongoTemplate.aggregate(agg,
                "localCustomMail", Document.class);
        return new Gson().toJson(results.getMappedResults());
    }

    public String getAllMails() {
        List<LocalCustomMail> list = mailsRepository.findAll();
        return mailsRepository.findAll().toString();
    }
}
