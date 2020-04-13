L.Control.RoutesCountColor = L.Control.extend({
  includes: L.version[0]==='1' ? L.Evented.prototype : L.Mixin.Events,

  options: {		
    layer: false,
    routes: Number,
    maxRoutes: Number,
    position: 'topright'
  },

  initialize: function(options) {
    L.Util.setOptions(this, options)
    this._container = null
    this._list = null
    this._layer = this.options.layer || new L.LayerGroup()
  },

  onAdd: function (map) {
    this._map = map
    let container = this._container = L.DomUtil.create('div', 'info legend'),
        containerGrd = L.DomUtil.create('div', 'routes-gradient', container)
    this._list = L.DomUtil.create('div', 'routes-count', container)
    map.on('moveend', this._updateList, this)
    this._updateList()

    return container
  },

  onRemove: function(map) {
    map.off('moveend', this._updateList, this)
    this._container = null
    this._list = null		
  },

  _getGradValue: function(c) {
    return  c ==0.5 ? 5 : c > 0.9 ? 0 : c > 0.8 ? 1 : c > 0.7 ? 2 : c > 0.6 ? 3 : 
            c > 0.5 ? 4 : c > 0.4 ? 6 : c > 0.3 ? 7 : c > 0.2 ? 8 : c > 0.1 ? 9 : 10;
  },

  _updateList: function() {
    let routesCountArr = [], labels = [], that = this, routesMax
    // Собираем видимые траектории в массив
    this._layer.eachLayer(function(layer) {
      if(layer instanceof L.Marker)
        if( that._map.getBounds().contains(layer.getLatLng()) )
          if(layer.options.routes)
            routesCountArr.push(layer.options.routes)
        routesMax = layer.options.maxRoutes
      })
    // Сортируем массив траекторий по убыванию, 1 элемент - максимум
    routesCountArr.sort(function(a, b) { return (b - a) })
    this._list.innerHTML = ''
    let routesCountRatio, routesNoRepetition = Array.from(new Set(routesCountArr)), gradValueIndex, step

    for (i=0;i<=10;i++) { labels[i] = '<i></i>' }i=0
    labels[0] = '<i>&#8734</i>'
    labels[10] = '<i>0</i>'
    // Отталкиваемся от количества значений нового массива routesNoRepetition
    if (routesNoRepetition.length<=10) {
      // Если значений хватает на количество ячеек, заполняем labels с индексом gradValueIndex значением routesNoRepetition[j]
      for (i=0;i<routesNoRepetition.length;i++) {
        routesCountRatio = routesNoRepetition[i] / routesMax
        gradValueIndex = that._getGradValue(routesCountRatio)
        labels[gradValueIndex] = '<i>' + routesNoRepetition[i] + '</i>'
      }i=0
    } else {
      // Создаем шаг для размещения значений в 9 ячейках
      step = Math.round(routesNoRepetition.length / 9)
      labels[0] = '<i>' + routesMax + '</i>'
      for (i=1;i<10;i++) {
        if ((routesNoRepetition.includes(routesNoRepetition[i*step-1], 0))&&(routesNoRepetition[i*step-1] != 0)) {
          routesCountRatio = routesNoRepetition[i*step-1] / routesMax
          gradValueIndex = that._getGradValue(routesCountRatio)
          labels[gradValueIndex] = '<i>' + routesNoRepetition[i*step-1] + '</i>'
        } 
      }i=0
    }
    this._list.innerHTML = labels.join('')
  } 
})
    
L.control.routesCountColor = function (options) {
  return new L.Control.RoutesCountColor(options)
}

L.Map.addInitHook(function () {
  if (this.options.routesCountColor) {
    this.routesCountColor = L.control.routesCountColor(this.options.routesCountColor)
    this.addControl(this.routesCountColor)
  }
})