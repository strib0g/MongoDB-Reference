# MongoDB-Reference
A collection of methods and objects used to keep track of basic CRUD operations in MongoDB using Java. Given to students as part of the Database Development and Administration course.
## File structure
The following describes the purpose and content of every file in the system.
### Main
Creates an instance of every other class in the system and runs the most important methods as a demo. Also contains the **MongoManager** class.
#### MongoManager
Handles database connection and operations with collections such as creation and deletion

### MongoFinder
Responsible for normal and filtered queries. Also handles data formatting such as sorting and projection. Contains basic aggregation method. Also contains utility method that prints FindIterable<Document> objects.

### MongoCreator
Responsible for inserting one or many specified records. Also supports inserting using bulkWrite.

### MongoDeleter
Responsible for deleting one or many specified records.

### MongoUpdater
Responsible for updating one or many specified records. Also contains support for updating with options.
