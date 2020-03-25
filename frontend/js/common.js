//Инициализация карты
let mapUrl = 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
      
let mapLayer = new L.TileLayer(mapUrl, {
    maxZoom: 18
});

let baseMaps = {"OpenStreetMap": mapLayer};

let map = new L.Map('map', {
    center: new L.LatLng(55.7190, 37.5774),
    zoom: 10,
    layers: [mapLayer]
});

//Объект развязки(имя, широта, долгота)
class Junction {
	constructor (name, latitude, longitude) {
		this.name = name,
		this.latitude = latitude,
		this.longitude = longitude
	}
	addMarker() {
		return L.marker([this.longitude, this.latitude]).addTo(map).bindPopup(this.name);
	}
}

let junction = new Junction('МКАД - ш. Энтузиастов', 37.84270, 55.77692);
junction.addMarker();
