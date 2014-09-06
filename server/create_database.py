import sqlite3

con = sqlite3.connect('data/db.db')
schema = open('data/schema.sql', 'r').read()
tables = schema.split(";")
for table in tables[:-1]:
    con.execute(table)
con.commit()
