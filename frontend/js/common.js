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
  constructor (id = Number, name = String, latitude = Number, longitude = Number) {
    this.id = id,
    this.name = name,
    this.latitude = latitude,
    this.longitude = longitude
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
  // Метож получения цвета по коэффициенту routesRatio (0-1)
  getColor = async (routesRatio) => {
    let color = String
    if (routesRatio > 0.5) {
      let R = routesRatio * 255
      return color = ('rgb(' + R + ', 0, 0)')
    } else if (routesRatio < 0.5) {
      let G = routesRatio * 255
      return color = ('rgb(0,'+ G +', 0)')
    } else return color = ('rgb(255, 255, 0)')
  }
  // Метод добавления маркера на слой markersLayer с цветом iconColor
  addMarker(iconColor) {
    return L.marker([this.longitude, this.latitude], {junction: this.name}).setIcon(iconColor).addTo(markersLayer).bindPopup(this.name)
  }
}

routesArr = []

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
  }
  // Сортируем по убыванию, первый элемент - максимум
  routesArr.sort(function(a, b) { return b - a; });
  let routesMax = routesArr[0]
  // Создаем обьект
  for (let key in junctionsObj) {
    let junction = new Junction(junctionsObj[key].id, junctionsObj[key].name, junctionsObj[key].longitude, junctionsObj[key].latitude)
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