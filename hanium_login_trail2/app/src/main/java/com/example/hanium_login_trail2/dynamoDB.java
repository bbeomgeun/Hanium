//package com.example.hanium_login_trail2;
//
//import android.content.Context;
//
//import com.amazonaws.auth.CognitoCachingCredentialsProvider;
//import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
//import com.amazonaws.mobileconnectors.dynamodbv2.document.UpdateItemOperationConfig;
//import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
//import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
//import com.amazonaws.services.dynamodbv2.model.ReturnValue;
//
//import java.util.List;
//import java.util.UUID;
//
//
//public class dynamoDB {
//    Context mContext;
//
//    public dynamoDB(Context context){
//        mContext = context;
//    }
//
//    Cognito cognito = new Cognito(mContext);
//
//    CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
//            mContext, cognito.getPoolID(), cognito.getAwsRegion());
//
//    AmazonDynamoDBClient dbClient = new AmazonDynamoDBClient(credentialsProvider);
//
//    Table dbTable = Table.loadTable(dbClient, );
//
//
//    public void create(Document memo) {
//        if (!memo.containsKey("userId")) {
//            memo.put("userId", credentialsProvider.getCachedIdentityId());
//        }
//        if (!memo.containsKey("noteId")) {
//            memo.put("noteId", UUID.randomUUID().toString());
//        }
//        if (!memo.containsKey("creationDate")) {
//            memo.put("creationDate", System.currentTimeMillis());
//        }
//        dbTable.putItem(memo);
//    }
//
//    public void update(Document memo) {
//        Document document = dbTable.updateItem(memo, new         UpdateItemOperationConfig().withReturnValues(ReturnValue.ALL_NEW));
//    }
//
//    public void delete(Document memo) {
//        dbTable.deleteItem(
//                memo.get("userId").asPrimitive(),
//                memo.get("noteId").asPrimitive());
//    }
//
//    public Document getById(String id) {
//        return dbTable.getItem(new Primitive(credentialsProvider.getCachedIdentityId()), new Primitive(noteId));
//    }
//
//    public List<Document> getAll() {
//        return dbTable.query(new Primitive(credentialsProvider.getCachedIdentityId())).getAllResults();
//    }
//}
