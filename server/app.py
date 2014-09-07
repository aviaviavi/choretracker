from bottle import route, run, debug, request, get, post
import pdb
import traceback
import sqlite3
import datetime

from bottle import default_app

conn = sqlite3.connect("data/db.db")

SUCCESS = {'success': True}
FAILURE = {'success': False}

number_to_uname = {}

@get('/')
def home():
    try:
        number = request.params['phone_number']
        user = conn.execute("select * from user where phone_number = %s;" % number).fetchall()
        if len(user) > 1:
            return failure("multiple users for this phone number")
        if not user:
            return {'route_to': 'create_user'}
        number_to_uname[number] = get_user_name(user[0])
        chore_list = fetch_chores(number)
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
        conn.execute("update user set group_name = '%s' where phone_number = %s;" % (group_name, number))
        conn.commit()
        return SUCCESS 
    except Exception as e:
        return failure(e)

@get('/create_chore')
def create_chore():
    user_name = request.params['user_name']
    number = request.params['phone_number']
    group_name = request.params['group_name'] 
    chore_name = request.params['chore_name'] 
    chore_desc = request.params['chore_desc']
    day = request.params['day']
    repeat = request.params['repeat']

    query = "insert into chore (chore_name, description, group_name, user_name, day_of_week, repeat) \
            values ('%s', '%s', '%s', '%s', %s, %s);" % (chore_name, chore_desc, group_name, user_name, day, repeat)
    conn.execute(query)
    conn.commit()
    return fetch_chores(number)

@get('/house_mates')
def get_house_mates():
    group = request.params['group_name']
    members = conn.execute("select user_name from user where group_name = '%s';" % group).fetchall()
    return {'members': members}


def fetch_chores(number):
    try:
        name = number_to_uname[number]
    except KeyError:
        name = conn.execute("select user_name from user where phone_number = '%s';" % number).fetchall()[0][0]
    chores = conn.execute("select * from chore where user_name = '%s' and day_of_week = %d;" % (name, day_of_week())).fetchall()
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
