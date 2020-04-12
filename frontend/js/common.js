/* 
 * Leaflet List Junctions v1.0.1 - 2020-04-04 
 * 
 * Copyright 2020 Ivan Chebotar&Dmitry Tikhomirov 
 *  
 * Demo: 
 * ../frontend/index.html 
 * 
 * Source: 
 * git@github.com: https://github.com/ivasio/bachelor-thesis.git
 * 
 */

const baseUrl = 'http://localhost:8888/'
let junctionsUrl = baseUrl + 'junctions/'
let routesUrl = 'routes_count/'

//Инициализация карты
let mapUrl = 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'
      
let mapLayer = new L.TileLayer(mapUrl, {
  maxZoom: 18,
  minZoom: 10  
})

let baseMaps = {"Moscow junctions": mapLayer}

let map = new L.Map('map', {
  center: new L.LatLng(55.7190, 37.5774),
  zoom: 10,
  layers: [mapLayer]
})

// Слой с маркер-элементами 
let markersLayer = new L.LayerGroup() 	 
map.addLayer(markersLayer)

// Инициализация списка макеров
let list = new L.Control.ListMarkers({layer: markersLayer})
map.addControl(list)

class Junction {
  constructor (id = Number, name = String, latitude = Number, longitude = Number, radius = Number) {
    this.id = id,
    this.name = name,
    this.latitude = latitude,
    this.longitude = longitude,
    this.radius = radius
  }
  // Метод получения количества траекторий развязки
  getRoutesInfo = async () => {
    const routesJson = await fetch(junctionsUrl + this.id + '/' + routesUrl)
    let routesObj = await routesJson.json()
    return routesObj
  }
  // Метод получения коэффициента количества траекторий развязки
  getRoutesRatio = async (routesObj, maxRoutes) => {
    let routesRatio = routesObj / maxRoutes
    return routesRatio
  }
  // Метод получения цвета по коэффициенту routesRatio (0-1)
  getColor = async (r) => {
    return  r ==0.5 ? '#ffff00' : r > 0.9 ? '#00ff00' : r > 0.8 ? '#33ff00' : r > 0.7 ? '#66ff00' : r > 0.6 ? '#99ff00' :
            r > 0.5 ? '#ccff00' : r > 0.4 ? '#ffcc00' : r > 0.3 ? '#ff9900' : r > 0.2 ? '#ff6600' : r > 0.1 ? '#ff3300' : '#ff0000'; 
  }
  // Метод добавления маркера на слой markersLayer с цветом iconColor
  addMarker(iconColor) {
    let marker = L.marker([this.longitude, this.latitude], {junction: this.name, radius: this.radius}).setIcon(iconColor).addTo(markersLayer).bindPopup(this.name)
    let activeCircle = false
    marker.on('click', function(e) {
      if (activeCircle) {map.removeLayer(activeCircle)}
      activeCircle = L.circle(e.target.getLatLng(), {radius: this.options.radius , color: "#ff0000"}).addTo(map)
    })
    marker.on('popupclose', function(e) {map.removeLayer(activeCircle)})
    return marker
  }
}

routesArr = []

function getGradValue(c) {
  return  c ==0.5 ? 5 : c > 0.9 ? 0 : c > 0.8 ? 1 : c > 0.7 ? 2 : c > 0.6 ? 3 : 
          c > 0.5 ? 4 : c > 0.4 ? 6 : c > 0.3 ? 7 : c > 0.2 ? 8 : c > 0.1 ? 9 : 10;
}

async function getJunctions () {
  const junctionsJson = await fetch(junctionsUrl)
  let junctionsObj = await junctionsJson.json()
  // Общее количество развязок
  let junctionsVal = junctionsObj.length
  // Собираем массив количества траекторий
  for (let i=1;i<=junctionsVal;i++) {
    const routesCountJson = await fetch(junctionsUrl + i + '/' + routesUrl)
    let routesCountObj = await routesCountJson.json()
    routesArr.push(routesCountObj)
  }i=0
  // Сортируем по убыванию, первый элемент - максимум
  routesArr.sort(function(a, b) { return b - a; })
  let routesMax = routesArr[0]
  
  // Создаем шкалу значений routes_count по цвету
  let legend = L.control({position: 'topright'})
	legend.onAdd = function (map) {
    //  Создаем html разметку, конечный массив значений
    let div = L.DomUtil.create('div', 'info legend'), 
        divGrd = L.DomUtil.create('div', 'routes-gradient', div), 
        divVal = L.DomUtil.create('div', 'routes-count', div), 
        labels = []
    // routesNoRepetition - массив значений routesArr без повторений
    let routesCountRatio, routesNoRepetition = Array.from(new Set(routesArr)), gradValueIndex, step
    // Задаем 11 пустых ячеек <i>
    // Первая и последняя ячейки будут иметь значения max&min
    for (i=0;i<=10;i++) { labels[i] = '<i></i>' }i=0
    labels[10] = '<i>0</i>'
    // Отталкиваемся от количества значений нового массива routesNoRepetition
    if (routesNoRepetition.length<=10) {
      // Если значений хватает на количество ячеек, заполняем labels с индексом gradValueIndex значением routesNoRepetition[j]
      for (i=0;i<routesNoRepetition.length;i++) {
        routesCountRatio = routesNoRepetition[i] / routesMax
        gradValueIndex = getGradValue(routesCountRatio)
        labels[gradValueIndex] = '<i>' + routesNoRepetition[i] + '</i>'
      }i=0
    } else {
      // Создаем шаг для размещения значений в 9 ячейках
      step = Math.round(routesNoRepetition.length / 9)
      labels[0] = '<i>' + routesMax + '</i>'
      for (i=1;i<10;i++) {
        if ((routesNoRepetition.includes(routesNoRepetition[i*step-1], 0))&&(routesNoRepetition[i*step-1] != 0)) {
          routesCountRatio = routesNoRepetition[i*step-1] / routesMax
          gradValueIndex = getGradValue(routesCountRatio)
          labels[gradValueIndex] = '<i>' + routesNoRepetition[i*step-1] + '</i>'
        } 
      }i=0
    }
    divVal.innerHTML = labels.join('')
		return div
	}
	legend.addTo(map)

  // Создаем обьект
  for (let key in junctionsObj) {
    let junction = new Junction(junctionsObj[key].id, junctionsObj[key].name, junctionsObj[key].longitude, junctionsObj[key].latitude, junctionsObj[key].radius)
    // Обработка методов
    junction.getRoutesInfo().then((routesObj) => {
      routesRatio = junction.getRoutesRatio(routesObj, routesMax)
      return routesRatio
    }).then((routesRatio) => {
      let junctionsColor = junction.getColor(routesRatio)
      return junctionsColor
    }).then((junctionsColor) => {
      let markerStyle = `background-color: ${junctionsColor}`
      let iconColor = L.divIcon({
        className: "map-pin",
        popupAnchor:  [0, -30],
        html: `<span style="${markerStyle}" />`
      })
      return iconColor
    }).then((iconColor) => junction.addMarker(iconColor))
  }
}
getJunctions()