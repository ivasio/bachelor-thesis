/* 
 * Leaflet List Junctions v1.0.1 - 2020-04-13 
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
let junctionsUrl = baseUrl + 'junctions/', routesUrl = 'routes_count/', routesArr = []

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

// Группа слоев с маркер-элементами 
let markersLayer = new L.LayerGroup() 	 
map.addLayer(markersLayer)

// Класс L.Control.RoutesCountColor - отображение количество траекторий развязок на шкале
let list = new L.Control.RoutesCountColor({layer: markersLayer})
map.addControl(list)

class Junction {
  constructor (id = Number, name = String, latitude = Number, longitude = Number, radius = Number, maxRoutes = Number) {
    this.id = id,
    this.name = name,
    this.latitude = latitude,
    this.longitude = longitude,
    this.radius = radius,
    this.maxRoutes = maxRoutes
  }
  // Метод получения количества траекторий по this.id
  getRoutesCount = async () => {
    const routes = await fetch(junctionsUrl + this.id + '/' + routesUrl)
    let routesCount = await routes.json()
    return routesCount
  }
  // Метод получения коэффициента отношения текущего количества развязок к максимальному
  getRoutesRatio = async (routesCount = Number) => { return (routesCount / this.maxRoutes) }
  // Метод получения  цвета маркера по коэффициенту отношения текущего количества развязок к максимальному
  getIconColor = async (r = Number) => {
    return  r ==0.5 ? '#ffff00' : r > 0.9 ? '#00ff00' : r > 0.8 ? '#33ff00' : r > 0.7 ? '#66ff00' : r > 0.6 ? '#99ff00' :
            r > 0.5 ? '#ccff00' : r > 0.4 ? '#ffcc00' : r > 0.3 ? '#ff9900' : r > 0.2 ? '#ff6600' : r > 0.1 ? '#ff3300' : '#ff0000'; 
  }
  // Метод создания уникальной иконки для маркера
  addMarkerIcon = async (iconColor) => {
    let markerStyle = `background-color: ${iconColor}`
    let markerIcon = L.divIcon({
      className: "map-pin",
      popupAnchor:  [0, -30],
      html: `<span style="${markerStyle}" />`
    })
    return markerIcon
  }
  // Метод добавления маркера и его свойств в группу слоя markersLayer
  addMarker = async (routes) => {
    this.getRoutesCount().then(response => {
      return (this.getRoutesRatio(response))
    }).then(response => {
      return (this.getIconColor(response))
    }).then(response => {
      return (this.addMarkerIcon(response))
    }).then(response => {
      let marker = L.marker([this.longitude, this.latitude], {routes: routes, maxRoutes: this.maxRoutes, radius: this.radius}).setIcon(response).addTo(markersLayer).bindPopup(this.name)
      let activeCircle = false
      marker.on('click', function(e) {
        if (activeCircle) {map.removeLayer(activeCircle)}
        activeCircle = L.circle(e.target.getLatLng(), {radius: this.options.radius , color: "#ff0000"}).addTo(map)
      })
      marker.on('popupclose', function(e) {map.removeLayer(activeCircle)})
    })
  }
}

async function getJunctions () {
  const junctions = await fetch(junctionsUrl)
  let junctionsObj = await junctions.json()
  // Собираем массив количества траекторий
  for (let i=1;i<=junctionsObj.length;i++) {
    const routesCount = await fetch(junctionsUrl + i + '/' + routesUrl)
    let routes = await routesCount.json()
    routesArr.push(routes)
  }i=0
  // Сортируем по убыванию, первый элемент - максимум
  routesArr.sort(function(a, b) { return (b - a) })
  let routesMax = routesArr[0]
  // Создаем обьект
  for (let key in junctionsObj) {
    let junction = new Junction(junctionsObj[key].id, junctionsObj[key].name, junctionsObj[key].longitude, junctionsObj[key].latitude, junctionsObj[key].radius, routesMax)
    junction.getRoutesCount().then(response => {
      return(junction.addMarker(response).then(response => response))
    })
  }
}
getJunctions()