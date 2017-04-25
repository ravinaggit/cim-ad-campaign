cim-ad-campaign: REST API
===========================
cim-ad-campaign -REST API service provider
## Main Feature
Service end points to manage partner ad campaign

---
## Technology Stack
Java 1.8, Spring Boot with Embedded Tomcat, Spring REST, Spring JPA, H2, Rest Assured, Junit, Mockito

---
## Installation
git clone https://github.com/ravinaggit/cim-ad-campaign.git
cd cim-ad-campaign
mvn clean
mvn package
cd target
java -jar cim-ad-campaign-0.0.1-SNAPSHOT.jar

---
## Testing Strategies
Unit Tests -Junit Mockito
Functional Tests -Rest Assured

## Integration Testing
mvn integration-test -P integration

## Service End Points
{
	"partner":<Unique String>,
	"duration":<Seconds>,
	"content":<Content String>
}
Ex: {"partner":"TRAVEL","duration":"100","content":"Life on the road!"}

http://<host>:8080/ -Get all campaigns

http://<host>:8080/ad -Create new campaign

http://<host>:8080/ad/<partnerId> -Get, Put, Delete End points

