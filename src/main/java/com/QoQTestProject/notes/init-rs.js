db = db.getSiblingDB("admin");
db.createUser({
    user: "admin",
    pwd: "password",
    roles: [{ role: "root", db: "admin" }]
});

rs.initiate({
    _id: "rs0",
    members: [{ _id: 0, host: "localhost:27017" }]
});