from bottle import route, run, debug, request, get, post
import pdb
import traceback
import sqlite3
import datetime


from bottle import default_app

conn = sqlite3.connect("data/db.db")

SUCCESS = {'success': True}
FAILURE = {'success': False} 

@get('/')
def home():
    try:
        number = request.params['phone_number']
        user = conn.execute("select * from user where phone_number = %s;" % number).fetchall()
        if not user:
            return {'route_to': 'create_user'}
        chore_list = fetch_chores(user[0])
        return chore_list
    except Exception as e:
        return failure(e) 

@get('/create_user')
def create_user():
    try:
        number = request.params['phone_number']
        user_name = request.params['user_name']
        conn.execute("insert into user (phone_number, user_name) values (%s, '%s');" % (number, user_name))
        conn.commit()
        return SUCCESS 
    except Exception as e:
        return failure(e) 

@get('/join_group')
def join_group():
    try:
        number = request.params['phone_number'] 
        group_name = request.params['group_name'] 
        conn.execute("update user set group_name = '%s' where phone_number = %s" % (group_name, number))
        conn.commit()
        return SUCCESS 
    except Exception as e:
        return failure(e)

@get('/create_chore')
def create_chore():
    number = request.params['phone_number'] 
    group_name = request.params['group_name'] 
    chore_name = request.params['chore_name'] 
    chore_desc = request.params['chore_desc']
    day = request.params['day']
    repeat = request.params['repeat']

    query = "insert into chore (chore_name, description, group_name, user_name, day_of_week, repeat) \
            values (%s, '%s', '%s', '%s', %s, %s);" % (number, group_name, chore_name, chore_desc, day, repeat)
    conn.execute(query)
    conn.commit()
    return fetch_chores(number)
                
def fetch_chores(user_data):
    chores = conn.execute("select * from chore where user_name = '%s' and day_of_week = %d;" % (get_user_name(user_data), day_of_week())).fetchall()
    return {'results': chores}

def day_of_week():
    return datetime.datetime.today().weekday()

def failure(error):
    out = FAILURE
    out["error"] = error 
    return out

def get_user_name(user_info):
    return user_info[1] 

def get_number(user_info):
    return user_info[0] 

if __name__ == '__main__':
    debug(True)
    run(reloader=True)

kkk
