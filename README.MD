<h1>user CRUD</h1>
<h4>Task for Aloteq</h4>
<p> API for create/read/update/delete users <br>
Used Spring boot, H2 relation database, Hibernate Data JPA, JUnit

<h4>To access database console</h4>

<code>localhost:port/h2-console</code><br>
Console credentials:<br>


<code>Saved Settings: Generic H2(Embedded)</code><br>
<code>Driver Class: org.h2.Driver</code><br>
<code>JDBC URL: jdbc:h2:file:./src/main/java/com/example/usercrud/Database/Database </code></br>
<code>username: admin  <br></code>
<code>password: password <br></code>

<h5>To enter test db:</h5> <br> <code>JDBC URL: jdbc:h2:file:./src/test/java/com/example/usercrud/Database/testDatabase <br> </code>

</p>

<h3>How to run docker image:</h3>
<ul>
    <li>docker run -p 8888:8080 holymolly/usercrud</li>
</ul>

<h3>API</h3>
<ul>
    <code>POST</code> Adding new user <br>
    <p>If you're running on docker, port should be <code>8888</code><br>
    otherwise <code>8080</code>
    </p>

    localhost:port/add

    Request Body:
    {
        "firstName": "Akzhol",
        "lastName": "Issaliyev",
        "middleName": "Askeruly",
        "email": "akzholbeast@gmail.com",
        "gender": 0
    }

    firstName, gender, email is required fields
    gender's value: [0-10]

<code>GET</code> Getting user by id/email

    localhost:port/user/id/{id}

    localhost:port/user/email/{email}
<code>DELETE</code> Deleting user by id/email

    localhost:port/user/id/{id}

    localhost:port/user/email/{email}
<code>PUT</code> Updating user by id/email

    localhost:port/user/id/{id}

    localhost:port/user/email/{email}

    Request Body:
    {
        "firstName": "newName",
        "lastName": "newLastName",
        "middleName": "newMiddleName",
        "country": "someCountry",
        "email": "newEmail@gmail.com",
        "gender": 1
    }
<code>GET</code> Get all users filtered by country

    localhost:port/users/{country}

<code>GET</code> Get one page of all users filtered by country

    localhost:port/users/{country}/{pageNumber}/{pageSize}

<code>GET</code> Get all users

    localhost:port/users/

<code>GET</code> Get one page of all users

    localhost:port/users/{pageNumber}/{pageSize}

</ul>
