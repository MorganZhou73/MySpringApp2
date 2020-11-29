db = db.getSiblingDB("Northwind");
db.createUser(
  {
    user: "user1",
    pwd: "V4321abcd!",
    roles: [ 
	{ role: "readWrite", db: "Northwind" } 
	]
  });
db.createCollection("person");
