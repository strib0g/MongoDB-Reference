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

public class MongoDeleter {
	private MongoCollection<Document> collection;
	
	public MongoDeleter(MongoCollection<Document> collection) {
		this.collection = collection;
	}
	
	public void deleteOne(Bson query) {
		collection.deleteOne(query);
	}
	
	public void deleteMany(Bson query) {
		collection.deleteMany(query);
	}
}
