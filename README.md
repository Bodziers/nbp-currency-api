## Simple REST api to calculate exchange rates

Application which allows to view exchange rates acquired through rest call from NBP for chosen day. Rates from table A, all available currencies. 

Additional future is currency calculation which allows to calculate given amount from one currency to another on exchange rate from chosen day. 

Currency table is separate from currency calculation. 

Application has after build swagger api documentation on address:

    http://localhost:8080/swagger-ui/index.html

and fronted on main build localhost address.
    
    http://localhost:8080/

Application build with:

    java 17,
    maven,
    lombok,
    swagger,
    angular.


Endpoints:
 ------------------------

GET
