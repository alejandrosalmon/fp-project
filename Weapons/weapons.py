import json
import random
import numpy
import time

random.seed(56782987)

class Weapon():
    location_list = ['School', 'Hospital', 'Mountain','Field','Sea','Sky']
    model_list = ['P911','P90', 'M240','Vector']
    workingi_lsit = [0,1]
    safety_list = [0,1]

    def __init__(self,idn):
        self.idn = idn
        self.location= random.choice(['School', 'Hospital', 'Mountain','Field','Sea','Sky']) 
        self.battery = random.randrange(0,99)
        # 1 = True  0 = False
        self.working = 1
        self.safety = 1
        self.shots = random.randrange(0,10000)
        self.timestamp = time.time()

    def randomize(self):
        if self.working == 0:
            self.working = random.randrange(0,2)
        else:
            self.working = int(numpy.random.choice([0,1], p=[0.1,0.9]))
        if self.battery > 15:
            self.battery -= random.randrange(0,3)
        else:
            choice = random.randrange(0,10000)
            if choice == 1337:
                self.battery = random.randrange(0,10)
            else:
                self.battery = 100
        self.location = random.choice(['School', 'Hospital', 'Mountain','Field','Sea','Sky'])
        if (self.safety == 1) and (random.randrange(0,10000) == 1337):
            self.safety = 0
        elif (self.safety == 0) and (random.randrange(0,1000) == 777):
                self.safety = 1
        if (self.working == 1) and (self.safety == 0):
                self.shots += random.randrange(0,30)
        self.timestamp = time.time()

    def to_json(self):
        data = json.dumps(self.__dict__) + "\n"
        return data

class Writer():
    def create_file(self, path, data):
        f = open(path,"w")
        f.write(data)
        f.close()
        
    def append_file(self, path, data):
        f = open(path,"a")
        f.write(data)
        f.close()

weapons = []
wr = Writer()
for i in range(10):
	we = Weapon(i+1)
	weapons.append(we)
file = "data.txt"
wr.create_file(file,weapons[0].to_json())
for i in range(10000):
	we = random.choice(weapons)
	wr.append_file(file,we.to_json())
	we.randomize()

