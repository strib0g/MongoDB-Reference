package mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.ValidationOptions;

public class Main {

	public static void main(String[] args) {
		
		//1. Basic MongoDB connection
		MongoManager.init("testdb");
		MongoCollection<Document> coll = MongoManager.getCollection("test");
		
		//The following 4 objects are instance objects created by me
		//They are not part of the mongo library
		//On instantiation they take the collection they will perform operations upon
		//as a parameter
		MongoCreator creator = new MongoCreator(coll);
		MongoFinder finder = new MongoFinder(coll);
		MongoDeleter deleter = new MongoDeleter(coll);
		MongoUpdater updater = new MongoUpdater(coll);
		
		//2. Inserting one document
		Document doc = new Document("_id", 1)
				.append("name", "Filip");
		
		creator.insert(doc);
		
		//3. Inserting many documents
		List<Document> list = new ArrayList<Document>();
		for(int i = 2; i < 10; i++) {
			list.add(new Document("_id", i)
			.append("name", "Filip's clone"));
		}
		creator.insert(list);
		
		//4. Bulk insert
		list = new ArrayList<Document>();
		for(int i = 10; i < 20; i++) {
			list.add(new Document("_id", i)
			.append("name", "Bulk clone"));
		}
		
		creator.bulkInsert(list);
		
		//5. Finding one document
		System.out.println(finder.findAll().first());

		//6. Finding many documents
		FindIterable<Document> result = finder.findAll();
		finder.printMany(result);
		
		//7. Finding with conditions(min. 2)
		finder.printMany(finder.findWithFilter(Filters.eq("name", "Filip")));
		
		finder.printMany(finder.findWithFilter(Filters.gt("_id", 15)));
		
		//8. Finding with nested conditions
		finder.printMany(finder.findWithFilter(Filters.and
				(
						Filters.lt("_id", 20), 
						Filters.gt("_id", 10)
						)
				));
		
		//9. Finding with sorting
		finder.printMany(finder.sort(finder.findAll()));
		
		finder.printMany(finder.sort(finder.findAll(), Sorts.descending("_id")));
		
		//10. Aggregation
		finder.printAggregation("name", "Bulk clone", "name");
		
		//11. Projection
		finder.printMany(finder.project(finder.findAll(), Projections.fields(
										Projections.include("name"),
										Projections.excludeId())));
		
		
		//12. Updating one record
		updater.updateOne(Filters.eq("name", "Filip"), Updates.set("updated", false));
		
		updater.updateOne(Filters.eq("name", "Filip"), Updates.combine(Updates.set("updated", true), Updates.currentDate("lastModified")));
		
		//13. Updating many records
		updater.updateMany(Filters.gt("_id", 15), Updates.set("updated", true));
		
		//14. Delete 1 record
		deleter.deleteOne(Filters.eq("name", "Filip"));

		//15. Delete many records
		deleter.deleteMany(Filters.gt("_id", 18));
		
		//Closes connection to database
		MongoManager.close();
	}
	
	//Static class that handles connection to a MongoDB server
	//Also handles operations done on collections
	public static class MongoManager {

		private static String dbName;
		private static MongoClient client;
		private static MongoDatabase database;
		private static MongoCredential credential;
		private static MongoClientSettings settings;
		
		
		//Initializes a database connection without any settings
		public static void init(String newDbName){
			dbName = newDbName;

			client = MongoClients.create();
			database = client.getDatabase(dbName);
		}
		
		
		//Initializes database connection with added credentials for specific database
		//Note: password must be stored in a char array
		public void init(String userName, String newDbName, char[] pwd) {
			dbName = newDbName;
			
			//Credential object passed to settings
			credential = MongoCredential.createCredential(userName, dbName, pwd);
			
			settings = MongoClientSettings.builder()	//Creates a settings builder
					.credential(credential)				//Adds our credentials object to it
					.build();							//and returns a built MongoSettings object
					
			client = MongoClients.create(settings);
			database = client.getDatabase(dbName);
		}
		
		//Creates a collection with specified name
		public static void createCollection(String name) {
			database.createCollection(name);
		}
		
		//Creates a collection with specified name and one specified obligatory field
		public static void createValidatedCollection(String name, String field) {
			ValidationOptions options = new ValidationOptions().validator(
					Filters.exists(field));
			
			database.createCollection(name, new CreateCollectionOptions().validationOptions(options));
		}
		
		//Returns a collection with specified name
		//To be used when initializing all instance objects
		public static MongoCollection<Document> getCollection(String collName){
			return database.getCollection(collName);
		}
		
		//Drops specified collection
		public static void dropCollection(String collName) {
			MongoCollection<Document> coll = database.getCollection(collName);
			coll.drop();
		}
		
		//Closes connection with client
		//Always run at the end of system lifecycle!
		public static void close() {
			client.close();
		}
	}
}
