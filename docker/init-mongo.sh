echo ">>>>>>> trying to create database and users"
if [ -n "${MONGO_INITDB_ROOT_USERNAME:-}" ] && [ -n "${MONGO_INITDB_ROOT_PASSWORD:-}" ] && [ -n "${MONGO_INITDB_DATABASE:-}" ]; then
mongo -u $MONGO_INITDB_ROOT_USERNAME -p $MONGO_INITDB_ROOT_PASSWORD<<EOF
	var rootUser = '$MONGO_INITDB_ROOT_USERNAME';
    var rootPassword = '$MONGO_INITDB_ROOT_PASSWORD';
    var admin = db.getSiblingDB('admin');
    admin.auth(rootUser, rootPassword);
	
	db=db.getSiblingDB('$MONGO_INITDB_DATABASE');
    db.createUser({user: rootUser, pwd: rootPassword, roles: ["readWrite"]});
	db.createCollection("person");
EOF
else
echo "MONGO_INITDB_ROOT_USERNAME, MONGO_INITDB_ROOT_PASSWORD and MONGO_INITDB_DATABASE must be provided. Some of these are missing, hence exiting database and user creation"
    exit 403
fi