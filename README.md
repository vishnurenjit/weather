Please update the follwing DB information in application properties

- spring.data.mongodb.host= 
- spring.data.mongodb.port= 
- spring.data.mongodb.database=

----------------------------------



Sign up new user
----------------
API: (URL: /auth/signup, METHOD: POST)

sample URL: http://localhost:8080/auth/signup

payload sample: 
{
    "username":"userName",
    "password":"password",
    "firstName":"firstName",
    "lastName":"lastName"
}


Get LOGIN token
---------------
API: (URL: /auth/login, METHOD: POST)

sample URL: http://localhost:8080/auth/login

payload sample:
{
    "userName":"userName",
    "password":"password"
}



Get wether data
---------------
API: (URL: /weather/<zip>, METHOD: GET)

sample URL: http://localhost:8080/weather/20001


Get history of logged in user
-----------------------------
API: (URL: /weather/history, METHOD: GET)


Delete history of logged in user
--------------------------------
API: (URL: /weather/history, METHOD: DELETE)


activate and deactivate user
----------------------------
API: (URL: /user/active/<activation status>, METHOD: PATCH), activation status is either tru or false

sample URL: http://localhost:8080/user/active/true
