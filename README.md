# luckoAppTest
In this application we fetch data from microsoft graph api, and we store essential mails information from each
user mails into a mongodb database to expose them for internal use.

### Guides
* You should dd --add-opens java.base/java.time=ALL-UNNAMED to Vm options, or from the cli if using the jar, to activate date converting from documents
* Exposed Endpoints are in the main controller **mails/MailsPerMonth** and **mails/all**
