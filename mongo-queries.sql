-- Mongo Queries

-- Use command from mongoshell will create the database.
Use MusicDB

-- Find songs with genre 'Classic Rock' and year less than 2002
db.collection('songs').find({
  genre: 'Classic Rock',
  year: { $lt: 2002 }
});