echo ">>>>>>> trying to create database and users"
if [ -n "${MONGO_INITDB_ROOT_USERNAME:-}" ] && [ -n "${MONGO_INITDB_ROOT_PASSWORD:-}" ] && [ -n "${MONGO_INITDB_DATABASE:-}" ]; then
  mongo -u "$MONGO_INITDB_ROOT_USERNAME" -p "$MONGO_INITDB_ROOT_PASSWORD" <<-EOJS
  var rootUser = "$MONGO_INITDB_ROOT_USERNAME";
  var rootPassword = "$MONGO_INITDB_ROOT_PASSWORD";
  var admin = db.getSiblingDB("admin");
  admin.auth(rootUser, rootPassword);
  
  var dbUser = "$dbUser";
  var dbPassword = "$dbPwd";
  db=db.getSiblingDB("$MONGO_INITDB_DATABASE");
  db.createUser({user: dbUser, pwd: dbPassword, roles: ["readWrite"]});
  db.createCollection("person");
EOJS
fi

echo "created $dbUser in database $MONGO_INITDB_DATABASE"
