import json
import matplotlib.pyplot as plt

from typing import List

with open('res/FreqAndPerfDens.json') as f:
	dic = json.load(f)

# x -> events attended
# y -> medal dense

x: List[int] = []
y: List[int] = []

for key in dic:
	for val in dic[key]:
		x.append(int(key))
		y.append(val)

plt.scatter(x , y)

with open('res/FreqAndAvgPerfDens.json') as f:
	dic = json.load(f)

intDic : dict = {}
for key in dic:
	intDic[int(key)] = dic[key]

# sort
items = intDic.items()
afterSort = sorted(items)

# avg vals
x1: List[int] = []
y1: List[float] = []

for item in afterSort:
	x1.append(item[0])
	y1.append(item[1])

plt.plot(x1 , y1 , "-or")

plt.show()
