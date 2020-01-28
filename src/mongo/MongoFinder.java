package mongo;
import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.bson.Document;
import org.bson.conversions.Bson;

public class MongoFinder {

	private MongoCollection<Document> collection;
	
	public MongoFinder(MongoCollection<Document> collection) {
		this.collection = collection;
	}
	
	public FindIterable<Document> findAll(){
		FindIterable<Document> result = collection.find();
		return result;
	}
	
	public FindIterable<Document> findWithFilter(Bson filter){
		FindIterable<Document> result = collection.find(filter);
		return result;
	}
	
	public FindIterable<Document> findWithFilter(Document filter){
		FindIterable<Document> result = collection.find(filter);
		return result;
	}
	
	public FindIterable<Document> sort(FindIterable<Document> unsorted){
		FindIterable<Document> result = unsorted.sort(null);
		return result;
	}
	
	public FindIterable<Document> sort(FindIterable<Document> unsorted, Bson criteria){
		FindIterable<Document> result = unsorted.sort(criteria);
		return result;
	}
	
	public FindIterable<Document> project(FindIterable<Document> unsorted, Bson projection){
		FindIterable<Document> result = unsorted.projection(projection);
		return result;
	}

	public void printAggregation(String matchField, String matchValue, String groupField) {
		AggregateIterable<Document> result = collection.aggregate(
				Arrays.asList(
						Aggregates.match(Filters.eq(matchField, matchValue)),
						Aggregates.group(("$" + groupField), Accumulators.sum("count", 1))));
		
		printMany(result);
	}
	
	public void printMany(MongoIterable<Document> docs) {
		Consumer<Document> printBlock = new Consumer<Document>() {
		     @Override
		     public void accept(final Document document) {
		         System.out.println(document.toJson());
		     }
		};
		
		docs.forEach(printBlock);
	}
}