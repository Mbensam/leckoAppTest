package com.lecko.demotest.Service;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.lecko.demotest.Model.LocalCustomMail;
import com.lecko.demotest.Repository.MailsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.Message;
import com.microsoft.graph.models.Recipient;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.MessageCollectionPage;
import com.microsoft.graph.requests.UserCollectionPage;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Service
public class GraphService {
    MailsRepository mailsRepository;

    private ClientSecretCredential _clientSecretCredential;
    private GraphServiceClient<Request> _appClient;
    Logger logger = LoggerFactory.getLogger(GraphService.class);
    ObjectMapper ow = new ObjectMapper();

    public GraphService(MailsRepository mailsRepository) throws Exception {
        this.mailsRepository = mailsRepository;
        ensureGraphForAppOnlyAuth();
        ow.registerModule(new JavaTimeModule());
        initMails();
    }

    private void ensureGraphForAppOnlyAuth() throws Exception {
        final String clientId;
        final String tenantId;
        final String clientSecret;
        final List<String> graphUserScopes;
        final Properties properties = new Properties();

        try {
            properties.load(this.getClass().getResourceAsStream("/credentials.properties"));
            clientId = properties.getProperty("app.clientId");
            tenantId = properties.getProperty("app.tenantId");
            clientSecret = properties.getProperty("app.clientSecret");
            graphUserScopes = Arrays.asList(properties.getProperty("app.graphUserScopes").split(","));
        } catch (IOException e) {
            logger.error("Unable to read configuration. Make sure you have a valid credentials.properties file.");
            return;
        }

        if (_clientSecretCredential == null) {
            _clientSecretCredential = new ClientSecretCredentialBuilder()
                    .clientId(clientId)
                    .tenantId(tenantId)
                    .clientSecret(clientSecret)
                    .build();
        }

        if (_appClient == null) {
            final TokenCredentialAuthProvider authProvider =
                    new TokenCredentialAuthProvider(graphUserScopes, _clientSecretCredential);

            _appClient = GraphServiceClient.builder()
                    .authenticationProvider(authProvider)
                    .buildClient();
        }
    }

    private UserCollectionPage getUsers() {

        return _appClient.users()
                .buildRequest()
                .select("displayName,id,mail")
                .top(999)
                .orderBy("displayName")
                .get();
    }

    private void initMails() throws JsonProcessingException {
        try {
            UserCollectionPage users = getUsers();
            while (!users.getCurrentPage().isEmpty()) {
                for (User user : users.getCurrentPage()) {
                    try {
                      if(user.id !=null){
                        MessageCollectionPage messages = getMessages(user);
                        while (!messages.getCurrentPage().isEmpty()) {
                            for (Message message : messages.getCurrentPage()) {
                                List<String> recipients = new ArrayList<>();
                                if (message.toRecipients != null) {
                                    for (Recipient rec : message.toRecipients) {
                                        if(rec.emailAddress != null )  recipients.add(rec.emailAddress.address);

                                        }
                                    }
                                LocalCustomMail mail = new LocalCustomMail(message.id,
                                        message.receivedDateTime != null ? message.receivedDateTime.toLocalDateTime() : null,
                                        message.sentDateTime != null ? message.sentDateTime.toLocalDateTime() : null,
                                        (message.from != null && message.from.emailAddress != null) ? message.from.emailAddress.address : null,
                                        recipients,
                                        message.subject);

                                mailsRepository.save(mail);
                                }

                            if (messages.getNextPage() != null) {
                                messages = messages.getNextPage().buildRequest().get();
                            } else {
                                break;
                            }
                        }
                      }      
                    } catch (Exception e) {
                        logger.error("Error while loading messages " + e);

                    }
                }
                if (users.getNextPage() != null) {
                    users = users.getNextPage().buildRequest().get();
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting users");
            System.out.println(e.getMessage());
        }
    }

    private MessageCollectionPage getMessages(User user) {

        return _appClient.users(user.id)
                .messages()
                .buildRequest()
                .select("from,isRead,receivedDateTime,subject")
                .top(999)
                .get();
    }

}
