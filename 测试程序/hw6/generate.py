import random


MAX_INT = 1<<31-1
MAX_FLOOR = 10
id_dict = {}
ele_id_dict={1:True,2:True,3:True,4:True,5:True,6:True}
move_v=[0.2,0.3,0.4,0.5,0.6]
def rd(a,b):
    return random.randint(a,b);
def get_id() :
    id = random.randint(1, MAX_INT)
    while (id_dict.get(id) == False) :
        id = random.randint(1, MAX_INT)
    id_dict[id] = True
    return id
def get_add_ele_id():
    id = rd(1,MAX_INT)
    while(ele_id_dict.get(id)!=None) :
        id=rd(1,MAX_INT)
    ele_id_dict[id]=True
    return id
def get_rm_ele_id():
    cnt = 0
    for key in ele_id_dict:
        if ele_id_dict[key] == True and (key>=1 and key<=6):
            cnt = cnt + 1
    if cnt<=2:
        id = random.choice(list(ele_id_dict))
        while id >=1 and id <=6:
            id = random.choice(list(ele_id_dict))
    else:
        id = random.choice(list(ele_id_dict))
    ele_id_dict.pop(id);
    return id

def get_time_gap() :
    chance = random.randint(0, MAX_INT) % 100;
    if (chance < 5) :
        return 10
    elif (chance >= 95) :
        return 5
    elif (chance >= 5 and chance < 10 or chance >= 90 and chance < 95) :
        return random.uniform(1.0, 5.0)
    elif chance >= 10 and chance <20 or chance >=80 and chance <90:
        return 0
    else : 
        return random.uniform(0, 1.0)

def get_floor() :
    floor = random.randint(0, MAX_INT) % MAX_FLOOR + 1
    return floor
def get_person():
    id = str(get_id())
    from_floor = str(get_floor())
    to_floor = str(get_floor())
    while (to_floor == from_floor):
        to_floor = str(get_floor())
    print(str(id) + '-FROM-'+ str(from_floor) + '-TO-'+ str(to_floor))
def get_add_ele():
    ele_id=get_add_ele_id()
    floor=get_floor()
    maxload=rd(3,8)
    move=move_v[rd(0,len(move_v)-1)]
    print('ADD-Elevator'+'-'+str(ele_id)+'-'+str(floor)+'-'+str(maxload)+'-'+str(move))
def get_rm_ele():
    ele_id=get_rm_ele_id()
    print('MAINTAIN-Elevator-'+str(ele_id))
def generate(num) :
    time = 1.0
    for _ in range(num) :
        order=rd(1,10)
        time += get_time_gap()
        print('[' + str(format(time, '.1f')) + ']',end='')
        cnt = 0
        for key in ele_id_dict:
            if ele_id_dict[key] == True:
                cnt = cnt + 1
        if order<=6:
            get_person()
        elif order<=7:
            if cnt>12:
                get_person()
            else:
                get_add_ele()
        else:
            if  cnt > 3:
                get_rm_ele()
            else:
                get_person()



#main()
num =  random.randint(0,MAX_INT)%30 + 1
generate(num)