package mongo;
import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.bson.Document;
import org.bson.conversions.Bson;

public class MongoCreator {

	private MongoCollection<Document> collection;
	
	public MongoCreator(MongoCollection<Document> collection) {
		this.collection = collection;
	}
	
	public void insert(Document doc) {
		collection.insertOne(doc);
	}
	
	public void insert(List<Document> docs) {
		collection.insertMany(docs);
	}
	
	public void replace(Bson query, Document document) {
		collection.replaceOne(query, document);
	}
	
	public void bulkInsert(List<Document> docs) {
		
		List<InsertOneModel<Document>> list = new ArrayList<InsertOneModel<Document>>();
		
		for(int i = 0; i < docs.size(); i++) {
			list.add(new InsertOneModel<>(docs.get(i)));
		}
		
		collection.bulkWrite(list);
	}
}
