import random


MAX_INT = 1<<31-1
MAX_FLOOR = 10
id_dict = {}

def get_id() :
    id = random.randint(1, MAX_INT)
    while (id_dict.get(id) == False) :
        id = random.randint(1, MAX_INT)
    id_dict[id] = True
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
    
def get_building() :
    tags = ['A', 'B', 'C', 'D', 'E']
    choice = random.randint(0, MAX_INT) % len(tags)
    return tags[choice]

def get_floor() :
    floor = random.randint(0, MAX_INT) % MAX_FLOOR + 1
    return floor 

def generate(num) :
    time = 0.0
    for _ in range(num) :
        if (time > 1000) :
            break
        time += get_time_gap()
        id = str(get_id())
        from_floor = str(get_floor())
        to_floor = str(get_floor())
        while (to_floor == from_floor) :
            to_floor = str(get_floor())
        print('[' + str(format(time, '.1f')) + ']' + id + '-FROM-'+ from_floor + '-TO-'+ to_floor)




#main()
num =  random.randint(0,MAX_INT)%10 + 1
generate(num)