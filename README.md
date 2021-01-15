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
