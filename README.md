NOTE : 1. SpringData Part is working in progress : same spring project but data are from db (using embedded db for now)
       2. Add spring data mock tests
          (Spring data Mock test references links :
     
         https://reflectoring.io/spring-boot-data-jpa-test/
         https://www.baeldung.com/spring-jpa-test-in-memory-database
         https://www.baeldung.com/spring-boot-testing 
         https://www.baeldung.com/spring-data-rest-relationships



<<<<<<< HEAD
#  wsproject1 : sample springboot application
Minimal [Spring Boot](http://projects.spring.io/spring-boot/) sample app.

Reference Links :
 
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
