api doc UI link : 

start springboot app : 
 mvn spring-boot:run

run all tests :  
 mvn test

run test suite  : 
  mvn -Dtest=<suite name>  test
  example : 
  mvn -Dtest=Wproject1IntegrationTest  test

run testcase : 
    
  mvn -Dtest=<suite name>#<testcase name>  test

api doc link :  
   http://localhost:8081/swagger-ui-custom.html

Useful Info and Links : 

OpenJDK download sites : 

  https://adoptopenjdk.net/releases.html

LATER : 

(Configuration,  Transactional, Security)

   https://www.baeldung.com/spring-jpa-test-in-memory-database
   https://auth0.com/blog/integrating-spring-data-jpa-postgresql-liquibase/

Reactive web : 

   https://www.callicoder.com/spring-5-reactive-webclient-webtestclient-examples/
   https://www.baeldung.com/spring-5-webclient


Spring Tutorials : 
https://spring.io/guides/tutorials/rest/  

MISC : 

 https://www.baeldung.com/spring-boot-yaml-vs-properties

spring tests  : 
  Springboot integration test with TestRest Template
     https://reflectoring.io/spring-boot-test/
     https://attacomsian.com/blog/spring-boot-resttemplate-get-request-parameters-headers
     https://www.codota.com/code/java/methods/org.springframework.boot.test.web.client.TestRestTemplate/put
     https://github.com/pppurple/spring_examples/blob/master/testresttemplate-example/src/test/java/com/example/contoroller/PeopleControllerTest.java

     https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/html/boot-features-testing.html
     https://rieckpil.de/difference-between-mock-and-mockbean-spring-boot-applications/



  Spring Unit tests : 

  https://www.baeldung.com/spring-boot-testing

  https://javadoc.io/static/org.mockito/mockito-core/3.6.28/org/mockito/Mockito.html

  https://www.logicbig.com/tutorials/unit-testing/mockito/stubbing-exceptions.html

  https://www.baeldung.com/spring-boot-testing

  https://tanzu.vmware.com/application-modernization-recipes/testing/spring-boot-testing-best-practices

  https://medium.com/personal-capital-tech-blog/testing-best-practices-for-java-spring-apps-762e9fde39ec

  https://github.com/mockito/mockito/wiki

  https://howtodoinjava.com/spring-boot2/testing/rest-controller-unit-test-example/

  https://www.javaguides.net/2018/09/spring-data-jpa-repository-testing-using-spring-boot-datajpatest.html

  https://hellokoding.com/spring-boot-test-service-layer-example-with-mockitos-mock-and-injectmock/
 
  https://asbnotebook.com/2020/05/28/spring-boot-rest-controller-junit-test-example/

  https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.entity-persistence

  https://developer.okta.com/blog/2019/03/28/test-java-spring-boot-junit5

  Exception Handler : 
  https://dzone.com/articles/best-practice-for-exception-handling-in-spring-boo

  Logging : 
     https://howtodoinjava.com/spring-boot2/logging/spring-boot-logging-configurations/

  openapi3 and swagger
  https://www.baeldung.com/spring-rest-openapi-documentation

  https://dzone.com/articles/doing-more-with-springdoc-openapi

  Request Validation : 
    https://mkyong.com/spring-boot/spring-rest-validation-example/
    https://www.baeldung.com/global-error-handler-in-a-spring-rest-api
    https://hellokoding.com/spring-boot-rest-api-validation-tutorial-with-example/

  JPA repository : 

  https://stackoverflow.com/questions/54255858/how-to-return-a-select-query-from-jpa-repository-with-foreign-key-type

  https://stackoverflow.com/questions/13154818/how-to-define-a-jpa-repository-query-with-a-join

  http://zetcode.com/springboot/datajpanamedquery/

  https://www.baeldung.com/jpa-entity-graph

  https://stackoverflow.com/questions/30288464/when-should-i-use-joincolumn-or-jointable-with-jpa

  https://springframework.guru/spring-data-jpa-query/

  https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#projections

  WebFlux : Reactive web services : 
     https://spring.io/guides/gs/reactive-rest-service/

  1. https://github.com/spring-projects/sts4/issues/395


