import csv
import matplotlib.pyplot as plt

from sklearn.metrics import ConfusionMatrixDisplay

medalTotal = {}
hostCountryWins = {}
countryData = {}

def searchCities(city, cityFile):
    with open(cityFile, newline='') as citycsv:
        reader = csv.DictReader(citycsv)
        for row in reader:
            #print(row)
            cityToTest = row['\ufeffCity']
            #print(cityToTest)
            if city == cityToTest:
                country = row['Country'] #Country
                return country 
        return 0

def showGraph(countryCodeToAnalyse):
    data = countryData[countryCodeToAnalyse]
    #print(data)
    years = []
    fraction = []
    for year in data:
        years.append(int(year))
        fraction.append(data[year])
    plt.plot(years, fraction)
    #plt.show()
    hostData = hostCountryWins[countryCodeToAnalyse]
    hostYears = []
    hostFraction = []
    for year in hostData:
        hostYears.append(int(year))
        hostFraction.append(hostData[year])
    for i in range(len(hostYears)):
        plt.plot(hostYears[i], hostFraction[i], marker="x", markerSize=5)
        #plt.show()
    plt.title(countryCodeToAnalyse)
    plt.show()
    return

def codeSearch(country, countryFile):
    with open(countryFile, newline='') as countrycsv:
        reader = csv.DictReader(countrycsv)
        for row in reader:
            #print(row)
            countryToTest = row['\ufeffCountry']
            #print(countryToTest)
            if country == countryToTest:
                countryCode = row['Alpha-3 code']
                return countryCode
        return 0


cityFile = 'olympic_cities_and_their_countries.csv'
file = 'DenormilisedMedals.csv'
countryFile = 'country_codes_and_coordinates.csv'

# main code for medals
with open(file, newline='') as csvfile:
    reader = csv.DictReader(csvfile)
    previousYear = 0
    hostTotal = 0
    for row in reader:
        #count = 0
        year = row['Year']
        count = row['Count']
        NOC = row['NOC']
        count = row['Count']
        #print(count)
        hostCity = row['City']
        hostCountry = searchCities(hostCity, cityFile)
        if hostCountry == 0:
            break
        hostCode = codeSearch(hostCountry, countryFile)
        if NOC not in countryData.keys():
            countryData[NOC] = {}
        if year != previousYear:
            if hostCode not in hostCountryWins.keys():
                hostCountryWins[hostCode] = {}
            try:
                hostCountryWins[hostCode][year] = int(hostTotal)/int(medalTotal[previousYear])
            except:
                count = 0
        if hostCode == 0:
            break
        #print(hostCity, hostCountry, hostCode)
        #NOC = row['NOC']
        try:
            medalTotal[year] = int(medalTotal[year]) + int(count)
        except:
            medalTotal[year] = count
        if NOC == hostCode:
            hostTotal = count
        else:
            #countryData[NOC] = {}
            countryData[NOC][year] = count
        previousYear = year
    #print(medalTotal)
    #print(hostCountryWins)
    #print(countryData)
    for country in countryData:
        #print(country)
        for year in countryData[country]:
            #print("     ", year)
            countryData[country][year] = int(countryData[country][year])/int(medalTotal[year])
    
    #print(countryData)

#countryCodeToAnalyse = 'GBR'
#showGraph(countryCodeToAnalyse)
for country in hostCountryWins:
    countryCodeToAnalyse = country
    showGraph(countryCodeToAnalyse)
'''
data = countryData[countryCodeToAnalyse]
#print(data)
years = []
fraction = []
for year in data:
    years.append(int(year))
    fraction.append(data[year])
plt.plot(years, fraction)
#plt.show()
hostData = hostCountryWins[countryCodeToAnalyse]
hostYears = []
hostFraction = []
for year in hostData:
    hostYears.append(int(year))
    hostFraction.append(hostData[year])
for i in range(len(hostYears)):
    plt.plot(hostYears[i], hostFraction[i], marker="x", markerSize=5)
    #plt.show()
plt.title(countryCodeToAnalyse)
plt.show()
'''
