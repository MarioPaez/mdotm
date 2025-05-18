# 🐾 MongoDB Setup (Docker)

This project also supports a **MongoDB** backend. In the `/docker` folder, you will find a `docker-compose.yml` file that launches a MongoDB container.

## 🛠️ How to Run with MongoDB

1. Make sure you are on the correct branch that uses MongoDB (after3h-mongoImplementation).
2. Start the Docker container by running the following command from the `/docker` directory:

```bash
   docker compose up
 ```

3. Set the Spring profile to mongo in application.yml file.
4. Run the application using:
```bash
mvn spring-boot:run
```
Or start it directly from IntelliJ IDEA, just like before.

## 🧪 Verifying Data in MongoDB
To verify that MongoDB is populated without launching any GUI tool, you can access the running container with:
```bash
docker exec -it petdb-mongo mongosh
```
Then run the following commands inside the Mongo shell:

```javascript
show dbs             // Lists available databases
use petdb            // Switch to the application's database
show collections     // Shows collections (tables)
db.pets.find().pretty()   // Displays the documents in the 'pets' collection
// or
db.pets.find().count()    // Count the number of documents
```

## 🔄 Switching Databases Made Easy
As we can see, switching from a relational database to a non-relational one is straightforward thanks to our modular architecture.
By isolating responsibilities and using clean abstractions, we can easily swap out components like the data access layer with minimal effort.
