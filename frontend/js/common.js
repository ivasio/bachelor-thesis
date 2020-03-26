//Created by ivasio&dimasio
//Инициализация карты
let mapUrl = 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'
      
let mapLayer = new L.TileLayer(mapUrl, {
    maxZoom: 18
})

let baseMaps = {"OpenStreetMap": mapLayer}

let map = new L.Map('map', {
    center: new L.LatLng(55.7190, 37.5774),
    zoom: 10,
    layers: [mapLayer]
});

//Класс - Развязка, конструктор(имя, широта, долгота)
class Junction {
	constructor (name, latitude, longitude) {
		this.name = name,
		this.latitude = latitude,
		this.longitude = longitude
	}
	addMarker() {
		return L.marker([this.longitude, this.latitude]).addTo(map).bindPopup(this.name)
	}
}

//API адрес развязок
const apiUrl = 'http://localhost:8888/junctions/'

//Json -> Obj
async function getJunctions () {
	const apiResponse = await fetch(apiUrl)
	const allJunctions = await apiResponse.json()

	//Перебираем объект, инициализируем новый объект класса Junction
	for (let key in apiJunctions) {
			let junction = new Junction(apiJunctions[key].name, apiJunctions[key].longitude, apiJunctions[key].latitude)
			junction.addMarker()
	}
}
//Работаем с backend
getJunctions()