To do : 

1.  testcase updates : currently most testcases are still failing now 
2.  changing Member as entity

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

api health link : 
   http://localhost:8083/actuator/health

api doc link :  
   http://localhost:8083/swagger-ui-custom.html

---

links : 

   https://zetcode.com/springboot/mongodbreactive/


