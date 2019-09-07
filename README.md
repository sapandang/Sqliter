# Sqliter
A single class java sqlite wrapper. Sqliter makes easy to work with sqlite database by wrapping the queries into hasmap.

## Getting started	
### Dependecy
> Dependent on `sqlite-jdbc-3.27.2.1.jar`  download from https://www.sqlite.org/ use the latest version.

### Create instance of class
```
Sqliter sqliter = new Sqliter("dbname");
```
### Create the table
```
HashMap<String,String> tableMap = new HashMap<>();  
tableMap.put("column1","'TEXT'");  
tableMap.put("column2","'TEXT'");  
tableMap.put("column3","'TEXT'");  
sqliter.createTable("table1",tableMap);
```
> As of now only string data type is support. `Id` column is automatically created with auto-increment

### Insert data
```
HashMap<String,String> dataMap = new HashMap<>();  
dataMap.put("column1","hii");  
dataMap.put("column2","bye");  
dataMap.put("column3","database");  
sqliter.insertData("table1", dataMap);
```
### Run Query / Select Data
```
ArrayList<HashMap<String,String>> records = sqliter.runQuery("select * from table1;");  
for(int i =0 ;i<records.size();i++)  
{  
  
    HashMap<String,String> record = records.get(i);  
    System.out.println(record);  
  
}
```
### Update Record
```
ArrayList<HashMap<String,String>> records= sqliter.runQuery("select * from table1;");  
  
HashMap<String,String> record = records.get(0);  
  
//put the records which need to be updated  
record.put("column1","updated Data");  
sqliter.updateData("table1",record);

//Print the record and test  
for(int i =0 ;i<records.size();i++)  
{  
HashMap<String,String> record = records.get(i);
    System.out.println(record);  
}
```
### Delete Record
```
ArrayList<HashMap<String,String>> recordsToDelete= sqliter.runQuery("select * from table1;");  
  
HashMap<String,String> record1 = recordsToDelete.get(0);  
sqliter.deleteData("table1",record1);
```
### Update or delete using query
Update
```
int rescode = sqliter.runUpdateOrDelete("update table1 SET column1='123' WHERE ID=1;");
```
Delete 
```
int rescode = sqliter.runUpdateOrDelete("delete from  table1  WHERE ID=1;");
```
>This is wrapper was written to meet specific need. This is not complete ORM











