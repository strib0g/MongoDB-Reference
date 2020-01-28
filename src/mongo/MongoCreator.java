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

//Handles insertion, replacement and bulk operations in a database
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
	
	//Finds document via query and replaces it with provided document
	public void replace(Bson query, Document document) {
		collection.replaceOne(query, document);
	}
	
	//Inserts a list of docs using bulkWrite
	public void bulkInsert(List<Document> docs) {
		//Creates a list of InsertOneModels that contain documents
		//InsertOneModels can be replaced by UpdateOneModels, DeleteOneModels
		//All of these extend the WriteModel object, so one could use it
		//To create a list w/ different models
		List<InsertOneModel<Document>> list = new ArrayList<InsertOneModel<Document>>();
		
		//For loop wraps every document in a InsertOneModel and adds it to the list
		for(int i = 0; i < docs.size(); i++) {
			list.add(new InsertOneModel<>(docs.get(i)));
		}
		
		collection.bulkWrite(list);
	}
}