NOTE : 1. SpringData Part is working in progress : same spring project but data are from db (using embedded db for now)

          (Spring data Mock test references links :
        https://github.com/spring-guides/gs-testing-web/blob/master/complete/src/test/java/com/example/testingweb/WebMockTest.java 
         https://reflectoring.io/spring-boot-data-jpa-test/
         https://www.baeldung.com/spring-jpa-test-in-memory-database
         https://www.baeldung.com/spring-boot-testing 
         https://www.baeldung.com/spring-data-rest-relationships



<<<<<<< HEAD
#  wsproject1 : sample springboot application
Minimal [Spring Boot](http://projects.spring.io/spring-boot/) sample app.

Reference Links :
 JPA :  
 1. https://howtodoinjava.com/hibernate/hibernate-jpa-cascade-types/


 https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using-boot-structuring-your-code
 
 openapi links : 
 https://www.baeldung.com/spring-rest-openapi-documentation
 https://springdoc.org/
 https://github.com/springdoc/springdoc-openapi-maven-plugin
 
 some other sample link just for reference : 
     https://raw.githubusercontent.com/codecentric/springboot-sample-app/master/README.md
     
  properties reference links : 
  https://docs.spring.io/spring-boot/docs/2.1.13.RELEASE/reference/html/boot-features-external-config.html
  
  test references : (Different levels of tests)
  hamcrest matchers : 
  http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matchers.html#hasSize
  
  Spring web layer tests : 
  https://spring.io/guides/gs/testing-web/
  
  https://reflectoring.io/spring-boot-web-controller-test/
  
  https://www.baeldung.com/mockito-void-methods
   
  maven doc link : 
  https://docs.spring.io/spring-boot/docs/1.5.9.RELEASE/maven-plugin/examples/repackage-disable-attach.html
  https://github.com/eugenp/tutorials/blob/master/maven-modules/maven-integration-test/pom.xml
  
  https://www.baeldung.com/spring-tests
 
  SpringBoot and Data Persistence : 
  
  https://developer.okta.com/blog/2019/02/01/spring-hibernate-guide
  
  https://stackoverflow.com/questions/25439813/difference-between-mapkey-mapkeycolumn-and-mapkeyjoincolumn-in-jpa-and-hiber
  https://softwarecave.org/2018/03/05/mapping-a-map-of-simple-types-in-jpa-using-elementcollection/
  https://www.baeldung.com/jpa-joincolumn-vs-mappedby
  
  https://www.baeldung.com/jpa-many-to-many
  
  https://www.baeldung.com/the-persistence-layer-with-spring-and-jpa
  
  https://www.baeldung.com/transaction-configuration-with-jpa-and-spring
  
  https://www.baeldung.com/configuration-properties-in-spring-boot
  
  http://zetcode.com/springboot/repository/
  
  https://stackoverflow.com/questions/35201546/spring-data-jpa-multiple-repositories-without-many-classes
  
  https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-adding-custom-methods-into-all-repositories/
  https://www.baeldung.com/spring-data-jpa-method-in-all-repositories
  
  https://github.com/gkatzioura/egkatzioura.wordpress.com/blob/master/SpringDataJPAIntegration/src/main/java/com/gkatzioura/springdata/jpa/persistence/entity/Employee.java
  
  https://www.baeldung.com/hibernate-named-query
  
  https://www.baeldung.com/jpa-queries
  https://thorben-janssen.com/map-association-java-util-map/
  https://stackoverflow.com/questions/33236664/difference-between-query-native-query-named-query-and-typed-query
  
  http://zetcode.com/springboot/datajpaquery/

Locale/MessageSource : 

  https://github.com/eugenp/tutorials/blob/master/spring-boot-modules/spring-boot-mvc/src/main/java/com/baeldung/internationalization/config/MvcConfig.java
  https://www.baeldung.com/spring-custom-validation-message-source

Misc : 

  https://stackoverflow.com/questions/38457074/spring-mvc-should-my-domain-classes-implement-serializable-for-over-the-wire-t

  spring profiles : 

  https://stackoverflow.com/questions/31038250/setting-active-profile-and-config-location-from-command-line-in-spring-boot

  mvn spring-boot:run -Dspring.profiles.active=it
  mvn spring-boot:run -Dspring.profiles.active=it
  
## Requirements

For building and running the application you need:

- [JDK 1.8](open jdk 8)
- [Maven 3](https://maven.apache.org)

## Running the application locally

either in ide or run from cli maven (mvnw)
```shell
./mvnw spring-boot:run
```

##running test : 

./mvnw clean test
./mvnw  -Dit.test=Wproject1RestAssuredIT verify

## Deploying the application


## Copyright

=======
# springbootRest
spring boot rest api and associated test samples
