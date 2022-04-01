## Simple REST api to calculate exchange rates

Application which allows to view exchange rates acquired through rest call from NBP table A for chosen day, all available currencies. 

Additional future is currency calculation which allows to calculate given amount from one currency to another with exchange rate from chosen day. 

Currency table is separate from currency calculation. 

Application build swagger api documentation on address:

    http://localhost:8080/swagger-ui/index.html

and fronted on main build localhost address.
    
    http://localhost:8080/

Application build with:

    java 17,
    maven,
    lombok,
    swagger,
    angular.

Front end is separated and build output files are in resources static folder, so it does not need to be build on application start.
In case of change in angular, frontend needs to be build with 'npm build' command. Output directory is set to resources/static folder.

Endpoints:
 ------------------------

GET table with exchange rates for given date
  
    /api/table/{date}

GET saves table with exchange rates for given date in resources static folder as csv file comma separated

    /api/save/{date}

GET calculate amount from one currency to another
    /api/calculate/{date}/{fromCurrencyCode}/{toCurrencyCode}/{amount}

