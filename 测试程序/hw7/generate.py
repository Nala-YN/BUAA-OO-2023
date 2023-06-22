import random


MAX_INT = 1<<31-1
MAX_FLOOR = 10
id_dict = {}
ele_id_dict={}
move_v=[0.2,0.3,0.4,0.5,0.6]
def rd(a,b):
    return random.randint(a,b);
def get_move_id():
    num=1
    move_id=0
    for i in range(num):
        floor=rd(1,10)
        move_id=move_id|(1<<(floor-1))|(1<<(floor))
    return move_id
person_id=1
def get_id() :
    global  person_id
    id = person_id
    person_id=person_id+1
    return id
ele_cnt=7
def get_add_ele_id():
    global ele_cnt
    global ele_id_dict
    id = ele_cnt
    ele_cnt=ele_cnt+1
    movable=get_move_id()
    ele_id_dict[id]=movable
    return id,movable
def get_rm_ele_id():
    id = random.choice(list(ele_id_dict))
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
markFloor=[]
markEle={}
reach=False
def get_movable(from_floor,to_floor):
    global reach
    global markFloor
    global markEle
    reach=False
    for key0 in ele_id_dict:
        markEle={}
        markFloor=[]
        for key in ele_id_dict:
            markEle[key]=False
        for i in range(15):
            markFloor.append(False)
        markEle[key0]=True
        markFloor[from_floor]=True
        dfs(key0,from_floor,to_floor)
        if reach==True:
            return True
    return False
def dfs(eleid,floor,to):
    global reach
    global markFloor
    global markEle
    if reach==True:
        return
    cango=ele_id_dict[eleid]
    if cango&(1<<(floor-1))==0:
        return
    if cango&(1<<(to-1))!=0:
        reach=True
        return
    for i in range(11):
        if cango&(1<<i)!=0 and i!=floor-1 and markFloor[i]==False:
            markFloor[i]=True
            for key in ele_id_dict:
                if markEle[key]==False:
                    markEle[key]=True
                    dfs(key,i+1,to)
                    markEle[key]=False
            markFloor[i]=False
def get_person():
    id = get_id()
    if rd(0,1)==0:
        from_floor = 1#get_floor()
        to_floor = 11#get_floor()
    else:
        from_floor=11
        to_floor=1
    cnt=0
    while ((to_floor == from_floor or get_movable(from_floor,to_floor)==False or from_floor-to_floor==1 or from_floor-to_floor==-1 )and cnt<100):
        to_floor = get_floor()
        from_floor=get_floor()
        cnt=cnt+1
    while (to_floor == from_floor or get_movable(from_floor,to_floor)==False):
        to_floor = get_floor()
        from_floor=get_floor()
    id_dict[person_id]=from_floor,to_floor
    print(str(id) + '-FROM-'+ str(from_floor) + '-TO-'+ str(to_floor))
def get_add_ele():
    ele_id,move_id=get_add_ele_id()
    floor=get_floor()
    maxload=rd(3,8)
    move=move_v[rd(0,len(move_v)-1)]
    print('ADD-Elevator'+'-'+str(ele_id)+'-'+str(floor)+'-'+str(maxload)+'-'+str(move)+'-'+str(move_id))
def get_rm_ele():
    ele_id=get_rm_ele_id()
    move = ele_id_dict[ele_id]
    ele_id_dict.pop(ele_id)
    check=True
    for key in id_dict:
        fromFloor,toFloor=id_dict[key]
        for fromFloor in range(11):
            if get_movable(fromFloor+1,toFloor)==False:
                check=False
                break
    if check==False:
        ele_id_dict[ele_id]=move
        get_person()
        return
    print('MAINTAIN-Elevator-'+str(ele_id))
def generate(num) :
    time = 0.0
    for _ in range(14):
        print('[' + str(format(time, '.1f')) + ']', end='')
        get_add_ele()
    for _ in range(6):
        print('[' + str(format(time, '.1f')) + ']', end='')
        print('MAINTAIN-Elevator-' + str(_ + 1))
    for _ in range(num) :
        order=rd(1,20)
        time += get_time_gap()
        print('[' + str(format(time, '.1f')) + ']',end='')
        cnt = len(ele_id_dict)
        if order<=12:
            get_person()
        elif order<=15:
            if cnt>12:
                get_person()
            else:
                get_add_ele()
        else:
            if  cnt > 1:
                get_rm_ele()
            else:
                get_person()



#main()
num =  random.randint(0,MAX_INT)%70 + 1
generate(num)