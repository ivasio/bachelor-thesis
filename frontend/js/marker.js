  // Создание класса Control.ListMarkers
  L.Control.ListMarkers = L.Control.extend({
    // Свойство класса, которое объединяет все указанные объекты в класс
    includes: L.version[0]==='1' ? L.Evented.prototype : L.Mixin.Events,
    // Свойство, которое будет объединено с родительским
    options: {		
      layer: false,
      maxItems: 10,
      collapsed: false,		
      label: 'junction',
      itemIcon: 'img/marker-icon-2x-red.png',
      itemArrow: '&#10148',	
      maxZoom: 18,
      position: 'bottomleft'
    },
    // Метод обьединения опций, передаваемых в конструктор
    initialize: function(options) {
      L.Util.setOptions(this, options)
      this._container = null
      this._list = null
      this._layer = this.options.layer || new L.LayerGroup()
    },
    // Метод добавления на карту контейнера со списком, добавление и обновление по событию moveend, возвращаем контейнер
    onAdd: function (map) {
      this._map = map
      var container = this._container = L.DomUtil.create('div', 'list-markers') 
      this._list = L.DomUtil.create('ul', 'list-markers-ul', container) 
      map.on('moveend', this._updateList, this)
      this._updateList()

      return container
    },
    // Метод удаления с карты элементов контейнера, при отсутствии маркеров на данной области по событию moveend
    onRemove: function(map) {
      map.off('moveend', this._updateList, this)
      this._container = null
      this._list = null		
    },
    // Создание элементов ul li контейнера, работа с маркерами
    _createItem: function(layer) {
  
      var li = L.DomUtil.create('li', 'list-markers-li'),
        a = L.DomUtil.create('a', '', li),
        icon = this.options.itemIcon ? '<img src="'+this.options.itemIcon+'" />' : '',
        that = this
  
      a.href = '#'
      L.DomEvent
        .disableClickPropagation(a)
        .on(a, 'click', L.DomEvent.stop, this)
        .on(a, 'click', function(e) {
          this._moveTo( layer.getLatLng() )
        }, this)
        .on(a, 'mouseover', function(e) {
          that.fire('item-mouseover', {layer: layer })
        }, this)
        .on(a, 'mouseout', function(e) {
          that.fire('item-mouseout', {layer: layer })
        }, this)
		
      // console.log('_createItem',layer.options)
  
      if( layer.options.hasOwnProperty(this.options.label) )
      {
        a.innerHTML = icon+'<span>'+layer.options[this.options.label]+'</span> <b>'+this.options.itemArrow+'</b>'
      }
      else
        console.log("propertyName '"+this.options.label+"' not found in marker")
  
      return li
    },
    // Метод обновления контейнера по существующим маркерам в видимой области 
    _updateList: function() {
    
      var that = this,
        n = 0

      this._list.innerHTML = ''
      this._layer.eachLayer(function(layer) {
        if(layer instanceof L.Marker)
          if( that._map.getBounds().contains(layer.getLatLng()) )
            if(++n < that.options.maxItems)
              that._list.appendChild( that._createItem(layer) )
      })
     },
  
    _moveTo: function(latlng) {
      if(this.options.maxZoom)
        this._map.setView(latlng, Math.min(this._map.getZoom(), this.options.maxZoom) )
      else
        this._map.panTo(latlng)    
      }
  })
  
  L.control.listMarkers = function (options) {
      return new L.Control.ListMarkers(options)
  }
  //Constructor hooks
  L.Map.addInitHook(function () {
      if (this.options.listMarkersControl) {
          this.listMarkersControl = L.control.listMarkers(this.options.listMarkersControl)
          this.addControl(this.listMarkersControl)
      }
  })