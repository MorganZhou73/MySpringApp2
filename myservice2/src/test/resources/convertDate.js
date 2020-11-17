// convert employees.BirthDate/HireDate field from string to Date type with improved performance using Bulk API

db = db.getSiblingDB('Northwind');

var bulkOps = [],
    cursor = db.employees.find({$or: [{"BirthDate": {"$exists": true, "$type": 2 }},
	{"HireDate": {"$exists": true, "$type": 2 }}
	]});

var myregexp = /(....)-(..)-(..)/;

cursor.forEach(function (doc) { 
	var newDate1 = doc.BirthDate;
	if(myregexp.test(doc.BirthDate)){
    	newDate1 = new ISODate(doc.BirthDate);
    }
 
	var newDate2 = doc.HireDate;
	if(myregexp.test(doc.HireDate)){
    	newDate2 = new ISODate(doc.HireDate);
    }
 
    bulkOps.push(         
        { 
            "updateOne": { 
                "filter": { "_id": doc._id } ,              
                "update": { "$set": { "BirthDate": newDate1 , "HireDate": newDate2} } 
            }         
        }           
    );

    if (bulkOps.length === 500) {
        db.employees.bulkWrite(bulkOps);
        bulkOps = [];
    }     
});

if (bulkOps.length > 0) db.employees.bulkWrite(bulkOps);
