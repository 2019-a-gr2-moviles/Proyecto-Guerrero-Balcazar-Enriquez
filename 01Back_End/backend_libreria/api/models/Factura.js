/**
 * Factura.js
 *
 * @description :: A model definition represents a database table/collection.
 * @docs        :: https://sailsjs.com/docs/concepts/models-and-orm/models
 */

module.exports = {

  attributes: {
    detalleFactura: {
      collection: 'detalle',
      via: 'idFactura'
    },
    idUsuario:{
      model:'usuario'
    },
    numeroTarjeta: {
      type: 'string',
      
    },
    fecha:{
      type: 'string'
    },
    total:{
      type:'number',
      required: true
    }
  },

};

