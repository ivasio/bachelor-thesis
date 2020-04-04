/* 
 * Leaflet List Junctions v1.0.1 - 2020-04-04 
 * 
 * Copyright 2020 Ivan Chebotar&Dmitry Tikhomirov 
 *  
 * Demo: 
 * ../frontend/index.html 
 * 
 * Source: 
 * git@github.com: 
 * 
 */

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
  
  // Меняем цвет маркера по выбранному элементу
  list.on('item-mouseover', function(e) {
	e.layer.setIcon(L.icon({
	  iconUrl: 'img/marker-icon-2x-red.png',
	  shadowUrl: 'img/marker-shadow.png',
	}))
  }).on('item-mouseout', function(e) {
	e.layer.setIcon(L.icon({
	  iconUrl: 'img/marker-icon-2x-blue.png',
	  shadowUrl: 'img/marker-shadow.png',
	}))
  })

  map.addControl(list)

//Класс - Развязка, конструктор(имя, широта, долгота)
class Junction {
  constructor (name, latitude, longitude, routes) {
	this.name = name,
	this.latitude = latitude,
	this.longitude = longitude,
	this.routes = routes
  }
  addMarker() {
	return L.marker([this.longitude, this.latitude], {junction: this.name}).addTo(markersLayer)
  }
}

//API адрес развязок
const apiUrl = 'http://localhost:8888/junctions/'

//Json -> Obj
async function getJunctions () {
  const apiResponse = await fetch(apiUrl)
  let apiJunctions = await apiResponse.json()
  // Сортировка по количеству траекторий
  apiJunctions.sort(function(a, b) {
	return Object.keys(a.routes).length - Object.keys(b.routes).length
  })
  //Проверяем сортировку
  console.log(apiJunctions)

  //Перебираем объект, инициализируем новый объект класса Junction
  for (let key in apiJunctions) {
	let routes = Object.keys(apiJunctions[key].routes).length
	let junction = new Junction(apiJunctions[key].name, apiJunctions[key].longitude, apiJunctions[key].latitude, routes)
	junction.addMarker()
  }
}
//Работаем с backend
getJunctions()