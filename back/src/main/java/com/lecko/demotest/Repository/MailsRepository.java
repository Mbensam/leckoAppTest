package com.lecko.demotest.Repository;

import com.lecko.demotest.Model.LocalCustomMail;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface MailsRepository  extends MongoRepository<LocalCustomMail, String> {


}


