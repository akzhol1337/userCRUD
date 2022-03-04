<h1>user CRUD</h1>
<h4>Task for Aloteq</h4>
<p> REST API for create/read/update/delete users <br>
Used Spring boot, H2 relation database, Hibernate Data JPA, JUnit

</p>

<h3>How to run docker image:</h3>

<ul>
    <li>docker pull holymolly/usercrud</li>
    <li>docker run holymolly/usercrud</li>
</ul>

<h3>API</h3>
<ul>
    <code>POST</code> Adding new user <br>

    localhost:8080/add
    
    Request Body:
    {
        "firstName": "Akzhol",
        "lastName": "Issaliyev",
        "middleName": "Askeruly",
        "email": "akzholbeast@gmail.com",
        "gender": 0
    }

    [lastName, middleName] is not necessary
    
<code>GET</code> Getting user by id/email
    
    localhost:8080/id/{id}

    localhost:8080/email/{email}
<code>DELETE</code> Deleting user by id/email

    localhost:8080/id/{id}

    localhost:8080/email/{email}
<code>PUT</code> Updating user by id/email
    
    localhost:8080/id/{id}

    localhost:8080/email/{email}
    
    Request Body:
    {
        "firstName": "newName",
        "lastName": "newLastName",
        "middleName": "newMiddleName",
        "email": "newEmail@gmail.com",
        "gender": 1
    }

    you can set only fields that needs to be updated
    

</ul>



