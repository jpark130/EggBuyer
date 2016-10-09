import parser

def freq(household, data):
  history = data[household]
  days = []

  # indices
  cDesc = 16
  day = 1

  for i in range(len(history)):
    if (history[i][cDesc] == "EGGS"):
      days.append(int(history[i][day]))

  # get freq
  freq = avg(days)
  return freq

def avg(L):
  if (len(L) <= 1): 
    return 0
  sum = 0
  for i in range(len(L)-1, 0, -1):
    sum += L[i] - L[i-1]
  return round(float(sum)/(len(L)-1), 1)
    
#def testing():
#  data = parser.parser()
#  result = []
#  for i in range(len(data["19"])):
#    if (data["19"][i][16] == "EGGS"):
#      result.append(data["19"][i])
#  return result 

# print testing()
# print freq("19", parser.parser())
