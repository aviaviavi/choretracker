from bottle import route, run, debug, request, get, post, static_file
import pdb
import traceback
import sqlite3
import datetime

from bottle import default_app

conn = sqlite3.connect("data/db.db")
APP_VERSION = 1.2

SUCCESS = {'success': True}
FAILURE = {'success': False}

number_to_uname = {}
number_to_group_name = {}

@get('/')
def home():
    try:
        number = request.params['phone_number']
        user = conn.execute("select * from user where phone_number = %s;" % number).fetchall()
        if len(user) > 1:
            return failure("multiple users for this phone number")
        if not user:
            return {'user_name': None, 'version': APP_VERSION}
        user_name = get_user_name(user[0])
        number_to_uname[number] = user_name  
        chore_list = fetch_chores(number)
        chore_list['user_name'] = user_name
        chore_list['group_name'] = get_group_name(user[0])
        chore_list['version'] = APP_VERSION
        return chore_list
    except Exception as e:
        return failure(str(e))

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
        number_to_group_name[number] = group_name
        conn.commit()
        result = SUCCESS 
        print "group joined"
        return result
    except Exception as e:
        return failure(e)

@get('/create_chore')
def create_chore():
    user_name = request.params['user_name']
    number = request.params['phone_number']
    group_name = request.params['group_name'] 
    chore_name = request.params['chore_name'] 
    day = request.params['day']
    repeat = request.params['repeat']

    query = "insert into chore (chore_name, group_name, user_name, day_of_week, repeat) \
            values ('%s', '%s', '%s', %s, %s);" % (chore_name, group_name, user_name, day, repeat)
    conn.execute(query)
    conn.commit()
    return fetch_chores(number)

@get('/house_mates')
def get_house_mates():
    group = request.params['group_name']
    members = conn.execute("select user_name from user where group_name = '%s';" % group).fetchall()
    return {'members': members}

@get('/groups')
def get_groups():
    groups = conn.execute("select distinct group_name from user where group_name != 'None';")
    return {'groups': [group[0] for group in groups]}

@route('/static/<filename>')
def server_app(filename):
    return static_file(filename, "./static")

@get('/delete_chore')
def delete_chore():
    try:
        user_name = request.params['user_name']
        number = request.params['phone_number']
        group_name = request.params['group_name'] 
        chore_name = request.params['chore_name'] 
        day = request.params['day']
        query = "delete from chore where user_name = '%s' and group_name = '%s' and day_of_week = %s and chore_name = '%s';" % (user_name, group_name, day, chore_name)
        conn.execute(query)
        conn.commit()
        return fetch_chores(number) 
    except Exception as e:
        print e
        return FAILURE

def fetch_chores(number):
    try:
        name = number_to_uname[number]
        group_name = number_to_group_name[number]
    except KeyError:
        results = conn.execute("select user_name, group_name from user where phone_number = '%s';" % number).fetchall()[0]
        name = results[0]
        group_name = results[1]
        number_to_uname[number] = name
        number_to_group_name[number] = group_name 
    print name, group_name
    chores = conn.execute("select * from chore where user_name = '%s' and day_of_week = %d and group_name = '%s';" % (name, day_of_week(), group_name)).fetchall()
    return {'chores': chores}

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

def get_group_name(user_info):
    return user_info[2]

if __name__ == '__main__':
    run(reloader=True, host='0.0.0.0')
