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

//Handles all queries and displaying of data
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
	
	//Optionally uses a document to filter query results
	public FindIterable<Document> findWithFilter(Document filter){
		FindIterable<Document> result = collection.find(filter);
		return result;
	}
	
	//Sorts a FindIterable object returned from a .find() query
	//Takes no criteria, sorts by id ascending by default
	public FindIterable<Document> sort(FindIterable<Document> unsorted){
		FindIterable<Document> result = unsorted.sort(null);
		return result;
	}
	
	public FindIterable<Document> sort(FindIterable<Document> unsorted, Bson criteria){
		FindIterable<Document> result = unsorted.sort(criteria);
		return result;
	}
	
	//Projects a FindIterable object returned from a .find() query
	public FindIterable<Document> project(FindIterable<Document> unsorted, Bson projection){
		FindIterable<Document> result = unsorted.projection(projection);
		return result;
	}

	//Groups a collection by a single field and returns the amount of records
	//with that field value.
	//Also prints the result.
	public void printAggregation(String groupField){
		AggregateIterable<Document> result = collection.aggregate(
				Arrays.asList(
						Aggregates.group(("$" + groupField), Accumulators.sum("count", 1))));
	
		printMany(result);
	}
	
	//Same as above, but runs only on records where the field matchField
	//has the value matchValue.
	public void printAggregation(String matchField, String matchValue, String groupField) {
		AggregateIterable<Document> result = collection.aggregate(
				Arrays.asList(
						Aggregates.match(Filters.eq(matchField, matchValue)),
						Aggregates.group(("$" + groupField), Accumulators.sum("count", 1))));
		
		printMany(result);
	}
	
	//Prints every record in a MongoIterable object
	public void printMany(MongoIterable<Document> docs) {
		//Creates a Consumer object
		Consumer<Document> printBlock = new Consumer<Document>() {
			//Overrides the consumer's accept object to print the provided document
		     @Override
		     public void accept(final Document document) {
		         System.out.println(document.toJson());
		     }
		};
		
		//Runs the consumer object's accept method on each record
		docs.forEach(printBlock);
	}
}