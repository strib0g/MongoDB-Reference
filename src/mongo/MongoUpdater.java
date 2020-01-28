package mongo;
import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.bson.Document;
import org.bson.conversions.Bson;

//Handles all update operations on a collection
public class MongoUpdater {
	
	private MongoCollection<Document> collection;
	
	public MongoUpdater(MongoCollection<Document> collection) {
		this.collection = collection;
	}
	
	//Updates the queried records with specified Bson update
	public void updateOne(Bson query, Bson update) {
		collection.updateOne(query, update);
	}
	
	//Same as above, but allows for insertion of an UpdateOptions object
	public void updateOne(Bson query, Bson update, UpdateOptions options) {
		collection.updateOne(query, update, options);
	}
	
	//Same as above, but performed on all records present in the query
	public void updateMany(Bson query, Bson update) {
		collection.updateMany(query, update);
	}
	
	public void updateMany(Bson query, Bson update, UpdateOptions options) {
		collection.updateMany(query, update, options);
	}
	
}